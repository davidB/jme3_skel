package jme3_ext;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import rx.Observer;

import com.jme3.input.event.InputEvent;

/**
 * InputMapper allow to store mapping rule between an InputEvent (template) and an Observer.
 *
 * InputMapper is an alternative to jmonkeyengine  default method (using Trigger + Action/AnalogListener).
 *
 * <ul>
 * <li>Using InputEvent as template allow to register template dynamicly (by recording last InputEvent via RawInputListenerLatest)</li>
 * <li>Using Extractor to convert InputEvent to value allow to connect several InputEvent (keyboard, joystick) to same Action</li>
 * <li>Using Function allow to customize Extractor, the way to compare InputEvent (template and onEvent)</li>
 * </ul>
 *
 * @author David Bernard
 *
 * @TODO find a better way to manage type checking
 */
@RequiredArgsConstructor
public final class InputMapper {
	@Data
	public static class Mapping<E extends InputEvent, R> {
		public final E template;
		public final Function<E, R> extract;
		public final Observer<R> dest;

		// the function is place as member of Entry to simplify type notation and checking
		// harder in other places due to collection of mixed Entry for several types of InputEvent and R
		public void apply(InputEvent e) {
			dest.onNext(extract.apply((E)e));
		}
	}

	/**
	 * mappings of all rules, the key is the result of inputEventHasher.apply(Mapping.template)
	 * Using a map avoid comparison with all rules to search Mapping by InputEvent.
	 * Using public field allow user to explore, to edit the map as he like
	 * (eg: to search template by Observer to expose controls, see InputMapperHelpers.findTemplatesOf()).
	 */
	public final Map<Integer, Mapping<? extends InputEvent, ? extends Object>> mappings = new TreeMap<>();

	/**
	 * Convert an InputEvent into an Integer.
	 * Used to compare two InputEvent (the template and the incoming)
	 */
	public final Function<InputEvent, Integer> inputEventHasher;

	public InputMapper() {
		this(InputMapperHelpers::defaultInputEventHash);
	}

	/**
	 * Store the mapping rules : template -- extract --> dest.
	 * It replaces existing rules for the template.
	 *
	 * A template can only be connected to one rule.
	 *
	 * @param template the InputEvent template that should match (via inputEventHasher
	 * @param extract the extractor/converter from future matching InputEvent to value to push to dest.
	 * @param dest the destination.
	 */
	public <E extends InputEvent, R> void map(E template, Function<E, R> extract, Observer<R> dest) {
		mappings.put(inputEventHasher.apply(template), new Mapping<E,R>(template, extract, dest));
	}

	public <E extends InputEvent> void onEvent(E evt) {
		int h = inputEventHasher.apply(evt);
		InputMapper.Mapping<?, ?> route = mappings.get(h);
		if (route != null) route.apply(evt);
	}

}

