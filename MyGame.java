package a2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.rendersystem.shader.GpuShaderProgram;
import ray.rage.util.BufferUtil;

import myGameEngine.*;
import net.java.games.input.Controller;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.action.Action;
import ray.rage.*;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.Texture;
import ray.rage.asset.texture.TextureManager;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.RenderState;
import ray.rage.rendersystem.states.TextureState;
import ray.rage.rendersystem.states.FrontFaceState;
import myGameEngine.*;

public class MyGame extends VariableFrameRateGame {
	public  Camera      camera, camera2;
	
	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	int   scoreP1     = 0;
	int   scoreP2     = 0;
	String elapsTimeStr, counterStr, dispStr, scoreStr;
	int elapsTimeSec, counter = 0;
	
	private InputManager im;
	private Action cameraForward,  cameraPanLeft, cameraPanRight, cameraForward_C, cameraPan_C;
	
	public SceneNode    cameraNode;
	public SceneManager sm;
	public SceneNode    dolphinN, earthN, skyDomeN;
	public  Entity      dolphin;
	SimpleController ntc;
	StretchController sc;

	ArrayList<Node> list = new ArrayList<Node>();
	
	private Camera3P_Control orbitController1, orbitController2;
	
    public MyGame() {
        super();
    }

    public static void main(String[] args) {
        Game game = new MyGame();
        try {
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally { 
            game.shutdown();
            game.exit();
        }
    }
	
	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}
	
	protected void setupWindowViewports(RenderWindow rw){ 
		rw.addKeyListener(this);
		Viewport topViewport = rw.getViewport(0);
		topViewport.setDimensions(.51f, .01f, .99f, .49f); // B,L,W,H
		Viewport botViewport = rw.createViewport(.01f, .01f, .99f, .49f);
	}

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
    	SceneNode rootNode = sm.getRootSceneNode();
    	
        camera = sm.createCamera("MainCamera",Projection.PERSPECTIVE);
    	rw.getViewport(0).setCamera(camera);
    	SceneNode cameraN = rootNode.createChildSceneNode("MainCameraNode");
    	cameraN.attachObject(camera);
    	camera.setMode('n');
    	camera.getFrustum().setFarClipDistance(1000.0f);
    	
    	camera2 = sm.createCamera("MainCamera2",Projection.PERSPECTIVE);
    	rw.getViewport(1).setCamera(camera2);
    	SceneNode cameraN2 = rootNode.createChildSceneNode("MainCamera2Node");
    	cameraN2.attachObject(camera2);
    	camera2.setMode('n');
    	camera2.getFrustum().setFarClipDistance(1000.0f);
    }
    
    protected void setupOrbitCameras(Engine eng, SceneManager sm) {
    	ArrayList <Controller> gpName = im.getControllers(); 
    	
    	          dolphinN = sm.getSceneNode("dolphinNode");
    	SceneNode cameraN  = sm.getSceneNode("MainCameraNode");
                  camera   = sm.getCamera("MainCamera");	
    	
    	          earthN   = sm.getSceneNode("earthNode");
    	SceneNode cameraN2 = sm.getSceneNode("MainCamera2Node");
    	          camera2  = sm.getCamera("MainCamera2");
    	          	
    	for(Controller oc : gpName) {
    		if(oc.getType() == Controller.Type.KEYBOARD) {
    	orbitController1   = new Camera3P_Control(camera, cameraN, dolphinN, oc, im);
    		}else {
    	orbitController2   = new Camera3P_Control(camera2, cameraN2, earthN, oc, im);
    		}
    	}
    	
    }
    
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
    	
    im = new GenericInputManager();
    

     // dolphin avatar for player in the top window
     Entity dolphinE = sm.createEntity("dolphin", "dolphinHighPoly.obj");
     dolphinE.setPrimitive(Primitive.TRIANGLES);
     dolphinN =
     sm.getRootSceneNode().createChildSceneNode("dolphinNode");
     dolphinN.attachObject(dolphinE);
     dolphinN.setLocalPosition(30.0f, 0.0f, 30.0f);
     dolphinN.setLocalScale(0.55f, 0.55f, 0.55f);
     dolphinN.yaw(Degreef.createFrom(180));
     // earth avatar for player in the bottom window
     Entity earthE = sm.createEntity("earth", "dolphinHighPoly.obj");
     earthE.setPrimitive(Primitive.TRIANGLES);
     earthN =
     sm.getRootSceneNode().createChildSceneNode("earthNode");
     earthN.attachObject(earthE);
     earthN.setLocalPosition(-30.0f, 0.0f, -30.0f);
     earthN.setLocalScale(0.55f, 0.55f, 0.55f);
     
     
     sm.getAmbientLight().setIntensity(new Color(.899f, .8f, .78f)); //AMBIENT
	Light plight = sm.createLight("testLamp1", Light.Type.POINT);//POSITIONAL V
		plight.setAmbient(new Color(.3f, .3f, .3f));
     plight.setDiffuse(new Color(.7f, .7f, .7f));
		plight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
     plight.setRange(5f); 
		
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
     plightNode.attachObject(plight);
     
