package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.sensors.Navx;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomous.PathviewerLoader;
import frc.robot.autonomous.RamseteCommandGenerator;
import frc.robot.subsystems.Drive;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    static {
    }

    @Override
    public void robotInit() {
        JoystickHandler.getInstance().setupJoysticks(List.of(Joysticks.Drive));
        Navx.setup(SPI.Port.kMXP);
        // Navx.setYawOffset(-180);
        Navx.getInstance();
        Drive.getInstance();
        Navx.getInstance().resetDisplacement();
        Navx.getInstance().reset();

        Initializer.getInstance().init();

        System.out.println("Initializer init");
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        Navx.getInstance().reset();

        String trajectoryJSON = "paths/output/TrajectoryRecording.wpilib.json";

        Trajectory pathWeavertest = PathviewerLoader.loadTrajectory(trajectoryJSON);

        m_autonomousCommand = RamseteCommandGenerator.generateRamseteCommand(pathWeavertest);

        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        Navx.getInstance().reset();

        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }
}