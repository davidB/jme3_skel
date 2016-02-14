/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import javax.inject.Inject;

import com.jme3x.jfx.FxPlatformExecutor;

import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import jme3_ext.InputMapper;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

/**
 *
 * @author David Bernard
 */
class PageWelcome extends AppState0 {
	private final HudTools hudTools;
	private final PublishSubject<Pages> pm;
	private final InputMapper inputMapper;
	private final Commands commands;

	private boolean prevCursorVisible;
	private Hud<HudWelcome> hud;
	private Subscription inputSub;

	@Inject
	public PageWelcome(HudTools hudTools, PublishSubject<Pages> pm, InputMapper inputMapper, Commands commands) {
		super();
		this.hudTools = hudTools;
		this.pm = pm;
		this.inputMapper = inputMapper;
		this.commands = commands;
	}
	@Override
	public void doInitialize() {
		hud = hudTools.newHud("Interface/HudWelcome.fxml", new HudWelcome());
	}
	@Override
	protected void doEnable() {
		prevCursorVisible = app.getInputManager().isCursorVisible();
		app.getInputManager().setCursorVisible(true);
		app.getInputManager().addRawInputListener(inputMapper.rawInputListener);
		hudTools.show(hud);

		FxPlatformExecutor.runOnFxApplication(() -> {
			HudWelcome p = hud.controller;
			p.play.onActionProperty().set((v) -> {
				app.enqueue(()-> {
					pm.onNext(Pages.InGame);
					return true;
				});
			});
			p.settings.onActionProperty().set((v) -> {
				app.enqueue(()-> {
					pm.onNext(Pages.Settings);
					return true;
				});
			});
			p.quit.onActionProperty().set((v) -> {
				app.enqueue(()->{
					app.stop();
					return true;
				});
			});
		});

		inputSub = Subscriptions.from(
			commands.exit.value.subscribe((v) -> {
				if (!v) hud.controller.quit.fire();
			})
		);
	}

	@Override
	protected void doDisable() {
		hudTools.hide(hud);
		app.getInputManager().setCursorVisible(prevCursorVisible);
		app.getInputManager().removeRawInputListener(inputMapper.rawInputListener);
		if (inputSub != null){
			inputSub.unsubscribe();
			inputSub = null;
		}
	}
}
