// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedObject;
import com.simsilica.lemur.core.VersionedReference;

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
    
    public static <T> void bindLabel(final Label label, VersionedObject<T> o, boolean autoUpdate) {
    	final VersionedReference<T> ref = o.createReference();
    	label.setText(ref.get().toString());
    	if (autoUpdate) {
	    	label.addControl(new AbstractControl() {
				@Override
				protected void controlRender(RenderManager arg0, ViewPort arg1) {
				}
	
				@Override
				protected void controlUpdate(float arg0) {
					if (ref.update()) {
						label.setText(ref.get().toString());
					}
				}
			});
    	}
    }
}
