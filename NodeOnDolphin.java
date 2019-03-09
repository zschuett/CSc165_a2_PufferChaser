package myGameEngine;

import a2.MyGame;
import ray.rage.Engine;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

//THIS CONTROLLER CLASS WILL MOVE A PUFFER FISH TO THE PLAYER'S CORNER UPON THE PLAYER TOUCHING IT

public class NodeOnDolphin extends AbstractController{
	SceneNode dolphinN, dolphinN2, pufferFish;
	String temp;
	SceneManager Manny;
	Vector3 pufferPosition;
	Engine Rage;
	private MyGame myGame;
	
	public NodeOnDolphin(MyGame mg, SceneManager sm) {
		myGame = mg;
		Manny = sm;
	}
//CONCURRENT MODIFICATION ERROR
@Override
protected void updateImpl(float elapsedTimeMillis){ 
	for(Node n : super.controlledNodesList) {
		dolphinN  = Manny.getSceneNode("dolphinNode");
		dolphinN2 = Manny.getSceneNode("earthNode");
		
		pufferPosition = n.getLocalPosition();
		temp = n.getName();
		pufferFish = Manny.getSceneNode(temp);
					
		double p1Distance = myGame.DistanceBetween(pufferFish, dolphinN);
	    double p2Distance = myGame.DistanceBetween(pufferFish, dolphinN2);
	    
	    if(p1Distance <= 5.0) {
	    	//dolphinN.attachChild(n);
	    	super.removeNode("planet0E");
	    	System.out.println("I GOT TO PLANET 0");
	    	break;
	    	//System.out.println(n.getName());
	    }else if(p2Distance <= 5.0) {
	    	super.removeNode(n);
	    	break;
	    	//addolphinN2.attachChild(n);
	    }
	  }
   }
}