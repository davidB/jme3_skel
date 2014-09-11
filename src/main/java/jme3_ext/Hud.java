/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;

import com.jme3x.jfx.AbstractHud;

@RequiredArgsConstructor
public class Hud<T> extends AbstractHud {

	public final Region region;
	public final T controller;

	@Override
	protected Region innerInit() throws Exception {
		return region;
	}

}
