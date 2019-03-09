package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitLR_C extends AbstractInputAction {
	private Camera3P_Control c;
	public OrbitLR_C(Camera3P_Control rot) {
		c = rot;
	}
	@Override
	public void performAction(float time, Event e) {
		if(e.getValue() >= 0.2f) {			//ROTATE RIGHT
			float rotAmt = -2.0f;		
			c.cameraAzimuth += rotAmt;
			c.cameraAzimuth = c.cameraAzimuth%360;
			c.updateCameraPosition();
		}else if(e.getValue() <= -0.2f) {	//ROTATE LEFT
			float rotAmt = 2.0f;
			c.cameraAzimuth += rotAmt;
			c.cameraAzimuth = c.cameraAzimuth%360;
			c.updateCameraPosition();
		}
	}
}
