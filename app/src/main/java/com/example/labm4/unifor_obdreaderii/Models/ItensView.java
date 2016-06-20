package com.example.labm4.unifor_obdreaderii.Models;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.labm4.unifor_obdreaderii.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

@EViewGroup(R.layout.table_list)
public class ItensView extends LinearLayout {

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

    private DecimalFormat fmt;

    public ItensView(Context context) {
        super(context);
        fmt = new DecimalFormat("0.00");
    }

    public void bind(OBDReadings_db obd) {
        RPM.setText(String.valueOf(obd.getRPM()));
        Borboleta.setText(fmt.format(obd.getThrottlePosition()));
        Ignicao.setText(fmt.format(obd.getTimingAdvance()));
        Consumo.setText(fmt.format(obd.getFuelConsumption()));
        MassAir.setText(fmt.format(obd.getMassAirFlow()));
    }

}
