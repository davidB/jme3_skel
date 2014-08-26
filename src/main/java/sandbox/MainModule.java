package sandbox;

import java.util.concurrent.CountDownLatch;
import java.util.prefs.BackingStoreException;

import gui_utils.PageManager;

import javax.inject.Named;
import javax.inject.Singleton;

import jme3_ext.JmeModule;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.system.AppSettings;

import dagger.Module;
import dagger.Provides;

@Module(
	injects = {
		SimpleApplication.class,
		PageManager.class,
	},
	includes = {
		JmeModule.class,
	}
)

class MainModule {
	@Named("prefKey")
	@Provides
	public String prefKey() {
		return "sandbox.MyGame";
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
	public AppSettings appSettings(@Named("prefKey") String prefKey) {
		AppSettings settings = new AppSettings(true);
		try {
			settings.load(prefKey);
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//settings.setResolution(640,480);
		//	settings.setRenderer("JOGL");
		//	settings.setRenderer(AppSettings.LWJGL_OPENGL3);
		return settings;
	}

	@Singleton
	@Provides
	public PageManager pageManager(SimpleApplication app, PageWelcome pageWelcome, PageSettings pageSettings) {
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
		pages[Pages.Settings.ordinal()] = pageSettings;
		PageManager pageManager = new PageManager(app.getStateManager(), pages);
		return pageManager;
	}
}