package frc.robot;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ZeroExpander;
import frc.robot.commands.Ballsubsystem.PickupCommand;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Thrower;

public class Robot extends TimedRobot {

    Joystick jst;
    JoystickButton zeroButton;
    JoystickButton runBrush;
    JoystickButton stopBrush;
    JoystickButton pidTestButton;
    JoystickButton backTest;
    JoystickButton throwerButton;
    JoystickButton throwerStopButton;

    @Override
    public void robotInit() {
        BallSubsystem.getInstance();
        Initializer.getInstance().init();
        jst = new Joystick(0);

        // runBrush = new JoystickButton(jst, 7);
        // stopBrush = new JoystickButton(jst, 5);
        // zeroButton = new JoystickButton(jst, 3);
        // backTest = new JoystickButton(jst, 2);
        // pidTestButton = new JoystickButton(jst, 1);
        // throwerButton = new JoystickButton(jst, 6);
        // throwerStopButton = new JoystickButton(jst, 8);

        zeroButton.whenPressed(new ZeroExpander());
        runBrush.whenPressed(() -> PickUp.getInstance().runBrush());
        stopBrush.whenPressed(() -> PickUp.getInstance().stopBrush());
        pidTestButton.whenPressed(() -> PickUp.getInstance().openExpander());
        backTest.whenPressed(() -> PickUp.getInstance().closeExpander());
        throwerButton.whileHeld(() -> Thrower.getInstance().setVelocity(1));
        throwerStopButton.whileHeld(() -> Thrower.getInstance().setVelocity(0));
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