//~~~~~~~~~~~~~~~~~~~~~~~~~~MAKE SKYDOME~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
     Entity skyDomeE = sm.createEntity("skydome", "sphere.obj");
     skyDomeE.setPrimitive(Primitive.TRIANGLES);
     
     
     SceneNode skyDomeN = sm.getRootSceneNode().createChildSceneNode(skyDomeE.getName()+ "Node");
     skyDomeN.setLocalScale(50.0f,50.0f,50.0f);
     skyDomeN.setLocalPosition(0.0f,0.0f,0.0f);
     skyDomeN.attachObject(skyDomeE);
 
     Texture      skyDomeTex = sm.getTextureManager().getAssetByPath("inkBlot.jpg");
     TextureState sdTexState = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
     sdTexState.setTexture(skyDomeTex);
     
     FrontFaceState skyDomeFFS = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
     skyDomeFFS.setVertexWinding(FrontFaceState.VertexWinding.CLOCKWISE);
     
     skyDomeE.setRenderState(skyDomeFFS);
     skyDomeE.setRenderState(sdTexState); 
     
     RotationController rcy = new RotationController(Vector3f.createUnitVectorY(), .02f);
     RotationController rcx = new RotationController(Vector3f.createUnitVectorZ(), .001f);
     rcy.addNode(skyDomeN);
     rcx.addNode(skyDomeN);
     sm.addController(rcy);
     //sm.addController(rcx);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
     
     TextureManager tm = eng.getTextureManager();
     Texture redTexture = tm.getAssetByPath("hexagons.jpeg");
     RenderSystem rs = sm.getRenderSystem();
     TextureState state = (TextureState)
     rs.createRenderState(RenderState.Type.TEXTURE);
     state.setTexture(redTexture);
     dolphinE.setRenderState(state);
     
