/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.core.GuiControl;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.LayoutUtil;

/**
 *
 * @author David Bernard
 */
class MigComponent implements ComponentWrapper {

    public static int pixelUnitFactor = 1;
    public static int horizontalScreenDPI = 72;
    public static int verticalScreenDPI = 72;
    public int[] visualPadding = new int[]{10, 10, 10, 10};
    public int minimumWidth = 0;
    public int minimumHeight = 0;
    public int maximumWidth = LayoutUtil.INF;
    public int maximumHeight = LayoutUtil.INF;
    public GuiControl element;
    MigContainer parent;

    public Node getNode() {
        return element.getNode();
    }

    @Override
    public Object getComponent() {
        return element;
    }

    @Override
    public int getX() {
        return (int) element.getNode().getLocalTranslation().x;
    }

    @Override
    public int getY() {
        return (int) element.getNode().getLocalTranslation().y;
    }

    @Override
    public int getWidth() {
        return (int) element.getSize().x;
    }

    @Override
    public int getHeight() {
        return (int) element.getSize().y;
    }

    @Override
    public int getScreenLocationX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getScreenLocationY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMinimumWidth(int i) {
        return minimumWidth;
    }

    @Override
    public int getMinimumHeight(int i) {
        return minimumHeight;
    }

    @Override
    public int getPreferredWidth(int i) {
        return (int) element.getPreferredSize().x;
    }

    @Override
    public int getPreferredHeight(int i) {
        return (int) element.getPreferredSize().y;
    }

    @Override
    public int getMaximumWidth(int i) {
        return maximumWidth;
    }

    @Override
    public int getMaximumHeight(int i) {
        return maximumHeight;
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        element.getNode().setLocalTranslation(x, -y, 0);
        element.setSize(new Vector3f((float) w, (float) h, 0.0f));
        System.out.println("pos : " + element.getNode().getLocalTranslation() + "//" + x + ".." + y + ".." + w + ".." + h);
    }

    @Override
    public boolean isVisible() {
        return element.isEnabled();
    }

    @Override
    public int getBaseline(int i, int i1) {
        return -1;
    }

    @Override
    public boolean hasBaseline() {
        return false;
    }

    @Override
    public ContainerWrapper getParent() {
        ContainerWrapper r = parent;
        /*
         Node p = element.getNode().getParent();
         if (p != null) {
         GuiControl p0 = p.getControl(GuiControl.class);
         if (p0 != null) {
         GuiLayout p1 = p0.getLayout();
         if (p1 instanceof MigLayout) {
         r = ((MigLayout) p1).wrapper;
         }
         }
         }
         */
//        System.out.println("getParent :" + r + " of " + element);
        return r;
    }

    @Override
    public float getPixelUnitFactor(boolean bln) {
        return pixelUnitFactor;
    }

    @Override
    public int getHorizontalScreenDPI() {
        return horizontalScreenDPI;
    }

    @Override
    public int getVerticalScreenDPI() {
        return verticalScreenDPI;
    }

    @Override
    public int getScreenWidth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getScreenHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLinkId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLayoutHashCode() {
        return element.getLayout().hashCode();
    }

    @Override
    public int[] getVisualPadding() {
        Insets3f in = ((Panel) element.getNode()).getInsets();
        if (in != null) {
            visualPadding[0] = (int) in.min.x;
            visualPadding[1] = (int) in.min.y;
            visualPadding[2] = (int) in.max.x;
            visualPadding[3] = (int) in.max.y;
        } else {
            visualPadding[0] = 0;
            visualPadding[1] = 0;
            visualPadding[2] = 0;
            visualPadding[3] = 0;
        }
        return visualPadding;
    }

    @Override
    public void paintDebugOutline() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getComponetType(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
