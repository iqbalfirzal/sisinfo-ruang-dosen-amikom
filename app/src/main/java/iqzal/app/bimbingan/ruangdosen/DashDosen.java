package iqzal.app.bimbingan.ruangdosen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashDosen extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_dosen);

        final SavedIdClass globalVariable = (SavedIdClass) getApplicationContext();
        final String id  = globalVariable.getId();

        firebaseRef = FirebaseDatabase.getInstance().getReference().child("dosen").child(id);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        posisiDosenLayout = findViewById(R.id.posisiDosen);
        ruanganDosenLayout = findViewById(R.id.ruangDosen);
        btLogout = findViewById(R.id.btLogOut);
        fotoDosen = findViewById(R.id.picDosen);
        namaDosen = findViewById(R.id.namaDosen);
        emailDosen = findViewById(R.id.emailDosen);
        diRuanngan = findViewById(R.id.diRuang);
        ruanganDosen = findViewById(R.id.ruanganDosen);
        lastUpdate = findViewById(R.id.lastUpdate);
        idDosen = findViewById(R.id.idDosen);
        catatanDosen = findViewById(R.id.catatanDosen);
        syncButton = findViewById(R.id.btSync);

        emailDosen.setText(user.getEmail());
        idDosen.setText(id);

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ppurl = dataSnapshot.child("ppurl").getValue().toString();
                Glide.with(getApplicationContext()).setDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.default_user).error(R.drawable.default_user)).load(ppurl).into(fotoDosen);
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
                Toast notice = Toast.makeText(DashDosen.this, "Gagal memuat data.", Toast.LENGTH_LONG);
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
                Toast notice = Toast.makeText(DashDosen.this, "Data berhasil diupdate.", Toast.LENGTH_LONG);
                notice.show();
            }
        });

    }

    public void changeStatus() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out");
        builder.setMessage("Yakin ingin keluar?");
        builder.setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signOut();
                Intent myIntent = new Intent(DashDosen.this,
                        Choose.class);
                startActivity(myIntent);
                finish();
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

}