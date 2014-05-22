// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.simsilica.lemur.RangedValueModel;
import com.simsilica.lemur.core.VersionedReference;

/**
*
* @author David Bernard
*/
public class Models {
	
    public static <T> RangedValueModel asRangedValueModel(OptionValueModel<T> m) {
    	return new RangedValueModelProxy(m);
    }
    
	static class RangedValueModelProxy implements RangedValueModel {
		private final OptionValueModel<?> wrappee; 
		private final int max;
		private double v;
		
		RangedValueModelProxy(OptionValueModel<?> m) {
			wrappee = m;
			max = wrappee.getValues().size() - 1;
		}
		@Override
		public VersionedReference<Double> createReference() {
			return new VersionedReference<Double>(this);
		}

		@Override
		public Double getObject() {
			return (double)wrappee.getIndex();
		}

		@Override
		public long getVersion() {
			return wrappee.getVersion();
		}

		@Override
		public double getMaximum() {
			return (double)max;
		}

		@Override
		public double getMinimum() {
			return 0;
		}

		@Override
		/**
		 * @return percent between [0.0, 1.0]
		 */
		public double getPercent() {
			return ((getObject() - 0) / max);
		}

		@Override
		public double getValue() {
			return (double)getObject();
		}

		@Override
		public void setMaximum(double arg0) {
			throw new UnsupportedOperationException("maximum is readonly");
		}

		@Override
		public void setMinimum(double arg0) {
			throw new UnsupportedOperationException("minimum is readonly");
		}

		@Override
		/**
		 * @param v between [0.0, 1.0]
		 */
		public void setPercent(double v) {
			setValue( 0 + (v * max));
		}

		@Override
		public void setValue(double v) {
			wrappee.setIndex((int) Math.round(v));
		}
		
	}

}
