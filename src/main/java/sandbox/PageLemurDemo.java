/*
 * Copyright (c) 2014-2014 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package sandbox;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.material.RenderState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.Styles;

/**
 *
 * @version $Revision: 1309 $
 * @author Paul Speed
 */
public class PageLemurDemo extends AbstractAppState {

    SimpleApplication app;
    // Define some model references we will use in
    // update.
    private VersionedReference<Double> redRef;
    private VersionedReference<Double> greenRef;
    private VersionedReference<Double> blueRef;
    private VersionedReference<Double> alphaRef;
    private VersionedReference<Boolean> showStatsRef;
    private VersionedReference<Boolean> showFpsRef;
    private ColorRGBA boxColor = ColorRGBA.Blue.clone();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.
        this.app = ((SimpleApplication) app);
        Node guiNode = this.app.getGuiNode();
        Node rootNode = this.app.getRootNode();

        // Now construct some HUD panels in the "glass" style that
        // we just configured above.
        Container hudPanel = new Container("glass");
        hudPanel.setLocalTranslation(0, app.getCamera().getHeight(), 0);
        guiNode.attachChild(hudPanel);

        // Create a top panel for some stats toggles.
        Container panel = new Container("glass");
        hudPanel.addChild(panel);

        panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f), 5, 5, 0.02f, false));
        panel.addChild(new Label("Stats Settings", new ElementId("header"), "glass"));
        panel.addChild(new Panel(2, 2, ColorRGBA.Cyan, "glass")).setUserData(LayerComparator.LAYER, 2);

        // Adding components returns the component so we can set other things
        // if we want.
        Checkbox temp = panel.addChild(new Checkbox("Show Stats"));
        temp.setChecked(true);
        showStatsRef = temp.getModel().createReference();

        temp = panel.addChild(new Checkbox("Show FPS"));
        temp.setChecked(true);
        showFpsRef = temp.getModel().createReference();


        // Custom "spacer" element type
        hudPanel.addChild(new Panel(10f, 10f, new ElementId("spacer"), "glass"));

        // Create a second panel in the same overall HUD panel
        // that lets us tweak things about the cube.
        panel = new Container("glass");
        panel.setBackground(new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f), 5, 5, 0.02f, false));
        // Custom "header" element type.
        panel.addChild(new Label("Cube Settings", new ElementId("header"), "glass"));
        panel.addChild(new Panel(2, 2, ColorRGBA.Cyan, "glass")).setUserData(LayerComparator.LAYER, 2);
        panel.addChild(new Label("Red:"));
        redRef = panel.addChild(new Slider("glass")).getModel().createReference();
        panel.addChild(new Label("Green:"));
        greenRef = panel.addChild(new Slider("glass")).getModel().createReference();
        panel.addChild(new Label("Blue:"));
        blueRef = panel.addChild(new Slider(new DefaultRangedValueModel(0, 100, 100), "glass")).getModel().createReference();
        panel.addChild(new Label("Alpha:"));
        alphaRef = panel.addChild(new Slider(new DefaultRangedValueModel(0, 100, 100), "glass")).getModel().createReference();
        Button btn = panel.addChild(new Button("Hello Btn"));
        btn.addClickCommands(new Command<Button>() {
            public void execute(Button source) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        btn.setTextHAlignment(HAlignment.Center);
        btn.setBackground(TbtQuadBackgroundComponent.create("/com/simsilica/lemur/icons/border.png", 1, 2, 2, 3, 3, 0, false));

        hudPanel.addChild(panel);
        guiNode.attachChild(hudPanel);

        // Increase the default size of the hud to be a little wider
        // if it would otherwise be smaller.  Height is unaffected.
        Vector3f hudSize = new Vector3f(200, 0, 0);
        hudSize.maxLocal(hudPanel.getPreferredSize());
        hudPanel.setPreferredSize(hudSize);

        // Note: after next nightly, this will also work:
        // hudPanel.setPreferredSize( new Vector3f(200,0,0).maxLocal(hudPanel.getPreferredSize()) );

        // Something in scene
        Box box = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", boxColor);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        // A draggable bordered panel
        Container testPanel = new Container();
        testPanel.setPreferredSize(new Vector3f(200, 200, 0));
        testPanel.setBackground(TbtQuadBackgroundComponent.create("/com/simsilica/lemur/icons/border.png",
                1, 2, 2, 3, 3, 0, false));
        Label test = testPanel.addChild(new Label("Border Test"));

        // Center the text in the box.
        test.setInsetsComponent(new DynamicInsetsComponent(0.5f, 0.5f, 0.5f, 0.5f));
        testPanel.setLocalTranslation(400, 400, 0);

        CursorEventControl.addListenersToSpatial(testPanel, new DragHandler());
        guiNode.attachChild(testPanel);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
        if (showStatsRef.update()) {
            app.setDisplayStatView(showStatsRef.get());
        }
        if (showFpsRef.update()) {
            app.setDisplayFps(showFpsRef.get());
        }

        boolean updateColor = false;
        if (redRef.update()) {
            updateColor = true;
        }
        if (greenRef.update()) {
            updateColor = true;
        }
        if (blueRef.update()) {
            updateColor = true;
        }
        if (alphaRef.update()) {
            updateColor = true;
        }
        if (updateColor) {
            boxColor.set((float) (redRef.get() / 100.0),
                    (float) (greenRef.get() / 100.0),
                    (float) (blueRef.get() / 100.0),
                    (float) (alphaRef.get() / 100.0));
        }
    }
}
