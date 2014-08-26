// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import javax.inject.Inject;

import jme3_ext.AppState0;
import lombok.RequiredArgsConstructor;

import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;

/**
 *
 * @author David Bernard
 */
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class Page<T> extends AppState0 {
	final GuiManager guiManager;
	FXMLHud<T> hud;

	FXMLHud<T> createHud(){
		return null;
	}

	@Override
	public void doInitialize() {
		hud = createHud();
		if (hud != null) {
			hud.precache();
			guiManager.attachHudAsync(hud);
		}
	}

	@Override
	public void doDispose() {
		if (hud != null){
			guiManager.detachHudAsync(hud);
		}
	}
}
