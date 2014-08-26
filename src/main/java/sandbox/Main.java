/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import com.google.common.base.Function;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SettingsDialog;

import gui_utils.PageManager;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.Styles;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private boolean debug = true;

    enum Page {

        Welcome,
        //        LevelSelection,
        //        InGame,
        //        Loading,
        //        Result,
        Settings,
        //        About,
        //        Scores,
        Lemur,
    }
    PageManager pageManager;
	Function<Main, Boolean> onNextReshape;

    public static void main(String[] args) {
        try {
            AppSettings settings = new AppSettings(true);
            settings.setTitle("Demo Lemur + Skeleton");
        	settings.load(settings.getTitle());
        	//settings.setResolution(settings.getMinWidth(), settings.getMinHeight());
        	settings.setFullscreen(false);
	        Main app = new Main();
	        app.settings = settings;
	        app.showSettings = false;
	        app.start();
        } catch(Exception exc) {
        	exc.printStackTrace();
        }
    }

    @Override
    public void simpleInitApp() {
        initGuiLemur();
        initPages();
        //spawn3DObject();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    void initPages() {
        AppState[] pages = new AppState[Page.values().length];
        /*
         pages[Page.About.ordinal()] = new PageAbout(screen);
         pages[Page.InGame.ordinal()] = new PageInGame(screen);
         pages[Page.LevelSelection.ordinal()] = new PageLevelSelection(screen);
         pages[Page.Loading.ordinal()] = new PageLoading(screen);
         pages[Page.Result.ordinal()] = new PageResult(screen);
         pages[Page.Scores.ordinal()] = new PageScores(screen);
         pages[Page.Settings.ordinal()] = new PageSettings(screen);
         */
        pages[Page.Welcome.ordinal()] = new PageWelcome();
        pages[Page.Settings.ordinal()] = new PageSettings();
        pages[Page.Lemur.ordinal()] = new PageLemurDemo();
        pageManager = new PageManager(stateManager, pages);
        if (debug) {
            inputManager.addMapping(PageManager.prefixGoto + Page.Welcome.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD0));
//            inputManager.addMapping(PageManager.prefixGoto + Page.LevelSelection.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD1));
//            inputManager.addMapping(PageManager.prefixGoto + Page.Loading.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD2));
//            inputManager.addMapping(PageManager.prefixGoto + Page.InGame.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD3));
//            inputManager.addMapping(PageManager.prefixGoto + Page.Result.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD4));
            inputManager.addMapping(PageManager.prefixGoto + Page.Settings.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD5));
//            inputManager.addMapping(PageManager.prefixGoto + Page.Scores.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD6));
//            inputManager.addMapping(PageManager.prefixGoto + Page.About.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD7));
            inputManager.addMapping(PageManager.prefixGoto + Page.Lemur.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD8));
        }
        pageManager.registerAction(inputManager);
        pageManager.goTo(Page.Settings.ordinal());
    }

    void initGuiLemur() {
        // Initialize the globals access so that the defualt
        // components can find what they need.
        GuiGlobals.initialize(this);

        // Remove the flycam because we don't want it in this
        // demo
        stateManager.detach(stateManager.getState(FlyCamAppState.class));

        // Now, let's create some styles in code.
        // For this demo, we'll just give some of the elements
        // different backgrounds as we define a "glass" style.
        // We also define a custom element type called "spacer" which
        // picks up a specific style.

        Styles styles = GuiGlobals.getInstance().getStyles();
        /*
         styles.getSelector(Slider.THUMB_ID, "glass").set("text", "[]", false);
         styles.getSelector(Panel.ELEMENT_ID, "glass").set("background",
         new QuadBackgroundComponent(new ColorRGBA(0, 0.25f, 0.25f, 0.5f)));
         styles.getSelector(Checkbox.ELEMENT_ID, "glass").set("background",
         new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f)));
         styles.getSelector("spacer", "glass").set("background",
         new QuadBackgroundComponent(new ColorRGBA(1, 0.0f, 0.0f, 0.0f)));
         styles.getSelector("header", "glass").set("background",
         new QuadBackgroundComponent(new ColorRGBA(0, 0.75f, 0.75f, 0.5f)));
         */
        styles.getSelector(Label.ELEMENT_ID).set("background", new QuadBackgroundComponent(new ColorRGBA(0, 0, 0, 0)), false);

    }

    void spawn3DObject() {
        // Something in scene
        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        Material mat = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Pink);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }

	public AppSettings getSettings() {
		return this.settings;
	}
	
	@Override
	public void reshape(int w, int h) {
		super.reshape(w, h);
		if (onNextReshape != null) {
			onNextReshape.apply(this);
			onNextReshape = null;
		}
	}
}
