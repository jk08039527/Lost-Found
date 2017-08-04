package com.jerry.zhoupro.ui.release;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.base.TitleBaseActivity;
import com.jerry.zhoupro.app.Key;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;

public class ThingTypeActivity extends TitleBaseActivity {

    @BindView(R.id.rg_type)
    RadioGroup mRgType;
    private String thingType;

    @Override
    protected String getTitleText() {
        return getString(R.string.type);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_thing_type;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        thingType = getIntent().getStringExtra(Key.THING_TYPE);
    }

    @Override
    protected void initData() {
        ((RadioButton) mRgType.findViewWithTag(thingType)).setChecked(true);
        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, @IdRes final int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                Intent intent = new Intent();
                intent.putExtra(Key.THING_TYPE, radioButton.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
