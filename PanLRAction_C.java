package myGameEngine;

import a2.MyGame;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rage.scene.SceneNode;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class PanLRAction_C extends AbstractInputAction{
	private Node avN;
	
	public PanLRAction_C(SceneNode d) {
		avN = d;
	}
	@Override
	public void performAction(float time, Event e) {
		if(e.getValue() >= 0.2f) {
		       Angle rotAmt = Degreef.createFrom(-5.0f);
		       avN.yaw(rotAmt);
		       
		       System.out.println("PANNING RIGHT || MODE : 'N'");       
		}else if(e.getValue() <= -0.2f) {
			       Angle rotAmt = Degreef.createFrom(5.0f);
			       avN.yaw(rotAmt);
			       
			       System.out.println("PANNING LEFT || MODE : 'N'");
				}else {}
	}
}
