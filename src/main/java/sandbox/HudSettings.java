package sandbox;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.val;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class HudSettings {
//	@FXML
//	public Region root;

	@FXML
	public ChoiceBox<Integer> antialiasing;

	@FXML
	public CheckBox fullscreen;

	@FXML
	public ChoiceBox<DisplayMode> resolution;

	@FXML
	public CheckBox showFps;

	@FXML
	public CheckBox showStats;

	@FXML
	public Slider volumeMaster;

	@FXML
	public Slider volumeMusic;

	@FXML
	public Slider volumeSound;

	@FXML
	public CheckBox vsync;

	@FXML
	public Button applyVideo;

	@FXML
	public void initialize() {
		antialiasing.setConverter(new StringConverter<Integer>() {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("com.jme3.app/SettingsDialog");

			@Override
			public String toString(Integer input) {
				return (input == 0)? resourceBundle.getString("antialias.disabled") : input +"x";
			}

			@Override
			public Integer fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		resolution.setConverter(new StringConverter<DisplayMode>() {
			@Override
			public String toString(DisplayMode input) {
				String dp = (input.getBitDepth() > 0) ? String.valueOf(input.getBitDepth()) : "??";
				String freq = (input.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN) ? String.valueOf(input.getRefreshRate()) : "??";
				return String.format("%dx%d (%s bit) %sHz", input.getWidth(), input.getHeight(), dp, freq);
			}

			@Override
			public DisplayMode fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		antialiasing.valueProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
		fullscreen.selectedProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
		resolution.valueProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
		showFps.selectedProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
		showStats.selectedProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
		vsync.selectedProperty().addListener((v,n,o) -> applyVideo.setDisable(false));
	}

	public void load(SimpleApplication app, String prefKey) {
		final AppSettings settingsInit = new AppSettings(false);
		settingsInit.copyFrom(app.getContext().getSettings());

		loadDisplayModes(settingsInit);
		fullscreen.setSelected(settingsInit.isFullscreen());
		vsync.setSelected(settingsInit.isVSync());
		//showStats.setSelected(settingsInit.is) = new DefaultCheckboxModel();
		//showFps.setSelected(settingsInit.isFullscreen());
		loadAntialias(settingsInit);
		applyVideo.onActionProperty().set((v) -> {
			System.out.println("apply Video");
			apply(app);
			try {
				app.getContext().getSettings().save(prefKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			applyVideo.setDisable(true);
		});
		applyVideo.setDisable(true);
	}

	void loadDisplayModes(AppSettings settings0) {
		List<DisplayMode> r = new LinkedList<DisplayMode>();
		r.add(new DisplayMode(settings0.getWidth(), settings0.getHeight(), settings0.getDepthBits(), settings0.getFrequency()));
		//r.add(new DisplayMode(800, 600, 24, 60));
		//r.add(new DisplayMode(1024, 768, 24, 60));
		//r.add(new DisplayMode(1280, 720, 24, 60));
		//r.add(new DisplayMode(1280, 1024, 24, 60));
		//    	for(GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		for (DisplayMode mode : device.getDisplayModes()) {
			r.add(mode);
		}
		//    	}
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
		resolution.itemsProperty().get().clear();
		resolution.itemsProperty().get().setAll(r);

		DisplayMode current = null;
		for(int i = 0; i < r.size(); i++) {
			DisplayMode mode = r.get(i);
			if (mode.getWidth() == settings0.getWidth() && mode.getHeight() == settings0.getHeight()) {
				current = mode;
			}
		}
		resolution.valueProperty().set(current);
	}

    void loadAntialias(AppSettings settings0) {
    	System.out.println("samplesss : "+ settings0.getSamples());
    	List<Integer> r = new LinkedList<Integer>();
    	r.add(0);
        r.add(2);
        r.add(4);
        r.add(6);
        r.add(8);
        r.add(16);
        antialiasing.itemsProperty().get().clear();
        antialiasing.itemsProperty().get().setAll(r);
        antialiasing.valueProperty().set(Math.max(settings0.getSamples(), 0));
    }

    void apply(SimpleApplication app) {
		try {
			app.setDisplayFps(showFps.isSelected());
			app.setDisplayStatView(showStats.isSelected());
			app.setShowSettings(true);
	    	AppSettings settingsEdit = new AppSettings(false);
	    	settingsEdit.copyFrom(app.getContext().getSettings());
	    	settingsEdit.setFullscreen(fullscreen.isSelected());
	    	settingsEdit.setVSync(vsync.isSelected());
	    	DisplayMode mode = resolution.getValue();
	    	settingsEdit.setResolution(mode.getWidth(), mode.getHeight());
	    	settingsEdit.setDepthBits(mode.getBitDepth());
	    	settingsEdit.setFrequency(!vsync.isSelected() ? 0 : (mode.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN)? mode.getRefreshRate() : 60);
	    	settingsEdit.setSamples(antialiasing.getValue());
	    	app.setSettings(settingsEdit);
	    	settingsEdit.save(settingsEdit.getTitle());
	    	app.restart();
//	    	((Main)app).onNextReshape = new Function<Main,Boolean>(){
//				@Override
//				public Boolean apply(Main input) {
//		    		Widgets.fullCamera(hudPanel, input.getCamera());
//		    		return true;
//				}
//
//	    	};
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
    }
}
