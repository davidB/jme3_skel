package jme3_ext;

import lombok.extern.slf4j.Slf4j;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 *
 * @author david.bernard
 */
@Slf4j
abstract public class AppState0 extends AbstractAppState {
	protected SimpleApplication app;

	@Override
	public final void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication)app;
		initialized = true;
		doInitialize();
		if( isEnabled() ) {
			doEnable();
		}
	}

	@Override
	public final void setEnabled( boolean enabled ) {
		if( isEnabled() == enabled )
			return;
		super.setEnabled(enabled);
		if( !isInitialized() )
			return;
		try {
			if( enabled ) {
				log.trace("enable():" + this);
				doEnable();
			} else {
				log.trace("disable():" + this);
				doDisable();
			}
		} catch(Exception exc) {
			log.warn("failed to setEnabled(" + enabled + ")", exc);
		}
	}

	@Override
	public final void update(float tpf) {
		if (isEnabled()){
			doUpdate(tpf);
		}
	};

	protected void doInitialize(){}
	protected void doEnable(){};
	protected void doUpdate(float tpf) {}
	protected void doDisable(){};
	protected void doDispose(){};

	@Override
	public final void cleanup() {
		setEnabled(false);
		try {
			if (isInitialized()) {
				doDispose();
			}
		}catch (Exception exc) {
			log.warn("failed to dispose", exc);
		}
		initialized = false;
	}

}
