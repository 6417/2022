package frc.robot.statemachines;

import org.jeasy.states.api.AbstractEvent;

public class Events {
    public static class PressedStart extends AbstractEvent {}
    public static class MoveUpFinished extends AbstractEvent {}
    public static class CheckFinished extends AbstractEvent {}
    public static class CheckPassed extends AbstractEvent {}
    public static class PullFinished extends AbstractEvent {}
    public static class HandoverFinished extends AbstractEvent {}
    public static class TilterResetted extends AbstractEvent {}
    public static class HandoverCheckSuccess extends AbstractEvent {}
    public static class finishedTraversalPreparation extends AbstractEvent {}
    public static class finishedExtendingArmToTraverse extends AbstractEvent {}
    public static class finishedCheckingTraversal extends AbstractEvent {}
    public static class traverseCheckSuccessful extends AbstractEvent {}
    public static class Finish extends AbstractEvent {}
}