// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import java.util.List;

import com.simsilica.lemur.core.VersionedObject;
import com.simsilica.lemur.core.VersionedReference;

public class OptionValueModel<T> implements VersionedObject<T>{

	private final List<T> values;
	private int value; 
    private long version;
    
	public OptionValueModel(List<T> values, int index) {
		this.values = values;
		this.value = index;
	}

	public OptionValueModel(List<T> values, T value) {
		this(values, values.indexOf(value));
	}
	
	@Override
	public VersionedReference<T> createReference() {
		return new VersionedReference<T>(this);
	}

	@Override
	public T getObject() {
		return values.get(value);
	}

	public int getIndex() {
		return value;
	}
	
	@Override
	public long getVersion() {
		return version;
	}

    public void setIndex( int v ) {
        if( this.value == v )
            return;
		if (v < 0) v = 0;
		if (v > (values.size() - 1)) v = (values.size() - 1);
        this.value = v;
        version++;
    }
    
    public void setObject( T value ) {
    	setIndex(values.indexOf(value));
    }

	public List<T> getValues() {
		return values;
	}
}
