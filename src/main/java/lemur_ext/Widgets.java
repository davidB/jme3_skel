// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;

/**
 *
 * @author David Bernard
 */
public class Widgets {

    public static Container newFullPageContainer(String style, Camera camera, String bgImagePath, int bgImageWidth, int bgImageHeight, String layoutConstr, String colConstr, String rowConstr) {
        Container r = new Container(style);
        r.setLocalTranslation(0, camera.getHeight(), 0);
        if (bgImagePath != null) {
        	r.setBackground(TbtQuadBackgroundComponent.create(bgImagePath, 1, 0, 0, bgImageWidth, bgImageHeight, 0, false));
        	//r.setBackground(new IconComponent("Textures/350x150.gif"));
        }
        MigLayout layout = new MigLayout(layoutConstr, colConstr, rowConstr);
        layout.debug = new MigLayoutDebugInfo();
        r.setLayout(layout);
        r.setPreferredSize(new Vector3f(camera.getWidth(), camera.getHeight(), 0));
        return r;
    }
}
