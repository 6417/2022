package frc.robot;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ZeroExpander;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Thrower;

public class Robot extends TimedRobot {

    Joystick jst;
    JoystickButton zeroButton;
    JoystickButton pidTestButton;
    FridolinsMotor throwerTest;

    @Override
    public void robotInit() {
        BallSubsystem.getInstance();
        Initializer.getInstance().init();
        jst = new Joystick(0);
        zeroButton = new JoystickButton(jst, 3);
        pidTestButton = new JoystickButton(jst, 1);

        zeroButton.whenPressed(new ZeroExpander());
        pidTestButton.whenPressed(() -> PickUp.getInstance().openExpander());
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