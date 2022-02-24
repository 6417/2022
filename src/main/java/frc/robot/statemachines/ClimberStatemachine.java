package frc.robot.statemachines;

import org.jeasy.states.api.Event;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.core.FiniteStateMachineBuilder;

import edu.wpi.first.wpilibj.DriverStation;

public class ClimberStatemachine {

    private static ClimberStatemachine instance;
    // The state machine itself ;)
    private FiniteStateMachine stateMachine;

    public static ClimberStatemachine getInstance() {
        if (instance == null) {
            instance = new ClimberStatemachine();
        }
        return instance;
    }

    private ClimberStatemachine() {
        stateMachine = new FiniteStateMachineBuilder(States.getInstance().getStates(), States.getInstance().started)
            .registerTransitions(Transitions.getInstance().getTransitions())
            .build();
    }

    public void fireEvent(Event e) {
        try {
            stateMachine.fire(e);
        } catch (FiniteStateMachineException e1) {
            DriverStation.reportError("[STATE MACHINE ERROR]", e1.getStackTrace());
        }
    }

    public State getCurrentState() {
        return stateMachine.getCurrentState();
    }
}