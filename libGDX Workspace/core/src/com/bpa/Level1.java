package com.bpa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//TO INCLUDE PPM GO TO EPISODE 8 *****************************************
// ********************************************

public class Level1 implements Screen{
	final DunGun game;
	//TMapLocations level1Map;
	public OrthographicCamera cam;
	public Viewport viewport;
	private TmxMapLoader maploader; //what loads map into game
	private TiledMap map; 
	private OrthogonalTiledMapRenderer mapRenderer; //renders map to the screen
	private TextureAtlas textureAtlas;
	//Sprite p1;
	TextureRegion textureRegion;
	MapLayer objectLayer;
	public static Vector3 mouse_position = new Vector3(0, 0, 0);

	//Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr; //graphical representation of body fixtures
	private PlayerOne playerOne;

	
	
	//private int[] layerBackround = {0, 1, 2, 3};
	//private int[] layerAfterBackground = {4};
	
	public Level1(final DunGun game) {
		
		
		this.game = game;

		
		cam = new OrthographicCamera();		
		viewport = new FitViewport(DunGun.V_WIDTH / DunGun.PPM, DunGun.V_HEIGHT / DunGun.PPM, cam); //fits view port to match map's dimensions (in this case 320x320) and scales. Adds black bars to adjust
		maploader = new TmxMapLoader();
		map = maploader.load("tileMaps/Level1/customset.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / DunGun.PPM);
		textureAtlas = new TextureAtlas(Gdx.files.internal("sprites/TDPlayer.atlas"));

        //Box2d variables
		world = new World(new Vector2(0, 0), true); // no gravity and yes we want to sleep objects (won't calculate simulations for bodies at rest)
		b2dr = new Box2DDebugRenderer();
		playerOne = new PlayerOne(world); //must be created after world creation or will crash

		new B2DWorldCreator(world, map);
		cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
		//Gdx.input.setInputProcessor((InputProcessor) p1);
		cam.zoom -= .50;
	}
	
	
	public void cameraUpdate(float delta) {

		//timeStep = 60 times a second, velocity iterations = 6, position iterations = 2
		world.step(1/60f, 6, 2); //tells game how many times per second for Box2d to make its calculations
		System.out.println(playerOne.b2body.getPosition().x + " " + playerOne.b2body.getPosition().y);
		cam.position.x = playerOne.b2body.getPosition().x;
		cam.position.y = playerOne.b2body.getPosition().y;

		cam.update();
	}
	
	@Override
	public void render(float delta) {
        cameraUpdate(delta);
        playerOne.handleInput(delta);
		//clears screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	 	if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			cam.zoom -= .01;

		}
	 	if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			cam.zoom += .01;
			
		}
		
        
        mapRenderer.render();
        b2dr.render(world, cam.combined); //renders the Box2d world

        mapRenderer.setView(cam);
        //render our game map
        //mapRenderer.render(); // renders map
		//mapRenderer.render(layerBackround); //renders layer in Tiled that p1 covers
        game.batch.setProjectionMatrix(cam.combined); //keeps player sprite from doing weird out of sync movement
		
        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouse_position); //gets mouse coordinates within viewport
        game.batch.begin(); //starts sprite spriteBatch

        playerOne.renderSprite(game.batch);

        game.batch.end(); //starts sprite spriteBatch
        //mapRenderer.render(layerAfterBackground); //renders layer of Tiled that hides p1
	}


	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true); //updates the viewport camera
		//float pW = p1.getWidth(); //Keeps p1 scaled
		//float pH = p1.getHeight(); // ^
		//p1.setSize(pW, pH); // Keeps players size matched regardless of zoom
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		map.dispose();
		mapRenderer.dispose();
		textureAtlas.dispose();
		world.dispose();
		b2dr.dispose();
		
	}

	@Override
	public void show() {

		
	}
	
}
