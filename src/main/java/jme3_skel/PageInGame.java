package jme3_skel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import jme3_ext.AppState0;
import jme3_ext.Hud;
import jme3_ext.HudTools;
import lombok.RequiredArgsConstructor;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3x.jfx.FxPlatformExecutor;
import com.jme3x.jfx.GuiManager;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class PageInGame extends AppState0 {
	private final HudInGame hudController;
	private final HudTools hudTools;

	private Joystick viewedJoystick;
	private GamepadView gamepad;
	private float yInfo = 0;
	private Hud<HudInGame> hud;
//
//	FunctionId forward = new FunctionId("InGame", "forward");
//	FunctionId left = new FunctionId("InGame", "left");
//	FunctionId yaw = new FunctionId("InGame", "yaw");
//	FunctionId pitch = new FunctionId("InGame", "pitch");
//	FunctionId action1 = new FunctionId("InGame", "action1");
//	FunctionId action2 = new FunctionId("InGame", "action2");
//	FunctionId action3 = new FunctionId("InGame", "action3");
//	FunctionId action4 = new FunctionId("InGame", "action4");
//	FunctionId select = new FunctionId("InGame", "select");
//	FunctionId mode = new FunctionId("InGame", "mode");
//	FunctionId actionUp = new FunctionId("InGame", "actionUp");
//	FunctionId actionDown = new FunctionId("InGame", "actionDown");
//	FunctionId actionLeft = new FunctionId("InGame", "actionLeft");
//	FunctionId actionRight = new FunctionId("InGame", "actionRight");
//	FunctionId actionLT = new FunctionId("InGame", "actionLT");
//	FunctionId actionLB = new FunctionId("InGame", "actionLB");
//	FunctionId actionRT = new FunctionId("InGame", "actionRT");
//	FunctionId actionRB = new FunctionId("InGame", "actionRB");

	@Override
	protected void doInitialize() {
		InputManager inputManager = app.getInputManager();
		Camera cam = app.getCamera();
		Node guiNode = app.getGuiNode();
		hud = hudTools.newHud("Interface/HudInGame.fxml", hudController);
		//hud.getResponsibleGuiManager().getjmeFXContainer().getInputListener();
		inputManager.addRawInputListener(new MyInputListener());
//		Joystick[] joysticks = inputManager.getJoysticks();
//		if (joysticks != null) {
//			int gamepadSize = cam.getHeight() / 2;
//			float scale = gamepadSize / 512.0f;
//			gamepad = new GamepadView();
//			gamepad.setLocalTranslation( cam.getWidth() - gamepadSize - (scale * 20), 0, 0 );
//			gamepad.setLocalScale( scale, scale, scale );
//			guiNode.attachChild(gamepad);
//
//			// Add a raw listener because it's eisier to get all joystick events
//			// this way.
//			inputManager.addRawInputListener( new JoystickEventListener() );
//		}
	}

	@Override
	protected void doEnable() {
		hudTools.show(hud);
	}

	@Override
	protected void doDisable() {
		hudTools.hide(hud);
	}

	protected void addInfo(String info) {
		FxPlatformExecutor.runOnFxApplication(() -> {
			// autoscroll to bottom
			hudController.consoleLog.setScrollTop(Double.MIN_VALUE);
			hudController.consoleLog.appendText("\n"+info);
			System.out.println(info);
		});
	}

	protected void setViewedJoystick( Joystick stick ) {
		if( this.viewedJoystick == stick ) {
			return;
		}
		this.viewedJoystick = stick;

		if( this.viewedJoystick != null ) {
			// Draw the hud
			yInfo = 0;

			addInfo(  "Joystick:\"" + stick.getName() + "\"  id:" + stick.getJoyId());

			yInfo -= 5;

			float ySave = yInfo;

			// Column one for the buttons
			addInfo( "Buttons:");
			for( JoystickButton b : stick.getButtons() ) {
				addInfo( " '" + b.getName() + "' id:'" + b.getLogicalId() + "'");
			}
			yInfo = ySave;

			// Column two for the axes
			addInfo( "Axes:");
			for( JoystickAxis a : stick.getAxes() ) {
				addInfo( " '" + a.getName() + "' id:'" + a.getLogicalId() + "' analog:" + a.isAnalog());
			}

		}
	}
	/**
	 *  Easier to watch for all button and axis events with a raw input listener.
	 */
	protected class MyInputListener implements RawInputListener {
		private void notify(Object raw, String name, float value) {
			System.out.printf("name : %s -- %s\n", name, value);
		}

		public void onJoyAxisEvent(JoyAxisEvent evt) {
			notify(evt, "J" + evt.getJoyIndex()+ "-" + evt.getAxis().getLogicalId(), evt.getValue());
		}

		public void onJoyButtonEvent(JoyButtonEvent evt) {
			notify(evt, "J" + evt.getJoyIndex()+ "-" + evt.getButton().getLogicalId(), evt.isPressed() ? 1.0f : 0.0f);
		}

		public void beginInput() {}
		public void endInput() {}
		public void onMouseMotionEvent(MouseMotionEvent evt) {
			notify(evt, "MX", evt.getDX());
			notify(evt, "MY", evt.getDY());
			notify(evt, "MW", evt.getDeltaWheel());
		}
		public void onMouseButtonEvent(MouseButtonEvent evt) {
			notify(evt, "MB"+evt.getButtonIndex(), evt.isPressed() ? 1.0f : 0.0f);
		}
		public void onKeyEvent(KeyInputEvent evt) {
			notify(evt, "K"+evt.getKeyCode(), evt.isPressed() ? 1.0f : 0.0f);
		}
		public void onTouchEvent(TouchEvent evt) {
			//notify(evt, "T"+evt.getPointerId()+ evt.getType().name(), evt.);
		}
	}

	/**
	 *  Easier to watch for all button and axis events with a raw input listener.
	 */
	protected class JoystickEventListener implements RawInputListener {

		public void onJoyAxisEvent(JoyAxisEvent evt) {
			setViewedJoystick( evt.getAxis().getJoystick() );
			gamepad.setAxisValue( evt.getAxis(), evt.getValue() );
		}

		public void onJoyButtonEvent(JoyButtonEvent evt) {
			setViewedJoystick( evt.getButton().getJoystick() );
			gamepad.setButtonValue( evt.getButton(), evt.isPressed() );
		}

		public void beginInput() {}
		public void endInput() {}
		public void onMouseMotionEvent(MouseMotionEvent evt) {}
		public void onMouseButtonEvent(MouseButtonEvent evt) {}
		public void onKeyEvent(KeyInputEvent evt) {}
		public void onTouchEvent(TouchEvent evt) {}
	}

	protected class GamepadView extends Node {

		float xAxis = 0;
		float yAxis = 0;
		float zAxis = 0;
		float zRotation = 0;

		float lastPovX = 0;
		float lastPovY = 0;

		Geometry leftStick;
		Geometry rightStick;

		Map<String, ButtonView> buttons = new HashMap<String, ButtonView>();
		private double lastTrigger;

		public GamepadView() {
			super( "gamepad" );

			// Sizes naturally for the texture size.  All positions will
			// be in that space because it's easier.
			int size = 512;
			AssetManager assetManager = app.getAssetManager();
			Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			m.setTexture( "ColorMap", assetManager.loadTexture( "Interface/Joystick/gamepad-buttons.png" ) );
			m.getAdditionalRenderState().setBlendMode( BlendMode.Alpha );
			Geometry buttonPanel = new Geometry( "buttons", new Quad(size, size) );
			buttonPanel.setLocalTranslation( 0, 0, -1 );
			buttonPanel.setMaterial(m);
			attachChild(buttonPanel);

			m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			m.setTexture( "ColorMap", assetManager.loadTexture( "Interface/Joystick/gamepad-frame.png" ) );
			m.getAdditionalRenderState().setBlendMode( BlendMode.Alpha );
			Geometry frame = new Geometry( "frame", new Quad(size, size) );
			frame.setMaterial(m);
			attachChild(frame);

			m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			m.setTexture( "ColorMap", assetManager.loadTexture( "Interface/Joystick/gamepad-stick.png" ) );
			m.getAdditionalRenderState().setBlendMode( BlendMode.Alpha );
			leftStick = new Geometry( "leftStick", new Quad(64, 64) );
			leftStick.setMaterial(m);
			attachChild(leftStick);
			rightStick = new Geometry( "rightStick", new Quad(64, 64) );
			rightStick.setMaterial(m);
			attachChild(rightStick);

			// A "standard" mapping... fits a majority of my game pads
			addButton( JoystickButton.BUTTON_0, 371, 512 - 176, 42, 42 );
			addButton( JoystickButton.BUTTON_1, 407, 512 - 212, 42, 42 );
			addButton( JoystickButton.BUTTON_2, 371, 512 - 248, 42, 42 );
			addButton( JoystickButton.BUTTON_3, 334, 512 - 212, 42, 42 );

			// Front buttons  Some of these have the top ones and the bottoms ones flipped.
			addButton( JoystickButton.BUTTON_4, 67, 512 - 111, 95, 21 );
			addButton( JoystickButton.BUTTON_5, 348, 512 - 111, 95, 21 );
			addButton( JoystickButton.BUTTON_6, 67, 512 - 89, 95, 21 );
			addButton( JoystickButton.BUTTON_7, 348, 512 - 89, 95, 21 );

			// Select and start buttons
			addButton( JoystickButton.BUTTON_8, 206, 512 - 198, 48, 30 );
			addButton( JoystickButton.BUTTON_9, 262, 512 - 198, 48, 30 );

			// Joystick push buttons
			addButton( JoystickButton.BUTTON_10, 147, 512 - 300, 75, 70 );
			addButton( JoystickButton.BUTTON_11, 285, 512 - 300, 75, 70 );

			// Fake button highlights for the POV axes
			//
			//    +Y
			//  -X  +X
			//    -Y
			//
			addButton( "POV +Y", 96, 512 - 174, 40, 38 );
			addButton( "POV +X", 128, 512 - 208, 40, 38 );
			addButton( "POV -Y", 96, 512 - 239, 40, 38 );
			addButton( "POV -X", 65, 512 - 208, 40, 38 );

			resetPositions();
		}

		private void addButton( String name, float x, float y, float width, float height ) {
			ButtonView b = new ButtonView(name, x, y, width, height);
			attachChild(b);
			buttons.put(name, b);
		}

		public void setAxisValue( JoystickAxis axis, float value ) {
			addInfo( "Axis:" + axis + "=" + value );
			if( axis == axis.getJoystick().getXAxis() ) {
				setXAxis(value);
			} else if( axis == axis.getJoystick().getYAxis() ) {
				setYAxis(-value);
			} else if( axis == axis.getJoystick().getAxis(JoystickAxis.Z_AXIS) ) {
				// Note: in the above condition, we could check the axis name but
				//       I have at least one joystick that reports 2 "Z Axis" axes.
				//       In this particular case, the first one is the right one so
				//       a name based lookup will find the proper one.  It's a problem
				//       because the erroneous axis sends a constant stream of values.
				setZAxis(value);
			} else if( axis == axis.getJoystick().getAxis(JoystickAxis.Z_ROTATION) ) {
				setZRotation(-value);
			} else if( axis == axis.getJoystick().getPovXAxis() ) {
				if( lastPovX < 0 ) {
					setButtonValue( "POV -X", false );
				} else if( lastPovX > 0 ) {
					setButtonValue( "POV +X", false );
				}
				if( value < 0 ) {
					setButtonValue( "POV -X", true );
				} else if( value > 0 ) {
					setButtonValue( "POV +X", true );
				}
				lastPovX = value;
			} else if( axis == axis.getJoystick().getPovYAxis() ) {
				if( lastPovY < 0 ) {
					setButtonValue( "POV -Y", false );
				} else if( lastPovY > 0 ) {
					setButtonValue( "POV +Y", false );
				}
				if( value < 0 ) {
					setButtonValue( "POV -Y", true );
				} else if( value > 0 ) {
					setButtonValue( "POV +Y", true );
				}
				lastPovY = value;
			} else if (axis == axis.getJoystick().getAxis("LeftTrigger")) {
				setButtonValue( JoystickButton.BUTTON_6, value > 0.0 );
			} else if (axis == axis.getJoystick().getAxis("RightTrigger")) {
                setButtonValue( JoystickButton.BUTTON_7, value > 0.0 );
            }
		}

		public void setButtonValue( JoystickButton button, boolean isPressed ) {
			addInfo( "Button:" + button + "=" + (isPressed ? "Down" : "Up") );
			setButtonValue( "" + button.getButtonId(), isPressed );
		}

		protected void setButtonValue( String name, boolean isPressed ) {
			ButtonView view = buttons.get(name);
			if( view != null ) {
				if( isPressed ) {
					view.down();
				} else {
					view.up();
				}
			}
		}

		public void setXAxis( float f ) {
			xAxis = f;
			resetPositions();
		}

		public void setYAxis( float f ) {
			yAxis = f;
			resetPositions();
		}

		public void setZAxis( float f ) {
			zAxis = f;
			resetPositions();
		}

		public void setZRotation( float f ) {
			zRotation = f;
			resetPositions();
		}

		private void resetPositions() {

			float xBase = 155;
			float yBase = 212;

			Vector2f dir = new Vector2f(xAxis, yAxis);
			float length = Math.min(1, dir.length());
			dir.normalizeLocal();

			float angle = dir.getAngle();
			float x = FastMath.cos(angle) * length * 10;
			float y = FastMath.sin(angle) * length * 10;
			leftStick.setLocalTranslation( xBase + x, yBase + y, 0 );


			xBase = 291;
			dir = new Vector2f(zAxis, zRotation);
			length = Math.min(1, dir.length());
			dir.normalizeLocal();

			angle = dir.getAngle();
			x = FastMath.cos(angle) * length * 10;
			y = FastMath.sin(angle) * length * 10;
			rightStick.setLocalTranslation( xBase + x, yBase + y, 0 );
		}
	}

	protected class ButtonView extends Node {

		private int state = 0;
		private Material material;
		private ColorRGBA hilite = new ColorRGBA( 0.0f, 0.75f, 0.75f, 0.5f );

		public ButtonView( String name, float x, float y, float width, float height ) {
			super( "Button:" + name );
			setLocalTranslation( x, y, -0.5f );

			AssetManager assetManager = app.getAssetManager();
			material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			material.setColor( "Color", hilite );
			material.getAdditionalRenderState().setBlendMode( BlendMode.Alpha );

			Geometry g = new Geometry( "highlight", new Quad(width, height) );
			g.setMaterial(material);
			attachChild(g);

			resetState();
		}

		private void resetState() {
			if( state <= 0 ) {
				setCullHint( CullHint.Always );
			} else {
				setCullHint( CullHint.Dynamic );
			}

			addInfo( getName() + " state:" + state );
		}

		public void down() {
			state++;
			resetState();
		}

		public void up() {
			state--;
			resetState();
		}
	}
}


