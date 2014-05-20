/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.event.BaseAppState;
import lemur_ext.MigLayout;
import lemur_ext.MigLayoutDebugInfo;

/**
 *
 * @author David Bernard
 */
class PageWelcome extends BaseAppState {

    Container hudPanel;
    SimpleApplication app;

    public PageWelcome() {
    }

    @Override
    protected void initialize(Application aplctn) {
        app = ((SimpleApplication) aplctn);

        hudPanel = new Container("glass");
        hudPanel.setLocalTranslation(0, app.getCamera().getHeight(), 0);

        MigLayout layout = new MigLayout("", "push[pref]push", "push[]push");
        layout.debug = new MigLayoutDebugInfo();
        hudPanel.setLayout(layout);
        hudPanel.setPreferredSize(new Vector3f(app.getCamera().getWidth(), app.getCamera().getHeight(), 0));
        for (int i = 0; i < 5; i++) {
            //Label l = new Label("label_" + i);
            Button l = new Button("label_" + "xxxxxxxxxxxxxxxxxxx".substring(0, i));
            l.setFontSize(50f);
            l.setTextHAlignment(HAlignment.Center);
            //TbtQuadBackgroundComponent bg = TbtQuadBackgroundComponent.create("/com/simsilica/lemur/icons/border.png", 1, 2, 2, 3, 3, 0, false);
            //bg.setColor(ColorRGBA.Red);
            //l.setBackground(bg);
            hudPanel.addChild(l, "cell 0 0, flowy, growx");
        }
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
