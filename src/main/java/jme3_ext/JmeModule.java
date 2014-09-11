/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import javax.inject.Singleton;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3x.jfx.GuiManager;
import com.jme3x.jfx.cursor.ICursorDisplayProvider;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;

import dagger.Module;
import dagger.Provides;

@Module(library=true, complete=false)
public class JmeModule{
	@Provides
	public Application application(SimpleApplication app) {
		return app;
	}

	@Provides
	public AssetManager assetManager(SimpleApplication app) {
		return app.getAssetManager();
	}

	@Provides
	public AppStateManager stateManager(SimpleApplication app) {
		return app.getStateManager();
	}

	@Provides
	public InputManager inputManager(SimpleApplication app) {
		return app.getInputManager();
	}

	//-- Jfx-jme
	@Provides
	@Singleton
	public ICursorDisplayProvider cursorDisplayProvider(SimpleApplication app) {
		return new ProtonCursorProvider(app, app.getAssetManager(), app.getInputManager());
	}

	@Provides
	@Singleton
	public GuiManager guiManager(SimpleApplication app, ICursorDisplayProvider c) {
		try {
			return app.enqueue(() -> {
				//guiManager modify app.guiNode so it should run in JME Thread
				GuiManager guiManager = new GuiManager(app.getGuiNode(), app.getAssetManager(), app, true, c);
				app.getInputManager().addRawInputListener(guiManager.getInputRedirector());
				return guiManager;
			}).get();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}

