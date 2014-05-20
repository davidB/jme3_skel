// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package lemur_ext;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import gui_utils.MeshGrid;
import static lemur_ext.MigLayout.yDir;

/**
 *
 * @author David Bernard
 */
public class MigLayoutDebugInfo {

    public static String nodeName = "MigLayoutDebugInfo";
    private MeshGrid cells = new MeshGrid();
    private MeshGrid components = new MeshGrid();
    private MeshGrid centerBorder = new MeshGrid();
    private MeshGrid grid = new MeshGrid();
    public Material componentsMat = GuiGlobals.getInstance().createMaterial(ColorRGBA.Blue, false).getMaterial();
    public Material cellsMat = GuiGlobals.getInstance().createMaterial(ColorRGBA.Red, false).getMaterial();
    public Material centerBorderMat = GuiGlobals.getInstance().createMaterial(ColorRGBA.Red.mult(0.8f), false).getMaterial();
    public Material gridMat = GuiGlobals.getInstance().createMaterial(ColorRGBA.Yellow, false).getMaterial();
    private final Node node = new Node(nodeName);

    public void addComponent(float x, float y, float w, float h) {
        components.addRect(x, y, w, h * yDir);
    }

    public void addCell(float x, float y, float w, float h) {
        cells.addRect(x, y, w, h * yDir);
    }

    public void setContainer(float w, float h) {
        centerBorder.clear();
        centerBorder.addSegment(0, 0.5f * h * yDir, w, 0.5f * h * yDir);
        centerBorder.addSegment(0.5f * w, 0 * yDir, 0.5f * w, h * yDir);
    }

    public Node getNode() {
        node.detachAllChildren();
        node.attachChild(cells.newGeometry("cells", cellsMat, 1.0f));
        node.attachChild(components.newGeometry("components", componentsMat, 1.0f));
        node.attachChild(centerBorder.newGeometry("centerBorder", centerBorderMat, 0.5f));
        node.attachChild(grid.newGeometry("grid", gridMat, 1.0f));
        return node;
    }

    void setGrid(float width, int[] widths, float height, int[] heights) {
        float x0 = 0;
        float y0 = 0;
        float x1 = width;
        float y1 = 0;
        grid.addSegment(x0, y0 * yDir, x1, y1 * yDir);
        for (int i = 0; i < heights.length; i++) {
            y0 += heights[i];
            y1 += heights[i];
            grid.addSegment(x0, y0 * yDir, x1, y1 * yDir);
        }
        x0 = 0;
        y0 = 0;
        x1 = 0;
        y1 = width;
        grid.addSegment(x0, y0 * yDir, x1, y1 * yDir);
        for (int i = 0; i < widths.length; i++) {
            x0 += widths[i];
            x1 += widths[i];
            grid.addSegment(x0, -y0, x1, -y1);
        }
    }
}
