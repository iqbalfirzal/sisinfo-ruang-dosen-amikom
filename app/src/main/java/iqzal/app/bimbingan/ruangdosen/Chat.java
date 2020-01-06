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
import androidx.core.content.ContextCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    private DateFormat df = new SimpleDateFormat("d/M/yyyy, HH:mm");
    private final String waktu = df.format(Calendar.getInstance().getTime());

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
        getWindow().setBackgroundDrawableResource(R.drawable.chat_bg) ;

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
                    map.put("time", waktu);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.getText().clear();
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("sender").toString();
                String time = map.get("time").toString();

                if(userName.equals(getMyUsername)){
                    addMessageBox("<b>" + getMyUsername +
                            "</b><br><hr><font>" + message +
                            "</font><br>","<i>" + time + "<i>", 1);
                }
                else{
                    addMessageBox("<b>" + getMyUsername +
                            "</b><br><hr><font>" + message +
                            "</font><br>","<i>" + time + "<i><br>", 1);
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

    public void addMessageBox(String message, String time, int type){
        TextView messageText = new TextView(Chat.this);
        TextView timeText = new TextView(Chat.this);
        messageText.setText(Html.fromHtml(message)) ;
        timeText.setText(Html.fromHtml(time)) ;
        LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageText.setPadding(20,20,20,0);
        timeText.setTextColor(ContextCompat.getColor(this, R.color.white));
        messageText.setTextSize(18);
        timeText.setTextSize(14);
        messageText.setTextIsSelectable(true);
        timeText.setTextIsSelectable(true);
        if(type == 1) {
            lpm.gravity = Gravity.END;
            lpt.gravity = Gravity.END;
            lpm.setMargins(100,10,0,0);
            lpt.setMargins(100,5,0,25);
            messageText.setBackgroundResource(R.drawable.my_message);
        }else {
            lpm.gravity = Gravity.START;
            lpt.gravity = Gravity.START;
            lpm.setMargins(0,10,100,0);
            lpt.setMargins(0,5,100,25);
            messageText.setBackgroundResource(R.drawable.their_message);
        }
        layout.addView(messageText);
        layout.addView(timeText);
        messageText.setLayoutParams(lpm);
        timeText.setLayoutParams(lpt);
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
