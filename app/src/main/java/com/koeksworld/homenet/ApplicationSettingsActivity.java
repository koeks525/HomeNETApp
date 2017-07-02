package com.koeksworld.homenet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import it.sephiroth.android.library.tooltip.Tooltip;

public class ApplicationSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_settings);
        toolbar = (Toolbar) findViewById(R.id.SettingsToolbar);
        toolbarTextView = (TextView) findViewById(R.id.SettingsToolbarTextView);
        toolbarTextView.setText("Application Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.SettingsContentView, new AppSettings()).commit();

    }
}
