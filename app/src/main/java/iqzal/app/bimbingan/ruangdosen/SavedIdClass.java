package iqzal.app.bimbingan.ruangdosen;

import android.app.Application;

public class SavedIdClass extends Application{

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String anId) {
        id = anId;
    }
}