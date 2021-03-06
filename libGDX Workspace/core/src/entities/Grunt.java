package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import BackEnd.LogFileHandler;
import BackEnd.Mutagen;
import screens.PlayerMode;

public class Grunt extends Sprite implements Disposable{
	public World world; // world player will live in
	public Body b2body; //creates body for player
	private BodyDef bdef = new BodyDef();
	public int health = 100;
	float angle2;
	private TextureAtlas gruntAtkAtlas, gruntDamagedAtlas;
	private Animation <TextureRegion> gruntAtkAnimation;
	private Animation <TextureRegion> gruntDamagedAnimation;
	private float timePassed = 0, runSpeed = 1.5f, differenceX, differenceY, oldX, oldY;
	private TextureRegion gruntStandingRegion;
	private Sound atkSwoosh;
	public boolean attack = false, contAtk = false, tookDamage = false;
	public int atkdmg = 8, target, aimTarget;
	private boolean initialDmg = false, animationFinished = false; //makes sure the player takes damage at first when the enemy touches player
	public static Array<Grunt> grunts = new Array<Grunt>();
	public static Vector2 gruntPos = new Vector2(0,0);
	private Texture blood;
	LogFileHandler lfh = new LogFileHandler();

	public Grunt(World world) {
		try {
			this.world = world;
			gruntAtkAtlas = Mutagen.manager.get("sprites/grunt/mutantAtkAnimation.atlas");
			gruntAtkAnimation = new Animation <TextureRegion>(1f/15f, gruntAtkAtlas.getRegions());			
			gruntStandingRegion = gruntAtkAtlas.findRegion("tile000");
			gruntDamagedAtlas = Mutagen.manager.get("sprites/grunt/gruntDamaged.atlas");
			gruntDamagedAnimation = new Animation <TextureRegion>(1f/15f, gruntDamagedAtlas.getRegions());
			atkSwoosh = Mutagen.manager.get("sound effects/enemies/gruntSwoosh.mp3");
			blood = Mutagen.manager.get("sprites/MutantBlood.png");

			defineGrunt();
			
		} catch (Exception e) {
			//Logs that this method of this class triggered an exception
			String name = Thread.currentThread().getStackTrace()[1].getMethodName();
			lfh.fileLog(this.getClass().getSimpleName() + " ", name + " ", "ERROR");
		}
	}

	public void defineGrunt() {
		try {
			//define player body

			bdef.position.set(gruntPos);
			bdef.type = BodyDef.BodyType.DynamicBody;
			//create body in the world
			b2body = world.createBody(bdef);
			b2body.setLinearDamping(5f);
			b2body.setUserData(this);
			FixtureDef fdef = new FixtureDef();
			CircleShape shape = new CircleShape();
			shape.setRadius(10 / Mutagen.PPM);
			fdef.density = 400;
			fdef.shape = shape;
			fdef.filter.categoryBits = Mutagen.ENEMY;
			fdef.filter.maskBits = Mutagen.PLAYER | Mutagen.WALL | Mutagen.BULLET | Mutagen.SHOOT_OVER; //collides with everything
			b2body.createFixture(fdef).setUserData("grunt");
			
		} catch (Exception e) {
			//Logs that this method of this class triggered an exception
			String name = Thread.currentThread().getStackTrace()[1].getMethodName();
			lfh.fileLog(this.getClass().getSimpleName() + " ", name + " ", "ERROR");
		}
	}

