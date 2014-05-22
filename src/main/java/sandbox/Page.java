// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.event.BaseAppState;

/**
 *
 * @author David Bernard
 */
abstract public class Page extends BaseAppState {

    Container hudPanel;
    SimpleApplication app;

    abstract Container newHud();

    @Override
    protected void initialize(Application aplctn) {
        app = ((SimpleApplication) aplctn);
        hudPanel = newHud();
    }

    @Override
    protected void cleanup(Application aplctn) {
        hudPanel = null;
    }

    @Override
    protected void enable() {
        app.getGuiNode().attachChild(hudPanel);
    }

    @Override
    protected void disable() {
        app.getGuiNode().detachChild(hudPanel);
    }
}
