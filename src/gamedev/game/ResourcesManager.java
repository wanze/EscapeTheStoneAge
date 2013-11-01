package gamedev.game;

import gamedev.objects.Player;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseActivity;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class ResourcesManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public BaseActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    public TextureManager textureManager;
    public Player player;
    
    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------

    public BitmapTextureAtlas playerAtlas;
    public ITiledTextureRegion playerRegion;
    public BitmapTextureAtlas dinosaurGreenAtlas;
    public ITiledTextureRegion dinosaurGreenRegion;
    public BitmapTextureAtlas landscapeAtlas;
    
	// Texture for splash scene
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;

	// Textures for menu scene
	public ITextureRegion menu_background_region;
	public ITiledTextureRegion buttons_region;
	private BitmapTextureAtlas menuTextureAtlas;
    
    //---------------------------------------------
    // Physic
    //---------------------------------------------
    
	public PhysicsWorld physicsWorld;

    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void loadPlayerGraphics() {
    }
    
    public void loadPhysics() {
    	this.physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);
    }
    
    public void loadMenuResources()
    {
        loadMenuGraphics();
		// loadMenuAudio();
    }
    
    public void loadGameResources()
    {
        loadGameGraphics();
		// loadGameFonts();
		// loadGameAudio();
    }
    
    private void loadMenuGraphics()
    {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		this.menuTextureAtlas = new BitmapTextureAtlas(textureManager, 800,
				600, TextureOptions.DEFAULT);

		this.menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuTextureAtlas,
						getInstance().activity,
						"menubackground.png", 0, 0, 1, 1);
		this.buttons_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.menuTextureAtlas,
						getInstance().activity,
						"menubuttons.png", 0, 0, 1, 3);

		this.menuTextureAtlas.load();

    }
    
    private void loadMenuAudio()
    {
        
    }

    private void loadGameGraphics()
    {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	
    	// Player
    	this.playerAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 768, 2400, TextureOptions.DEFAULT);

    	this.playerRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.playerAtlas, getInstance().activity,
						"player_sprite.png", 0, 0, 8, 25);
    	this.playerAtlas.load();
    	
    	this.dinosaurGreenAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 3328, 4096, TextureOptions.DEFAULT);

    	this.dinosaurGreenRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dinosaurGreenAtlas, getInstance().activity,
						"green_dino.png", 0, 0, 26, 32);

    	this.dinosaurGreenAtlas.load();
    	
    	this.landscapeAtlas = new BitmapTextureAtlas(getInstance().textureManager, 512, 1204);
    	this.landscapeAtlas.load();
    }
    
    public ITextureRegion getRandomTreeTexture() {
    	return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.landscapeAtlas, getInstance().activity, "trees.png", 0, 0, 8, 4);
    }
    
    private void loadGameFonts()
    {
        
    }
    
    private void loadGameAudio()
    {
        
    }
    
	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				getInstance().textureManager, 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
    }
    
	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
    }
    
    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, BaseActivity activity, BoundCamera camera, VertexBufferObjectManager vbom, TextureManager textureManager)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        getInstance().textureManager = textureManager;
        
        // We also load physics and player.. //TODO Move out from here, since a Menu scene does not require them in memory
		getInstance().loadPhysics();
		getInstance().loadGameResources();
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }
}