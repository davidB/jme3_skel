/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import javafx.application.Platform;

import javax.inject.Inject;
import javax.inject.Provider;


import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import jme3_ext.PageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.jme3x.jfx.FxPlatformExecutor;

/**
 *
 * @author David Bernard
 */
@Slf4j
@RequiredArgsConstructor(onConstructor=@__(@Inject))
class PageWelcome extends AppState0 {
	private final HudTools hudTools;
	private final Provider<PageManager> pm; // use Provider as Hack to break the dependency cycle PageManager -> Page -> PageManager

	private boolean prevCursorVisible;
	private Hud<HudWelcome> hud;

	@Override
	public void doInitialize() {
		hud = hudTools.newHud("Interface/HudWelcome.fxml", new HudWelcome());
	}
	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		hudTools.show(hud);

		FxPlatformExecutor.runOnFxApplication(() -> {
			HudWelcome p = hud.controller;
			p.play.onActionProperty().set((v) -> { });
			p.settings.onActionProperty().set((v) -> {
				app.enqueue(()-> {
					pm.get().goTo(Pages.Settings.ordinal());
					return true;
				});
			});
			p.quit.onActionProperty().set((v) -> {
				app.enqueue(()->{
					app.getContext().destroy(false);
					return true;
				});
				//System.exit(0);
			});
		});
	}

	@Override
	protected void doDisable() {
		hudTools.hide(hud);
		app.getInputManager().setCursorVisible(prevCursorVisible);
	}
}
