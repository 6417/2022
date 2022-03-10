package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.telescopeArm.MoveArmToPosition1Release;
import frc.robot.commands.telescopeArm.MoveArmToPosition2Release;
import frc.robot.commands.tilter.MoveTilterToRelease1Position;
import frc.robot.commands.tilter.MoveTilterToRelease2Position;

public class ReleaseWrungToTilter extends SequentialCommandGroup {
    public ReleaseWrungToTilter() {
        addCommands(
            new MoveArmToPosition1Release(),
            new MoveTilterToRelease1Position(),

            new MoveTilterToRelease2Position(),
            new MoveArmToPosition2Release()
        );
    }
}
