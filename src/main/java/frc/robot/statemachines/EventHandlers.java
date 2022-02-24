package frc.robot.statemachines;

import org.jeasy.states.api.EventHandler;

import frc.robot.commands.climber.MoveToFirstWrungPosition;
import frc.robot.commands.telescopeArm.CheckWrungContact;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.telescopeArm.MoveArmToHandover;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;

public class EventHandlers {
    public static class MoveUp implements EventHandler<Events.PressedStart> {
        @Override
        public void handleEvent(Events.PressedStart arg0) throws Exception {
            new MoveToFirstWrungPosition();
            System.out.println("moving up"); 
        }
    }
    public static class MoveBackUp implements EventHandler<Events.CheckFinished> {
        @Override
        public void handleEvent(Events.CheckFinished arg0) throws Exception {
            new MoveArmToFirstwrungPosition();
            System.out.println("moving back up"); 
        }
    }
    public static class Check implements EventHandler<Events.PressedStart> {
        @Override
        public void handleEvent(Events.PressedStart arg0) throws Exception {
            new CheckWrungContact();
            System.out.println("checking");
        }
    }
    public static class Pull implements EventHandler<Events.CheckPassed> {
        @Override
        public void handleEvent(Events.CheckPassed arg0) throws Exception {
            new RetractTelescopearm();
            System.out.println("pulling");
        }
    }
    public static class PullBackUp implements EventHandler<Events.TilterResetted> {
        @Override
        public void handleEvent(Events.TilterResetted arg0) throws Exception {
            new RetractTelescopearm();
            System.out.println("pulling");
        }
    }
    public static class Handover implements EventHandler<Events.PullFinished> {
        @Override
        public void handleEvent(Events.PullFinished arg0) throws Exception {
            new MoveArmToHandover();
            System.out.println("handing over");
        }
    }
    public static class ResetTilter implements EventHandler<Events.HandoverFinished> {
        @Override
        public void handleEvent(Events.HandoverFinished arg0) throws Exception {
            new MoveTilterToTraversalPosition();
            System.out.println("resetting tilter"); 
        }
    }
}
