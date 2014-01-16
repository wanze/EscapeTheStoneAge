package gamedev.objects;

import gamedev.game.ResourcesManager;
import gamedev.game.SceneManager;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.widget.Toast;

abstract public class CollectableObject extends Sprite {

	public CollectableObject(float pX, float pY, ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, ResourcesManager.getInstance().vbom);
	}

	@Override
	public void onManagedUpdate(float seconds) {
		super.onManagedUpdate(seconds);
		if (this.collidesWith(ResourcesManager.getInstance().avatar)) {
			ResourcesManager.getInstance().avatar.getInventory()
					.addObject(this);

			ResourcesManager.getInstance().removeSpriteAndBody(this);

			// Give feedback:
			ResourcesManager.getInstance().activity.toastOnUIThread(
					"Item collected.", Toast.LENGTH_SHORT);
		}
	}

}