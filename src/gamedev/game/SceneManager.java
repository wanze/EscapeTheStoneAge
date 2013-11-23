package gamedev.game;

import gamedev.levels.Level1;
import gamedev.scenes.BaseScene;
import gamedev.scenes.LevelCompleteScene;
import gamedev.scenes.LevelScene;
import gamedev.scenes.LoadingScene;
import gamedev.scenes.MainMenuScene;
import gamedev.scenes.SplashScene;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {
	// ---------------------------------------------
	// SCENES
	// ---------------------------------------------

	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene levelScene;
	private BaseScene levelCompleteScene;
	private BaseScene loadingScene;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_LEVEL, SCENE_LEVEL_COMPLETE, SCENE_LOADING,
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_LEVEL:
			setScene(levelScene);
			break;
		case SCENE_LEVEL_COMPLETE:
			setScene(levelCompleteScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}

	// ---------------------------------------------
	// Creating and Disposing of the different scenes
	// ---------------------------------------------

	// ---------------------------------------------
	// Splash Scene
	// ---------------------------------------------
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	private void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	public void disposeLoadingScene() {
		if (!loadingScene.isDisposed()) {
			loadingScene.disposeScene();
		}
	}

	// ---------------------------------------------
	// Menu Scene
	// ---------------------------------------------
	public void createMenuScene() {
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		loadingScene = new LoadingScene();
		setScene(menuScene);
		disposeSplashScene();
	}

	public void disposeMenuScene() {
		if (!menuScene.isDisposed()) {
			menuScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadMenuResources();
	}

	public void loadMenuScene(final Engine mEngine) {
		disposeCurrentScene(false);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance().loadMenuResources();
						setScene(menuScene);
						disposeLoadingScene();
					}
				}));
	}

	// ---------------------------------------------
	// Level Scene
	// ---------------------------------------------

	public void createLevelScene(final Engine mEngine, int levelId) {
		loadGameResources();

		switch (levelId) {
		case 1:
			levelScene = new Level1();
			break;
		case 2:
			// TODO: More levels ;)
			break;
		default:
			break;
		}

		loadLevelScene(engine);
	}

	public void disposeLevelScene() {
		if (!levelScene.isDisposed()) {
			levelScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadGameResources();
	}

	public void loadLevelScene(final Engine mEngine) {
		disposeCurrentScene(true);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						if (levelScene != null) {
							mEngine.unregisterUpdateHandler(pTimerHandler);
							loadGameResources();
							setScene(levelScene);
							disposeLoadingScene();
						} else {
							createLevelScene(engine, 1);
						}
					}
				}));
	}

	public void restartLevelScene(int levelId) {
		levelScene = null;
		createLevelScene(engine, levelId);
	}

	public void loadGameResources() {
		if (ResourcesManager.getInstance().areGameResourcesCreated() == false) {
			ResourcesManager.getInstance().loadGameResources();
		}
	}

	// ---------------------------------------------
	// LevelComplete Scene
	// ---------------------------------------------
	public void loadLevelCompleteScene(final Engine mEngine) {
		if (levelCompleteScene == null) {
			levelCompleteScene = new LevelCompleteScene();
		}

		disposeCurrentScene(false);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						ResourcesManager.getInstance()
								.loadLevelCompleteResources();
						setScene(levelCompleteScene);
					}
				}));
	}

	public void disposeLevelCompleteScene() {
		if (!levelCompleteScene.isDisposed()) {
			levelCompleteScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadLevelCompleteResources();
	}

	/**
	 * 
	 * @param setLoadingScene
	 *            true, if you want to show the loading scene
	 */
	public void disposeCurrentScene(boolean setLoadingScene) {
		if (setLoadingScene) {
			setScene(loadingScene);
		}

		if (currentSceneType.equals(SceneType.SCENE_LEVEL)) {
			disposeLevelScene();
		} else if (currentSceneType.equals(SceneType.SCENE_LEVEL_COMPLETE)) {
			disposeLevelCompleteScene();
		} else if (currentSceneType.equals(SceneType.SCENE_MENU)) {
			disposeMenuScene();
		}

	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}

	public LevelScene getCurrentLevelScene() {
		return (LevelScene) levelScene;
	}

	public boolean isLevelSceneCreated() {
		return levelScene != null;
	}

}