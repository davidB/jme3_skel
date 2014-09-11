/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 * A RawInputListener that forward every InputEvent to InputMapper.onEvent(...)
 * where dispatching can be applied.
 *
 * @author David Bernard
 */
@RequiredArgsConstructor(onConstructor=@__(@Inject))
class RawInputListener4InputMapper implements RawInputListener {
	final public InputMapper inputMapper;

	@Override
	public void beginInput() {
	}

	@Override
	public void endInput() {
	}

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		inputMapper.onEvent(evt);
	}

	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		inputMapper.onEvent(evt);
	}

	@Override
	public void onMouseMotionEvent(MouseMotionEvent evt) {
		inputMapper.onEvent(evt);
	}

	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {
		inputMapper.onEvent(evt);
	}

	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		inputMapper.onEvent(evt);
	}

	@Override
	public void onTouchEvent(TouchEvent evt) {
		inputMapper.onEvent(evt);
	}

}
