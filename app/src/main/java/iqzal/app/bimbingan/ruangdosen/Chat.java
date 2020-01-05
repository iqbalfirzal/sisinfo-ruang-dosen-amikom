package iqzal.app.bimbingan.ruangdosen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
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
        getSupportActionBar().setTitle(getChatWithName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0593D3")));

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
                    addMessageBox("<b>" + getMyUsername + "</b><br><hr>" + message, 1);
                }
                else{
                    addMessageBox("<b>" + getChatWithName + "</b><br><hr>" + message, 2);
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
        TextView messageText = new TextView(Chat.this);
        messageText.setText(Html.fromHtml(message)) ;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageText.setPadding(20,20,20,20);
        messageText.setTextSize(16);
        if(type == 1) {
            lp.gravity = Gravity.END;
            lp.setMargins(100,10,0,10);
            messageText.setBackgroundResource(R.drawable.my_message);
        }else {
            lp.gravity = Gravity.START;
            lp.setMargins(0,10,100,10);
            messageText.setBackgroundResource(R.drawable.their_message);
        }
        layout.addView(messageText);
        messageText.setLayoutParams(lp);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}
