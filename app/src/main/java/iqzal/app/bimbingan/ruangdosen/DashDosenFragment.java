package iqzal.app.bimbingan.ruangdosen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashDosenFragment extends Fragment {

    private DatabaseReference firebaseRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    LinearLayout posisiDosenLayout, ruanganDosenLayout;
    CircleImageView fotoDosen;
    ImageButton btLogout;
    Button syncButton;
    TextView namaDosen, emailDosen, diRuanngan, ruanganDosen, lastUpdate, idDosen;
    EditText catatanDosen;

    private DateFormat df = new SimpleDateFormat("d MMMM yyyy, HH:mm");
    private final String waktu = df.format(Calendar.getInstance().getTime());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_dosen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final SavedIdClass globalVariable = (SavedIdClass) getActivity().getApplicationContext();
        final String id  = globalVariable.getId();

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("dosen").child(id);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        posisiDosenLayout = (LinearLayout) view.findViewById(R.id.posisiDosen);
        ruanganDosenLayout = (LinearLayout) view.findViewById(R.id.ruangDosen);
        btLogout = (ImageButton) view.findViewById(R.id.btLogOut);
        fotoDosen = (CircleImageView) view.findViewById(R.id.picDosen);
        namaDosen = (TextView) view.findViewById(R.id.namaDosen);
        emailDosen = (TextView) view.findViewById(R.id.emailDosen);
        diRuanngan = (TextView) view.findViewById(R.id.diRuang);
        ruanganDosen = (TextView) view.findViewById(R.id.ruanganDosen);
        lastUpdate = (TextView) view.findViewById(R.id.lastUpdate);
        idDosen = (TextView) view.findViewById(R.id.idDosen);
        catatanDosen = (EditText) view.findViewById(R.id.catatanDosen);
        syncButton = (Button) view.findViewById(R.id.btSync);

        emailDosen.setText(user.getEmail());
        idDosen.setText(id);

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ppurl = dataSnapshot.child("ppurl").getValue().toString();
                Glide.with(getActivity().getApplicationContext()).setDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.default_user).error(R.drawable.default_user)).load(ppurl).into(fotoDosen);
                String nama = dataSnapshot.child("fullname").getValue().toString();
                namaDosen.setText(nama);
                String ruang = dataSnapshot.child("ruang").getValue().toString();
                ruanganDosen.setText(ruang);
                String catatan = dataSnapshot.child("catatan").getValue().toString();
                catatanDosen.setText(catatan);
                String waktuterakhir = dataSnapshot.child("waktu").getValue().toString();
                lastUpdate.setText("Update Terakhir : "+waktuterakhir);
                String diruang = dataSnapshot.child("diruangan").getValue().toString();
                diRuanngan.setText(diruang);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast notice = Toast.makeText(getActivity(), "Gagal memuat data.", Toast.LENGTH_LONG);
                notice.show();
            }
        });

        posisiDosenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });
        ruanganDosenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRuang();
            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRef.child("catatan").setValue(catatanDosen.getText().toString());
                firebaseRef.child("diruangan").setValue(diRuanngan.getText().toString());
                firebaseRef.child("ruang").setValue(ruanganDosen.getText().toString());
                firebaseRef.child("waktu").setValue(waktu);
                Toast notice = Toast.makeText(getActivity(), "Data berhasil diupdate.", Toast.LENGTH_LONG);
                notice.show();
            }
        });
    }

    public void changeStatus() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        final String[] items = {"IYA", "TIDAK"};
        builder.setTitle("Apakah Anda sedang berada di ruangan?")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseRef.child("diruangan").setValue(items[which]);
                        firebaseRef.child("waktu").setValue(waktu);
                    }
                });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void changeRuang() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Ruangan Dosen");
        final View customLayout = getLayoutInflater().inflate(R.layout.changeruang_layout, null);
        builder.setView(customLayout);
        EditText editText = customLayout.findViewById(R.id.txtChangeRuang);
        editText.setText(ruanganDosen.getText());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.txtChangeRuang);
                String text = editText.getText().toString();
                String time = waktu;
                sendChangeRuangEditedDataToActivity(text, time);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void sendChangeRuangEditedDataToActivity(String text, String time) {
        firebaseRef.child("ruang").setValue(text);
        firebaseRef.child("waktu").setValue(time);
    }

    private void confirmLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Log Out");
        builder.setMessage("Yakin ingin keluar?");
        builder.setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearPrefData();
                auth.signOut();
                Intent myIntent = new Intent(getActivity(), Choose.class);
                startActivity(myIntent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void clearPrefData(){
        new LoginPrefManager(this.getActivity().getApplicationContext()).clearData();
    }

}
