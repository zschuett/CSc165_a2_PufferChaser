package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitDown extends AbstractInputAction {
	private Camera3P_Control c;
	public OrbitDown(Camera3P_Control rot) {
		c = rot;
	}
	@Override
	public void performAction(float arg0, Event arg1) {
		if(c.cameraElevation >= 1.0f) {
			float rotAmt = -2.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
		}	
	}
}
