package jme3_ext;

import com.jme3.system.AppSettings;

public interface AppSettingsLoader {

	AppSettings loadInto(AppSettings settings) throws Exception;
	AppSettings save(AppSettings settings) throws Exception;
}
