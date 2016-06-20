package com.example.labm4.unifor_obdreaderii.Activities.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labm4.unifor_obdreaderii.R;

public class RpmDialog extends Dialog implements
        android.view.View.OnClickListener {
    private Activity activity;
    private Button iniciar, cancelar;
    private int inicio;
    private int fim;

    public int getInicio() {
        return inicio;
    }

    public int getFim() {
        return fim;
    }

    public RpmDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            activity.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.valores);
        iniciar = (Button) findViewById(R.id.iniciar);
        cancelar = (Button) findViewById(R.id.cancelar);
        iniciar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iniciar:
                EditText rpmi = (EditText) findViewById(R.id.rpmi);
                EditText rpmf = (EditText) findViewById(R.id.rpmf);
                if(rpmi.getText().equals("") && rpmf.getText().equals("")){
                    inicio = Integer.parseInt(rpmi.getText().toString());
                    fim = Integer.parseInt(rpmf.getText().toString());
                    dismiss();
                }else{
                    Toast.makeText(getContext(), "Campo(os) em branco", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancelar:
                dismiss();
                break;
            default:
                break;
        }
    }
}