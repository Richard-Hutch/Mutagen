package com.bpa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class ControlScreen implements Screen {
	final Mutagen game;
	Texture controlScreen;
	private OrthographicCamera cam;
	private Viewport gamePort;
	boolean justClicked = false;
	boolean onCrash = true;
	private Vector3 mouse_position = new Vector3(0, 0, 0);

	
	public ControlScreen(final Mutagen game) {
		this.game = game;
		controlScreen = new Texture("screens/controlScreen.jpg");
		cam = new OrthographicCamera();		
		gamePort = new FitViewport(Mutagen.V_WIDTH, Mutagen.V_HEIGHT, cam); //fits view port to match map's dimensions (in this case 320x320) and scales. Adds black bars to adjust
		cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0); //centers the map to center of screen
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0 , 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mouse_position);
		
		game.batch.begin();
		game.batch.setProjectionMatrix(cam.combined);
		
		game.batch.draw(controlScreen, 0, 0);
		
		
		float mX = mouse_position.x;
		float mY = mouse_position.y;
		
		//RETURNS TO MAIN MENU
	
		
		cam.update();
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);		
	}

	@Override
	public void pause() {
	
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
		game.batch.dispose();
		controlScreen.dispose();
		}

}
