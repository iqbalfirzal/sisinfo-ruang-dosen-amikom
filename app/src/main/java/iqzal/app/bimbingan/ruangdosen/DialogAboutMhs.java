package iqzal.app.bimbingan.ruangdosen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogAboutMhs extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    private DatabaseReference firebaseRef;
    CircleImageView fotoMhs;
    TextView namaMhs, nimMhs;

    private ItemClickListener mListener;

    public static DialogAboutMhs newInstance() {
        return new DialogAboutMhs();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_profilmhs, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SavedIdClass globalVariable = (SavedIdClass) getActivity().getApplicationContext();
        final String id  = globalVariable.getId();
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("mhs").child(id);
        fotoMhs = view.findViewById(R.id.fotoMhs);
        namaMhs = view.findViewById(R.id.namaMhs);
        nimMhs = view.findViewById(R.id.nimMhs);
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String urlfoto = dataSnapshot.child("ppurl").getValue().toString();
                Glide.with(getActivity().getApplicationContext()).setDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.default_user).error(R.drawable.default_user)).load(urlfoto).into(fotoMhs);
                String nama = dataSnapshot.child("fullname").getValue().toString();
                namaMhs.setText(nama);
                nimMhs.setText(id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handle (if want) // iqzal
            }
        });
        view.findViewById(R.id.mhsLogout).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override public void onClick(View view) {
        mListener.onItemClick();
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick();
    }
}