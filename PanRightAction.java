package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rage.scene.SceneNode;
import ray.rml.Angle;
import ray.rml.Degreef;


public class PanRightAction extends AbstractInputAction{
	private SceneNode avN;
	
	public PanRightAction(SceneNode d) {
		avN = d;
	}
	
	@Override
	public void performAction(float time, Event e) {
		       Angle rotAmt = Degreef.createFrom(-5.0f);
		       avN.yaw(rotAmt);
		       
		       System.out.println("PANNING RIGHT || MODE : 'N'");       
	  }
}
