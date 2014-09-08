package jme3_ext;

import java.util.Collection;
import java.util.stream.Collectors;

import rx.Observer;

import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 * A collection of functions, mainly to simplify usage of InputMapper.
 *
 * @author David Bernard
 */
public class InputMapperHelpers {

	/**
	 * The default InputEventHash function used by InputMapper.
	 */
	static public int defaultInputEventHash(InputEvent evt) {
		//pseudo-pattern matching
		if (evt instanceof JoyAxisEvent) {
			JoyAxisEvent e = (JoyAxisEvent)evt;
			return 1000 + e.getJoyIndex() * 100 + e.getAxisIndex();
		} else if (evt instanceof MouseMotionEvent) {
			return 2000;
		} else if (evt instanceof JoyButtonEvent) {
			JoyButtonEvent e = (JoyButtonEvent)evt;
			return 3000 + e.getJoyIndex() *100 + e.getButtonIndex();
		} else if (evt instanceof MouseButtonEvent) {
			MouseButtonEvent e = (MouseButtonEvent)evt;
			return 4000 + e.getButtonIndex();
		} else if (evt instanceof KeyInputEvent) {
			KeyInputEvent e = (KeyInputEvent)evt;
			return 5000 + e.getKeyCode();
		} else if (evt instanceof TouchEvent) {
			TouchEvent e = (TouchEvent)evt;
			return 6000 + e.getPointerId() * 100 + e.getType().ordinal();
		}
		return 0;//evt.hashCode();
	}

	/**
	 * A factory to create a Template KeyInputEvent usable with defaultInputEventHash.
	 */
	static public KeyInputEvent tmplKeyInputEvent(int keyCode) {
		return new KeyInputEvent(keyCode, (char)0, true, false);
	}

	/**
	 * A factory to create a Template JoyAxisEvent usable with defaultInputEventHash.
	 */
	static public JoyAxisEvent tmplJoyAxisEvent(JoystickAxis axis) {
		return new JoyAxisEvent(axis, 0);
	}

	/**
	 * A factory to create a Template JoyButtonEvent usable with defaultInputEventHash.
	 */
	static public JoyButtonEvent tmplJoyButtonEvent(JoystickButton button) {
		return new JoyButtonEvent(button, true);
	}

	/**
	 * A factory to create a Template MouseMotionEvent usable with defaultInputEventHash.
	 */
	static public MouseMotionEvent tmplMouseMotionEvent() {
		return new MouseMotionEvent(0,0,0,0,0,0);
	}

	/**
	 * A factory to create a Template MouseButtonEvent usable with defaultInputEventHash.
	 */
	static public MouseButtonEvent tmplMouseButtonEvent(int btnIndex) {
		return new MouseButtonEvent(btnIndex,true, 0, 0);
	}

	/**
	 * Convert KeyInputEvent into 1.0f when isPressed() else 0.0f.
	 */
	static public float isPressedAsOne(KeyInputEvent evt) {
		return evt.isPressed() ? 1.0f : 0.0f;
	}

	/**
	 * Convert KeyInputEvent into -1.0f when isPressed() else 0.0f.
	 */
	static public float isPressedAsNegOne(KeyInputEvent evt) {
		return evt.isPressed() ? -1.0f : 0.0f;
	}

	static public Collection<InputEvent> findTemplatesOf(InputMapper inputMapper, Observer<?> dest) {
		return inputMapper.mappings.entrySet().stream()
			.filter((v) -> dest.equals(v.getValue().dest))
			.map((v) -> v.getValue().template)
			.collect(Collectors.toList())
			;
	}

	static public void mapKey(InputMapper m, int keyCode, boolean asOne, Observer<Float> dest) {
		m.map(tmplKeyInputEvent(keyCode), asOne ? InputMapperHelpers::isPressedAsOne : InputMapperHelpers::isPressedAsNegOne, dest);
	}
}