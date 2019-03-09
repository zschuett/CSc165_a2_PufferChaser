package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class ZoomOut extends AbstractInputAction {
	private Camera3P_Control c;
	public ZoomOut(Camera3P_Control zm) {
		c = zm;
	}
	@Override
	public void performAction(float arg0, Event arg1) {
		if(c.radius <= 3.0f) {
			float zmAmt = 0.15f;
			c.radius += zmAmt;
			//c.radius = c.radius%360;
			c.updateCameraPosition();
		}	
	}
}