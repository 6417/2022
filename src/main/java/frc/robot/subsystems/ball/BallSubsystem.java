package frc.robot.subsystems.ball;

import java.util.List;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.sensors.AnalogLightbarrier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.commands.Ballsubsystem.EndPickup;
import frc.robot.commands.Ballsubsystem.ReverseflowCommand;
import frc.robot.commands.Ballsubsystem.ShootCommand;
import frc.robot.commands.Ballsubsystem.StartpickupCommand;
import frc.robot.commands.Expander.ZeroExpander;
import frc.robot.commands.Tunnel.PullbackBallsCommand;
import frc.robot.subsystems.ball.base.BallSubsystemBase;
import frc.robot.subsystems.ball.base.PickUpBase;
import frc.robot.subsystems.ball.base.ThrowerBase;
import frc.robot.subsystems.ball.base.TransportBase;

public class BallSubsystem extends BallSubsystemBase {
    private static BallSubsystemBase instance;
    private static boolean enabled = false;

    private PickUpBase pickUpSubmodule;
    private TransportBase transportSubmodule;
    private ThrowerBase throwerSubmodule;
    private boolean isPickingUp;

    private CommandBase startPickupCommand;
    private CommandBase endPickupCommand;

    private AnalogLightbarrier throwerBarrier;
    private AnalogLightbarrier throwerProtectionBarrier;

    public static BallSubsystemBase getInstance() {
        if (instance == null) {
            if (enabled) {
                instance = new BallSubsystem();
            } else {
                instance = new BallSubsystemBase();
            }
        }
        return instance;
    }

    private BallSubsystem() {
        requires(PickUp.getInstance());
        requires(Thrower.getInstance());
        requires(Transport.getInstance());

        // TODO: figure out why this is neccessary
        Thrower.getInstance().init();

        Initializer.getInstance().addInitialisable(this);

        JoystickHandler.getInstance().bind(this);

        startPickupCommand = new StartpickupCommand();
        endPickupCommand = new EndPickup();

        throwerBarrier = new AnalogLightbarrier(0, 1);
        throwerProtectionBarrier = new AnalogLightbarrier(1, 1);
    }

    @Override
    public void init() {
        super.init();
        pickUpSubmodule = PickUp.getInstance();
        throwerSubmodule = Thrower.getInstance();
        transportSubmodule = Transport.getInstance();


        registerSubmodule(pickUpSubmodule, throwerSubmodule, transportSubmodule);
    }

    public void setShooterToVelocity() {
        this.throwerSubmodule.setVelocity(this.throwerSubmodule.getRequiredVelocity());
    }

    ;

    @Override
    public boolean getShooterLightbarrier() {
        return throwerBarrier.isTriggered();
    }

    public void togglePickup() {
        if (!isPickingUp) {
            isPickingUp = true;
            if (CommandScheduler.getInstance().isScheduled(endPickupCommand))
                CommandScheduler.getInstance().cancel(endPickupCommand);

            CommandScheduler.getInstance().schedule(startPickupCommand);
        } else {
            isPickingUp = false;
            if (CommandScheduler.getInstance().isScheduled(startPickupCommand))
                CommandScheduler.getInstance().cancel(startPickupCommand);

            CommandScheduler.getInstance().schedule(endPickupCommand);
        }
    }

    @Override
    public List<Binding> getMappings() {
        return List.of(
            new Binding(() -> 0, () -> 1, Button::whenPressed, new ShootCommand()),
            new Binding(() -> 0, () -> 2, Button::whenPressed, new InstantCommand(this::togglePickup)),
            new Binding(() -> 0, () -> 7, Button::whileHeld, new ReverseflowCommand()),
            new Binding(() -> 0, () -> 8, Button::whileHeld, new ZeroExpander())
        );
    }
}
