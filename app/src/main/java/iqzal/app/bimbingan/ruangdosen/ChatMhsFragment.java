package iqzal.app.bimbingan.ruangdosen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChatMhsFragment extends Fragment {
    DatabaseReference firebaseRef;
    ListView chatList;
    TextView noChatText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> newList = new ArrayList<>();
    int totalChats = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_mhs, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        final SavedIdClass globalVariable = (SavedIdClass) getActivity().getApplicationContext();
        final String getMyId = globalVariable.getId();

        chatList = (ListView) view.findViewById(R.id.chatList);
        noChatText = (TextView) view.findViewById(R.id.noChatText);

        String url = "https://iqzal-app-bimbngan-ruang-dosen.firebaseio.com/chats/" + getMyId +".json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getChatWithName = newList.get(position);
                globalVariable.setChatWithName(getChatWithName);
                firebaseRef.child("dosen").orderByChild("fullname").equalTo(getChatWithName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            String getParent = childSnapshot.getKey();
                            globalVariable.setChatWith(getParent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast notice = Toast.makeText(getActivity(), "Koneksi internet terputus.", Toast.LENGTH_LONG);
                        notice.show();
                    }
                });
                startActivity(new Intent(getActivity(), Chat.class));
            }
        });
    }

    public void doOnSuccess(String s){
        final SavedIdClass globalVariable = (SavedIdClass) getActivity().getApplicationContext();
        String getMyId = globalVariable.getId();
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(getMyId)) {
                    al.add(key);
                }
                totalChats++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalChats < 1){
            noChatText.setVisibility(View.VISIBLE);
            chatList.setVisibility(View.GONE);
        }
        else{
            nextProccess(al);
        }
    }

    private void nextProccess(ArrayList<String> id){
        final Object[] listStringArray = id.toArray();
        for(int i = 0; i<listStringArray.length; i++){
                firebaseRef.child("dosen").child((String) listStringArray[i]).child("fullname").orderByChild("fullname").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String dataList = dataSnapshot.getValue(String.class);
                        newList.add(dataList);
                        chatList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, newList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        }
        noChatText.setVisibility(View.GONE);
        chatList.setVisibility(View.VISIBLE);
    }
}