	public void renderSprite(SpriteBatch batch) {
		try {
			if (this.health > 0){

				//Selects which player to attack depending on which player is closer by using the pythagorean theorem
				if (!PlayerMode.OneP) {
					//PlayerOne
					float differencePlayerX =  PlayerOne.p1PosX - b2body.getPosition().x;
					float differencePlayerY =  PlayerOne.p1PosY - b2body.getPosition().y;

					//PlayerTwo
					float differencePlayer2X =  PlayerTwo.p2PosX - b2body.getPosition().x;
					float differencePlayer2Y =  PlayerTwo.p2PosY - b2body.getPosition().y;

					float player1Dif = (float) Math.sqrt((differencePlayerX*differencePlayerX)+(differencePlayerY*differencePlayerY));
					float player2Dif = (float) Math.sqrt((differencePlayer2X*differencePlayer2X)+(differencePlayer2Y*differencePlayer2Y));

					if (player1Dif < player2Dif) {
						differenceX = PlayerOne.p1PosX - b2body.getPosition().x;
						differenceY = PlayerOne.p1PosY - b2body.getPosition().y;
					}else {
						differenceX = PlayerTwo.p2PosX - b2body.getPosition().x;
						differenceY = PlayerTwo.p2PosY - b2body.getPosition().y;
					}
				}else {
					differenceX = PlayerOne.p1PosX - b2body.getPosition().x;
					differenceY = PlayerOne.p1PosY - b2body.getPosition().y;
				}

				angle2 = MathUtils.atan2(differenceY, differenceX);
				float angle = angle2 * MathUtils.radDeg;	
				angle = angle - 90; //makes it a full 360 degrees
				if (angle < 0) {
					angle += 360 ;
				}
				float posX = this.b2body.getPosition().x;
				float posY = this.b2body.getPosition().y;
				float gposX = (float) (Math.cos(angle2)) * runSpeed;
				float gposY = (float) (Math.sin(angle2)) * runSpeed;

				if (!tookDamage && !attack && !contAtk && animationFinished) {
					batch.draw(gruntStandingRegion, posX - .17f, posY - .13f, 20 / Mutagen.PPM, 10 / Mutagen.PPM, 40 / Mutagen.PPM, 32 / Mutagen.PPM, 1, 1, angle);
				}
				else if (tookDamage) {

					batch.draw(gruntDamagedAnimation.getKeyFrame(timePassed), posX - .20f, posY -.27f, 20 / Mutagen.PPM, 25 / Mutagen.PPM, 40 / Mutagen.PPM, 50 / Mutagen.PPM, 1.18f, 1.18f, angle);
					timePassed += Gdx.graphics.getDeltaTime();
					if(gruntDamagedAnimation.isAnimationFinished(timePassed)) {
						timePassed = 0;
						tookDamage = false;
					}
				}

				else {
					if (!initialDmg) {
						if (target == 1) {
							PlayerOne.player1HP -= atkdmg;						
						}
						else {
							PlayerTwo.player2HP -= atkdmg;
						}
						initialDmg = true;
					}
					batch.draw(gruntAtkAnimation.getKeyFrame(timePassed), posX - .17f, posY - .13f, 20 / Mutagen.PPM, 10 / Mutagen.PPM, 40 / Mutagen.PPM, 32 / Mutagen.PPM, 1, 1, angle);
					timePassed += Gdx.graphics.getDeltaTime();
					animationFinished = false;

					if(gruntAtkAnimation.isAnimationFinished(timePassed)) {
						if (Mutagen.sfxVolume != 0) {
							long aSId = atkSwoosh.play(Mutagen.sfxVolume);
						}
						timePassed = 0;
						initialDmg = false;
						animationFinished = true;
					}
				}


				if (!PlayerMode.OneP) {
					if (!PlayerOne.p1Dead) {
						b2body.applyLinearImpulse(gposX, gposY, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);	
						b2body.setTransform(this.b2body.getPosition().x, this.b2body.getPosition().y, angle2); //sets the position of the body to the position of the body and implements rotation
					}
					else if (!PlayerTwo.p2Dead) {
						b2body.applyLinearImpulse(gposX, gposY, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
						b2body.setTransform(this.b2body.getPosition().x, this.b2body.getPosition().y, angle2); //sets the position of the body to the position of the body and implements rotation

					}
				}
				else {
					if (!PlayerOne.p1Dead) {
						b2body.applyLinearImpulse(gposX, gposY, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);	
						b2body.setTransform(this.b2body.getPosition().x, this.b2body.getPosition().y, angle2); //sets the position of the body to the position of the body and implements rotation
					}
				}

				oldX = this.b2body.getPosition().x;
				oldY = this.b2body.getPosition().y;
			}else {
				batch.draw(blood, oldX, oldY, 32 / Mutagen.PPM, 32 / Mutagen.PPM);
			}
			
		} catch (Exception e) {
			//Logs that this method of this class triggered an exception
			String name = Thread.currentThread().getStackTrace()[1].getMethodName();
			lfh.fileLog(this.getClass().getSimpleName() + " ", name + " ", "ERROR");
		}

	}


	@Override
	public void dispose() {
	}


}