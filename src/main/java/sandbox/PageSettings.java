package sandbox;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import jme3_ext.PageManager;

import com.jme3.audio.AudioNode;
import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;

public class PageSettings extends Page<HudSettings>{
	boolean prevCursorVisible;
	private String prefKey;
	private AudioManager audioMgr;

	private AudioNode audioTestMusic;
	private AudioNode audioTestSound;
	private final Provider<PageManager> pm; // use Provider as Hack to break the dependency cycle PageManager -> Page -> PageManager

	@Inject
	public PageSettings(GuiManager g0, Provider<PageManager> pm0, @Named("prefKey") String prefKey, AudioManager audioMgr) {
		super(g0);
		this.prefKey = prefKey;
		this.audioMgr = audioMgr;
		this.pm = pm0;
	}

	@Override
	FXMLHud<HudSettings> createHud() {
		return new FXMLHud<>(this.getClass().getPackage().getName().replace(".", "/") + "/HudSettings.fxml");
	}

	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		HudSettings p = hud.getController();
		p.load(app, prefKey, audioMgr);

		audioTestMusic = new AudioNode(app.getAssetManager(), "Sound/Environment/Nature.ogg", true);
		audioTestMusic.setLooping(false);
		audioTestMusic.setPositional(true);
		audioMgr.musics.add(audioTestMusic);
		app.getRootNode().attachChild(audioTestMusic);
		p.testMusic.onActionProperty().set((e) -> {
			app.enqueue(()-> {
				audioTestMusic.play();
				return true;
			});
		});
		p.testMusic.setDisable(false);

		audioTestSound = new AudioNode(app.getAssetManager(), "Sound/Effects/Gun.wav", false); // buffered
		audioTestSound.setLooping(false);
		audioTestMusic.setPositional(false);
		app.getRootNode().attachChild(audioTestSound);
		p.testSound.onActionProperty().set((e) -> {
			app.enqueue(()-> {
				audioTestSound.playInstance();
				return true;
			});
		});
		p.testSound.setDisable(false);

		p.back.onActionProperty().set((e) -> {
			app.enqueue(()-> {
				pm.get().goTo(Pages.Welcome.ordinal());
				return true;
			});
		});
	}

	@Override
	protected void doDisable() {
		app.getInputManager().setCursorVisible(prevCursorVisible);
		app.getRootNode().detachChild(audioTestSound);
		app.getRootNode().detachChild(audioTestMusic);
		audioMgr.musics.remove(audioTestMusic);
	}
}
