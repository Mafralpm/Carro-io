package com.example.labm4.unifor_obdreaderii.Activities.Fragments;

import android.graphics.Color;
import android.support.v4.app.ListFragment;

import com.example.labm4.unifor_obdreaderii.Adapter.AdapterListView;
import com.example.labm4.unifor_obdreaderii.Controller.OBDReadingsController;
import com.example.labm4.unifor_obdreaderii.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_pdf)
public class PDFFragment extends ListFragment {

    @Bean
    AdapterListView adapterListView;

    @AfterViews
    public void createPDF() {
        this.getListView().setBackgroundColor(Color.rgb(115, 139, 177));
        this.setListAdapter(adapterListView);
    }

}
