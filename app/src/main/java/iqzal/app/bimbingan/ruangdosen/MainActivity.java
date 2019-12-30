package iqzal.app.bimbingan.ruangdosen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener{
    private Query firebaseRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    CircleImageView userIconFab;
    ArrayList<ListDosen> list;
    RecyclerView recyclerView;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("dosen").orderByChild("fullname");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userIconFab = findViewById(R.id.iconFab);
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRef != null){
            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(ListDosen.class));
                        }
                        ListDosenAdapter listDosenAdapter = new ListDosenAdapter(list);
                        recyclerView.setAdapter(listDosenAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,databaseError.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    public void openMhsProfile(View view) {
        ActionBottomDialogFragment addPhotoBottomDialogFragment = ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), ActionBottomDialogFragment.TAG);
    }

    @Override public void onItemClick() {
        auth.signOut();
        Intent myIntent = new Intent(MainActivity.this,
                Choose.class);
        startActivity(myIntent);
        finish();
    }

    private void search(String str){
        ArrayList<ListDosen> myList = new ArrayList<>();
        for(ListDosen object : list){
            if(object.getFullname().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        ListDosenAdapter listDosenAdapter = new ListDosenAdapter(myList);
        recyclerView.setAdapter(listDosenAdapter);
    }

}
