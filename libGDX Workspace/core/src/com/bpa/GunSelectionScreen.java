package com.bpa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GunSelectionScreen implements Screen{
	final DunGun game;
	private Texture gunPickScreen;
	public Viewport gamePort;

	private OrthographicCamera cam;
	private Vector3 mouse_position = new Vector3(0, 0, 0);
	public static String weaponSelected;
	private int wait = 0;
	
	public GunSelectionScreen(final DunGun game) {
		this.game = game;
		gunPickScreen = DunGun.manager.get("screens/gunPick.jpg", Texture.class);

		cam = new OrthographicCamera();		
		gamePort = new FitViewport(1500, 800, cam); //fits view port to match map's dimensions (in this case 320x320) and scales. Adds black bars to adjust
		cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0); //centers the map to center of screen
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render(float delta) {
		//clears screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	    
		wait += 1;
		
		cam.unproject(mouse_position); //gets mouse coordinates within viewport
		
		//System.out.println(Gdx.input.getX());
		
	    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && wait >= 30) {
	    	//REVOLVER 
	    	if (mouse_position.x > 570 && mouse_position.x < 933 && mouse_position.y > 546 && mouse_position.y < 610) {
	    		weaponSelected = "revolver";
				game.setScreen(new Level1(game));

	    	}
	    	else if (mouse_position.x > 644 && mouse_position.x < 852 && mouse_position.y > 441 && mouse_position.y < 506) {
	    		weaponSelected = "rifle";
				game.setScreen(new Level1(game));

	    	}
	    	if (mouse_position.x > 33 && mouse_position.x < 153 && mouse_position.y > 34 && mouse_position.y < 83) {
				game.setScreen(new MainMenu(game));

	    	}
	    }
	    game.batch.begin();

		game.batch.draw(gunPickScreen, 0, 0);
		
		game.batch.end();
		cam.update();

	}


	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		
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
		gunPickScreen.dispose();
	}
}