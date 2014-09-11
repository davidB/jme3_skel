/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
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
		try {
			this.app = (SimpleApplication)app;
			log.trace("doInitialize(): {}", this);
			doInitialize();
			initialized = true;
			super.setEnabled(false);
			setEnabled(true);
		} catch(Exception exc) {
			log.warn("failed to initialize(..,..)", exc);
			throw exc;
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
				log.trace("doEnable(): {}", this);
				doEnable();
			} else {
				log.trace("doDisable(): {}", this);
				doDisable();
			}
		} catch(Exception exc) {
			log.warn("failed to setEnabled(" + enabled + ")", exc);
			throw exc;
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
				log.trace("doDispose(): {}", this);
				doDispose();
			}
		}catch (Exception exc) {
			log.warn("failed to doDispose()", exc);
			throw exc;
		}
		initialized = false;
	}

}
