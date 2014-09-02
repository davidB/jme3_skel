/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import javafx.scene.Scene;
import javafx.scene.text.Font;

import javax.inject.Inject;

import jme3_ext.PageManager;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3x.jfx.FxPlatformExecutor;
import com.jme3x.jfx.GuiManager;

import dagger.ObjectGraph;

public class Main {

	private static boolean assertionsEnabled;
	private static boolean enabled() {
		Main.assertionsEnabled = true;
		return true;
	}

	public static void main(final String[] args) {
		assert Main.enabled();
		if (!Main.assertionsEnabled) {
			throw new RuntimeException("Assertions must be enabled (vm args -ea");
		}
		ObjectGraph injector = ObjectGraph.create(new MainModule());
		injector.get(Main.class); // Main constructor used to initialize service
	}

	//HACK to receive service without need to explicitly list them and to initialize them
	@Inject
	Main(SimpleApplication app, GuiManager guiManager, PageManager pageManager) {
//		setAspectRatio(app, 16, 9);
		setDebug(app, true);
		initGui(guiManager);
		initPages(pageManager, app, true);
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

	static void initPages(PageManager pageManager, SimpleApplication app, boolean debug) {
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

	static void initGui(GuiManager guiManager) {
		//see http://blog.idrsolutions.com/2014/04/use-external-css-files-javafx/
		Scene scene = guiManager.getjmeFXContainer().getScene();
		FxPlatformExecutor.runOnFxApplication(() -> {
			Font.loadFont(Main.class.getResource("/Fonts/Eduardo-Barrasa.ttf").toExternalForm(), 10);
			Font.loadFont(Main.class.getResource("/Fonts/bobotoh.ttf").toExternalForm(), 10);
			String css = Main.class.getResource("main.css").toExternalForm();
			scene.getStylesheets().clear();
			scene.getStylesheets().add(css);
		});
	}
}