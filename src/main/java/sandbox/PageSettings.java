package sandbox;

import javax.inject.Inject;
import javax.inject.Named;

import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;

public class PageSettings extends Page<HudSettings>{
	boolean prevCursorVisible;
	private String prefKey;
	
	@Inject
	public PageSettings(GuiManager g0, @Named("prefKey") String prefKey) {
		super(g0);
		this.prefKey = prefKey;
	}

	@Override
	FXMLHud<HudSettings> createHud() {
		return new FXMLHud<>(this.getClass().getPackage().getName().replace(".", "/") + "/HudSettings.fxml");
	}

	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		hud.getController().load(app, prefKey);
	}

	@Override
	protected void doDisable() {
		app.getInputManager().setCursorVisible(prevCursorVisible);
	}
}
