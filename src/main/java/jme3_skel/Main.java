/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import com.google.inject.Guice;
import com.google.inject.Injector;

import jme3_ext.JmeModule;
import jme3_ext.SetupHelpers;

public class Main {

	private static boolean assertionsEnabled;
	private static boolean enabled() {
		Main.assertionsEnabled = true;
		return true;
	}

	public static void main(final String[] args) {
		//-Djava.util.logging.config.file=logging.properties
		SetupHelpers.installSLF4JBridge();

		assert Main.enabled();
		if (!Main.assertionsEnabled) {
			//throw new RuntimeException("Assertions must be enabled (vm args -ea");
		}
		Injector injector = Guice.createInjector(new JmeModule(), new MainModule());

		injector.getInstance(MainApp.class);
	}
}
