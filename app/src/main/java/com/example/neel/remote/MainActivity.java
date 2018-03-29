package com.example.neel.remote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

import static com.example.neel.remote.Util.btSocket;

public class MainActivity extends AppCompatActivity {

    Button go_left,go_right,go_forward,go_backward,stop;
    SeekBar speed;
    TextView lumn;
    String address = "AA:AA:AA:AA:AA:AA";
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl

        setContentView(R.layout.activity_main);

        //call the widgtes
        go_left = (Button)findViewById(R.id.Lef);
        go_right = (Button)findViewById(R.id.rig);
        go_forward = (Button)findViewById(R.id.forw);
        go_backward = (Button)findViewById(R.id.bck);
        stop = (Button)findViewById(R.id.stp);

        new ConnectBT().execute(); //Call the class to connect
        go_forward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_forward();    //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"FORWARD");
            }
        });
        go_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_left();      //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"LEFT");
            }
        });

        go_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                go_backward();   //method to turn off
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"BACKWARD");
            }
        });
        go_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_right();      //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"RIGHT");
            }
        });
    }

    public void give_command(View view){
        Intent voice_intent = new Intent(MainActivity.this, voice.class);
        MainActivity.this.startActivity(voice_intent);

    }

    public void ford(View view)
    {
        go_forward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_forward();    //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"FORWARD");
            }
        });

    }

    public void resetConnection(View view) {

        if (btSocket != null) {
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
            Toast mytoast = new Toast(this);
            mytoast.setText("Bluetooth Disconnected");
        }


    }

    public void connect1(View view)
    {
        Intent blu_conn = new Intent(MainActivity.this, DeviceList.class);
        MainActivity.this.startActivity(blu_conn);
    }

    public void backward(View view)
    {
        go_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                go_backward();   //method to turn off
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"BACKWARD");
            }
        });
    }


    public void left(View view)
    {
        go_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_left();      //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"LEFT");
            }
        });
    }


    public void right(View view)
    {
        go_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                go_right();      //method to turn on
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"RIGHT");
            }
        });
    }


    public void stop(View view)
    {
        stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop_rb(); //close connection
                TextView txt = (TextView) findViewById(R.id.display);
                txt.setText(""+"STOPPED");
            }
        });
    }

    private void go_left()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("LEFT".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void go_right()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("RIGHT".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void go_forward()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("FORWARD".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void go_backward()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("BACKWARD".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void stop_rb()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("STOP".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}