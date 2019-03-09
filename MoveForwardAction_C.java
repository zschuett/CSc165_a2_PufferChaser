package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.Node;
import ray.rage.scene.SceneNode;

public class MoveForwardAction_C extends AbstractInputAction {
	private Node avN;
	
	public MoveForwardAction_C(Node d) {
		avN = d;
	}
	@Override
	public void performAction(float time, Event e) {
			if(e.getValue() >= 0.2f) {
				avN.moveForward(-0.2f);
				System.out.println("Moving Forward || MODE: 'N'");
			}
		}
	}
