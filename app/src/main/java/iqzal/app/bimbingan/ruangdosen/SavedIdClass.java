package iqzal.app.bimbingan.ruangdosen;

import android.app.Application;

public class SavedIdClass extends Application{

    private String id;
    private String chatMyUsername;
    private String chatWith;
    private String chatWithName;

    public String getId() {
        return id;
    }

    public void setId(String anId) {
        id = anId;
    }

    public String getChatMyUsername() {
        return chatMyUsername;
    }

    public void setChatMyUsername(String aChatMyUsername) {
        chatMyUsername = aChatMyUsername;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String aChatWith) {
        chatWith = aChatWith;
    }

    public String getChatWithName() {
        return chatWithName;
    }

    public void setChatWithName(String aChatWithName) {
        chatWithName = aChatWithName;
    }
}