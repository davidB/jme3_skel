/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BoxLayout;

import lemur_ext.MigLayout;
import lemur_ext.MigLayoutDebugInfo;
import lemur_ext.Widgets;

/**
 *
 * @author David Bernard
 */
//TODO add effects (visual + sound) + transition
class PageSettings extends Page {

    Button newSelector(String label, Container container) {
        return container.addChild(new Button(label), "sg selector");
    }

    @SuppressWarnings("unchecked")
	@Override
    Container newHud() {
        Container r = Widgets.newFullPageContainer("glass", app.getCamera(), null, 350, 150, "flowy", "[][fill,grow]", "[][][][fill,grow]");
        Container holder = newHolder();
        newSelector("Video", r).addClickCommands(new CommandShowSubPane(newVideoPane(), holder));
        newSelector("Audio", r).addClickCommands(new CommandShowSubPane(newAudioPane(), holder));
        newSelector("Commands", r).addClickCommands(new CommandShowSubPane(newCommandsPane(), holder));
        //Button backBtn = newSelector("Back", r);
        r.addChild(holder, "newline, grow, spany");
        return r;
    }

    Container newHolder() {
        Container holder = new Container();
        holder.setLayout(new BoxLayout(Axis.X, FillMode.First));
        return holder;
    }

    Label newLabel(String txt) {
        Label l = new Label(txt);
        l.setFontSize(50f);
        l.setTextHAlignment(HAlignment.Center);
        return l;
    }

    Container newVideoPane() {
        Container pane = new Container();
        MigLayout ml = new MigLayout();
        ml.debug = new MigLayoutDebugInfo();
        pane.setLayout(ml);
        //pane.setPreferredSize(new Vector3f(10,10,0));
        pane.addChild(new Label("Video"));
        return pane;
    }

    Container newAudioPane() {
        Container pane = new Container();
        pane.addChild(new Label("Audio"));
        return pane;
    }

    Container newCommandsPane() {
        Container pane = new Container();
        pane.addChild(new Label("Commands"));
        return pane;
    }

    static class CommandShowSubPane implements Command<Button> {

        final Container holder;
        final Panel content;

        CommandShowSubPane(Panel c, Container h) {
            holder = h;
            content = c;
        }

        @Override
        public void execute(Button s) {
            List<Spatial> children = holder.getChildren();
            Spatial previous = (children.size() > 0) ? children.get(0) : null;
            if (previous == content) {
                return;
            }
            if (previous != null) {
                holder.removeChild((Node) previous);
            }
            holder.addChild(content);
            System.out.println("show " + content + " .. " + holder.getSize() + ".." + content.getSize());
        }
    }
}
