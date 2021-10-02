package org.pietroscuttari.controllers;

import java.io.IOException;

import org.pietroscuttari.App;

public abstract class Controller {
    public void init() {
    }

    public void onNavigateFrom(Controller sender, Object ... parameter){
    }

    public int getrackId() {
        return App.getrackId();
    }

    public void setrackId(int id) {
        App.setrackId(id);
    }

    public boolean isFirstHomeLoad() {
        return !App.homeLoaded;
    }

    public void navigate(String view, Object...parameter) {
        try {
            App.navigate(this, view, parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
