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
    public State getState(State state){
        for(State mState : this.states){
            if(mState.equals(state)){
                return mState;
            }
        }
        return null;
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
        StringBuilder str = new StringBuilder();
        for(State state : this.states){
            str.append(state.toString() + "\n");
        }
        return str.toString();
    }

}
