package jme3_skel;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

import javax.inject.Singleton;

import jme3_ext.AppSettingsLoader;
import jme3_ext.InputMapper;
import jme3_ext.InputMapperHelpers;
import jme3_ext.JmeModule;
import jme3_ext.PageManager;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;
import com.jme3x.jfx.FxPlatformExecutor;

import dagger.Module;
import dagger.Provides;

@Module(
	injects = {
		Main.class,
	},
	includes = {
		JmeModule.class,
	}
)

class MainModule {

	@Provides
	public AppSettingsLoader appSettingsLoader() {
		return new AppSettingsLoader() {
			final String prefKey = "sandbox.MyGame";

			@Override
			public AppSettings loadInto(AppSettings settings) throws Exception{
				settings.load(prefKey);
				return settings;
			}

			@Override
			public AppSettings save(AppSettings settings) throws Exception{
				settings.save(prefKey);
				return settings;
			}
		};
	}

	@Singleton
	@Provides
	public SimpleApplication simpleApplication(AppSettings appSettings) {
		//HACK
		final CountDownLatch initializedSignal = new CountDownLatch(1);
		SimpleApplication app = new SimpleApplication(){
			@Override
			public void simpleInitApp() {
				initializedSignal.countDown();
			}

			@Override
			public void destroy() {
				super.destroy();
				FxPlatformExecutor.runOnFxApplication(() -> {
					Platform.exit();
				});
			}
		};
		app.setSettings(appSettings);
		app.setShowSettings(false);
		app.setDisplayStatView(false);
		app.setDisplayFps(false);
		app.start();
		try {
			initializedSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return app;
	}

	@Singleton
	@Provides
	public AppSettings appSettings(AppSettingsLoader appSettingsLoader, ResourceBundle resources) {
		AppSettings settings = new AppSettings(true);
		try {
			settings = appSettingsLoader.loadInto(settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
		settings.setTitle(resources.getString("title"));
		settings.setUseJoysticks(true);
		//settings.setResolution(640,480);
		//	settings.setRenderer("JOGL");
		//	settings.setRenderer(AppSettings.LWJGL_OPENGL3);
		return settings;
	}

	@Singleton
	@Provides
	public PageManager pageManager(SimpleApplication app, PageWelcome pageWelcome, PageSettings pageSettings, PageInGame pageInGame) {
		AppState[] pages = new AppState[Pages.values().length];
		/*
         pages[Page.About.ordinal()] = new PageAbout(screen);
         pages[Page.InGame.ordinal()] = new PageInGame(screen);
         pages[Page.LevelSelection.ordinal()] = new PageLevelSelection(screen);
         pages[Page.Loading.ordinal()] = new PageLoading(screen);
         pages[Page.Result.ordinal()] = new PageResult(screen);
         pages[Page.Scores.ordinal()] = new PageScores(screen);
         pages[Page.Settings.ordinal()] = new PageSettings(screen);
		 */
		pages[Pages.Welcome.ordinal()] = pageWelcome;
		pages[Pages.InGame.ordinal()] = pageInGame;
		pages[Pages.Settings.ordinal()] = pageSettings;
		PageManager pageManager = new PageManager(app.getStateManager(), pages);
		return pageManager;
	}

	@Singleton
	@Provides
	public Locale locale() {
		return Locale.getDefault();
	}

	@Provides
	public ResourceBundle resources(Locale locale) {
		return ResourceBundle.getBundle("Interface.labels", locale);
	}

	@Provides
	public FXMLLoader fxmlLoader(ResourceBundle resources) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(resources);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		return fxmlLoader;
	}

	@Provides
	@Singleton
	public InputMapper inputMapper(Commands controls) {
		//TODO save / restore mapper, until then harcoded mapping
		InputMapper m = new InputMapper();
		InputMapperHelpers.mapKey(m, KeyInput.KEY_ESCAPE, controls.exit.value);
		// arrow
		InputMapperHelpers.mapKey(m, KeyInput.KEY_UP, controls.moveZ.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_DOWN, controls.moveZ.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_RIGHT, controls.moveX.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_LEFT, controls.moveX.value, false);
		// WASD / ZQSD
		InputMapperHelpers.mapKey(m, KeyInput.KEY_A, controls.moveZ.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_S, controls.moveZ.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_D, controls.moveX.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_W, controls.moveX.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_Z, controls.moveZ.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_Q, controls.moveX.value, false);
		// WASD / ZQSD
		InputMapperHelpers.mapKey(m, KeyInput.KEY_S, controls.moveZ.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_D, controls.moveX.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_W, controls.moveX.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_Z, controls.moveZ.value, true);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_Q, controls.moveX.value, false);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_1, controls.action1.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_NUMPAD1, controls.action1.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_2, controls.action2.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_NUMPAD2, controls.action2.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_3, controls.action3.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_NUMPAD3, controls.action3.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_4, controls.action4.value);
		InputMapperHelpers.mapKey(m, KeyInput.KEY_NUMPAD4, controls.action4.value);
		return m;
	}
}