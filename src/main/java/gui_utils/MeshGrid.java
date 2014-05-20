// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package gui_utils;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.util.LinkedList;

/**
 *
 * @author David Bernard
 */
public class MeshGrid {

    private LinkedList<Vector3f> points = new LinkedList<>();

    public void clear() {
        points.clear();
    }

    public void addRect(float x, float y, float w, float h) {
        Vector3f x0y0 = new Vector3f(x, y, 0f);
        Vector3f x1y0 = new Vector3f(x + w, y, 0f);
        Vector3f x1y1 = new Vector3f(x + w, y + h, 0f);
        Vector3f x0y1 = new Vector3f(x, y + h, 0f);
        points.add(x0y0);
        points.add(x1y0);
        points.add(x1y0);
        points.add(x1y1);
        points.add(x1y1);
        points.add(x0y1);
        points.add(x0y1);
        points.add(x0y0);
    }

    public void addSegment(float x0, float y0, float x1, float y1) {
        points.add(new Vector3f(x0, y0, 0f));
        points.add(new Vector3f(x1, y1, 0f));
    }

    public Geometry newGeometry(String name, Material mat, float lineWidth) {
        // Vertex positions in space
        Vector3f[] vertices = new Vector3f[points.size()];
        vertices = points.toArray(vertices);

        // Indexes. We define the order in which mesh should be constructed
        int numIndexes = vertices.length; //2 * vertices.length;
        int[] indexes = new int[numIndexes];
//        int numLines = numIndexes / 2;
//        int padding = 0;
//        for (int i = 0; i < numLines – padding; i++) {
//            indexes[2 * i] = i;
//            indexes[2 * i + 1] = (i + 1) % numLines;
//        }
        for (int i = 0; i < numIndexes; i++) {
            indexes[i] = i;
        }

        // Setting buffers
        Mesh lineMesh = new Mesh();
        lineMesh.setMode(Mesh.Mode.Lines);
        lineMesh.setLineWidth(lineWidth);
        lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        lineMesh.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
        lineMesh.updateBound();

        Geometry lineGeom = new Geometry(name, lineMesh);
        Material matWireframe = mat.clone();
        //matWireframe.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        //matWireframe.getAdditionalRenderState().setWireframe(true);
        lineGeom.setMaterial(matWireframe);
        return lineGeom;
    }
}
