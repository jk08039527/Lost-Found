package com.feiying.breedapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.feiying.breedapp.R;
import com.feiying.breedapp.util.DateUtils;

public class FYBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, DateUtils.compareTo("19911027", "19910803"),Toast.LENGTH_LONG).show();
    }
}
