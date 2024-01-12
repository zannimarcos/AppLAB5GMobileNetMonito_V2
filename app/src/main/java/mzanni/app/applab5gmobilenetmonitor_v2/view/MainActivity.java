package mzanni.app.applab5gmobilenetmonitor_v2.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import mzanni.app.applab5gmobilenetmonitor_v2.monitoramento.Controller;
import mzanni.app.applab5gmobilenetmonitor_v2.R;

public class MainActivity extends AppCompatActivity {

    EditText textEdit1;
    Button btnFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinalizar = findViewById(R.id.btnFinalizar);

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Volte semprer!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });



    }
}