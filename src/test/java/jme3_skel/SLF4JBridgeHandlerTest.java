package jme3_skel;

import jme3_ext.SetupHelpers;

import org.junit.Test;

public class SLF4JBridgeHandlerTest {

	@Test
	public void testInstall() {
		SetupHelpers.installSLF4JBridge();
		SetupHelpers.testJul();
		SetupHelpers.uninstallSLF4JBridge();
	}
}
