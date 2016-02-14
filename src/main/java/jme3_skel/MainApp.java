/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3x.jfx.FxPlatformExecutor;
import com.jme3x.jfx.GuiManager;

import dagger.Component;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import jme3_ext.PageManager;
import jme3_ext.SetupHelpers;

@Singleton
@Component(modules = MainModule.class)
interface MainAppMaker {
  MainApp make();
}

class MainApp {
	//HACK to receive service without need to explicitly list them and to initialize them
	@Inject
	MainApp(SimpleApplication app, GuiManager guiManager, PageManager<Pages> pageManager) {
//		setAspectRatio(app, 16, 9);
		SetupHelpers.disableDefaults(app);
		SetupHelpers.setDebug(app, false);
		SetupHelpers.logJoystickInfo(app.getInputManager());
		initGui(guiManager);
		initPages(pageManager, app, false);
	}

	static void initPages(PageManager<Pages> pageManager, SimpleApplication app, boolean debug) {
		app.enqueue(() -> {
			InputManager inputManager = app.getInputManager();
			if (debug) {
		        final String prefixGoto = "GOTOPAGE_";
		        ActionListener a = new ActionListener() {
		            public void onAction(String name, boolean isPressed, float tpf) {
		                if (isPressed && name.startsWith(prefixGoto)) {
		                    Pages page = Pages.valueOf(name.substring(prefixGoto.length()));
		                    pageManager.goTo(page);
		                };
		            }
		        };
		        for (int i = 0; i < Pages.values().length; i++) {
		            inputManager.addListener(a, prefixGoto + i);
		        }
				inputManager.addMapping(prefixGoto + Pages.Welcome.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD0));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.LevelSelection.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD1));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Loading.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD2));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.InGame.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD3));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Result.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD4));
				inputManager.addMapping(prefixGoto + Pages.Settings.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD5));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.Scores.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD6));
				//            inputManager.addMapping(PageManager.prefixGoto + Page.About.ordinal(), new KeyTrigger(KeyInput.KEY_NUMPAD7));
			}
			pageManager.goTo(Pages.Welcome);
			//pageManager.goTo(Page.Settings.ordinal());
			return true;
		});
	}

	static void initGui(GuiManager guiManager) {
		//see http://blog.idrsolutions.com/2014/04/use-external-css-files-javafx/
		Scene scene = guiManager.getjmeFXContainer().getScene();
		FxPlatformExecutor.runOnFxApplication(() -> {
			Font.loadFont(MainApp.class.getResource("/Fonts/Eduardo-Barrasa.ttf").toExternalForm(), 10);
			Font.loadFont(MainApp.class.getResource("/Fonts/bobotoh.ttf").toExternalForm(), 10);
			String css = MainApp.class.getResource("/Interface/main.css").toExternalForm();
			scene.getStylesheets().clear();
			scene.getStylesheets().add(css);
		});
	}

}