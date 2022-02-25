package frc.robot.subsystems.ball;

import ch.fridolins.fridowpi.command.ICommand;
import ch.fridolins.fridowpi.initializer.Initializer;
import frc.robot.commands.RetractGripperCommand;
import frc.robot.commands.Ballsubsystem.PickupCommand;
import frc.robot.commands.Ballsubsystem.ReverseflowCommand;
import frc.robot.commands.Ballsubsystem.ShootCommand;
import frc.robot.subsystems.ball.base.BallSubsystemBase;
import frc.robot.subsystems.ball.base.PickUpBase;
import frc.robot.subsystems.ball.base.ThrowerBase;
import frc.robot.subsystems.ball.base.TransportBase;

public class BallSubsystem extends BallSubsystemBase {
    private static BallSubsystemBase instance;
    private static boolean enabled = true;

    private PickUpBase pickUpSubmodule;
    private TransportBase transportSubmodule;
    private ThrowerBase throwerSubmodule;

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
        Initializer.getInstance().addInitialisable(this);
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
    };

    @Override
    public ICommand getShootCommand() {
        return new ShootCommand(transportSubmodule);
    };

    @Override
    public ICommand getPickupCommand() {
        return new PickupCommand(pickUpSubmodule, transportSubmodule);
    };

    @Override
    public ICommand getRetractCommand() {
        return new RetractGripperCommand(pickUpSubmodule);
    };

    @Override
    public ICommand getReverseFlowCommand() {
        return new ReverseflowCommand(transportSubmodule, pickUpSubmodule);
    };
}
