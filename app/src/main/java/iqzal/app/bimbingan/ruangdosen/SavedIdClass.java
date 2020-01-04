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

    public void setChatMyUsername(String anChatMyUsername) {
        chatMyUsername = anChatMyUsername;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String anChatWith) {
        chatWith = anChatWith;
    }

    public String getChatWithName() {
        return chatWithName;
    }

    public void setChatWithName(String anChatWithName) {
        chatWithName = anChatWithName;
    }
}