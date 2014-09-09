package jme3_skel;

import java.io.IOException;

import lombok.Data;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Track;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.util.TempVars;

@Data
public class RemoveTrack implements Track {

	/** delay before removeFromParent */
	public final float at;

	@Override
	public void write(JmeExporter ex) throws IOException {
	}

	@Override
	public void read(JmeImporter im) throws IOException {
	}

	@Override
	public void setTime(float time, float weight, AnimControl control, AnimChannel channel, TempVars vars) {
		if (time >= at) {
			control.getSpatial().removeFromParent();
		}
	}

	@Override
	public float getLength() {
		return at;
	}

	@Override
	public Track clone() {
		return new RemoveTrack(at);
	}

}
