package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitUD_C extends AbstractInputAction {
	private Camera3P_Control c;
	public OrbitUD_C(Camera3P_Control rot) {
		c = rot;
	}
	@Override
	public void performAction(float time, Event e) {
		if(e.getValue() >= 0.2f) {			//ROTATE UP
			if(c.cameraElevation < 25) {
			float rotAmt = 2.0f;		
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
			}
		}else if(e.getValue() <= -0.2f) {	//ROTATE DOWN
			if(c.cameraElevation > 1) {
			float rotAmt = -2.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
			}
		}
	}
}
