package jme3_ext;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 * A RawInputListener that store in a LIFO the last 10 input events.
 *
 * Usage examples: configure controls by finding last Input used.
 *
 * @author David Bernard
 */
public class RawInputListenerLatest implements RawInputListener{

	public final Deque<InputEvent> latest = new  LinkedBlockingDeque<>(10);

	@Override
	public void beginInput() {
	}

	@Override
	public void endInput() {
	}

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		latest.addFirst(evt);
	}

	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		latest.addFirst(evt);
	}

	@Override
	public void onMouseMotionEvent(MouseMotionEvent evt) {
		latest.addFirst(evt);
	}

	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {
		latest.addFirst(evt);
	}

	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		latest.addFirst(evt);
	}

	@Override
	public void onTouchEvent(TouchEvent evt) {
		latest.addFirst(evt);
	}

}
