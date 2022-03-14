package frc.robot;

import ch.fridolins.fridowpi.joystick.IJoystickId;

public enum Joysticks implements IJoystickId {
    Drive(0),
    SteeringWheel(1),
    Climb(2);

    private final int port;

    private Joysticks(int id) {
        port = id;
    }

    @Override
    public int getPort() {
        return port;
    }
}
