package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;

public class ZoomIO_C extends AbstractInputAction {
	private Camera3P_Control c;
	public ZoomIO_C(Camera3P_Control zm) {
		c = zm;
	}
	@Override
	public void performAction(float time, Event e) {
     		//ZOOM IN
			if(c.cameraElevation < 25) {
			float rotAmt = 2.0f;		
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
			}
			//ZOOM IN
			else if(c.cameraElevation > 1) {
			float rotAmt = -2.0f;
			c.cameraElevation += rotAmt;
			c.cameraElevation = c.cameraElevation%360;
			c.updateCameraPosition();
			}
		}
	}

