package com.jerry.zhoupro.activity;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.pop.CustomDatePickerDialog;
import com.jerry.zhoupro.view.MeasureGridView;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class ReleaseActivity extends TitleBaseActivity {

    @BindView(R.id.et_release_content)
    EditText mEtReleaseContent;
    @BindView(R.id.gridView)
    MeasureGridView mGridView;
    @BindView(R.id.tv_thing_type_value)
    TextView mTvThingTypeValue;
    @BindView(R.id.tv_thing_date_value)
    TextView mTvThingDateValue;
    @BindView(R.id.tv_thing_place_value)
    TextView mTvThingPlaceValue;
    private String releaseType;
    private String thingType;
    private String date;
    private String place;
    private String location;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_release;
    }

    @Override
    protected void beforeViews() {
        super.beforeViews();
        releaseType = getIntent().getStringExtra(Key.TAG_RELEASE_TYPE);
    }

    @Override
    protected String getTitleText() {
        return releaseType.equals(Key.TAG_RELEASE_LOST)
                ? getString(R.string.realese_lost)
                : getString(R.string.realese_found);
    }

    @Override
    public void initView() {
        super.initView();
        setVisible(titleRight);
        titleRight.setText(getString(R.string.realese));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_thing_type, R.id.tv_thing_date, R.id.tv_thing_place})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_title:
                intent = new Intent();
                break;
            case R.id.tv_thing_type:
                intent = new Intent(this, ThingTypeActivity.class);
                intent.putExtra(Key.THING_TYPE, mTvThingTypeValue.getText().toString());
                startActivityForResult(intent, Key.CODE_101);
                break;
            case R.id.tv_thing_date:
                final CustomDatePickerDialog dialog = new CustomDatePickerDialog(this);
                dialog.show();
                dialog.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        dialog.dismiss();
                        date = dialog.getDate();
                        mTvThingDateValue.setText(date);
                    }
                });
                break;
            case R.id.tv_thing_place:
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, Key.CODE_102);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) { return; }
        switch (requestCode) {
            case Key.CODE_101:
                thingType = data.getStringExtra(Key.THING_TYPE);
                mTvThingTypeValue.setText(thingType);
                break;
            case Key.CODE_102:
                place = data.getStringExtra(Key.ADDRESS);
                location = data.getStringExtra(Key.LOCATION);
                mTvThingPlaceValue.setText(place);
                break;
            default:
                break;
        }
    }
}
