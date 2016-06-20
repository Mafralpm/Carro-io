package com.example.labm4.unifor_obdreaderii.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labm4.unifor_obdreaderii.Controller.OBDReadingsController;
import com.example.labm4.unifor_obdreaderii.Models.OBDReading_Sigleton;
import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.example.labm4.unifor_obdreaderii.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;


@EActivity(R.layout.activity_obd_infos)
@OptionsMenu(R.menu.menu_main)
public class ObdInfosActivity extends AppCompatActivity {

    private android.os.Handler handler = new android.os.Handler();

    Thread t;

    private boolean controle;

    OBDReadings_db obdRead;
    Intent intent;
    int inicio, fim;

    private AlertDialog alerta;
    private View view;

    @Bean
    OBDReadingsController obdReadingsController;

    @ViewById
    TextView RPM;

    @ViewById
    TextView Borboleta;

    @ViewById
    TextView Ignicao;

    @ViewById
    TextView Consumo;

    @ViewById
    TextView MassAir;

    @ViewById
    TextView unitThrottler;

    @ViewById
    TextView unitIgnitionTiming;

    @ViewById
    TextView unitFuelConsumptionRate;

    @ViewById
    TextView unitMassAirFlow;

    @ViewById
    ProgressBar progress;

    @ViewById
    TextView textView;

    @ViewById
    Button cancelarBT;

    @ViewById
    Button testBT;

    @AfterViews
    public void start() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Unifor-OBDReaderII </font>"));
        }

        organiza();
        controle = true;
        t = new Thread() {

            private DecimalFormat fmt = new DecimalFormat("0.0");

            public void run() {
                while (controle) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RPM.setText(OBDReading_Sigleton.getInstance().getRPM() + "");
                            Borboleta.setText(fmt.format(OBDReading_Sigleton.getInstance().getThrottlePosition()));
                            Ignicao.setText(fmt.format(OBDReading_Sigleton.getInstance().getTimingAdvance()));
                            MassAir.setText(fmt.format(OBDReading_Sigleton.getInstance().getMassAirFlow()));
                            Consumo.setText(fmt.format(OBDReading_Sigleton.getInstance().getFuelConsumption()));
                        }
                    });
                }
            }
        };
        t.start();
    }

    @Click
    protected void testBT() {
        int rpm = OBDReading_Sigleton.getInstance().getRPM();

        if (rpm != 0) {
            getRpms();
        } else {
            Toast.makeText(getApplicationContext(), "Conecte um dispositivo", Toast.LENGTH_LONG).show();
        }
    }

    @Click
    protected void cancelarBT() {
        t.interrupt();
        progress.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        cancelarBT.setVisibility(View.GONE);
        testBT.setVisibility(View.VISIBLE);
    }

    private void salva() {
        progress.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        t = new Thread(new Runnable() {
            @Override
            public void run() {

                while (inicio > OBDReading_Sigleton.getInstance().getRPM()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (OBDReading_Sigleton.getInstance().getRPM() < fim) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    obdRead = new OBDReadings_db(
                            OBDReading_Sigleton.getInstance().getRPM(),
                            OBDReading_Sigleton.getInstance().getThrottlePosition(),
                            OBDReading_Sigleton.getInstance().getTimingAdvance(),
                            OBDReading_Sigleton.getInstance().getFuelConsumption(),
                            OBDReading_Sigleton.getInstance().getMassAirFlow());
                    obdReadingsController.createRead(obdRead);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ObdAnalisesActivity_.class);
                        startActivity(intent);
                        progress.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        cancelarBT.setVisibility(View.GONE);
                        testBT.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        t.start();
    }

    protected void clearBd() {
        obdReadingsController.clearAll();
    }

    public void organiza() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");

        RPM.setTypeface(font);
        Borboleta.setTypeface(font);
        Ignicao.setTypeface(font);
        Consumo.setTypeface(font);
        MassAir.setTypeface(font);
    }


    public void setControle(boolean controle) {
        this.controle = controle;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.setControle(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ObdConnectionActivity.getService() != null) {
            intent = ObdConnectionActivity.getService();
        }
        this.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setControle(false);
        if (intent != null)
            stopService(intent);
    }

    @OptionsItem
    public void analise() {
        if (obdReadingsController.getSize() > 0) {
            Intent intent = new Intent(this.getApplicationContext(), ObdAnalisesActivity_.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Sem dados coletados", Toast.LENGTH_LONG).show();
        }
    }

    @OptionsItem
    public void bluetooth() {
        Intent intent = new Intent(this.getApplicationContext(), ObdConnectionActivity_.class);
        startActivity(intent);
    }

    @OptionsItem
    public void unidade() {
        if (unitThrottler.getText().equals("")) {
            unitThrottler.setText("%");
            unitIgnitionTiming.setText("%");
            unitFuelConsumptionRate.setText("L/h");
            unitMassAirFlow.setText("g/s");
        } else {
            unitThrottler.setText("");
            unitFuelConsumptionRate.setText("");
            unitIgnitionTiming.setText("");
            unitMassAirFlow.setText("");
        }

    }

    @OptionsItem
    public void unifor() {
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

    public void getRpms() {
        LayoutInflater li = getLayoutInflater();
        view = li.inflate(R.layout.valores, null);

        Button iniciar = (Button) view.findViewById(R.id.iniciar);
        Button cancelar = (Button) view.findViewById(R.id.cancelar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText rpmi = (EditText) view.findViewById(R.id.rpmi);
                EditText rpmf = (EditText) view.findViewById(R.id.rpmf);
                String i = rpmi.getText().toString();
                String f = rpmf.getText().toString();

                if (i.equals("") || f.equals("")) {
                    Toast.makeText(getApplicationContext(), "Campo(os) em branco", Toast.LENGTH_LONG).show();
                } else {
                    inicio = Integer.parseInt(i);
                    fim = Integer.parseInt(f);
                    if (inicio >= fim) {
                        Toast.makeText(getApplicationContext(), "Valor inicial maior que o final.", Toast.LENGTH_LONG).show();
                    } else {
                        salva();
                        alerta.dismiss();
                        testBT.setVisibility(View.GONE);
                        cancelarBT.setVisibility(View.VISIBLE);
                        clearBd();
                    }

                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setInverseBackgroundForced(true);
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }
}
