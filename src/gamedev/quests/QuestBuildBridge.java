package gamedev.quests;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;
import gamedev.objects.Inventory;
import gamedev.objects.Wood;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class QuestBuildBridge extends Quest {
	
	private final static float RECTANGLE_X = 37*32;
	private final static float RECTANGLE_Y = 14*32;
	private final static float RECTANGLE_HEIGHT = 100;
	private final static float RECTANGLE_WIDTH = 20;
	
	protected Body body;
	protected Rectangle rectangle;
	protected Wood wood1;
	protected Wood wood2;
	protected Wood wood3;
	
	public QuestBuildBridge(Scene map) {
		super(map);
		this.title = "Cross the River";
		this.description = "I need to find something so I can go to the other side of the river!";
		this.rectangle = new Rectangle(RECTANGLE_X, RECTANGLE_Y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT, ResourcesManager.getInstance().vbom);
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0,0, 0);
		this.body = PhysicsFactory.createBoxBody(ResourcesManager.getInstance().physicsWorld, this.rectangle, BodyType.StaticBody, boxFixtureDef);
		this.map.attachChild(this.rectangle);
		this.wood1 = new Wood(200,200);
		this.wood2 = new Wood(400,600);
		this.wood3 = new Wood(500, 500);
		map.attachChild(wood1);
		map.attachChild(wood2);
		map.attachChild(wood3);
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCompleted() {
		Inventory inventory = ResourcesManager.getInstance().avatar.getInventory();
		return (inventory.contains(wood1) && inventory.contains(wood2) && inventory.contains(wood3));
	}

	

}
