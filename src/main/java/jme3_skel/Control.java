package jme3_skel;

import lombok.Data;
import rx.subjects.PublishSubject;

@Data
public final class Control<T> {
	public final String label;
	public final PublishSubject<T> value = PublishSubject.create();
}
