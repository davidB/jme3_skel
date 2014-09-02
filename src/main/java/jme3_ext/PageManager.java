/// License [CC0](http://creativecommons.org/publicdomain/zero/1.0/)
package jme3_ext;

import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;

public class PageManager {

    public static final String prefixGoto = "GOTOPAGE_";
    private final AppStateManager stateManager;
    private final AppState[] pages;
    private int current = -1;

    public PageManager(AppStateManager stateManager, AppState[] pages) {
        this.stateManager = stateManager;
        this.pages = pages;
    }

    public void goTo(int p) {
        if (p == current) {
            return;
        }
        hide(current);
        current = p;
        show(current);
    }

    public void show(int p) {
        if (p < 0 || p >= pages.length) {
            return;
        }
        stateManager.attach(pages[p]);
    }

    public void hide(int p) {
        if (p < 0 || p >= pages.length) {
            return;
        }
        stateManager.detach(pages[p]);
    }

    public void registerAction(InputManager inputManager) {
        ActionListener a = new ActionListener() {
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed && name.startsWith(prefixGoto)) {
                    int page = Integer.parseInt(name.substring(prefixGoto.length()));
                    goTo(page);
                };
            }
        };
        for (int i = 0; i < pages.length; i++) {
            inputManager.addListener(a, prefixGoto + i);
        }
    }
}
