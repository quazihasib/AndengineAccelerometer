package com.example.test;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainActivity extends BaseGameActivity implements
		SensorEventListener {

	Scene scene;
	private ITextureRegion mTextureRegion;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	
	protected static final int CAMERA_WIDTH = 800;
	protected static final int CAMERA_HEIGHT = 480;
	private BuildableBitmapTextureAtlas playerTexture;
	private Sprite sprCell;
	private SensorManager sensorManager;

	private float accellSpeedX, accellSpeedY;
	private float sprX, sprY;

	Rectangle rect;
	@Override
	public EngineOptions onCreateEngineOptions() 
	{
		// TODO Auto-generated method stub
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception { 
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 100, 100);
		this.mTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "mo.png");
		
		try
		{
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			this.mBitmapTextureAtlas.load();
		} 
		catch (TextureAtlasBuilderException e) 
		{
			Debug.e(e);
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// TODO Auto-generated method stub
		
		this.scene = new Scene();
		this.scene.setBackground(new Background(0, 122, 222));
		
		sprX = (CAMERA_WIDTH - this.mTextureRegion.getWidth()) / 2;
		sprY = (CAMERA_HEIGHT - this.mTextureRegion.getHeight()) / 2;
		sprCell = new Sprite(sprX, sprY, mTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(sprCell);
		
		sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);

		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				updateSpritePosistion();
			}

			private void updateSpritePosistion() {
				// TODO Auto-generated method stub
				if (accellSpeedX != 0 || accellSpeedY != 0) {
					int tL = 0;
					int iL = 0;
					int rL = CAMERA_WIDTH - (int) sprCell.getWidth();
					int bL = CAMERA_HEIGHT - (int) sprCell.getHeight();

					if (sprX >= iL) {
						sprX += accellSpeedX;
					} else {
						sprX = iL;
					}
					if (sprX <= rL) {
						sprX += accellSpeedX;
					} else {
						sprX = rL;
					}
					if (sprY >= tL) {
						sprY += accellSpeedY;
					} else {
						sprY = tL;
					}
					if (sprY <= bL) {
						sprY += accellSpeedY;
					} else {
						sprY = bL;
					}
					if (sprX < iL) {
						sprX = iL;
					} else if (sprX > rL) {
						sprX = rL;
					}
					if (sprY < tL) {
						sprY = tL;
					} else if (sprY > bL) {
						sprY = bL;
					}

					sprCell.setPosition(sprX, sprY);

				}
			}
		});

		
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellSpeedX = (int) event.values[1];
				accellSpeedY = (int) event.values[0];
				break;
			}
		}
	}
}