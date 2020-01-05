package iqzal.app.bimbingan.ruangdosen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListDosenFragment extends Fragment{

    private Query firebaseRef;
    CircleImageView userIconFab;
    ArrayList<ListDosen> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listdosen_mhs, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("dosen").orderByChild("fullname");
        userIconFab = (CircleImageView) view.findViewById(R.id.iconFab);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        userIconFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

    }

    @Override
    public void onStart() {
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
                        ListDosenAdapter listDosenAdapter = new ListDosenAdapter(list, getActivity());
                        recyclerView.setAdapter(listDosenAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(),databaseError.getMessage(), Toast.LENGTH_LONG);
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

    private void openProfile(){
        DialogAboutMhs addphotoBottomDialogFragment = DialogAboutMhs.newInstance();
        addphotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(), DialogAboutMhs.TAG);
    }

    private void search(String str){
        ArrayList<ListDosen> myList = new ArrayList<>();
        for(ListDosen object : list){
            if(object.getFullname().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        ListDosenAdapter listDosenAdapter = new ListDosenAdapter(myList, getActivity());
        recyclerView.setAdapter(listDosenAdapter);
    }

}
