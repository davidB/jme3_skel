/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import lombok.Data;
import rx.subjects.PublishSubject;

@Data
public final class Command<T> {
	public final String label;
	public final PublishSubject<T> value = PublishSubject.create();
}
