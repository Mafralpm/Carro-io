package com.example.labm4.unifor_obdreaderii.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.labm4.unifor_obdreaderii.R;
import com.example.labm4.unifor_obdreaderii.Services.ObdBluetooth_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Set;

@EActivity(R.layout.activity_obd_conection)
@OptionsMenu(R.menu.menu)
public class ObdConnectionActivity extends AppCompatActivity {


    @ViewById(resName = "PariedDevicesList")
    ListView PariedDevicesList;

    @ViewById
    ListView UnpariedDevicesList;

    @ViewById
    TextView text1;

    @ViewById
    TextView text2;

    @ViewById
    ProgressBar loader;

    @ViewById
    Button procurar;

    ArrayAdapter<String> pairedDevicesAdapter;
    ArrayList<String> pairedDevicesAddress = new ArrayList<String>();

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayAdapterAdress = new ArrayList<String>();

    private static Intent bluetooth;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;

    private AlertDialog alerta;
    private View view;

    @AfterViews
    public void afterViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Conexão </font>"));
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        pareados();

    }

    public void pareados() {

        pairedDevicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() >= 1) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                pairedDevicesAddress.add(device.getAddress());
            }
        } else {
            PariedDevicesList.setVisibility(View.GONE);
            text1.setVisibility(View.VISIBLE);
        }

        PariedDevicesList.setAdapter(pairedDevicesAdapter);

        PariedDevicesList.setOnItemClickListener(list1);
    }

    @Click
    void procurar() {
        procurar.setClickable(false);

        doDiscovery();

        text2.setVisibility(View.GONE);
        UnpariedDevicesList.setVisibility(View.VISIBLE);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        UnpariedDevicesList.setAdapter(arrayAdapter);
        UnpariedDevicesList.setOnItemClickListener(list2);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    private void doDiscovery() {
        loader.setVisibility(View.VISIBLE);

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    arrayAdapterAdress.add(device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                loader.setVisibility(View.GONE);
                procurar.setClickable(true);
                if (arrayAdapter.getCount() == 0) {
                    UnpariedDevicesList.setVisibility(View.GONE);
                    text2.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private AdapterView.OnItemClickListener list1
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), ObdBluetooth_.class);
            intent.putExtra("deviceAdress", pairedDevicesAddress.get(position));
            bluetooth = intent;
            startService(intent);

            acabou();
            finish();
        }
    };

    private AdapterView.OnItemClickListener list2
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), ObdBluetooth_.class);
            intent.putExtra("deviceAdress", arrayAdapterAdress.get(position));
            bluetooth = intent;
            startService(intent);

            acabou();
            finish();
        }
    };

    public static Intent getService(){
        return bluetooth;
    }

    public void acabou(){
        mBluetoothAdapter.cancelDiscovery();
    }

    @OptionsItem
    public void unifor(){
        LayoutInflater li = getLayoutInflater();
        view = li.inflate(R.layout.sobre, null);

        Button fechar = (Button) view.findViewById(R.id.button);

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setInverseBackgroundForced(true);
        alerta = builder.create();
        alerta.show();
    }

}
