package jme3_skel;

import javax.inject.Singleton;
import javax.inject.Inject;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class Controls {
	public final Control<Float> moveZ = new Control<>("moveZ");
	public final Control<Float> moveX = new Control<>("moveX");
	public final Control<Float> rotateY = new Control<>("rotateY");
	public final Control<Float> rotateX = new Control<>("rotateX");
	public final Control<Boolean> action1 = new Control<>("action1");
	public final Control<Boolean> action2 = new Control<>("action2");
	public final Control<Boolean> action3 = new Control<>("action3");
	public final Control<Boolean> action4 = new Control<>("action4");

	public final Control<?>[] all = {moveZ, moveX, rotateX, rotateY, action1, action2, action3, action4};
}
