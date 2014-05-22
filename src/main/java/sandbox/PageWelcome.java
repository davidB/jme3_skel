/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;

import lemur_ext.Widgets;

/**
 *
 * @author David Bernard
 */
class PageWelcome extends Page {

    @Override
    Container newHud() {
        Container r = Widgets.newFullPageContainer("glass", app.getCamera(), "Textures/350x150.gif", 350, 150, "", "push[pref]push", "push[]push");
        for (int i = 0; i < 5; i++) {
            Label l = new Label("label_" + i);
            //Button l = new Button("label_" + "xxxxxxxxxxxxxxxxxxx".substring(0, i));
            l.setFontSize(50f);
            l.setTextHAlignment(HAlignment.Center);
            //TbtQuadBackgroundComponent bg = TbtQuadBackgroundComponent.create("/com/simsilica/lemur/icons/border.png", 1, 2, 2, 3, 3, 0, false);
            //bg.setColor(ColorRGBA.Red);
            //l.setBackground(bg);
            r.addChild(l, "cell 0 0, flowy, growx");
        }
        return r;
    }
}
