package frc.robot.subsystems.ball;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import frc.robot.subsystems.ball.base.TransportBase;

public class Transport extends TransportBase{
    public static final class Constants {
        public static class Motor {
            public static final int id = 0;
            public static final double speed = 1;
        }
    }

    private Transport() {
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

        motor = new FridoCanSparkMax(Constants.Motor.id, MotorType.kBrushless);
        motor.factoryDefault();
        motor.setIdleMode(IdleMode.kBrake);
        motor.configEncoder(FridoFeedBackDevice.kBuildin, 42);

        Transport.this.registerSubmodule(motor);
    }

    @Override
    public void run() {
        motor.set(Constants.Motor.speed);
    }

    @Override
    public void reverse() {
        motor.set(-Constants.Motor.speed);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }
}
