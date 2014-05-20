/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import java.util.Set;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import static lemur_ext.MigLayout.yDir;

/**
 *
 * @author David Bernard
 */
class MigContainer extends MigComponent implements ContainerWrapper {

    MigLayout layout;

    @Override
    public ComponentWrapper[] getComponents() {
        Set<ComponentWrapper> keys = layout.ccMap.keySet();
        return keys.toArray(new ComponentWrapper[keys.size()]);
    }

    @Override
    public int getComponentCount() {
        return layout.ccMap.size();
    }

    @Override
    public Object getLayout() {
        return layout;
    }

    @Override
    public boolean isLeftToRight() {
        return true;
    }

    @Override
    public void paintDebugCell(int x, int y, int w, int h) {
        if (layout != null && layout.debug != null) {
            layout.debug.addCell(x, y * yDir, w, h);
        }
    }
}
