package frc.robot.statemachines;

import java.util.HashSet;
import java.util.Set;

import org.jeasy.states.api.Transition;
import org.jeasy.states.core.TransitionBuilder;

public class Transitions {
    private static Transitions instance;
    private Set<Transition> transitions;

    private Transition beginningSetup;
    private Transition movingUpToReady;
    private Transition startingCheck;
    private Transition checkFallback;
    private Transition startingPull;
    private Transition startingHandover;
    private Transition handoverCheckFallback;
    private Transition resettingTilterToPull;
    private Transition finishing;

    public static Transitions getInstance() {
        if (instance == null) {
            instance = new Transitions();
        }
        return instance;
    }

    private Transitions() {
        transitions = new HashSet<>();

        beginningSetup = new TransitionBuilder()
            .name("beginningSetup")
            .sourceState(States.getInstance().started)
            .eventType(Events.PressedStart.class)
            .eventHandler(new EventHandlers.MoveUp())
            .targetState(States.getInstance().movingUp)
            .build();

        movingUpToReady = new TransitionBuilder()
            .name("movingUpToReady")
            .sourceState(States.getInstance().movingUp)
            .eventType(Events.MoveUpFinished.class)
            .targetState(States.getInstance().ready)
            .build();

        startingCheck = new TransitionBuilder()
            .name("startingCheck")
            .sourceState(States.getInstance().ready)
            .eventType(Events.PressedStart.class)
            .eventHandler(new EventHandlers.Check())
            .targetState(States.getInstance().checkingContact)
            .build();

        checkFallback = new TransitionBuilder()
            .name("checkFallback")
            .sourceState(States.getInstance().checkingContact)
            .eventType(Events.CheckFinished.class)
            .eventHandler(new EventHandlers.MoveBackUp())
            .targetState(States.getInstance().movingUp)
            .build();

        startingPull = new TransitionBuilder()
            .name("startingPull")
            .sourceState(States.getInstance().checkingContact)
            .eventType(Events.CheckPassed.class)
            .eventHandler(new EventHandlers.Pull())
            .targetState(States.getInstance().pulling)
            .build();

        startingHandover = new TransitionBuilder()
            .name("startingHandover")
            .sourceState(States.getInstance().pulling)
            .eventType(Events.PullFinished.class)
            .eventHandler(new EventHandlers.Handover())
            .targetState(States.getInstance().handingOver)
            .build();

        handoverCheckFallback = new TransitionBuilder()
            .name("handoverCheckFallback")
            .sourceState(States.getInstance().handingOver)
            .eventType(Events.HandoverFinished.class)
            .eventHandler(new EventHandlers.ResetTilter())
            .targetState(States.getInstance().resettingTilter)
            .build();

        resettingTilterToPull = new TransitionBuilder()
            .name("resettingTilterToPull")
            .sourceState(States.getInstance().resettingTilter)
            .eventType(Events.TilterResetted.class)
            .eventHandler(new EventHandlers.PullBackUp())
            .targetState(States.getInstance().pulling)
            .build();

        finishing = new TransitionBuilder()
            .name("finishing")
            .sourceState(States.getInstance().handingOver)
            .eventType(Events.HandoverCheckSuccess.class)
            .targetState(States.getInstance().finished)
            .build();

        transitions.add(beginningSetup);
        transitions.add(movingUpToReady);
        transitions.add(startingCheck);
        transitions.add(checkFallback);
        transitions.add(startingPull);
        transitions.add(startingHandover);
        transitions.add(handoverCheckFallback);
        transitions.add(resettingTilterToPull);
        transitions.add(finishing);
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }
    
}
