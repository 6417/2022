package frc.robot.statemachines;

import java.util.HashSet;
import java.util.Set;

import org.jeasy.states.api.State;

public class States {
    private static States instance;
    public static States getInstance()
    {
        if (instance == null) {
            instance = new States();
        }
        return instance;
    }    

    public State started;
    public State movingUp;
    public State ready;
    public State checkingContact;
    public State pulling;
    public State handingOver;
    public State resettingTilter;
    public State finished;

    private Set<State> states;

    private States() {
        states = new HashSet<>();

        started = new State("started");
        movingUp = new State("movingUp");
        ready = new State("ready");
        checkingContact = new State("checkingContact");
        pulling = new State("pulling");
        handingOver = new State("checkingHandover");
        resettingTilter = new State("resettingTilter");
        finished = new State("finished");

        states.add(started);
        states.add(movingUp);
        states.add(ready);
        states.add(checkingContact);
        states.add(pulling);
        states.add(handingOver);
        states.add(resettingTilter);
        states.add(finished);
    }

    public Set<State> getStates() {
        return states;
    }
}
