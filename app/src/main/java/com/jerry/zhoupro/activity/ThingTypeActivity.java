package com.jerry.zhoupro.activity;

import java.util.ArrayList;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.adapter.CommonAdapter;
import com.jerry.zhoupro.adapter.ViewHolder;
import com.jerry.zhoupro.command.Key;
import com.jerry.zhoupro.util.Mlog;
import com.jerry.zhoupro.util.TimeTask;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import butterknife.BindView;

import static com.jerry.zhoupro.R.id.checkedTextView;

public class ThingTypeActivity extends TitleBaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_type)
    ListView mLvType;
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
        String[] typeStr = getResources().getStringArray(R.array.thing_type);
        int[] typeInco = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
        };
        final ArrayList<TypeMenu> typeMenus = new ArrayList<>();
        for (int i = 0; i < typeStr.length; i++) {
            typeMenus.add(new TypeMenu(typeInco[i], typeStr[i], typeStr[i].equals(thingType)));
        }
        mLvType.setAdapter(new CommonAdapter<TypeMenu>(this, typeMenus) {
            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                ViewHolder holder = ViewHolder.get(ThingTypeActivity.this, convertView, R.layout.text_view);
                CheckedTextView ctv = holder.getView(checkedTextView);
                ctv.setText(typeMenus.get(position).text);
                ctv.setChecked(typeMenus.get(position).checked);
                Drawable drawable = ContextCompat.getDrawable(ThingTypeActivity.this, R.mipmap.ic_launcher);
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                ctv.setCompoundDrawables(drawable, null, null, null);
                return holder.getConvertView();
            }
        });
        mLvType.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        Mlog.d("onItemClick: ");
        if (view instanceof CheckedTextView) {
            final CheckedTextView ctv = (CheckedTextView) view;
            ctv.toggle();
            new TimeTask(500, new TimeTask.TimeOverListerner() {
                @Override
                public void onFinished() {
                    if (ThingTypeActivity.this.isFinishing()) { return; }
                    Intent intent = new Intent();
                    intent.putExtra(Key.THING_TYPE, ctv.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }).start();

        }
    }

    private class TypeMenu {

        int inco;
        String text;
        boolean checked;

        TypeMenu(final int inco, final String text, boolean checked) {
            this.inco = inco;
            this.text = text;
            this.checked = checked;
        }
    }
}
