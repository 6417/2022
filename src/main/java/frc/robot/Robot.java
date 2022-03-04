package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Expander.ZeroExpander;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.Thrower;

public class Robot extends TimedRobot {
    Joystick jst;
    JoystickButton zeroExpanderButton;
    JoystickButton pickupButton;
    JoystickButton reverseFlowButton;
    JoystickButton shootButton;


    @Override
    public void robotInit() {
        JoystickHandler.getInstance().setupJoysticks(List.of(() -> 0));
        BallSubsystem.getInstance();

        Initializer.getInstance().init();

        System.out.println("Robot init completed");
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

    }

    @Override
    public void teleopPeriodic() {
        // Thrower.getInstance().setPercentage(JoystickHandler.getInstance().getJoystick((() -> 0)).getY());
        // Thrower.getInstance().setVelocity(-5000);
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}