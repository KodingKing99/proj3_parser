/*
 * Look for TODO comments in this file for suggestions on how to implement
 * your parser.
 */
package parser;

import java.io.IOException;
import java.util.*;

import lexer.ExprLexer;
import lexer.ParenLexer;
import lexer.SimpleLexer;
import lexer.TinyLexer;
import org.antlr.v4.runtime.*;

/**
 *
 */
public class Parser {

  final Grammar grammar;

  /**
   * All states in the parser.
   */
  private final States states;

  /**
   * Action table for bottom-up parsing. Accessed as
   * actionTable.get(state).get(terminal). You may replace
   * the Integer with a State class if you choose.
   */
  private final HashMap<Integer, HashMap<String, Action>> actionTable;
  /**
   * Goto table for bottom-up parsing. Accessed as gotoTable.get(state).get(nonterminal).
   * You may replace the Integers with State classes if you choose.
   */
  private final HashMap<Integer, HashMap<String, Integer>> gotoTable;

  public Parser(String grammarFilename) throws IOException {
    actionTable = new HashMap<>();
    gotoTable = new HashMap<>();

    grammar = new Grammar(grammarFilename);

    states = new States();

    // TODO: Call methods to compute the states and parsing tables here.
    {
      Item head = new Item(grammar.startRule, 0, Util.EOF);
      State state = computeClosure(head, grammar);
      states.addState(state);
      List<State> newstates = new ArrayList<>();
      this.computeStates(state, grammar);
      System.out.println(states.toString());
    }
  }
  private void computeStates(State state, Grammar grammar){
      for(String symbol : grammar.symbols){
        State newstate = GOTO(state, symbol, grammar); 
        if(newstate.size() > 0 && !this.states.contains(newstate)){
          newstate.setName(states.getNewName());
          this.states.addState(newstate);
          computeStates(newstate, grammar);
        }
      }
    }

  public States getStates() {
    return states;
  }

  // TODO: Implement this method.
  static public State computeClosure(Item I, Grammar grammar) {
    State closure = new State();
    // do the recursion to compute each items closure
    computeClosureHelper(I, closure, grammar);
    System.out.println("Post closure. state is: " + closure.toString());
    return closure;
  }
  static private void computeClosureHelper(Item I, State closure, Grammar grammar){
    // if item is not already in closure, add it
    if(closure.contains(I)){
      return;
    }
    closure.addItem(I);

    // if next symbol is a terminal, return
    if(grammar.isTerminal(I.getNextSymbol())){
      return;
    }
    // If there are no production rules for that non terminal, return
    if(!grammar.nt2rules.containsKey(I.getNextSymbol())){
      return;
    }
    // else
    ArrayList<Rule> ntProductions = grammar.nt2rules.get(I.getNextSymbol());
    for(Rule production : ntProductions){
      ArrayList<Item> items = new ArrayList<>();
      // Compute first of beta, a
      if(I.getNextNextSymbol() != null){

        if(grammar.first.get(I.getNextNextSymbol()) != null){

          for(String first : grammar.first.get(I.getNextNextSymbol())){
            items.add(new Item(production, 0, first));
          }
        }
        else{
          items.add(new Item(production, 0, I.getLookahead()));
        }
      }
      else{

        items.add(new Item(production, 0, I.getLookahead()));
      }
      for(Item item : items){
        computeClosureHelper(item, closure, grammar);
      }
    }
  }

  // TODO: Implement this method.
  //   This returns a new state that represents the transition from
  //   the given state on the symbol X.
  static public State GOTO(State state, String X, Grammar grammar) {
    State ret = new State();
    for(Item item : state.getItems()){
      if(item.getNextSymbol() == null){
        continue;
      }
      if(item.getNextSymbol().equals(X)){
        computeClosureHelper(item.advance(), ret, grammar);
      }
    }
    return ret;
  }

  // TODO: Implement this method
  // You will want to use StringBuilder. Another useful method will be String.format: for
  // printing a value in the table, use
  //   String.format("%8s", value)
  // How much whitespace you have shouldn't matter with regard to the tests, but it will
  // help you debug if you can format it nicely.
  public String actionTableToString() {
    StringBuilder builder = new StringBuilder();
    return builder.toString();
  }

  // TODO: Implement this method
  // You will want to use StringBuilder. Another useful method will be String.format: for
  // printing a value in the table, use
  //   String.format("%8s", value)
  // How much whitespace you have shouldn't matter with regard to the tests, but it will
  // help you debug if you can format it nicely.
  public String gotoTableToString() {
    StringBuilder builder = new StringBuilder();
    return builder.toString();
  }

  // TODO: Implement this method
  // You should return a list of the actions taken.
  public List<Action> parse(Lexer scanner) throws ParserException {
    // tokens is the output from the scanner. It is the list of tokens
    // scanned from the input file.
    // To get the token type: v.getSymbolicName(t.getType())
    // To get the token lexeme: t.getText()
    ArrayList<? extends Token> tokens = new ArrayList<>(scanner.getAllTokens());
    Vocabulary v = scanner.getVocabulary();

    Stack<String> input = new Stack<>();
    Collections.reverse(tokens);
    input.add(Util.EOF);
    for (Token t : tokens) {
      input.push(v.getSymbolicName(t.getType()));
    }
    Collections.reverse(tokens);
//    System.out.println(input);

    // TODO: Parse the tokens. On an error, throw a ParseException, like so:
    //    throw ParserException.create(tokens, i)
    List<Action> actions = new ArrayList<>();
    return actions;
  }

  //-------------------------------------------------------------------
  // Convenience functions
  //-------------------------------------------------------------------

  public List<Action> parseFromFile(String filename) throws IOException, ParserException {
//    System.out.println("\nReading input file " + filename + "\n");
    final CharStream charStream = CharStreams.fromFileName(filename);
    Lexer scanner = scanFile(charStream);
    return parse(scanner);
  }

  public List<Action> parseFromString(String program) throws ParserException {
    Lexer scanner = scanFile(CharStreams.fromString(program));
    return parse(scanner);
  }

  private Lexer scanFile(CharStream charStream) {
    // We use ANTLR's scanner (lexer) to produce the tokens.
    Lexer scanner = null;
    switch (grammar.grammarName) {
      case "Simple":
        scanner = new SimpleLexer(charStream);
        break;
      case "Paren":
        scanner = new ParenLexer(charStream);
        break;
      case "Expr":
        scanner = new ExprLexer(charStream);
        break;
      case "Tiny":
        scanner = new TinyLexer(charStream);
        break;
      default:
        System.out.println("Unknown scanner");
        break;
    }

    return scanner;
  }

}
