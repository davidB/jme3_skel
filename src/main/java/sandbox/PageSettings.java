package sandbox;

import javax.inject.Inject;
import javax.inject.Named;

import com.jme3.audio.AudioNode;
import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;

public class PageSettings extends Page<HudSettings>{
	boolean prevCursorVisible;
	private String prefKey;
	private AudioManager audioMgr;

	private AudioNode audioTestMusic;
	private AudioNode audioTestSound;

	@Inject
	public PageSettings(GuiManager g0, @Named("prefKey") String prefKey, AudioManager audioMgr) {
		super(g0);
		this.prefKey = prefKey;
		this.audioMgr = audioMgr;
	}

	@Override
	FXMLHud<HudSettings> createHud() {
		return new FXMLHud<>(this.getClass().getPackage().getName().replace(".", "/") + "/HudSettings.fxml");
	}

	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		hud.getController().load(app, prefKey, audioMgr);

		audioTestMusic = new AudioNode(app.getAssetManager(), "Sound/Environment/Nature.ogg", true);
		audioTestMusic.setLooping(false);
		audioTestMusic.setPositional(true);
		audioMgr.musics.add(audioTestMusic);
		app.getRootNode().attachChild(audioTestMusic);
		hud.getController().testMusic.onActionProperty().set((e) -> {
			app.enqueue(()-> {
				audioTestMusic.play();
				return true;
			});
		});
		hud.getController().testMusic.setDisable(false);

		audioTestSound = new AudioNode(app.getAssetManager(), "Sound/Effects/Gun.wav", false); // buffered
		audioTestSound.setLooping(false);
		audioTestMusic.setPositional(false);
		app.getRootNode().attachChild(audioTestSound);
		hud.getController().testSound.onActionProperty().set((e) -> {
			app.enqueue(()-> {
				audioTestSound.playInstance();
				return true;
			});
		});
		hud.getController().testSound.setDisable(false);
	}

	@Override
	protected void doDisable() {
		app.getInputManager().setCursorVisible(prevCursorVisible);
		app.getRootNode().detachChild(audioTestSound);
		app.getRootNode().detachChild(audioTestMusic);
		audioMgr.musics.remove(audioTestMusic);
	}
}