//~~~~~~~~~~~~~~~~~~~~~~~~~~MAKE PLANETS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//        
        Entity planet0E = sm.createEntity("planet0", "sphere.obj");
        planet0E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet1E = sm.createEntity("planet1", "sphere.obj");
        planet1E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet2E = sm.createEntity("planet2", "sphere.obj"); 
        planet2E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet3E = sm.createEntity("planet3", "sphere.obj"); 
        planet3E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet4E = sm.createEntity("planet4", "sphere.obj"); 
        planet4E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet5E = sm.createEntity("planet5", "sphere.obj"); 
        planet5E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet6E = sm.createEntity("planet6", "sphere.obj"); 
        planet6E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet7E = sm.createEntity("planet7", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet8E = sm.createEntity("planet8", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet9E = sm.createEntity("planet9", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
       
        Entity planet10E = sm.createEntity("planet10", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet11E = sm.createEntity("planet11", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet12E = sm.createEntity("planet12", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet13E = sm.createEntity("planet13", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet14E = sm.createEntity("planet14", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);
        
        Entity planet15E = sm.createEntity("planet15", "sphere.obj"); 
        planet7E.setPrimitive(Primitive.TRIANGLES);  
 
        SceneNode planet0N  = sm.getRootSceneNode().createChildSceneNode(planet0E.getName() + "Node");
        SceneNode planet1N  = sm.getRootSceneNode().createChildSceneNode(planet1E.getName() + "Node");
        SceneNode planet2N  = sm.getRootSceneNode().createChildSceneNode(planet2E.getName() + "Node");
        SceneNode planet3N  = sm.getRootSceneNode().createChildSceneNode(planet3E.getName() + "Node");
        SceneNode planet5N  = sm.getRootSceneNode().createChildSceneNode(planet4E.getName() + "Node");
        SceneNode planet4N  = sm.getRootSceneNode().createChildSceneNode(planet5E.getName() + "Node");
        SceneNode planet6N  = sm.getRootSceneNode().createChildSceneNode(planet6E.getName() + "Node");
        SceneNode planet7N  = sm.getRootSceneNode().createChildSceneNode(planet7E.getName() + "Node");
        SceneNode planet8N  = sm.getRootSceneNode().createChildSceneNode(planet8E.getName() + "Node");
        SceneNode planet9N  = sm.getRootSceneNode().createChildSceneNode(planet9E.getName() + "Node");
        SceneNode planet10N = sm.getRootSceneNode().createChildSceneNode(planet10E.getName() + "Node");
        SceneNode planet11N = sm.getRootSceneNode().createChildSceneNode(planet11E.getName() + "Node");
        SceneNode planet12N = sm.getRootSceneNode().createChildSceneNode(planet12E.getName() + "Node");
        SceneNode planet13N = sm.getRootSceneNode().createChildSceneNode(planet13E.getName() + "Node");
        SceneNode planet14N = sm.getRootSceneNode().createChildSceneNode(planet14E.getName() + "Node");
        SceneNode planet15N = sm.getRootSceneNode().createChildSceneNode(planet15E.getName() + "Node");
      

        Texture      planet0Tex = sm.getTextureManager().getAssetByPath("giantPufferSkin.jpg");
        TextureState pufferTex = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        pufferTex.setTexture(planet0Tex);        

        planet0N.setLocalPosition(15f, 0.0f, 15f);
        planet0N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet0N.attachObject(planet0E);
        planet0E.setRenderState(pufferTex);

        planet1N.setLocalPosition(25f,0.0f,25f);
        planet1N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet1N.attachObject(planet1E);
        planet1E.setRenderState(pufferTex);

        planet2N.setLocalPosition(35f, 0.0f, 35f);
        planet2N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet2N.attachObject(planet2E);
        planet2E.setRenderState(pufferTex);

        planet3N.setLocalPosition(45f, 0.0f, 45f);
        planet3N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet3N.attachObject(planet3E);
        planet3E.setRenderState(pufferTex);

        planet4N.setLocalPosition(-15f, 0.0f, 15f);
        planet4N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet4N.attachObject(planet4E);
        planet4E.setRenderState(pufferTex);

        planet5N.setLocalPosition(-25f, 0.0f, 25f);
        planet5N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet5N.attachObject(planet5E);
        planet5E.setRenderState(pufferTex);

        planet6N.setLocalPosition(-35f, 0.0f, 35f);
        planet6N.setLocalScale(0.2f, 0.02f, 0.2f);
        planet6N.attachObject(planet6E);
        planet6E.setRenderState(pufferTex);

        planet7N.setLocalPosition(-45f, 0.0f, 45f);
        planet7N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet7N.attachObject(planet7E);
        planet7E.setRenderState(pufferTex);
        
        planet8N.setLocalPosition(-15f, 0.0f, -15f);
        planet8N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet8N.attachObject(planet8E);
        planet8E.setRenderState(pufferTex);
        
        planet9N.setLocalPosition(-25f, 0.0f, -25f);
        planet9N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet9N.attachObject(planet9E);
        planet9E.setRenderState(pufferTex);
        
        planet10N.setLocalPosition(-0.35f, 0.0f, -35f);
        planet10N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet10N.attachObject(planet10E);
        planet10E.setRenderState(pufferTex);
        
        planet11N.setLocalPosition(-45f, 0.0f, -45f);
        planet11N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet11N.attachObject(planet11E);
        planet11E.setRenderState(pufferTex);
        
        planet12N.setLocalPosition(15f, 0.0f, -15f);
        planet12N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet12N.attachObject(planet12E);
        planet12E.setRenderState(pufferTex);
        
        planet13N.setLocalPosition(25f, 0.0f, -25f);
        planet13N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet13N.attachObject(planet13E);
        planet13E.setRenderState(pufferTex);
        
        planet14N.setLocalPosition(35f, 0.0f, -35f);
        planet14N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet14N.attachObject(planet14E);
        planet14E.setRenderState(pufferTex);
        
        planet15N.setLocalPosition(45f, 0.0f, -45f);
        planet15N.setLocalScale(0.2f, 0.2f, 0.2f);
        planet15N.attachObject(planet15E);
        planet15E.setRenderState(pufferTex);
      
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//        
        list.add(planet0N);
        list.add(planet1N);
        list.add(planet2N); 
        list.add(planet3N); 
        list.add(planet4N); 
        list.add(planet5N); 
        list.add(planet6N); 
        list.add(planet7N); 
        list.add(planet8N);
        list.add(planet9N);
        list.add(planet10N); 
        list.add(planet11N); 
        list.add(planet12N); 
        list.add(planet13N); 
        list.add(planet14N); 
        list.add(planet15N);
        //list.add(skyDomeN);
        
        ntc = new SimpleController();
        ntc.addNode(dolphinN);
        ntc.addNode(earthN);
        
        sm.addController(ntc);
        

        
        sc = new StretchController(); 
        sc.addNode(dolphinN);
        sc.addNode(earthN);
        

        
        sm.addController(sc);

        
        setupOrbitCameras(eng, sm);
        dolphinN.yaw(Degreef.createFrom(45.0f));
        
        ManualObject sea = makeGroundPlane(eng, sm);
        sea.setPrimitive(Primitive.TRIANGLES);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~HEIRARCHY~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//  
        SceneNode seaN = skyDomeN.createChildSceneNode("waves");
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        seaN.attachObject(sea);
        
       	setupInputs(sm);
    }
	protected ManualObject makeGroundPlane(Engine eng, SceneManager sm) throws IOException{ 
		
		ManualObject sea = sm.createManualObject("sea");
		ManualObjectSection seaSec = sea.createManualSection("seaSection");
		sea.setGpuShaderProgram(sm.getRenderSystem().
		getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

			float[] vertices = new float[]
			{ 
				-50.0f, -0.03f, -50.0f, 
				 50.0f, -0.03f,  50.0f, 
				-50.0f, -0.03f,  50.0f, 
				 50.0f, -0.03f,  50.0f, 
				-50.0f, -0.03f, -50.0f, 
				 50.0f, -0.03f, -50.0f 
			};

			float[] texcoords = new float[]
			{
				0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
			};

			float[] normals = new float[]
			{ 
				0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
				0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f
			};

			int[] indices = new int[] { 0,1,2,3,4,5 };

			FloatBuffer vertBuf  = BufferUtil.directFloatBuffer(vertices);
			FloatBuffer texBuf   = BufferUtil.directFloatBuffer(texcoords);
			FloatBuffer normBuf  = BufferUtil.directFloatBuffer(normals);
			IntBuffer   indexBuf = BufferUtil.directIntBuffer(indices);

			seaSec.setVertexBuffer(vertBuf);
			seaSec.setTextureCoordsBuffer(texBuf);
			seaSec.setNormalsBuffer(normBuf);
			seaSec.setIndexBuffer(indexBuf);

			Texture tex = eng.getTextureManager().getAssetByPath("waves.jpg");
			TextureState texState = (TextureState)sm.getRenderSystem().
			createRenderState(RenderState.Type.TEXTURE);
			texState.setTexture(tex);
			FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().
			createRenderState(RenderState.Type.FRONT_FACE);
			faceState.setVertexWinding(FrontFaceState.VertexWinding.CLOCKWISE);

			sea.setDataSource(DataSource.INDEX_BUFFER);
			sea.setRenderState(texState);
			sea.setRenderState(faceState);
			return sea;
}
   protected void setupInputs(SceneManager sm) {    	
    	System.out.println( sm.getSceneNodes());

    	ArrayList<Controller> controllers = im.getControllers();
    	earthN = sm.getSceneNode("earthNode");
    	cameraForward     = new MoveForwardAction    (dolphinN);
    	cameraPanLeft     = new PanLeftAction	     (dolphinN);
    	cameraPanRight    = new PanRightAction       (dolphinN);
    	
//_________________________HANDLE CONTROLLER________________________//
    	cameraPan_C       = new PanLRAction_C      	 (earthN);
    	cameraForward_C   = new MoveForwardAction_C	 (earthN);
//__________________________________________________________________//
    	
    for(Controller c : controllers) {
    	if(c.getType() == Controller.Type.KEYBOARD) {
    //~~~~~~~~~~~~~~~~~~~~~Keyboard Stuff~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        	im.associateAction(c, net.java.games.input.Component.Identifier.Key.W, cameraForward,
        			   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        	im.associateAction(c, net.java.games.input.Component.Identifier.Key.A, cameraPanLeft,
    				   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        	im.associateAction(c, net.java.games.input.Component.Identifier.Key.D, cameraPanRight,
    				   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        	
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    	}	else if(c.getType() == Controller.Type.GAMEPAD) {
    //~~~~~~~~~~~~~~~~~~~~~GamePad Stuff~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    	    im.associateAction(c, net.java.games.input.Component.Identifier.Axis.Y, cameraForward_C,
 	 			       InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
 	 	    im.associateAction(c, net.java.games.input.Component.Identifier.Axis.X, cameraPan_C,
 					   InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); 	 	    
 	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
         }
      }
   }
    
    protected void update(Engine engine)	{// build and set both HUDs
    	
    	rs = (GL4RenderSystem) engine.getRenderSystem();
    	elapsTime += engine.getElapsedTimeMillis();
    	elapsTimeSec = Math.round(elapsTime/1000.0f);
    	elapsTimeStr = Integer.toString(elapsTimeSec);
    	dispStr = "P2 Time =     " + elapsTimeStr + "     SCORE:     " + scoreP2;
    	rs.setHUD(dispStr, 15, 15);
    	dispStr = "P1 Time =     " + elapsTimeStr + "     SCORE:     " + scoreP1;
    	rs.setHUD2(dispStr, 15, rs.getRenderWindow().getViewport(0).getActualBottom());
    	// tell the input manager to process the inputs
    	im.update(elapsTime);

    	CollisionDetector();
    	
    	orbitController1.updateCameraPosition(); 
    	orbitController2.updateCameraPosition();} 
    
    
	public double DistanceBetween(Node temp, SceneNode d) {
    	
    	Vector3f scenePo = (Vector3f)temp.getLocalPosition();

		Vector3f camPo = (Vector3f)d.getLocalPosition();
		
		
		double	dx = (double)camPo.x(); 
		double	dy = (double)camPo.y();
		double	dz = (double)camPo.z();
		
		double	ex = (double)scenePo.x(); 
		double	ey = (double)scenePo.y();
		double	ez = (double)scenePo.z();
		
		double distance1=(double)Math.sqrt(  (Math.pow((dx-ex),2)) +  (Math.pow((dy-ey),2)) + (Math.pow(dz-ez,2)) ) ;//planet1 to camera
		return distance1;
	
    }
    private void CollisionDetector(){
    	Node puffer;
    	for(int i =0; i<list.size(); i++) {
    		puffer=list.get(i);
    		if(DistanceBetween(puffer,dolphinN) < 1) {
    			scoreP1++;
    			sc.addNode(puffer);
    		}else if(DistanceBetween(puffer,earthN) < 1) {
    			scoreP2++;
    			sc.addNode(puffer);
    		}
    		
    	}
    }
}