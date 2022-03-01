package parser;

import java.util.*;

public class States {
    private Set<State> stateSet;
    private List<State> states;
    private int id = 0;

    public States() {
        this.stateSet = new HashSet<>();
        this.states = new ArrayList<>();
    }

    public State getState(int name) {
        if (name >= states.size()) {
            return null;
        }
        return states.get(name);
    }
    public void addState(State state){
        this.stateSet.add(state);
        this.states.add(state);
    }
    public List<State> getStates(){
        return new ArrayList<>(this.states);
    }
    public int getNewName(){
        return stateSet.size();
    }
    public boolean contains(State state){
        return this.stateSet.contains(state);
    }
    public int size(){
        return stateSet.size();
    }
    @Override
    public String toString() {
        return states.toString();
    }

}
