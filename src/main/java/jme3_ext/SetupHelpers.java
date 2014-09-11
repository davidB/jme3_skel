/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;

public class SetupHelpers {
	static public void disableDefaults(SimpleApplication app) {
		app.enqueue(() -> {
			AppStateManager stateManager = app.getStateManager();
			stateManager.detach(stateManager.getState(FlyCamAppState.class));
			app.getInputManager().clearMappings();
			return true;
		});
	}

	// see http://hub.jmonkeyengine.org/wiki/doku.php/jme3:advanced:debugging
	static public void setDebug(SimpleApplication app, boolean v) {
		app.enqueue(() -> {
			AppStateManager stateManager = app.getStateManager();
			//stateManager.attach(new StatsAppState());
			stateManager.attach(new DebugKeysAppState());
			//val s = app.getStateManager().getState(BulletAppState.class);
			//if (s != null) s.setDebugEnabled(v);
			app.getInputManager().setCursorVisible(v);
			app.getViewPort().setBackgroundColor(v? ColorRGBA.Pink : ColorRGBA.White);
			//Display.setResizable(v);
			return true;
		});
	}

	static public void setAspectRatio(SimpleApplication app, float w, float h) {
		app.enqueue(() -> {
			Camera cam = app.getCamera();
			//cam.resize(w, h, true);
			float ratio = (h * cam.getWidth()) / (w * cam.getHeight());
			if (ratio < 1.0) {
				float margin = (1f - ratio) * 0.5f;
				float frustumW = cam.getFrustumRight();
				float frustumH = cam.getFrustumTop() / ratio;
				//cam.resize(cam.getWidth(), (int)(cam.getHeight() * 0.5), true);
				cam.setViewPort(0f, 1f, margin,  1 - margin);
				cam.setFrustum(cam.getFrustumNear(), cam.getFrustumFar(), -frustumW, frustumW, frustumH, -frustumH);
			}
			//			app.getRenderManager().getPreViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			//			app.getRenderManager().getPostViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			//			app.getRenderManager().getMainViews().forEach((vp) -> {;
			//				cp(cam, vp.getCamera());
			//			});
			cp(cam, app.getGuiViewPort().getCamera());
			return true;
		});
	}

	static void cp(Camera src, Camera dest) {
		if (src != dest) {
			dest.setViewPort(src.getViewPortLeft(), src.getViewPortRight(), src.getViewPortBottom(), src.getViewPortTop());
			dest.setFrustum(src.getFrustumNear(), src.getFrustumFar(), src.getFrustumLeft(), src.getFrustumRight(), src.getFrustumTop(), src.getFrustumBottom());
		}
	}

	static public void logJoystickInfo(InputManager inputManager) {
		File f = new File("log/joysticks.txt");
		f.getParentFile().mkdirs();
		try(PrintWriter out = new PrintWriter( new FileWriter( f ))) {
			dumpJoysticks( inputManager.getJoysticks(), out );
		} catch( IOException e ) {
			throw new RuntimeException( "Error writing joystick dump", e );
		}
	}

	static public void dumpJoysticks( Joystick[] joysticks, PrintWriter out ) {
		for( Joystick j : joysticks ) {
			out.println( "Joystick[" + j.getJoyId() + "]:" + j.getName() );
			out.println( "  buttons:" + j.getButtonCount() );
			for( JoystickButton b : j.getButtons() ) {
				out.println( "   " + b );
			}

			out.println( "  axes:" + j.getAxisCount() );
			for( JoystickAxis axis : j.getAxes() ) {
				out.println( "   " + axis );
			}
		}
	}
}
