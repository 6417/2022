package frc.robot.statemachines;

import java.util.Set;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.Event;
import org.jeasy.states.api.EventHandler;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.FiniteStateMachineBuilder;
import org.jeasy.states.core.TransitionBuilder;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.commands.climber.MoveToFirstWrungPosition;
import frc.robot.commands.telescopeArm.CheckWrungContact;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.telescopeArm.MoveArmToHandover;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;

public class FirstWrungStatemachine {
    private static FirstWrungStatemachine instance;

    // States
    private State started;
    private State movingUp;
    private State ready;
    private State checkingContact;
    private State pulling;
    private State handingOver;
    private State resettingTilter;
    private State finished;

    // Transitions
    private Transition beginningSetup;
    private Transition movingUpToReady;
    private Transition startingCheck;
    private Transition checkFallback;
    private Transition startingPull;
    private Transition startingHandover;
    private Transition handoverCheckFallback;
    private Transition resettingTilterToPull;
    private Transition finishing;

    // Events to fire
    public static class PressedStart extends AbstractEvent {}
    public static class MoveUpFinished extends AbstractEvent {}
    public static class CheckFinished extends AbstractEvent {}
    public static class CheckPassed extends AbstractEvent {}
    public static class PullFinished extends AbstractEvent {}
    public static class HandoverFinished extends AbstractEvent {}
    public static class TilterResetted extends AbstractEvent {}
    public static class HandoverCheckSuccess extends AbstractEvent {}

    // Event handlers
    private class MoveUp implements EventHandler<PressedStart> {
        @Override
        public void handleEvent(PressedStart arg0) throws Exception {
            new MoveToFirstWrungPosition();
            System.out.println("moving up"); 
        }
    }
    private class MoveBackUp implements EventHandler<CheckFinished> {
        @Override
        public void handleEvent(CheckFinished arg0) throws Exception {
            new MoveArmToFirstwrungPosition();
            System.out.println("moving back up"); 
        }
    }
    private class Check implements EventHandler<PressedStart> {
        @Override
        public void handleEvent(PressedStart arg0) throws Exception {
            new CheckWrungContact();
            System.out.println("checking");
        }
    }
    private class Pull implements EventHandler<CheckPassed> {
        @Override
        public void handleEvent(CheckPassed arg0) throws Exception {
            new RetractTelescopearm();
            System.out.println("pulling");
        }
    }
    private class PullBackUp implements EventHandler<TilterResetted> {
        @Override
        public void handleEvent(TilterResetted arg0) throws Exception {
            new RetractTelescopearm();
            System.out.println("pulling");
        }
    }
    private class Handover implements EventHandler<PullFinished> {
        @Override
        public void handleEvent(PullFinished arg0) throws Exception {
            new MoveArmToHandover();
            System.out.println("handing over");
        }
    }
    private class ResetTilter implements EventHandler<HandoverFinished> {
        @Override
        public void handleEvent(HandoverFinished arg0) throws Exception {
            new MoveTilterToTraversalPosition();
            System.out.println("resetting tilter"); 
        }
    }

    // The state machine itself ;)
    private FiniteStateMachine stateMachine;

    public static FirstWrungStatemachine getInstance() {
        if (instance == null) {
            instance = new FirstWrungStatemachine();
        }
        return instance;
    }

    private FirstWrungStatemachine() {
        started = new State("started");
        movingUp = new State("movingUp");
        ready = new State("ready");
        checkingContact = new State("checkingContact");
        pulling = new State("pulling");
        handingOver = new State("checkingHandover");
        resettingTilter = new State("resettingTilter");
        finished = new State("finished");

        beginningSetup = new TransitionBuilder()
            .name("beginningSetup")
            .sourceState(started)
            .eventType(PressedStart.class)
            .eventHandler(new MoveUp())
            .targetState(movingUp)
            .build();

        movingUpToReady = new TransitionBuilder()
            .name("movingUpToReady")
            .sourceState(movingUp)
            .eventType(MoveUpFinished.class)
            .targetState(ready)
            .build();

        startingCheck = new TransitionBuilder()
            .name("startingCheck")
            .sourceState(ready)
            .eventType(PressedStart.class)
            .eventHandler(new Check())
            .targetState(checkingContact)
            .build();

        checkFallback = new TransitionBuilder()
            .name("checkFallback")
            .sourceState(checkingContact)
            .eventType(CheckFinished.class)
            .eventHandler(new MoveBackUp())
            .targetState(movingUp)
            .build();

        startingPull = new TransitionBuilder()
            .name("startingPull")
            .sourceState(checkingContact)
            .eventType(CheckPassed.class)
            .eventHandler(new Pull())
            .targetState(pulling)
            .build();

        startingHandover = new TransitionBuilder()
            .name("startingHandover")
            .sourceState(pulling)
            .eventType(PullFinished.class)
            .eventHandler(new Handover())
            .targetState(handingOver)
            .build();

        handoverCheckFallback = new TransitionBuilder()
            .name("handoverCheckFallback")
            .sourceState(handingOver)
            .eventType(HandoverFinished.class)
            .eventHandler(new ResetTilter())
            .targetState(resettingTilter)
            .build();

        resettingTilterToPull = new TransitionBuilder()
            .name("resettingTilterToPull")
            .sourceState(resettingTilter)
            .eventType(TilterResetted.class)
            .eventHandler(new PullBackUp())
            .targetState(pulling)
            .build();

        finishing = new TransitionBuilder()
            .name("finishing")
            .sourceState(handingOver)
            .eventType(HandoverCheckSuccess.class)
            .targetState(finished)
            .build();

        Set<State> states = Set.of(started,
                                   movingUp,
                                   ready,
                                   checkingContact,
                                   pulling,
                                   handingOver,
                                   resettingTilter,
                                   finished);

        Set<Transition> transitions = Set.of(beginningSetup,
                                             movingUpToReady,
                                             startingCheck,
                                             checkFallback,
                                             startingPull,
                                             startingHandover,
                                             handoverCheckFallback,
                                             resettingTilterToPull,
                                             finishing);

        stateMachine = new FiniteStateMachineBuilder(states, started)
            .registerTransitions(transitions)
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