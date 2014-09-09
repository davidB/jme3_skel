package jme3_skel;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;

import javax.inject.Inject;
import javax.inject.Provider;

import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import jme3_ext.InputMapper;
import jme3_ext.InputTextureFinder;
import jme3_ext.PageManager;
import lombok.RequiredArgsConstructor;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.LoopMode;
import com.jme3.input.event.InputEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3x.jfx.FxPlatformExecutor;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class PageInGame extends AppState0 {
	private final HudInGame hudController;
	private final HudTools hudTools;
	private final Commands controls;
	private final InputMapper inputMapper;
	private final Provider<PageManager> pm;
	private final InputTextureFinder inputTextureFinders;

	private Hud<HudInGame> hud;

	Subscription inputSub;
	Node player;
	int spawnEventCnt = 0;

	@Override
	protected void doInitialize() {
		hud = hudTools.newHud("Interface/HudInGame.fxml", hudController);

		//inputManager.addRawInputListener(inputListener);
		//hudTools.guiManager.setEverListeningRawInputListener(inputListener);
		//hud.getResponsibleGuiManager().getjmeFXContainer().getInputListener();
		player = new Node();
		Geometry g = new Geometry("Player", new Sphere(16, 16, 0.5f));
		Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		g.setMaterial(mat);
		player.attachChild(g);

		CameraNode camn = new CameraNode("follower", app.getCamera());
		camn.setLocalTranslation(new Vector3f(0,2,6));
		camn.lookAt(player.getWorldTranslation(), Vector3f.UNIT_Y);
		player.attachChild(camn);

	}

	@Override
	protected void doEnable() {
		hudTools.show(hud);
		//hud.getResponsibleGuiManager().setEverListeningRawInputListener(inputListener);
		//hudTools.guiManager.setEverListeningRawInputListener(inputLast);
		hudTools.guiManager.setEverListeningRawInputListener(inputMapper.rawInputListener);

		inputSub = Subscriptions.from(
				inputMapper.last.subscribe((v) -> spawnEvent(v))
				, controls.action1.value.subscribe((v) -> action1(v))
				, controls.action2.value.subscribe((v) -> action2(v))
				, controls.action3.value.subscribe((v) -> action3(v))
				, controls.action4.value.subscribe((v) -> action4(v))
				, controls.exit.value.subscribe((v) -> {
					if (!v) pm.get().goTo(Pages.Welcome.ordinal());
				})
				);
		spawnPlayer();
	}

	@Override
	protected void doDisable() {
		unspawnPlayer();
		hudTools.guiManager.setEverListeningRawInputListener(null);
		hudTools.hide(hud);
		if (inputSub != null){
			inputSub.unsubscribe();
			inputSub = null;
		}
	}

	private void action1(Boolean v) {
		action(v, hud.controller.action1);
	}

	private void action2(Boolean v) {
		action(v, hud.controller.action2);
	}

	private void action3(Boolean v) {
		action(v, hud.controller.action3);
	}

	private void action4(Boolean v) {
		action(v, hud.controller.action4);
	}

	private void action(Boolean v, javafx.scene.Node n) {
		FxPlatformExecutor.runOnFxApplication(() -> {
			System.err.println("action");
			Effect e = (v) ? new Glow(0.8): null;
			n.setEffect(e);
		});
	}

	void spawnPlayer() {
		app.enqueue(()-> {
			app.getRootNode().attachChild(player);
			return true;
		});
	}

	void unspawnPlayer() {
		app.enqueue(()-> {
			if (player != null) {
				player.removeFromParent();
			}
			return true;
		});
	}

	void spawnEvent(InputEvent evt) {
		Quad q = new Quad(0.5f, 0.5f);
		Geometry g = new Geometry("Quad", q);
		Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		//mat.setColor("Color", ColorRGBA.Blue);
		String path = inputTextureFinders.findPath(evt);
		System.err.println("path : " + path);
		mat.setTexture("ColorMap", app.getAssetManager().loadTexture(path));
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		g.setQueueBucket(Bucket.Transparent);
		g.setMaterial(mat);

		BillboardControl billboard = new BillboardControl();
		g.addControl(billboard);

		spawnEventCnt++;
		Animation anim = new Animation("goUp", 6.0f);
		anim.addTrack(new TranslationTrack(new Vector3f((spawnEventCnt % 10) - 5f,10f,0f), 5.0f));
		anim.addTrack(new RemoveTrack(5.0f));
		AnimControl ac = new AnimControl();
		ac.addAnim(anim);
		g.addControl(ac);

		app.enqueue(()-> {
			app.getRootNode().attachChild(g);
			AnimChannel c = ac.createChannel();
			c.setLoopMode(LoopMode.DontLoop);
			c.setAnim("goUp");
			return true;
		});
	}

	protected void addInfo(String info) {
		FxPlatformExecutor.runOnFxApplication(() -> {
			// autoscroll to bottom
			hudController.consoleLog.setScrollTop(Double.MIN_VALUE);
			hudController.consoleLog.appendText("\n"+info);
			System.out.println(info);
		});
	}
}
