package sandbox;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.FloatPropertyBase;

import javax.inject.Inject;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;

public class AudioManager {

	static FloatProperty newVolume(String name) {
		return new FloatPropertyBase(){
			@Override
			public Object getBean() {
				return null;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public void set(float newValue) {
				super.set(Math.min(1.0f, Math.max(0.0f, newValue)));
			}
		};
	}

	final List<AudioNode> musics = new LinkedList<>();
	final SimpleApplication app;

	public final FloatProperty master = newVolume("master");
	public final FloatProperty music = newVolume("music");
	public final FloatProperty sound = newVolume("sound");

	@Inject
	public AudioManager(SimpleApplication app) {
		this.app = app;
		master.addListener((v, n, o) -> {
			applyVolumes();
		});
		music.addListener((v, n, o) -> {
			applyVolumes();
		});
		sound.addListener((v, n, o) -> {
			applyVolumes();
		});

	}

	// every AudioNode (not in musics list)  have a volume of 1.0
	// to avoid change existing node volume and to avoid new AudioNode ask "what is the volume"
	// we adjust listener volume so that sound could stay to 1.0
	public void applyVolumes() {
		app.getListener().setVolume(master.floatValue() * sound.floatValue());
		float volume = music.floatValue() / sound.floatValue();
		app.enqueue(()-> {
			musics.forEach((m) -> {
				m.setVolume(volume);
			});
			return true;
		});
	}

	public void loadFromAppSettings() {
		System.out.println("load audio");
		loadVolume(master);
		loadVolume(music);
		loadVolume(sound);
	}

	public void saveIntoAppSettings() {
		System.out.println("save audio");
		saveVolume(master);
		saveVolume(music);
		saveVolume(sound);
	}

	void loadVolume(FloatProperty p) {
		Float v = (Float)app.getContext().getSettings().get("audio_"+ p.getName() +"_volume");
		System.out.println("load volume of " + p.getName() + " : " + v);
		p.set((v == null) ? 1.0f : v.floatValue());
	}

	void saveVolume(FloatProperty p) {
		System.out.println("save volume of " + p.getName() + " : " + p.get());
		app.getContext().getSettings().put("audio_"+ p.getName() +"_volume", p.get());
	}
}
