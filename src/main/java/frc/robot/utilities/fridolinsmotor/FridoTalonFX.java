package frc.robot.utilities.fridolinsmotor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FridoTalonFX extends TalonFX implements FridolinsMotor{

    public FridoTalonFX(int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public void set(double speed) {
        super.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public double get() {
        return super.getSelectedSensorVelocity();
    }

    @Override
    public void disable() {
    }

    @Override
    public void stopMotor() {
        super.set(ControlMode.PercentOutput, 0);
    }
}
