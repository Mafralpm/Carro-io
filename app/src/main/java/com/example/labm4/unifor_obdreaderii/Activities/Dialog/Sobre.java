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

public class Sobre extends Dialog implements
        android.view.View.OnClickListener{

    public Button fechar;

    public Sobre(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sobre);
        fechar = (Button) findViewById(R.id.button);
        fechar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iniciar:
                dismiss();
                break;
            default:
                break;
        }
    }
}
