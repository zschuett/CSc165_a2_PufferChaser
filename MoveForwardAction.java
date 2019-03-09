package myGameEngine;

import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.SceneNode;

public class MoveForwardAction extends AbstractInputAction {
	private SceneNode avN;
	
	public MoveForwardAction(SceneNode d) {
		avN = d;
	}
	@Override
	public void performAction(float time, Event e) {
			System.out.println("Moving Forward || KEYBOARD");
			avN.moveForward(0.2f);
		}
}
