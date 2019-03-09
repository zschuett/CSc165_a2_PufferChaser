package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rage.scene.SceneNode;
import ray.rml.Angle;
import ray.rml.Degreef;


public class PanLeftAction extends AbstractInputAction {
	private SceneNode avN;
	
	public PanLeftAction(SceneNode d) {
		avN = d;
	}
	
	@Override
	public void performAction(float time, Event e) {
		       Angle rotAmt = Degreef.createFrom(5.0f);
		       avN.yaw(rotAmt);
		       System.out.println("PANNING LEFT || MODE : 'N'");			
	  }
}
