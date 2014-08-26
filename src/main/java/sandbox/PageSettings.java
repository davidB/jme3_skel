/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package sandbox;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import lemur_ext.MigLayout;
import lemur_ext.MigLayoutDebugInfo;
import lemur_ext.ModelOptionValue;
import lemur_ext.Widgets;

import com.google.common.base.Function;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultCheckboxModel;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.BoxLayout;

/**
 *
 * @author David Bernard
 */
//TODO add effects (visual + sound) + transition
class PageSettings extends Page {

    @SuppressWarnings("unchecked")
	@Override
    Container newHud() {
        Container r = Widgets.newFullPageContainer("glass", app.getCamera(), null, 350, 150, "flowy", "[][fill,grow]", "[][][][fill,grow]");
        Container holder = newHolder();
        newSelector("Video", r).addClickCommands(new CommandShowSubPane(VideoPane.newPanel((Main)app, r), holder));
        newSelector("Audio", r).addClickCommands(new CommandShowSubPane(AudioPane.newPanel((Main)app), r));
        newSelector("Commands", r).addClickCommands(new CommandShowSubPane(newCommandsPane(), holder));
        //Button backBtn = newSelector("Back", r);
        r.addChild(holder, "newline, grow, spany");
        return r;
    }

    Button newSelector(String label, Container container) {
        return container.addChild(new Button(label), "sg selector");
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
        }
    }

    static class VideoPane {
        @SuppressWarnings("unchecked")
    	static Container newPanel(final Main app, final Panel hudPanel) {
        	final AppSettings settingsInit = new AppSettings(false);
        	settingsInit.copyFrom(app.getSettings());

        	final ModelOptionValue<DisplayMode> resolution = findDisplayModes(settingsInit);
        	final DefaultCheckboxModel fullscreen = new DefaultCheckboxModel(settingsInit.isFullscreen());
        	final DefaultCheckboxModel vsync = new DefaultCheckboxModel(settingsInit.isVSync());
        	final DefaultCheckboxModel showstats = new DefaultCheckboxModel();
        	final DefaultCheckboxModel showfps = new DefaultCheckboxModel(settingsInit.isFullscreen());
        	final ModelOptionValue<Integer> antialias = findAntialias(settingsInit);
        	final Command<Button> applyVideoSettings = new Command<Button>(){
    			@Override
    			public void execute(Button arg0) {
    				try {
    					app.setDisplayFps(showfps.isChecked());
    					app.setDisplayStatView(showstats.isChecked());
    					app.setShowSettings(true);
    			    	AppSettings settingsEdit = new AppSettings(false);
    			    	settingsEdit.copyFrom(settingsInit);
    			    	settingsEdit.setFullscreen(fullscreen.isChecked());
    			    	settingsEdit.setVSync(vsync.isChecked());
    			    	DisplayMode mode = resolution.getObject();
    			    	settingsEdit.setResolution(mode.getWidth(), mode.getHeight());
    			    	settingsEdit.setDepthBits(mode.getBitDepth());
    			    	settingsEdit.setFrequency(!vsync.isChecked() ? 0 : (mode.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN)? mode.getRefreshRate() : 60);
    			    	settingsEdit.setSamples(antialias.getObject());
    			    	app.setSettings(settingsEdit);
    			    	settingsEdit.save(settingsEdit.getTitle());
    			    	app.restart();
    			    	((Main)app).onNextReshape = new Function<Main,Boolean>(){
    						@Override
    						public Boolean apply(Main input) {
    				    		Widgets.fullCamera(hudPanel, input.getCamera());
    				    		return true;
    						}
    			    		
    			    	};
    				} catch(Exception exc) {
    					throw new RuntimeException(exc);
    				}
    			}
            };
        	
            Function<DisplayMode, String> conv = new Function<DisplayMode, String>(){
    			@Override
    			public String apply(DisplayMode input) {
    				String dp = (input.getBitDepth() > 0) ? String.valueOf(input.getBitDepth()) : "??"; 
    				String freq = (input.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN) ? String.valueOf(input.getRefreshRate()) : "??";
    				return String.format("%dx%d (%s bit) %sHz", input.getWidth(), input.getHeight(), dp, freq);
    			}
    		};

    		Function<Integer, String> convAA = new Function<Integer, String>(){
            	ResourceBundle resourceBundle = ResourceBundle.getBundle("com.jme3.app/SettingsDialog");
    			@Override
    			public String apply(Integer input) {
    				return (input == 0)? resourceBundle.getString("antialias.disabled") : input +"x";
    			}
    		};

            Container pane = new Container();
            MigLayout ml = new MigLayout("", "[40%][60%]", "[][70]");
            ml.debug = new MigLayoutDebugInfo();
            pane.setLayout(ml);
            pane.addChild(Widgets.newTitle("Video"), "grow, span, wrap");
            pane.addChild(new Label("Resolution"), "sgy label");
            pane.addChild(Widgets.newSelect(resolution, conv), "growx, wrap");
            pane.addChild(new Label("FullScreen"), "sgy label");
            pane.addChild(new Checkbox("", fullscreen), "wrap, align left").setEnabled(isFullscreenSupported());;
            pane.addChild(new Label("VSync"), "sgy label");
            //TODO add tooltips "vsync need restart of the application"
            pane.addChild(new Checkbox("", vsync), "wrap, align left");
            pane.addChild(new Label("AntiAlias"), "sgy label");
            pane.addChild(Widgets.newSelect(antialias, convAA), "growx, wrap");
            pane.addChild(new Label("Show Fps"), "sgy label");
            pane.addChild(new Checkbox("", showfps), "wrap, align left");
            pane.addChild(new Label("Show Stats"), "sgy label");
            pane.addChild(new Checkbox("", showstats), "wrap, align left");

            pane.addChild(new Button("Apply"), "span, grow, wrap").addClickCommands(applyVideoSettings);
            return pane;
        }

        static ModelOptionValue<DisplayMode> findDisplayModes(AppSettings settings0) {
        	List<DisplayMode> r = new LinkedList<DisplayMode>();
        	r.add(new DisplayMode(settings0.getWidth(), settings0.getHeight(), settings0.getDepthBits(), 60));
        	r.add(new DisplayMode(800, 600, 24, 60));
            r.add(new DisplayMode(1024, 768, 24, 60));
            r.add(new DisplayMode(1280, 720, 24, 60));
            r.add(new DisplayMode(1280, 1024, 24, 60));
//        	for(GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    		for (DisplayMode mode : device.getDisplayModes()) {
    			r.add(mode);
    		}
//        	}
    		Collections.sort(r, new Comparator<DisplayMode>(){
    			@Override
    			public int compare(DisplayMode d1, DisplayMode d2) {
    				int r = 0;
    				if (d1.getWidth() > d2.getWidth()) {
    					r = 1;
    				} else if (d1.getHeight() > d2.getHeight()) {
    					r = 1;
    				} else if (d1.getWidth() != d2.getWidth() || d1.getHeight() != d2.getHeight()) {
    					r = -1;
    				}
    				return r;
    			}
    		});
    		int current = 0;
    		for(int i = 0; i < r.size(); i++) {
    			DisplayMode mode = r.get(i);
    			if (mode.getWidth() == settings0.getWidth() && mode.getHeight() == settings0.getHeight()) {
    				current = i;
    			}
    		}
         	return new ModelOptionValue<>(r, current);
        }

        static ModelOptionValue<Integer> findAntialias(AppSettings settings0) {
        	List<Integer> r = new LinkedList<Integer>();
        	r.add(0);
            r.add(2);
            r.add(4);
            r.add(6);
            r.add(8);
            r.add(16);
            return new ModelOptionValue<>(r,  Math.min(r.indexOf(settings0.getSamples()), 0));
        }

        static boolean isFullscreenSupported() {
        	GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        	return device.isFullScreenSupported() /*&& device.isDisplayChangeSupported()*/;
    	}
    }
    static class AudioPane {
    	static Container newPanel(Main app) {
        	final AppSettings settingsInit = new AppSettings(false);
        	settingsInit.copyFrom(app.getSettings());
        	//TODO init volume with current value 
        	//TODO apply change on fly (and on every audioNode
        	

        	final DefaultRangedValueModel musicVolume = new DefaultRangedValueModel(0, 100, 50);
        	final DefaultRangedValueModel soundVolume = new DefaultRangedValueModel(0, 100, 50);

            Container pane = new Container();
            MigLayout ml = new MigLayout("", "[40%][60%]", "[][70]");
            ml.debug = new MigLayoutDebugInfo();
            pane.setLayout(ml);
            pane.addChild(Widgets.newTitle("Audio"), "grow, span, wrap");
            pane.addChild(new Label("Music"), "sgy label");
            pane.addChild(new Slider(musicVolume), "growx, wrap");
            pane.addChild(new Label("Sound"), "sgy label");
            pane.addChild(new Slider(soundVolume), "growx, wrap");
            return pane;
        }
    }
    
}
