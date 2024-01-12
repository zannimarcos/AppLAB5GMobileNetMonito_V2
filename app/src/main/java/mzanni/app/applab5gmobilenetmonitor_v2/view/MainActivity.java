package mzanni.app.applab5gmobilenetmonitor_v2.view;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import mzanni.app.applab5gmobilenetmonitor_v2.monitoramento.Controller;
import mzanni.app.applab5gmobilenetmonitor_v2.R;

public class MainActivity<Network> extends AppCompatActivity {

    EditText textEdit1;
    Button btnFinalizar;
    Button btnIniciar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnIniciar = findViewById(R.id.btnIniciar);



        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Volte semprer!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);

                Network currentNetwork = (Network) connectivityManager.getActiveNetwork();

                NetworkCapabilities caps = connectivityManager.getNetworkCapabilities((android.net.Network) currentNetwork);
                LinkProperties linkProperties = connectivityManager.getLinkProperties((android.net.Network) currentNetwork);

                NetworkInfo info = connectivityManager.getActiveNetworkInfo();

                Log.i("POOAndroid", "Dados :" + info);




            }
        });




        }
}