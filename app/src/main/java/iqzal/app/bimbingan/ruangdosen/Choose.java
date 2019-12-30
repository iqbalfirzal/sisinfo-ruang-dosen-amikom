package iqzal.app.bimbingan.ruangdosen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choose extends AppCompatActivity {
    Button btChooseDosen,btChooseMhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        btChooseDosen = findViewById(R.id.choosedosen);
        btChooseMhs = findViewById(R.id.choosemhs);

        btChooseDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Choose.this,
                        LoginDosen.class);
                startActivity(myIntent);
                finish();
            }
        });

        btChooseMhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntenttwo = new Intent(Choose.this,
                        LoginMhs.class);
                startActivity(myIntenttwo);
                finish();
            }
        });
    }
}
