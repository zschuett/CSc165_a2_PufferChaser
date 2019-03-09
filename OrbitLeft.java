package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class OrbitLeft extends AbstractInputAction {
	private Camera3P_Control c;
	public OrbitLeft(Camera3P_Control rot) {
		c = rot;
	}
	@Override
	public void performAction(float arg0, Event arg1) {
		float rotAmt = 2.0f;
		c.cameraAzimuth += rotAmt;
		c.cameraAzimuth = c.cameraAzimuth%360;
		c.updateCameraPosition();
	}
}
