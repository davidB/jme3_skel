/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import gui_utils.PageManager;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.system.AppSettings;

import dagger.ObjectGraph;

public class Main {

	private static boolean assertionsEnabled;
	private static boolean enabled() {
		Main.assertionsEnabled = true;
		return true;
	}

	//@SuppressWarnings("AssertWithSideEffects")
	public static void main(final String[] args) {
		assert Main.enabled();
		if (!Main.assertionsEnabled) {
			throw new RuntimeException("Assertions must be enabled (vm args -ea");
		}
		ObjectGraph injector = ObjectGraph.create(new MainModule());
		SimpleApplication app = injector.get(SimpleApplication.class);
		PageManager pageManager = injector.get(PageManager.class);
		//TODO load style.css

//		app.enqueue(()-> {
//			app.getStateManager().attach(injector.get(AppStateInGame.class));
//			return true;
//		});
//		setAspectRatio(app, 16, 9);
		setDebug(app, true);
		initPages(app, true, pageManager);
	}

	public static AppSettings appSettings() {
		AppSettings settings = new AppSettings(true);
		//settings.setResolution(640,480);
		//	settings.setRenderer("JOGL");
		//	settings.setRenderer(AppSettings.LWJGL_OPENGL3);
		return settings;
	}

	static public void setDebug(SimpleApplication app, boolean v) {
		app.enqueue(() -> {
			//val s = app.getStateManager().getState(BulletAppState.class);
			//if (s != null) s.setDebugEnabled(v);
			app.getInputManager().setCursorVisible(v);
			app.getViewPort().setBackgroundColor(v? ColorRGBA.Pink : ColorRGBA.White);
			//Display.setResizable(v);
			return true;
		});
	}

	static public void setAspectRatio(SimpleApplication app, float w, float h) {
		app.enqueue(() -> {
			Camera cam = app.getCamera();
			//cam.resize(w, h, true);
			float ratio = (h * cam.getWidth()) / (w * cam.getHeight());
			if (ratio < 1.0) {
				float margin = (1f - ratio) * 0.5f;
				float frustumW = cam.getFrustumRight();
				float frustumH = cam.getFrustumTop() / ratio;
				//cam.resize(cam.getWidth(), (int)(cam.getHeight() * 0.5), true);
				cam.setViewPort(0f, 1f, margin,  1 - margin);
				cam.setFrustum(cam.getFrustumNear(), cam.getFrustumFar(), -frustumW, frustumW, frustumH, -frustumH);
			}
			//			app.getRenderManager().getPreViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			//			app.getRenderManager().getPostViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			//			app.getRenderManager().getMainViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			cp(cam, app.getGuiViewPort().getCamera());
			return true;
		});
	}

	static void cp(Camera src, Camera dest) {
		if (src != dest) {
			dest.setViewPort(src.getViewPortLeft(), src.getViewPortRight(), src.getViewPortBottom(), src.getViewPortTop());
			dest.setFrustum(src.getFrustumNear(), src.getFrustumFar(), src.getFrustumLeft(), src.getFrustumRight(), src.getFrustumTop(), src.getFrustumBottom());
		}
	}



	static void initPages(SimpleApplication app, boolean debug, PageManager pageManager) {
		app.enqueue(() -> {
			InputManager inputManager = app.getInputManager();
			if (debug) {
				inputManager.addMapping(PageManager.prefixGoto + Pages.Welcome.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD0));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.LevelSelection.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD1));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Loading.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD2));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.InGame.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD3));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Result.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD4));
				inputManager.addMapping(PageManager.prefixGoto + Pages.Settings.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD5));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Scores.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD6));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.About.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD7));
			}
			pageManager.registerAction(inputManager);
			pageManager.goTo(Pages.Welcome.ordinal());
			//pageManager.goTo(Page.Settings.ordinal());
			return true;
		});
	}


	//    void spawn3DObject() {
	//        // Something in scene
	//        Box box = new Box(1, 1, 1);
	//        Geometry geom = new Geometry("Box", box);
	//        Material mat = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
	//        mat.setColor("Color", ColorRGBA.Pink);
	//        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
	//        geom.setMaterial(mat);
	//        rootNode.attachChild(geom);
	//    }
}