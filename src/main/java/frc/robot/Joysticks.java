package frc.robot;

import ch.fridolins.fridowpi.joystick.IJoystickId;

public enum Joysticks implements IJoystickId {
    Drive(0);

    private final int port;

    private Joysticks(int port) {this.port = port;}

    @Override
    public int getPort() {
        // TODO Auto-generated method stub
        return port;
    }
    
}
