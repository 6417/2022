package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.IJoystickId;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Expander.ZeroExpander;
import frc.robot.subsystems.ball.BallSubsystem;

public class Robot extends TimedRobot {
    Joystick jst;
    JoystickButton zeroExpanderButton;
    JoystickButton pickupButton;
    JoystickButton reverseFlowButton;
    JoystickButton shootButton;


    @Override
    public void robotInit() {
        BallSubsystem.getInstance();
        JoystickHandler.getInstance().setupJoysticks(List.of(() -> 0));
        Initializer.getInstance().init();
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
    }

    @Override
    public void autonomousPeriodic() {
    }

    // ThrowerBase thrower;

    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();

        CommandScheduler.getInstance().schedule(new ZeroExpander());
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}