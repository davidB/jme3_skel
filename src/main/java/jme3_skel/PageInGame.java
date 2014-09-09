package jme3_skel;

import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;

import javax.inject.Inject;
import javax.inject.Provider;

import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import jme3_ext.InputMapper;
import jme3_ext.InputMapperHelpers;
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
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.debug.Grid;
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
	final Node scene = new Node("scene");
	final Control4Translation c4t = new Control4Translation();
	int spawnEventCnt = 0;

	@Override
	protected void doInitialize() {
		hud = hudTools.newHud("Interface/HudInGame.fxml", hudController);
	}

	@Override
	protected void doEnable() {
		hudTools.show(hud);
		app.getInputManager().addRawInputListener(inputMapper.rawInputListener);

		inputSub = Subscriptions.from(
			controls.exit.value.subscribe((v) -> {
				if (!v) pm.get().goTo(Pages.Welcome.ordinal());
			})
			, inputMapper.last.subscribe((v) -> spawnEvent(v))
			, controls.action1.value.subscribe((v) -> action1(v))
			, controls.action2.value.subscribe((v) -> action2(v))
			, controls.action3.value.subscribe((v) -> action3(v))
			, controls.action4.value.subscribe((v) -> action4(v))
			, controls.moveX.value.subscribe((v) -> {c4t.speedX = v * 0.5f;})
			, controls.moveZ.value.subscribe((v) -> {c4t.speedZ = v * -0.5f;})
		);
		spawnScene();
	}

	@Override
	protected void doDisable() {
		unspawnScene();
		app.getInputManager().removeRawInputListener(inputMapper.rawInputListener);
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
			Effect e = (v) ? new Glow(0.8): null;
			n.setEffect(e);
		});
	}

	void spawnScene() {
		app.enqueue(()-> {
			scene.getChildren().clear();
			scene.attachChild(makePlayer());
			scene.attachChild(makeEnvironment());
			app.getRootNode().attachChild(scene);
			return true;
		});
	}

	void unspawnScene() {
		app.enqueue(()-> {
			scene.removeFromParent();
			return true;
		});
	}

	void spawnEvent(InputEvent evt) {
		addInfo(InputMapperHelpers.toString(evt, false));
		Quad q = new Quad(0.5f, 0.5f);
		Geometry g = new Geometry("Quad", q);
		Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		//mat.setColor("Color", ColorRGBA.Blue);
		String path = inputTextureFinders.findPath(evt);
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

		g.setLocalTranslation(scene.getChild("player").getWorldTranslation());
		app.enqueue(()-> {
			scene.attachChild(g);
			AnimChannel c = ac.createChannel();
			c.setLoopMode(LoopMode.DontLoop);
			c.setAnim("goUp");
			return true;
		});
	}

	Spatial makePlayer() {
		Node root = new Node("player");
		Geometry g = new Geometry("Player", new Sphere(16, 16, 0.5f));
		Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		g.setMaterial(mat);
		root.attachChild(g);

		CameraNode camn = new CameraNode("follower", app.getCamera());
		camn.setLocalTranslation(new Vector3f(0,2,6));
		camn.lookAt(root.getWorldTranslation(), Vector3f.UNIT_Y);
		root.attachChild(camn);

		root.addControl(c4t);
		return root;
	}

	Spatial makeEnvironment() {
		Node root = new Node("environment");
		root.attachChild(makeGrid(Vector3f.ZERO, 30f, ColorRGBA.Green));
		return root;
	}

	Spatial makeGrid(Vector3f pos, float size, ColorRGBA color){
		int nb = (int) Math.ceil(size / 0.2);
		Geometry g = new Geometry("wireframe grid", new Grid(nb, nb, 0.2f) );
		Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		g.center().move(pos);
		return g;
	}

	protected void addInfo(String info) {
		FxPlatformExecutor.runOnFxApplication(() -> {
			// autoscroll to bottom
			hudController.consoleLog.appendText("\n"+info);
			hudController.consoleLog.setScrollTop(Double.MIN_VALUE);
		});
	}

	class Control4Translation extends AbstractControl {
		public float speedX = 0f;
		public float speedZ = 0f;
		@Override
		protected void controlUpdate(float tpf) {
			Vector3f pos = getSpatial().getLocalTranslation();
			pos.x += speedX * tpf;
			pos.z += speedZ * tpf;
			getSpatial().setLocalTranslation(pos);
		}

		@Override
		protected void controlRender(RenderManager rm, ViewPort vp) {
		}

	}
}
