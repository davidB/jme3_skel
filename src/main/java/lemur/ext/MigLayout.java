/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur.ext;

import com.simsilica.lemur.component.AbstractGuiComponent;
import java.util.Collection;
import java.util.Map;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.core.GuiLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;

/**
 * A layout that manages layout of children via the powerfull <a
 * href="htt://www.migcalendar.com/miglayout/">MigLayout</a>
 * It can layout like a Border, Flow, Grid,...
 *
 * Issues:
 * <ul>
 * <li>ignore scale, rotation of children</li>
 * <li>not fully tested</li>
 * </ul>
 *
 * @author David Bernard
 */
public class MigLayout extends AbstractGuiComponent implements GuiLayout, Cloneable {

    private GuiControl parent;
    protected MigContainer wrapper;
    protected transient final Map<ComponentWrapper, CC> ccMap = new LinkedHashMap<>(8);
    protected transient LC lc = null;
    protected transient AC colSpecs = null, rowSpecs = null;

    public MigLayout() {
        this("", "", "");
    }

    public MigLayout(String layoutConstr, String colConstr, String rowConstr) {
        setLayoutConstraints(layoutConstr);
        setColumnConstraints(colConstr);
        setRowConstraints(rowConstr);
        wrapper = new MigContainer();
        wrapper.layout = this;
    }

    @Override
    public MigLayout clone() {
        MigLayout r = new MigLayout();
        return r;
    }

    @Override
    protected void invalidate() {
        if (parent != null) {
            parent.invalidate();
        }
    }

    @Override
    public void calculatePreferredSize(Vector3f size) {
        size.set(parent.getPreferredSize());
    }

    @Override
    public void reshape(Vector3f pos, Vector3f size) {
        System.out.println("wrapper : " + wrapper + " .. " + parent);
        // Note: we use the pos and size for scratch because we
        // are a layout and we should therefore always be last.
        Grid grid = new Grid(wrapper, lc, rowSpecs, colSpecs, ccMap, null);
        int[] b = new int[]{
            //(int) pos.x,
            //(int) pos.y,
            0, 0,
            (int) size.x,
            (int) size.y
        };
        grid.layout(b, lc.getAlignX(), lc.getAlignY(), false, false);
    }

    @Override
    public <T extends Node> T addChild(T n, Object... constraints) {
        CC cc = null;
        Object constr = "";
        if (constraints != null && constraints.length > 0) {
            constr = constraints[0];
        }
        if (constr instanceof String) {
            String cStr = ConstraintParser.prepare((String) constr);
            cc = ConstraintParser.parseComponentConstraint(cStr);
        } else if (constr instanceof CC) {
            cc = (CC) constr;
        } else {
            throw new IllegalArgumentException("exoected constraint of type String or CC");
        }

        MigComponent cw = new MigComponent();
        cw.element = n.getControl(GuiControl.class);
        cw.parent = wrapper;
        ccMap.put(cw, cc);
        if (parent != null) {
            parent.getNode().attachChild(n);
        }
        invalidate();
        return n;
    }

    @Override
    public void removeChild(Node n) {
        invalidate();
    }

    @Override
    public Collection<Node> getChildren() {
        Set<ComponentWrapper> c = ccMap.keySet();
        ArrayList<Node> r = new ArrayList<>(c.size());
        for (ComponentWrapper e : c) {
            r.add(((MigComponent) e).getNode());
        }
        return r;
    }

    @Override
    public void clearChildren() {
        if (parent != null) {
            // Need to detach any children
            for (ComponentWrapper e : ccMap.keySet()) {
                // Detaching from the parent we know prevents
                // accidentally detaching a node that has been
                // reparented without our knowledge
                parent.getNode().detachChild(((MigComponent) e).getNode());
            }
        }
        ccMap.clear();
        invalidate();
    }

    @Override
    public void attach(GuiControl parent) {
        this.parent = parent;
        Node self = parent.getNode();
        for (Node child : getChildren()) {
            self.attachChild(child);
        }
        wrapper.element = parent;
        System.out.println(">>>>>>>>>>>>>>> attach parent");
    }

    @Override
    public void detach(GuiControl parent) {
        System.out.println(">>>>>>>>>>>>>>> detach parent");
        this.parent = null;
        for (Node child : getChildren()) {
            child.removeFromParent();
        }
        wrapper.element = null;
    }

    final public void setConstraints(String layoutConstr, String colConstr, String rowConstr) {
        setLayoutConstraints(layoutConstr);
        setColumnConstraints(colConstr);
        setRowConstraints(rowConstr);
    }

    /**
     * Sets the layout constraints for the layout manager instance as a String.
     * <p>
     * See the class JavaDocs for information on how this string is formatted.
     *
     * @param constr The layout constraints as a String representation. <code>null</code> is converted * * * * * * * *
     * to <code>""</code> for storage.
     * @throws RuntimeException if the constraint was not valid.
     */
    public final void setLayoutConstraints(String constr) {
        constr = ConstraintParser.prepare((String) constr);
        lc = ConstraintParser.parseLayoutConstraint((String) constr);
        invalidate();
    }

    /**
     * Sets the column layout constraints for the layout manager instance as a String.
     * <p>
     * See the class JavaDocs for information on how this string is formatted.
     *
     * @param constr The column layout constraints as a String representation. <code>null</code> is converted * * * * *
     * to <code>""</code> for storage.
     * @throws RuntimeException if the constraint was not valid.
     */
    public final void setColumnConstraints(String constr) {
        constr = ConstraintParser.prepare((String) constr);
        colSpecs = ConstraintParser.parseColumnConstraints((String) constr);
        invalidate();
    }

    /**
     * Sets the row layout constraints for the layout manager instance as a String.
     * <p>
     * See the class JavaDocs for information on how this string is formatted.
     *
     * @param constr The row layout constraints as a String representation. <code>null</code> is converted * * * * * *
     * to <code>""</code> for storage.
     * @throws RuntimeException if the constraint was not valid.
     */
    public final void setRowConstraints(String constr) {
        constr = ConstraintParser.prepare((String) constr);
        rowSpecs = ConstraintParser.parseRowConstraints((String) constr);
        invalidate();
    }
}
