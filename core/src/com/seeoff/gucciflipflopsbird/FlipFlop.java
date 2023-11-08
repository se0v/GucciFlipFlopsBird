package com.seeoff.gucciflipflopsbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlipFlop extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] smile;
	int smileStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 0;
	int gameStateFlag = 0;

	Texture topTube;
	Texture bottomTube;
	int spaceBetweenTubes = 1000;
	Random random;
	int tubeSpeed = 5;
	int tubesNumber = 5;
	float tubeX[] = new float[tubesNumber];
	float tubeShift[] = new float[tubesNumber];
	float distanceBetweenTubes;

	Circle smileCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	//ShapeRenderer shapeRenderer;

	int gameScore = 0;
	int passedTubeIndex = 0;
	BitmapFont scoreFont;

	Texture gameOver;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("backG.jpg");
		//shapeRenderer = new ShapeRenderer();

		smileCircle = new Circle();
		topTubeRectangles = new Rectangle[tubesNumber];
		bottomTubeRectangles = new Rectangle[tubesNumber];

		smile = new Texture[10];
		smile[0] = new Texture("smile_up.png");
		smile[1] = new Texture("smile_upup.png");
		smile[2] = new Texture("smile_upupup.png");
		smile[3] = new Texture("smile_preup.png");
		smile[4] = new Texture("smile_prepreup.png");
		smile[5] = new Texture("smile_prepredown.png");
		smile[6] = new Texture("smile_predown.png");
		smile[7] = new Texture("smile_down.png");
		smile[8] = new Texture("smile_downdown.png");
		smile[9] = new Texture("smile_downdowndown.png");

		topTube = new Texture("tube_down.png");
		bottomTube = new Texture("tube_up.png");
		random = new Random();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().setScale(10);

		gameOver = new Texture("wasted.png");
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2;
		//nicerave,mov
		initGame();
	}

	public  void initGame() {

		flyHeight = Gdx.graphics.getHeight() / 2
				- smile[0].getHeight() / 2;

		//для создания пяти труб через заданный интервал:
		for (int i = 0; i < tubesNumber; i++){

			tubeX[i] = Gdx.graphics.getWidth() / 2
					- topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes * 1.5f;
			tubeShift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()
					- spaceBetweenTubes - 200);
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//логика полёта:
		if (gameStateFlag == 1){

			Gdx.app.log("Game score", String.valueOf(gameScore));
			//проверка трубы для ведения счёта:
			if(tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
				gameScore++;

				if (passedTubeIndex < tubesNumber - 1) {
					passedTubeIndex++;
				} else {
					passedTubeIndex = 0;
				}
			}
			if (Gdx.input.justTouched()){
				fallingSpeed = -25;
			}

			//для отображения пяти труб через заданный интервал:
			for (int i = 0; i < tubesNumber; i++) {
				//для восполнения труб
				if (tubeX[i] < -topTube.getWidth()){

					tubeX[i] = tubesNumber * distanceBetweenTubes * 1.4f;
				} else {
					tubeX[i] -= tubeSpeed;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i],
						topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i],
						bottomTube.getWidth(), bottomTube.getHeight());

			}

			if (flyHeight > 0){
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			} else {
				gameStateFlag = 2;
			}
		} else if (gameStateFlag == 0){
			if (Gdx.input.justTouched()){
				Gdx.app.log("Tap", "Taped");
				gameStateFlag = 1;
			}
		} else if (gameStateFlag == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					 Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
			if (Gdx.input.justTouched()) {
				Gdx.app.log("Tap", "Taped");
				gameStateFlag = 1;
				initGame();
				gameScore = 0;
				passedTubeIndex = 0;
				fallingSpeed = 0;
			}
		}
		//анимация mainObject'a
		if (smileStateFlag == 0 ) {
			smileStateFlag = 1;
		} else if (smileStateFlag == 1) {
			smileStateFlag = 2;
		}else if (smileStateFlag == 2) {
			smileStateFlag = 3;
		} else if (smileStateFlag == 3) {
			smileStateFlag = 4;
		} else if (smileStateFlag == 4) {
			smileStateFlag = 5;
		} else if (smileStateFlag == 5) {
			smileStateFlag = 6;
		} else if (smileStateFlag == 6) {
			smileStateFlag = 7;
		} else if (smileStateFlag == 7) {
			smileStateFlag = 8;
		} else if (smileStateFlag == 8) {
			smileStateFlag = 9;
		}else if (smileStateFlag == 9) {
			smileStateFlag = 0;
		}
		//отрисовка главного объекта
		batch.draw(smile[smileStateFlag], Gdx.graphics.getWidth() / 2
				- smile[smileStateFlag].getWidth() / 2, flyHeight);

		scoreFont.draw(batch, String.valueOf(gameScore), 100, 150); //отрисовка счёта
		batch.end();
		//коллайдер главногоб.
		smileCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + smile[smileStateFlag].getHeight() / 2,
				smile[smileStateFlag].getWidth() / 2);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.CYAN);
		shapeRenderer.circle(smileCircle.x, smileCircle.y, smileCircle.radius);*/
		//коллайдер труб
		for (int i = 0; i < tubesNumber; i++) {
			/*shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeShift[i],
					topTube.getWidth(), topTube.getHeight());
			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 - bottomTube.getHeight() + tubeShift[i],
					bottomTube.getWidth(), bottomTube.getHeight());*/
			//проверка пересечения коллайдеров:
			if(Intersector.overlaps(smileCircle, topTubeRectangles[i]) || (Intersector.overlaps(smileCircle, bottomTubeRectangles[i]))){

				Gdx.app.log("Intersected","BullSh!");
				gameStateFlag = 2;
			}
		}
		//shapeRenderer.end();
		/*ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/
	}
	
	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
