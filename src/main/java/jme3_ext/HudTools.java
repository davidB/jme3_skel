package jme3_ext;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import lombok.RequiredArgsConstructor;

import com.jme3x.jfx.AbstractHud;
import com.jme3x.jfx.FXMLUtils;
import com.jme3x.jfx.GuiManager;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class HudTools {
	public final Provider<FXMLLoader> pFxmlLoader;
	public final GuiManager guiManager;

	public <T> Hud<T> newHud(String fxmlPath, T controller) {
		try {
			final FXMLLoader fxmlLoader = pFxmlLoader.get();
			final URL location = Thread.currentThread().getContextClassLoader().getResource(fxmlPath);
			if (location == null) {
				throw new IllegalArgumentException("fxmlPath not available : " + fxmlPath );
			}
			fxmlLoader.setLocation(location);
			fxmlLoader.setController(controller);
			final Region rv = fxmlLoader.load(location.openStream());
			assert FXMLUtils.checkClassInjection(controller);
			return new Hud<T>(rv, controller);
		} catch(RuntimeException exc) {
			throw exc;
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public void show(AbstractHud hud) {
		if (hud != null) {
			hud.precache();
			guiManager.attachHudAsync(hud);
		}
	}

	public void hide(AbstractHud hud) {
		if (hud != null){
			guiManager.detachHudAsync(hud);
		}
	}
}
