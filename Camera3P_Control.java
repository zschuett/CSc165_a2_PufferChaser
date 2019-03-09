package myGameEngine;

import java.util.ArrayList;

import net.java.games.input.Controller;
import ray.input.InputManager;
import ray.input.action.Action;
import ray.rage.scene.*;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class Camera3P_Control {
	private Camera    camera;	
	private SceneNode cameraN;
	private SceneNode target;
	protected float   cameraAzimuth;
	protected float   cameraElevation;
	protected float   radius;
	private Vector3   targetPos;
	private Vector3   worldUpVec;
	
	
	public Camera3P_Control(Camera cam, SceneNode camN, SceneNode targN, Controller oc, InputManager im) {
		cameraN = camN;
		target  = targN;
		
		cameraAzimuth   = 225.0f;
		cameraElevation = 20.0f;
		radius          = 2.0f;
		
		worldUpVec = Vector3f.createFrom(0.0f,1.0f,0.0f);
		
		setupInput(im, oc);
		
		updateCameraPosition();
		
	}

	private void setupInput(InputManager im, Controller gpName) {
		
		Action orbitActionL  = new OrbitLeft (this);
		Action orbitActionR  = new OrbitRight(this);
		Action orbitActionU	 = new OrbitUp   (this);
		Action orbitActionD  = new OrbitDown (this);
		Action zoomIn        = new ZoomIn    (this);
		Action zoomOut	     = new ZoomOut   (this);
		
		Action orbitActionLR = new OrbitLR_C (this);
		Action orbitActionUD = new OrbitUD_C (this);
		//Action zoomInOut	 = new ZoomIO_C  (this);
		
		Controller c = gpName;
		
			if(c.getType() == Controller.Type.KEYBOARD) {
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.LEFT, orbitActionL,   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.RIGHT,orbitActionR,   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.UP,   orbitActionU,   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.DOWN, orbitActionD,   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.Q,    zoomIn,         InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Key.E,    zoomOut,        InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			}else {
				im.associateAction(c, net.java.games.input.Component.Identifier.Axis.RX,   orbitActionLR, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Axis.RY,   orbitActionUD, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, net.java.games.input.Component.Identifier.Button._5, zoomIn,        InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);//RB
				im.associateAction(c, net.java.games.input.Component.Identifier.Button._4, zoomOut,       InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);//LB
			}
		}
	

	public void updateCameraPosition() {
		double theta = Math.toRadians(cameraAzimuth);
		double phi   = Math.toRadians(cameraElevation);
		
		double x     = radius*Math.cos(phi)*Math.sin(theta);
		double y     = radius*Math.sin(phi);
		double z	 = radius*Math.cos(phi)*Math.cos(theta);
		
		cameraN.setLocalPosition(Vector3f.createFrom((float)x,(float)y,(float)z).add(target.getWorldPosition()));
		cameraN.lookAt(target,worldUpVec);
	}
}
