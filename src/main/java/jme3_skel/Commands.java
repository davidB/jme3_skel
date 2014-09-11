/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import javax.inject.Singleton;
import javax.inject.Inject;

import jme3_ext.Command;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class Commands {
	public final Command<Boolean> exit = new Command<>("quit");
	public final Command<Float> moveZ = new Command<>("moveZ");
	public final Command<Float> moveX = new Command<>("moveX");
	public final Command<Float> rotateY = new Command<>("rotateY");
	public final Command<Float> rotateX = new Command<>("rotateX");
	public final Command<Boolean> action1 = new Command<>("action1");
	public final Command<Boolean> action2 = new Command<>("action2");
	public final Command<Boolean> action3 = new Command<>("action3");
	public final Command<Boolean> action4 = new Command<>("action4");

	public final Command<?>[] all = {moveZ, moveX, rotateX, rotateY, action1, action2, action3, action4, exit};
}
