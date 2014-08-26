/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import gui_utils.PageManager;

import javax.inject.Inject;
import javax.inject.Provider;

import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;

/**
 *
 * @author David Bernard
 */
class PageWelcome extends Page<HudWelcome> {
	final Provider<PageManager> pm; // use Provider as Hack to break the dependency cycle PageManager -> Page -> PageManager

	@Inject
	public PageWelcome(GuiManager g0, Provider<PageManager> pm0) {
		super(g0);
		pm = pm0;
	}

	@Override
	FXMLHud<HudWelcome> createHud() {
		return new FXMLHud<>(this.getClass().getPackage().getName().replace(".", "/") + "/HudWelcome.fxml");
	}

	@Override
	protected void doEnable() {
		HudWelcome p = hud.getController();
		p.play.onActionProperty().set((v) -> { });
		p.options.onActionProperty().set((v) -> {
			app.enqueue(()-> {
				System.out.println("goto settings");
				pm.get().goTo(Pages.Settings.ordinal());
				return true;
			});
		});
		p.quit.onActionProperty().set((v) -> {
			app.getContext().destroy(true);
			System.exit(0);
		});
	}
}
