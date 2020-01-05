package iqzal.app.bimbingan.ruangdosen;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();

        Firebase.setAndroidContext(this);
        String getMyId = globalVariable.getId();
        String getChatWithId = globalVariable.getChatWith();
        final String getMyUsername = new LoginPrefManager(this).getChatUserName();
        final String getChatWithName = globalVariable.getChatWithName();

        layout = (LinearLayout)findViewById(R.id.layoutChat);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollViewChat);

        reference1 = new Firebase("https://iqzal-app-bimbngan-ruang-dosen.firebaseio.com/chats/" + getMyId + "/" + getChatWithId);
        reference2 = new Firebase("https://iqzal-app-bimbngan-ruang-dosen.firebaseio.com/chats/" + getChatWithId + "/" + getMyId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("sender", getMyUsername);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.getText().clear();
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("sender").toString();

                if(userName.equals(getMyUsername)){
                    addMessageBox("Anda :\n" + message, 1);
                }
                else{
                    addMessageBox(getChatWithName + " :\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        if(type == 1) {
            textView.setBackgroundResource(R.drawable.my_message);
        }else {
            textView.setBackgroundResource(R.drawable.their_message);
        }
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

}
