/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import static jme3_ext.InputMapperHelpers.tmplKeyInputEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;
import jme3_ext.InputMapper;
import jme3_ext.InputMapperHelpers;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import rx.Observer;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;

public class InputMapperTest {
	InputMapper sut = new InputMapper();

	@Test
	public void testDirectUsage() {
		// 1. Define action
		@SuppressWarnings("unchecked")
		Observer<Float> observer = mock(Observer.class);

		// 2. map InputEvents --to--> actions (setup InputMapper's mappings)
		sut.mappings.clear();
		sut.map(tmplKeyInputEvent(KeyInput.KEY_0), InputMapperHelpers::isPressedAsOne, observer);

		//3. simulate events
		sut.onEvent(new KeyInputEvent(KeyInput.KEY_0, '0', true, false));
		sut.onEvent(new KeyInputEvent(KeyInput.KEY_0, '0', false, false));

		//4. check
		InOrder inOrder1 = inOrder(observer);
		inOrder1.verify(observer, times(1)).onNext(1.0f);
		inOrder1.verify(observer, times(1)).onNext(0.0f);
		verify(observer, Mockito.never()).onCompleted();
	}

	@Test
	public void testIndirectUsage() {
		// 1. Define action (a biz facade points from where 'real' action)
		Subject<Float,Float> actionf0 = PublishSubject.create();

		// 2. map InputEvents --to--> actions (setup InputMapper's mappings)
		sut.mappings.clear();
		sut.map(tmplKeyInputEvent(KeyInput.KEY_0), InputMapperHelpers::isPressedAsOne, actionf0);

		// 3. map actions --to--> subscribe listener/observer
		@SuppressWarnings("unchecked")
		Observer<Float> observer = mock(Observer.class);
		actionf0.subscribe(observer);

		sut.onEvent(new KeyInputEvent(KeyInput.KEY_0, '0', true, false));
		sut.onEvent(new KeyInputEvent(KeyInput.KEY_0, '0', false, false));

		InOrder inOrder1 = inOrder(observer);
		inOrder1.verify(observer, times(1)).onNext(1.0f);
		inOrder1.verify(observer, times(1)).onNext(0.0f);
		verify(observer, Mockito.never()).onCompleted();
	}
}

