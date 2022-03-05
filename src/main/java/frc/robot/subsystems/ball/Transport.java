package frc.robot.subsystems.ball;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import frc.robot.subsystems.ball.base.TransportBase;

public class Transport extends TransportBase{
    public static final class Constants {
        public static class Motor {
            public static final int id = 34;
            public static final double speed = 0.5;
            public static final double shootSpeed = 0.75;
            public static final double reverseSpeed = -1;
        }
    }

    private Transport() {
        motor = new FridoCanSparkMax(Constants.Motor.id, MotorType.kBrushless);

        Initializer.getInstance().addInitialisable(this);
        JoystickHandler.getInstance().bind(this);
    }

    private FridolinsMotor motor;
    
    private static TransportBase instance = null;
    private static final boolean enabled = true;


    public static TransportBase getInstance() {
        if (instance == null) {
            if (enabled) 
                instance = new Transport();
            else
                instance = new TransportBase();
        }
        return instance;
    }

    @Override
    public void init() {
        super.init();

        motor.factoryDefault();
        motor.setIdleMode(IdleMode.kBrake);
        motor.setInverted(true);
        motor.configEncoder(FridoFeedBackDevice.kBuildin, 42);

        Transport.this.registerSubmodule(motor);

        System.out.println("called init of transport");
    }

    @Override
    public void run() {
        motor.set(Constants.Motor.speed);
    }

    @Override
    public void runShootspeed() {
        motor.set(Constants.Motor.shootSpeed);
    }

    @Override
    public void reverse() {
        motor.set(Constants.Motor.reverseSpeed);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }
}
