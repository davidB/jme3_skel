/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_skel;

import java.io.IOException;

import lombok.Data;
import lombok.val;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Track;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

@Data
public class TranslationTrack implements Track {
	public final Vector3f translation;
	public final float length;
	public Vector3f p0;

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}

	@Override
	public void setTime(float time, float weight, AnimControl control, AnimChannel channel, TempVars vars) {
		val spatial = control.getSpatial();
		if (p0 == null) p0 = spatial.getLocalTranslation().clone();
		float ratio = FastMath.clamp(time / length, -1.0f, 1.0f);
		spatial.setLocalTranslation(vars.vect1.set(translation).multLocal(ratio).addLocal(p0));
	}

	@Override
	public float getLength() {
		return length;
	}

	@Override
	public Track clone() {
		return new TranslationTrack(translation, length);
	}

}
