package com.study.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
	final MyGame game;
	OrthographicCamera camera;
	Texture img;
	Texture img_red;
	Texture bucketImage;
	Sound gamesound;
	Music bombsound;
	Music rainMusic;
	Rectangle bucket;
	Vector3 touchPos;
	Array<Rectangle> raindrops;
	Array<Rectangle> bombs;
	long lastDropTime;
	long lastDropTimeb;
	int dropsGatchered;


	public GameScreen (final MyGame gam) {
		this.game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,480);

		touchPos = new Vector3();


		bucketImage = new Texture("bucket.png");
		img = new Texture("droplet.png");
		img_red = new Texture("droplet_red.png");
		gamesound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		bombsound = Gdx.audio.newMusic(Gdx.files.internal("bolshoy_vzryv.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));

        rainMusic.setLooping(true);
        rainMusic.play();

		bucket = new Rectangle();
		bucket.x = 800/2 - 64/2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		bombs = new Array<Rectangle>();
		if(dropsGatchered>5) spawnbomb();

	}


	private void spawnRaindrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0,800-64);
		raindrop.y = 480;
		raindrop.width =64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();

	}

	private void spawnbomb(){
		Rectangle bomb = new Rectangle();
		bomb.x = MathUtils.random(0,800-64);
		bomb.y = 480;
		bomb.width =64;
		bomb.height = 64;
		bombs.add(bomb);
		lastDropTimeb = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		game.font.draw(game.batch, "Drops Collected " + dropsGatchered,10,470);

		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops){
			game.batch.draw(img,raindrop.x,raindrop.y);
		}
		for(Rectangle bomb: bombs){
			game.batch.draw(img_red,bomb.x,bomb.y);
		}
		game.batch.end();

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x=touchPos.x -64/2;
		}
		double k=1000000000*2;
		double n=2.3;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -=200*Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x +=200*Gdx.graphics.getDeltaTime();

		if(bucket.x<0) bucket.x=0;
		if(bucket.x>800) bucket.x=800-64;

		if(dropsGatchered>5) k=1000000000*1.5;
		if(dropsGatchered>10) k=1000000000;
		if(dropsGatchered>15) {k=1000000000/1.5; n=2.3/1.5;}
		if(dropsGatchered>20) k=1000000000/1.9;
		if(dropsGatchered>25) k=1000000000/3;
		if(dropsGatchered>30) k=1000000000/5;

		if(TimeUtils.nanoTime()-lastDropTime > k ) spawnRaindrop();

		if((TimeUtils.nanoTime()-lastDropTimeb > n*k+1000000 ) && (dropsGatchered>5)) spawnbomb();

		Iterator<Rectangle> iter = raindrops.iterator();
		Iterator<Rectangle> iterb = bombs.iterator();
		while(iter.hasNext()){
			Rectangle raindrop = iter.next();
			raindrop.y -= 200*Gdx.graphics.getDeltaTime();
			if(raindrop.y+64<0) {
				iter.remove();
				dropsGatchered--;
			}
			if(raindrop.overlaps(bucket))	{
			dropsGatchered++;
				gamesound.play();
				iter.remove();
			}
		}

		while(iterb.hasNext()){
			Rectangle bomb = iterb.next();
			bomb.y -= 200*Gdx.graphics.getDeltaTime();
			if(bomb.y+64<0) {
				iterb.remove();
			}
			if(bomb.overlaps(bucket))	{
				dropsGatchered=dropsGatchered-2;
				bombsound.play();
				iterb.remove();
			}
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		img.dispose();
		img_red.dispose();
		gamesound.dispose();
		bombsound.dispose();
		bucketImage.dispose();
		rainMusic.dispose();
	}

	@Override
	public void show() {
		rainMusic.play();
	}
}
