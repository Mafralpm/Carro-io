package com.example.labm4.unifor_obdreaderii.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.labm4.unifor_obdreaderii.Activities.Fragments.GraphFragment;
import com.example.labm4.unifor_obdreaderii.Activities.Fragments.GraphFragment_;
import com.example.labm4.unifor_obdreaderii.Activities.Fragments.PDFFragment;
import com.example.labm4.unifor_obdreaderii.Activities.Fragments.PDFFragment_;
import com.example.labm4.unifor_obdreaderii.Adapter.ViewPagerAdapter;
import com.example.labm4.unifor_obdreaderii.Controller.OBDReadingsController;
import com.example.labm4.unifor_obdreaderii.Models.OBDReadings_db;
import com.example.labm4.unifor_obdreaderii.PDF.FileOperations;
import com.example.labm4.unifor_obdreaderii.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_obd_analises)
@OptionsMenu(R.menu.menu)
public class ObdAnalisesActivity extends AppCompatActivity {

    private ViewPagerAdapter mSectionsPagerAdapter;
    private String[] tabs = {"Tabela", "Grafico"};
    private List<OBDReadings_db> infosList;

    private AlertDialog alerta;
    private View view;

    private PDFFragment pdfFragment;
    private GraphFragment graphFragment;

    @Bean
    OBDReadingsController obdReadingsController;

    @ViewById
    ViewPager pager;

    @ViewById
    TabHost tabHost;

    @AfterViews
    public void start() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Analise </font>"));
        }

        infosList = obdReadingsController.getReadings();

        initViewPager();

        initTabHost();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<Fragment>();

        pdfFragment = new PDFFragment_();
        graphFragment = new GraphFragment_();

        fragments.add(pdfFragment);
        fragments.add(graphFragment);

        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(mSectionsPagerAdapter);

        pager.addOnPageChangeListener(pageChangeListener);
    }

    private void initTabHost() {
        tabHost.setup();

        String[] tabNames = {"Lista", "Grafico"};
        for (int i = 0; i < tabNames.length; i++) {
            setNewTab(tabNames[i], tabNames[i], new FakeContent(getApplicationContext()));
        }
        tabHost.setOnTabChangedListener(tabChangeListener);
    }

    private void setNewTab(String tag, String title, FakeContent contentID) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(title);
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    @Click
    public void doc() {
        infosList = obdReadingsController.getReadings();

        Bitmap bitmap = graphFragment.getBitmap();
        FileOperations fileOperations = new FileOperations();
        fileOperations.write(getApplicationContext(), infosList, bitmap);
        Toast.makeText(getApplicationContext(), "Teste salvo", Toast.LENGTH_LONG).show();

    }

    public class FakeContent implements TabHost.TabContentFactory {

        Context context;

        public FakeContent(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tabHost.setCurrentTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            int position = tabHost.getCurrentTab();
            pager.setCurrentItem(position);
        }
    };

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

}
