package jme3_ext;

import lombok.Data;
import rx.subjects.PublishSubject;

@Data
public final class Command<T> {
	public final String label;
	public final PublishSubject<T> value = PublishSubject.create();
}
