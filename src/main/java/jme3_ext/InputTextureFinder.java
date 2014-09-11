/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import java.net.URL;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;

import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class InputTextureFinder {
	public String base = "Textures/Inputs";
	public String folderKeyInputEvent = "Keyboard & Mouse";
	public String theme = "Black";

	/**
	 * @TODO complete missing conversion, texture,...
	 * @TODO provide alternative for LSHIFT/RSHIFT, LCONTROL/RCONTROL, DIVIDE/SLASH,...
	 * @TODO provide alternative for numpad and regular number
	 */
	public String find(KeyInputEvent evt) {
		String variant = null;
		switch(evt.getKeyCode()) {
		case KeyInput.KEY_0: variant = "0"; break;
		case KeyInput.KEY_1: variant = "1"; break;
		case KeyInput.KEY_2: variant = "2"; break;
		case KeyInput.KEY_3: variant = "3"; break;
		case KeyInput.KEY_4: variant = "4"; break;
		case KeyInput.KEY_5: variant = "5"; break;
		case KeyInput.KEY_6: variant = "6"; break;
		case KeyInput.KEY_7: variant = "7"; break;
		case KeyInput.KEY_8: variant = "8"; break;
		case KeyInput.KEY_9: variant = "9"; break;
		case KeyInput.KEY_NUMPAD0: variant = "0"; break;
		case KeyInput.KEY_NUMPAD1: variant = "1"; break;
		case KeyInput.KEY_NUMPAD2: variant = "2"; break;
		case KeyInput.KEY_NUMPAD3: variant = "3"; break;
		case KeyInput.KEY_NUMPAD4: variant = "4"; break;
		case KeyInput.KEY_NUMPAD5: variant = "5"; break;
		case KeyInput.KEY_NUMPAD6: variant = "6"; break;
		case KeyInput.KEY_NUMPAD7: variant = "7"; break;
		case KeyInput.KEY_NUMPAD8: variant = "8"; break;
		case KeyInput.KEY_NUMPAD9: variant = "9"; break;
		case KeyInput.KEY_A: variant = "A"; break;
		case KeyInput.KEY_B: variant = "B"; break;
		case KeyInput.KEY_C: variant = "C"; break;
		case KeyInput.KEY_D: variant = "D"; break;
		case KeyInput.KEY_E: variant = "E"; break;
		case KeyInput.KEY_F: variant = "F"; break;
		case KeyInput.KEY_G: variant = "G"; break;
		case KeyInput.KEY_H: variant = "H"; break;
		case KeyInput.KEY_I: variant = "I"; break;
		case KeyInput.KEY_J: variant = "J"; break;
		case KeyInput.KEY_K: variant = "K"; break;
		case KeyInput.KEY_L: variant = "L"; break;
		case KeyInput.KEY_M: variant = "M"; break;
		case KeyInput.KEY_N: variant = "N"; break;
		case KeyInput.KEY_O: variant = "O"; break;
		case KeyInput.KEY_P: variant = "P"; break;
		case KeyInput.KEY_Q: variant = "Q"; break;
		case KeyInput.KEY_R: variant = "R"; break;
		case KeyInput.KEY_S: variant = "S"; break;
		case KeyInput.KEY_T: variant = "T"; break;
		case KeyInput.KEY_U: variant = "U"; break;
		case KeyInput.KEY_V: variant = "V"; break;
		case KeyInput.KEY_W: variant = "W"; break;
		case KeyInput.KEY_X: variant = "X"; break;
		case KeyInput.KEY_Y: variant = "Y"; break;
		case KeyInput.KEY_Z: variant = "Z"; break;
		case KeyInput.KEY_UP: variant = "Arrow_Up"; break;
		case KeyInput.KEY_DOWN: variant = "Arrow_Down"; break;
		case KeyInput.KEY_LEFT: variant = "Arrow_Left"; break;
		case KeyInput.KEY_RIGHT: variant = "Arrow_Right"; break;
		case KeyInput.KEY_SPACE: variant = "Space"; break;
		case KeyInput.KEY_TAB: variant = "Tab"; break;
		case KeyInput.KEY_CAPITAL: variant = "Caps_Lock"; break;
		case KeyInput.KEY_LSHIFT:
		case KeyInput.KEY_RSHIFT: variant = "Shift"; break;
		case KeyInput.KEY_LCONTROL:
		case KeyInput.KEY_RCONTROL: variant = "Ctrl"; break;
		case KeyInput.KEY_LMENU:
		case KeyInput.KEY_RMENU: variant = "Alt"; break;
		case KeyInput.KEY_LMETA:
		case KeyInput.KEY_RMETA: variant = "Command"; break; // or "Win"
		case KeyInput.KEY_BACK: variant = "Backspace"; break;
		case KeyInput.KEY_DELETE: variant = "Del"; break;
		case KeyInput.KEY_RETURN: variant = "Enter_Alt"; break;
		case KeyInput.KEY_ESCAPE: variant = "Esc"; break;
		case KeyInput.KEY_HOME: variant = "Home"; break;
		case KeyInput.KEY_END: variant = "End"; break;
		case KeyInput.KEY_PGDN: variant = "Page_Down"; break;
		case KeyInput.KEY_PGUP: variant = "Page_Up"; break;
		case KeyInput.KEY_INSERT: variant = "Insert"; break;
		case KeyInput.KEY_SUBTRACT:
		case KeyInput.KEY_MINUS: variant = "Minus"; break;
		case KeyInput.KEY_DIVIDE: // ???
		case KeyInput.KEY_SLASH: variant = "Slash"; break;
		case KeyInput.KEY_MULTIPLY: variant = "Asterisk"; break;
		case KeyInput.KEY_NUMLOCK: variant = "Num_Lock"; break;
		}
		return (variant != null)? String.format("%s/%s/Keyboard_%s_%s.png", base, folderKeyInputEvent, theme, variant) : null;
	}

	public String find(MouseButtonEvent evt) {
		String variant = null;
		switch(evt.getButtonIndex()) {
		case 0: variant = "Left"; break;
		case 1: variant = "Right"; break;
		case 2: variant = "Middle"; break;
		}
		return (variant != null)? String.format("%s/%s/Keyboard_%s_Mouse_%s.png", base, folderKeyInputEvent, theme, variant) : null;
	}

	public String find(MouseMotionEvent evt) {
		return String.format("%s/%s/Keyboard_%s_Mouse_Simple.png", base, folderKeyInputEvent, theme);
	}

	//TODO use value of the event to provide more info (eg: Dpad_Left, mixing with Directional Arrow)
	public String find(JoyAxisEvent evt) {
		String prefix = findJoystickPrefix(evt.getAxis().getJoystick());
		String variant = null;
		switch(evt.getAxis().getName()) {
		case "Left Thumb 3": variant = "LB"; break;
		case "Right Thumb 3": variant = "RB"; break;
		case "y":
		case "x": variant = "Left_Stick"; break;
		case "rx":
		case "ry": variant = "Right_Stick"; break;
		case "z":  variant = "LT"; break;
		case "rz":  variant = "RT"; break;
		case "pov": variant = "Dpad"; break;
		case "pov_x": variant = (evt.getValue() > 0.5) ? "Dpad_Right" : (evt.getValue() < -0.5) ? "Dpad_Left" : "Dpad"; break;
		case "pov_y": variant = (evt.getValue() > 0.5) ? "Dpad_Up" : (evt.getValue() < -0.5) ? "Dpad_Down" : "Dpad"; break;
		}
		//TODO force capitalize(variant) ?
		return String.format("%s/%s_%s.png", base, prefix, variant);
	}

	public String find(JoyButtonEvent evt) {
		String prefix = findJoystickPrefix(evt.getButton().getJoystick());
		String variant = evt.getButton().getName();
		switch(variant) {
		case "Left Thumb 3": variant = "LT"; break;
		case "Right Thumb 3": variant = "RT"; break;
		case "Left Thumb": variant = "LB"; break;
		case "Right Thumb": variant = "RB"; break;
		case "Select": variant = "Back"; break;
		case "Mode": variant = "Start"; break;
		}
		//TODO force capitalize(variant) ?
		return String.format("%s/%s_%s.png", base, prefix, variant);
	}

	public String findJoystickPrefix(Joystick joystick) {
		String kindName = joystick.getName().toLowerCase();
		String b = "Generic"; // xbox is used as generic gamepad until there are assets for others.
		if ((kindName.contains("xbox") || kindName.contains("x-box")) && kindName.contains("360")) {
			b = "Xbox 360/360";
		} else if ((kindName.contains("xbox") || kindName.contains("x-box")) && kindName.contains("one")) {
			b = "Xbox One/One";
		} else if (kindName.contains("ps3")) {
			b = "PS3/PS3";
		} else if (kindName.contains("ps4")) {
			b = "PS4/PS4";
		} else if (kindName.contains("ouya")) {
			b = "Ouya/Ouya";
		}
		return b;
	}

	//TODO manage TouchEvent
	//TODO send url of a default (unknown texture)
	private String findPath0(InputEvent evt) {
		String path =
			(evt instanceof JoyButtonEvent) ? find((JoyButtonEvent)evt)
			: (evt instanceof JoyAxisEvent) ? find((JoyAxisEvent)evt)
			: (evt instanceof KeyInputEvent) ? find((KeyInputEvent)evt)
			: (evt instanceof MouseButtonEvent) ? find((MouseButtonEvent)evt)
			: (evt instanceof MouseMotionEvent) ? find((MouseMotionEvent)evt)
			: null
			;
		return path;
	}

	public String findPath(InputEvent evt) {
		String path = findPath0(evt);
		URL url = (path != null) ? Thread.currentThread().getContextClassLoader().getResource(path) : null;
		return (url == null) ? base + "/undef.png" : path;
	}

	public URL findUrl(InputEvent evt) {
		String path = findPath(evt);
		return (path != null) ? Thread.currentThread().getContextClassLoader().getResource(path) : null;
	}

}
