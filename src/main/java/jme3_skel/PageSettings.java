package jme3_skel;

import javax.inject.Inject;
import javax.inject.Provider;


import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import jme3_ext.PageManager;
import lombok.RequiredArgsConstructor;


import com.jme3.audio.AudioNode;
import com.jme3x.jfx.FxPlatformExecutor;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class PageSettings extends AppState0{
	private final HudTools hudTools;
	private final Provider<PageManager> pm; // use Provider as Hack to break the dependency cycle PageManager -> Page -> PageManager
	private final AudioManager audioMgr;
	private final HudSettings hudSettings;

	boolean prevCursorVisible;
	private AudioNode audioMusicTest;
	private AudioNode audioSoundTest;
	private Hud<HudSettings> hud;

	@Override
	public void doInitialize() {
		hud = hudTools.newHud("Interface/HudSettings.fxml", hudSettings);
	}

	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		hudTools.show(hud);

		audioMusicTest = new AudioNode(app.getAssetManager(), "Sound/Environment/Nature.ogg", true);
		audioMusicTest.setLooping(false);
		audioMusicTest.setPositional(true);
		audioMgr.musics.add(audioMusicTest);
		app.getRootNode().attachChild(audioMusicTest);

		audioSoundTest = new AudioNode(app.getAssetManager(), "Sound/Effects/Gun.wav", false); // buffered
		audioSoundTest.setLooping(false);
		audioMusicTest.setPositional(false);
		app.getRootNode().attachChild(audioSoundTest);

		FxPlatformExecutor.runOnFxApplication(() -> {
			HudSettings p = hud.controller;
			p.load(app);

			p.audioMusicTest.onActionProperty().set((e) -> {
				app.enqueue(()-> {
					audioMusicTest.play();
					return true;
				});
			});
			p.audioMusicTest.setDisable(false);

			p.audioSoundTest.onActionProperty().set((e) -> {
				app.enqueue(()-> {
					audioSoundTest.playInstance();
					return true;
				});
			});
			p.audioSoundTest.setDisable(false);

			p.back.onActionProperty().set((e) -> {
				app.enqueue(()-> {
					pm.get().goTo(Pages.Welcome.ordinal());
					return true;
				});
			});
		});
	}

	@Override
	protected void doDisable() {
		hudTools.hide(hud);
		app.getInputManager().setCursorVisible(prevCursorVisible);
		app.getRootNode().detachChild(audioSoundTest);
		app.getRootNode().detachChild(audioMusicTest);
		audioMgr.musics.remove(audioMusicTest);
	}
}
