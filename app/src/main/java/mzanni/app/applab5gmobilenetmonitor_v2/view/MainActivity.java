package mzanni.app.applab5gmobilenetmonitor_v2.view;

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
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import mzanni.app.applab5gmobilenetmonitor_v2.R;

public class MainActivity<Network> extends AppCompatActivity {

    EditText textEdit1;
    Button btnFinalizar;
    Button btnIniciar;
    Button btnParar;
    TextInputEditText textInput1;

    EditText editTextMultiLine1;


    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TelephonyManager telephonyManager;

    private boolean isProcedureRunning = false;

    public ArrayList<String> registros = new ArrayList<String>();
    String currentDateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnParar = findViewById(R.id.btnParar);
        textInput1 = findViewById(R.id.textInput1);
        editTextMultiLine1 = findViewById(R.id.editTextMultiLine1);

        final double[] latitude = new double[1];
        final double[] longitude = new double[1];


        //allow all threading policies
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        } else {
            Log.i("LAB5G@Monitor", "Permissão p localização");
        }

        // Check if permission to write to external storage is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_STORAGE);
        } else {
            Log.i("LAB5G@Monitor", "Permissão p gravação de arquivo");
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

            }


        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

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
                Log.i("LAB5G@Monitor", "Pressionado Parar");
                stopProcedure();
            }
        });
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LAB5G@Monitor", "Pressionado Iniciar");
                startProcedure();
            }
        });
    }


        private void startProcedure () {

            final double[] latitude = new double[1];
            final double[] longitude = new double[1];
            isProcedureRunning = true;
            Log.i("LAB5G@Monitor", "Procedimento iniciado.");

            // Simulando um procedimento que é repetido até que o botão de parar seja clicado.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isProcedureRunning) {
                        // Execute o procedimento aqui.
                        // Por exemplo, exiba uma mensagem a cada segundo.

                        final int[] cincoG = new int[1];
                        //editTextMultiLine1.setText("");

                        //Coletar Data e hora
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        currentDateAndTime = dateFormat.format(new Date());

                        Log.i("LAB5G@Monitor", currentDateAndTime);


                        //Coletar status da rede do celular

                        //Coletar status 5G

                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


                        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                            // Verificar o tipo de tecnologia disponível (2G/3G/4G/5G)
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                                // Liberando acesso a interface do telefone

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        PERMISSION_REQUEST_CODE);
                                return;
                            }

                            int networkType = telephonyManager.getNetworkType();
                            //cincoG[0] = networkType;
                            if (networkType == TelephonyManager.NETWORK_TYPE_NR) {  // 5G
                                // A conexão é 5G
                                Log.d("LAB5G@Monitor", "Conexão 5G");
                                cincoG[0] = 5;
                            } else {// Outro tipo de conexão
                                Log.d("LAB5G@Monitor", "Não é uma conexão 5G");
                                cincoG[0] = 0;
                            }
                        } else {
                            // Dispositivo não suportado ou não é uma rede GSM
                            Log.d("LAB5G@Monitor", "Dispositivo não suportado");
                        }


                        //Coletar status rede do celular
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

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                // Handle location provider status changes

                                Log.i("LAB5G@Monitor", "StatusChanged");
                                Log.i("LAB5G@Monitor", "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0]);
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                // Handle when the location provider is enabled
                                Log.i("LAB5G@Monitor", "ProviderEnabled");
                                Log.i("LAB5G@Monitor", "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0]);
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                // Handle when the location provider is disabled
                                Log.i("LAB5G@Monitor", "ProviderDisabled");
                                Log.i("LAB5G@Monitor", "Coordenadas Geográficas :" + latitude[0] + " " + longitude[0]);
                            }
                        };

                        //Efetuar PING

                        String destino = textInput1.getText().toString();
                        String ipAddress = destino;

                        String pingResult = "TESTANDO PING";


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



                        String dadoRetorno = "Destino: " + ipAddress + "|" + currentDateAndTime + "|" + "Coordenadas Geográficas : @" + latitude[0] + "," + longitude[0] + "|" + pingResult + "|" + cincoG[0] + "|" + info.getTypeName() + "_" + info.getSubtypeName();
                        editTextMultiLine1.setText(dadoRetorno);
                        Log.i("LAB5G@DADOS", dadoRetorno);

                        registros.add(dadoRetorno);
                        for(String item:registros){
                            Log.i("LAB5G@DADOS_registros", item);

                        }


                        try {
                            Thread.sleep(5000); // Aguarde 5 segundos antes de repetir o procedimento.
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }


        private void stopProcedure () {
            isProcedureRunning = false;
            Log.i("LAB5G@Monitor", "Parando procedimento");

            saveArrayToFile();

        }

    private void saveArrayToFile() {
        try {
            // Create a new file in the Downloads directory
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String stringWithoutSpaces = currentDateAndTime.replaceAll("\\s", "").replace("-", "").replace(":","");


            String nomeArquivo = "LAB5GMonitor_" + stringWithoutSpaces;
            File file = new File(dir, nomeArquivo +".txt");

            // Write the array elements to the file
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for (String item : registros) {
                osw.write(item + "\n");
            }
            osw.close();
            fos.close();

            Log.d("LAB5G@Monitor", "Dados salvos no arquivo: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        registros.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the array to a text file
                saveArrayToFile();
            } else {
                // Permission denied
                Log.d("LAB5G@Monitor", "Write permission denied, unable to save array to file.");
            }
        }
    }

}










