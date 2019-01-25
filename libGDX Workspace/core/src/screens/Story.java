package screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
 
import BackEnd.Mutagen;
import levels.Level1;

public class Story implements Screen, InputProcessor{
	final Mutagen game;
	public Viewport gamePort;
	private Texture terminal;
	private OrthographicCamera cam;
	private Vector3 mousePosition = new Vector3(0, 0, 0);
	private boolean buttonPressed = false;
	private float mX, mY;
	public static int difficulty;
	private Sound terminalSFX, staticSFX;
	
	public Story(final Mutagen game) {
		this.game = game;
		cam = new OrthographicCamera();		
		gamePort = new StretchViewport(1500, 800, cam); //fits view port to match map's dimensions (in this case 320x320) and scales. Adds black bars to adjust
		cam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		Gdx.input.setInputProcessor(this);
		
		terminalSFX = Mutagen.manager.get("screens/story/terminalSFX.mp3");
		staticSFX = Mutagen.manager.get("screens/story/static.mp3");
		terminal = Mutagen.manager.get("screens/story/Terminal.png");
		terminal.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		terminalSFX.play(Mutagen.sfxVolume);
		staticSFX.play(Mutagen.sfxVolume);
		staticSFX.loop();
	}

	@Override
	public void render(float delta) {

		//clears screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mousePosition); //gets mouse coordinates within viewport

		mX = mousePosition.x;
		mY = mousePosition.y;
		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();

		//IMAGE OF THE TERMINAL
		game.batch.draw(terminal, 0, 0);	


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
		game.batch.dispose();

	}	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if (!buttonPressed) {
			terminalSFX.stop();
			staticSFX.stop();
			Mutagen.clicking();
			
			//continue to level 1
			game.setScreen(new Level1(game));


			//normal
			//challenging
		}

		buttonPressed = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		buttonPressed = false;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

}


