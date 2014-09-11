/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import java.util.LinkedList;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.FloatPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.RequiredArgsConstructor;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;

@Singleton
public class AudioManager {
	public final FloatProperty masterVolume = new VolumeProperty("master");
	public final FloatProperty musicVolume = new VolumeProperty("music");
	public final FloatProperty soundVolume = new VolumeProperty("sound");
	public final ObservableList<AudioNode> musics = FXCollections.observableList(new LinkedList<AudioNode>());

	final SimpleApplication app;

	@Inject
	public AudioManager(SimpleApplication app) {
		this.app = app;
		masterVolume.addListener((v, n, o) -> {
			applyVolumes();
		});
		musicVolume.addListener((v, n, o) -> {
			applyVolumes();
		});
		soundVolume.addListener((v, n, o) -> {
			applyVolumes();
		});
		musics.addListener(new ListChangeListener<AudioNode>(){
			@Override
			public void onChanged(ListChangeListener.Change<? extends AudioNode> c) {
				applyVolumes();
			}
		});
	}

	// every AudioNode (not in musics list)  have a volume of 1.0
	// to avoid change existing node volume and to avoid new AudioNode ask "what is the volume"
	// we adjust listener volume so that sound could stay to 1.0
	public void applyVolumes() {
		float factor1 = Math.max(0.01f, soundVolume.floatValue());
		app.getListener().setVolume(masterVolume.floatValue() * factor1);
		float volume = musicVolume.floatValue() / factor1;
		app.enqueue(()-> {
			musics.forEach((m) -> {
				m.setVolume(volume);
			});
			return true;
		});
	}

	public void loadFromAppSettings() {
		loadVolume(masterVolume);
		loadVolume(musicVolume);
		loadVolume(soundVolume);
	}

	public void saveIntoAppSettings() {
		saveVolume(masterVolume);
		saveVolume(musicVolume);
		saveVolume(soundVolume);
	}

	private final void loadVolume(FloatProperty p) {
		Float v = (Float)app.getContext().getSettings().get(prefKeyOf(p));
		p.set((v == null) ? 1.0f : v.floatValue());
	}

	private final void saveVolume(FloatProperty p) {
		app.getContext().getSettings().put(prefKeyOf(p), p.get());
	}

	private final String prefKeyOf(FloatProperty p) {
		return String.format("audio_%s_volume", p.getName());
	}

	@RequiredArgsConstructor
	static class VolumeProperty extends FloatPropertyBase {
		private final String name;

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
