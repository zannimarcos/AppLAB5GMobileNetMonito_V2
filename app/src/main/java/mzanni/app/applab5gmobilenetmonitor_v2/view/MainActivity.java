package mzanni.app.applab5gmobilenetmonitor_v2.view;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import mzanni.app.applab5gmobilenetmonitor_v2.monitoramento.Controller;
import mzanni.app.applab5gmobilenetmonitor_v2.R;

public class MainActivity<Network> extends AppCompatActivity {

    EditText textEdit1;
    Button btnFinalizar;
    Button btnIniciar;
    Button btnParar;
    TextInputEditText textInput1;




    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnParar= findViewById(R.id.btnParar);
        textInput1 = findViewById(R.id.textInput1);

        final double[] latitude = new double[1];
        final double[] longitude = new double[1];

        //allow all threading policies
        if(Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            Log.i("LAB5G@Monitor", "Foi p else!!");
        }

        //Inicializa GPS

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Handle the new location
                latitude[0] = location.getLatitude();
                longitude[0] = location.getLongitude();

                Log.i("LAB5G@Monitor", "LocationChanged");
                Log.i("LAB5G@Monitor", "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0]);
                Toast.makeText(MainActivity.this, "Latitude: " + latitude[0] + "\nLongitude: " + longitude[0], Toast.LENGTH_LONG).show();

            }


        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,1, locationListener);

        //Leitura dos botões



        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Volte semprer!!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LAB5G@Monitor", "Pressionado PARAR");

            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Coletar Data e hora
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateAndTime = dateFormat.format(new Date());

                Log.i("LAB5G@Monitor", currentDateAndTime);



                //Coletar status da rede do celular
                ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);

                Network currentNetwork = (Network) connectivityManager.getActiveNetwork();

                NetworkCapabilities caps = connectivityManager.getNetworkCapabilities((android.net.Network) currentNetwork);
                LinkProperties linkProperties = connectivityManager.getLinkProperties((android.net.Network) currentNetwork);

                NetworkInfo info = connectivityManager.getActiveNetworkInfo();

                Log.i("LAB5G@Monitor", "StatusRede :" + info);


                //Coletar coodenada geografica
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        // Handle the new location
                        latitude[0] = location.getLatitude();
                        longitude[0] = location.getLongitude();

                        Log.i("LAB5G@Monitor", "LocationChanged");
                        Log.i("LAB5G@Monitor", "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0]);
                        Toast.makeText(MainActivity.this, "Latitude: " + latitude[0] + "\nLongitude: " + longitude[0], Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // Handle location provider status changes

                        Log.i("LAB5G@Monitor", "StatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // Handle when the location provider is enabled
                        Log.i("LAB5G@Monitor", "ProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // Handle when the location provider is disabled
                        Log.i("LAB5G@Monitor", "ProviderDisabled");
                    }
                };

                //Efetuar PING

                String destino = textInput1.getText().toString();
                String ipAddress = destino;

                String pingResult = "TESTANDO";


                try {
                    InetAddress inetAddress = InetAddress.getByName(ipAddress);
                    Log.i("LAB5G@Monitor", "Pingando: " + ipAddress);

                    System.out.println("Sending Ping Request to " + ipAddress);
                    if (inetAddress.isReachable(2000)) {
                        System.out.println(ipAddress + " is reachable");
                        Log.i("LAB5G@Monitor", "Ping abaixo de 2000ms");
                        pingResult = "PING OK";
                    } else {
                        System.out.println(ipAddress + " is not reachable");
                        Log.i("LAB5G@Monitor", "Falhou o ping!!!");
                        pingResult = "INALCANCAVEL";
                    }
                } catch (IOException ex) {
                    System.out.println("Ping Failed");
                    ex.printStackTrace();
                }

                //Montando retorno


                Log.i("LAB5G@DADOS", "Destino: "+ ipAddress + ";" + currentDateAndTime + ";" + "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0] +";" +  pingResult + ";" + info);
            }


            });


                };
            }






