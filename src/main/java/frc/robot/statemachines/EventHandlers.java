package frc.robot.statemachines;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.jeasy.states.api.Event;
import org.jeasy.states.api.EventHandler;

import frc.robot.commands.climber.BeginTraversing;
import frc.robot.commands.climber.MoveToFirstWrungPosition;
import frc.robot.commands.climber.PrepareTraverse;
import frc.robot.commands.climber.FinishTraverse;
import frc.robot.commands.telescopeArm.CheckFirstWrungContact;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.telescopeArm.MoveArmToHandover;
import frc.robot.commands.telescopeArm.MoveArmToTraversalPosition;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.commands.tilter.MoveTilterToHandoverPosition;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;
import frc.robot.statemachines.Events.HandoverCheckSuccess;
import frc.robot.statemachines.Events.finishedCheckingTraversal;
import frc.robot.statemachines.Events.finishedTraversalPreparation;
import frc.robot.statemachines.Events.traverseCheckSuccessful;

public class EventHandlers {
    public static class MoveUp implements EventHandler<Events.PressedStart> {
        @Override
        public void handleEvent(Events.PressedStart arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new MoveToFirstWrungPosition());
            System.out.println("moving up"); 
        }
    }
    public static class MoveBackUp implements EventHandler<Events.CheckFinished> {
        @Override
        public void handleEvent(Events.CheckFinished arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new MoveArmToFirstwrungPosition());
            System.out.println("moving back up"); 
        }
    }
    public static class Check implements EventHandler<Events.PressedStart> {
        @Override
        public void handleEvent(Events.PressedStart arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new CheckFirstWrungContact());
            System.out.println("checking");
        }
    }
    public static class Pull implements EventHandler<Events.CheckPassed> {
        @Override
        public void handleEvent(Events.CheckPassed arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new RetractTelescopearm());
            System.out.println("pulling");
        }
    }
    public static class PullBackUp implements EventHandler<Events.TilterResetted> {
        @Override
        public void handleEvent(Events.TilterResetted arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new RetractTelescopearm());
            System.out.println("pulling");
        }
    }
    public static class Handover implements EventHandler<Events.PullFinished> {
        @Override
        public void handleEvent(Events.PullFinished arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new MoveArmToHandover());
            System.out.println("handing over");
        }
    }
    public static class ResetTilter implements EventHandler<Events.HandoverFinished> {
        @Override
        public void handleEvent(Events.HandoverFinished arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new MoveTilterToTraversalPosition());
            System.out.println("resetting tilter"); 
        }
    }

    public static class PrepareTraversal implements EventHandler<Events.HandoverCheckSuccess> {
        @Override
        public void handleEvent(HandoverCheckSuccess arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new PrepareTraverse());
            System.out.println("preparingTraversal");
        }
    }

    public static class ExtendTelescopeArmToTraverse implements EventHandler<Events.finishedTraversalPreparation> {
        @Override
        public void handleEvent(finishedTraversalPreparation arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new MoveArmToTraversalPosition());
            System.out.println("extending Telescope arm"); 
        }
    }

    public static class CheckTraverse implements EventHandler<Events.finishedExtendingArmToTraverse> {
        @Override
        public void handleEvent(Events.finishedExtendingArmToTraverse arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new BeginTraversing());
            System.out.println("checking traversal"); 
        }
    }

    public static class TraverseFallback implements EventHandler<Events.finishedCheckingTraversal> {
        @Override
        public void handleEvent(finishedCheckingTraversal arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new PrepareTraverse());
            System.out.println("traverse check fallback");
        }
    }

    public static class Traverse implements EventHandler<Events.traverseCheckSuccessful> {
        @Override
        public void handleEvent(traverseCheckSuccessful arg0) throws Exception {
            CommandScheduler.getInstance().schedule(new FinishTraverse());
            System.out.println("traversing"); 
        }
    }
}
