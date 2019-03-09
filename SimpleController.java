package myGameEngine;
import ray.rage.scene.Node;
import ray.rage.scene.controllers.AbstractController;
import ray.rml.Angle;
import ray.rml.Degreef;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class SimpleController extends AbstractController{
	private float cycleTime = 2000.0f; // default cycle time
	private float totalTime = 0.0f;
	private float direction = .025f;

	@Override
	protected void updateImpl(float elapsedTimeMillis){
		totalTime += elapsedTimeMillis;
		if (totalTime > cycleTime)
		{ 
		direction = -direction;
		totalTime = 0.0f;
		}
		for (Node n : super.controlledNodesList)
		{ 
			n.moveUp(direction);
        }
   }
}

