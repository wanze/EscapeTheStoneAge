package gamedev.scenes;

import gamedev.game.GameActivity;
import gamedev.game.GameActivity.GameMode;
import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.quests.Quest;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import android.widget.Toast;

public class QuestScene extends CameraScene {

	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	private Sprite background;
	private static final int X_POSITION = 125;
	private static final int Y_POSITION_MIN = 110;
	private static final int GAP_BETWEEN_QUESTS = 50;

	public QuestScene() {
		super(ResourcesManager.getInstance().camera);
		this.setBackgroundEnabled(false);
		Background backgroundColor = new Background(new Color(0f, 0f, 0f, 0.5f));
		this.setBackground(backgroundColor);

		background = new Sprite(0, 0, resourcesManager.questFrameRegion,
				resourcesManager.vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		background.setScaleX(1.6f);
		background.setScaleY(1.1f);
		centerShapeInCamera(background);
		attachChild(background);

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					closeQuestScene();
				}
				return true;
			}
		});

	}

	private void addCurrentQuestsToScene() {
		GameMapScene map = SceneManager.getInstance().getCurrentGameMapScene();
		ArrayList<Quest> activeQuests = new ArrayList<Quest>();
		ArrayList<Quest> finishedQuests = new ArrayList<Quest>();
		for (Quest quest : map.getQuests()) {
			if (quest.isActive()) {
				activeQuests.add(quest);
			} else if (quest.isFinished()) {
				finishedQuests.add(quest);
			}
		}

		if (activeQuests.isEmpty() && finishedQuests.isEmpty()) {
			ResourcesManager.getInstance().activity.toastOnUIThread(
					"You have no active or finished quests yet.",
					Toast.LENGTH_SHORT);
		}

		for (int i = 0; i < activeQuests.size(); i++) {
			Quest quest = activeQuests.get(i);

			Sprite sprite = new Sprite(X_POSITION, Y_POSITION_MIN + i
					* GAP_BETWEEN_QUESTS - 7,
					resourcesManager.questActiveRegion.deepCopy(),
					resourcesManager.vbom);
			sprite.setScale(0.8f);
			attachChild(sprite);

			Text text = new Text(X_POSITION + 65, Y_POSITION_MIN + i
					* GAP_BETWEEN_QUESTS, resourcesManager.font,
			// quest.getDescription(), quest.getDescription().length(),
					quest.getTitle(), resourcesManager.vbom);
			attachChild(text);
		}

		for (int i = 0; i < finishedQuests.size(); i++) {
			Quest quest = finishedQuests.get(i);

			Sprite sprite = new Sprite(X_POSITION, Y_POSITION_MIN
					+ (i + activeQuests.size()) * GAP_BETWEEN_QUESTS - 7,
					resourcesManager.questFinishedRegion.deepCopy(),
					resourcesManager.vbom);
			sprite.setScale(0.8f);
			attachChild(sprite);

			Text text = new Text(X_POSITION + 65,
					Y_POSITION_MIN + (i + activeQuests.size())
							* GAP_BETWEEN_QUESTS,
					// resourcesManager.font, quest.getDescription(),
					// quest.getDescription().length(), resourcesManager.vbom);
					resourcesManager.font, quest.getTitle(),
					resourcesManager.vbom);

			attachChild(text);
		}
	}

	public void openQuestScene() {
		resourcesManager.loadQuestSceneGraphics();
		resourcesManager.unloadHUDResources();
		SceneManager.getInstance().getCurrentGameMapScene().setChildScene(this);
		GameActivity.mode = GameMode.POPUP;
		addCurrentQuestsToScene();
	}

	public void closeQuestScene() {
		SceneManager.getInstance().getCurrentGameMapScene().clearChildScene();
		resourcesManager.loadHUDResources();
		resourcesManager.unloadQuestSceneGraphics();
		GameActivity.mode = GameMode.EXPLORING;

		for (int i = 0; i < this.getChildCount(); i++) {
			IEntity entity = this.getChildByIndex(i);
			entity.detachSelf();
			if (!entity.isDisposed()) {
				entity.dispose();
			}
		}

		if (!this.isDisposed()) {
			this.dispose();
		}
	}

}
