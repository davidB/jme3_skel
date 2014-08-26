// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.google.common.base.Function;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.BorderLayout;
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
        
        if (bgImagePath != null) {
        	r.setBackground(TbtQuadBackgroundComponent.create(bgImagePath, 1, 0, 0, bgImageWidth, bgImageHeight, 0, false));
        	//r.setBackground(new IconComponent("Textures/350x150.gif"));
        }
        MigLayout layout = new MigLayout(layoutConstr, colConstr, rowConstr);
        layout.debug = new MigLayoutDebugInfo();
        r.setLayout(layout);
        fullCamera(r, camera);
        return r;
    }
    
    public static void fullCamera(Panel panel, Camera camera) {
    	panel.setLocalTranslation(0, camera.getHeight(), 0);
    	panel.setPreferredSize(new Vector3f(camera.getWidth(), camera.getHeight(), 0));
    }
    
    public static <T> void bindText(final Label widget, VersionedObject<T> o, boolean autoUpdate, final Function<T,String> conv) {
    	final VersionedReference<T> ref = o.createReference();
		T v = ref.get();
		String txt = (conv == null) ? String.valueOf(v) : conv.apply(v); 
		widget.setText(txt);
    	if (autoUpdate) {
	    	widget.addControl(new AbstractControl() {
				@Override
				protected void controlRender(RenderManager arg0, ViewPort arg1) {
				}
	
				@Override
				protected void controlUpdate(float arg0) {
					if (ref.update()) {
						T v = ref.get();
						String txt = (conv == null) ? String.valueOf(v) : conv.apply(v); 
						widget.setText(txt);
					}
				}
			});
    	}
    }

    public static <T> void bindEnabled(final Button widget, VersionedObject<Boolean> o, boolean autoUpdate) {
    	final VersionedReference<Boolean> ref = o.createReference();
    	widget.setEnabled(ref.get());
    	if (autoUpdate) {
	    	widget.addControl(new AbstractControl() {
				@Override
				protected void controlRender(RenderManager arg0, ViewPort arg1) {
				}
	
				@Override
				protected void controlUpdate(float arg0) {
					if (ref.update()) {
						widget.setEnabled(ref.get());
					}
				}
			});
    	}
    }
    
    public static <T> Panel newSelect(ModelOptionValue<T> model, Function<T,String> conv) {
    	final Label l = new Label("undef");
    	l.setName("Label");
    	Widgets.bindText(l, model, true, conv);
    	Slider s = new Slider(Models.asRangedValueModel(model));
    	s.setName("slider");

    	Container c = new Container();
//    	c.setLayout(new MigLayout());
//    	c.addChild(l, "center");
//    	c.addChild(s, "south");

    	c.setLayout(new BorderLayout());
    	c.addChild(l, BorderLayout.Position.Center);
    	c.addChild(s, BorderLayout.Position.South);

    	return c;
    }
    
    public static Label newTitle(String txt) {
        Label l = new Label(txt);
        l.setFontSize(150f);
        l.setTextHAlignment(HAlignment.Center);
        return l;
    }


}
