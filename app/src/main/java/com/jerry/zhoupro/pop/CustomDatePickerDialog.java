package com.jerry.zhoupro.pop;

import java.lang.reflect.Method;
import java.util.Calendar;

import com.jerry.zhoupro.R;

import android.content.Context;
import android.os.Bundle;


public class CustomDatePickerDialog extends BaseDialog {

    private NumberPicker yearNp;
    private NumberPicker monthNp;
    private NumberPicker dayNp;
    private int year;
    private int month;
    private int day;

    public CustomDatePickerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_date_picker);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        yearNp = (NumberPicker) findViewById(R.id.year_np);
        monthNp = (NumberPicker) findViewById(R.id.month_np);
        dayNp = (NumberPicker) findViewById(R.id.day_np);

        yearNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%s%s", value, mContext.getString(R.string.year));
            }
        });

        // 解决首次弹出选择器时，formatter对当前value不起作用
        try {
            Method yearNpMethod = yearNp.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            yearNpMethod.setAccessible(true);
            yearNpMethod.invoke(yearNp, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        monthNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%s%s", value, mContext.getString(R.string.month));
            }
        });

        try {
            Method monthNpMethod = monthNp.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            monthNpMethod.setAccessible(true);
            monthNpMethod.invoke(monthNp, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dayNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%s%s", value, mContext.getString(R.string.day));
            }
        });

        try {
            Method dayNpMethod = dayNp.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            dayNpMethod.setAccessible(true);
            dayNpMethod.invoke(dayNp, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Calendar c = Calendar.getInstance();

        yearNp.setMaxValue(2020);
        yearNp.setMinValue(2014);
        yearNp.setValue(c.get(Calendar.YEAR));

        monthNp.setMaxValue(12);
        monthNp.setMinValue(1);
        monthNp.setValue(c.get(Calendar.MONTH) + 1);

        dayNp.setMaxValue(31);
        dayNp.setMinValue(1);
        dayNp.setValue(c.get(Calendar.DAY_OF_MONTH));

        yearNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                year = picker.getValue();
                month = monthNp.getValue();
                setDaynp();
            }
        });

        monthNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month = picker.getValue();
                year = yearNp.getValue();
                setDaynp();
            }
        });

        dayNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                day = picker.getValue();
            }
        });

    }

    private void setDaynp() {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            dayNp.setMaxValue(30);
        } else if (month == 2) {
            dayNp.setMaxValue(isLeapYear(year) ? 29 : 28);
        } else {
            dayNp.setMaxValue(31);
        }
    }

    /**
     * 获取当前日期年月日的字符串
     */
    public String getDate() {
        String month = String.valueOf(monthNp.getValue());
        if (month.length() < 2) {
            month = "0" + month;
        }
        String day = String.valueOf(dayNp.getValue());
        if (day.length() < 2) {
            day = "0" + day;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(yearNp.getValue()).append(mContext.getString(R.string.year));
        sb.append(month).append(mContext.getString(R.string.month));
        sb.append(day).append(mContext.getString(R.string.day));
        return sb.toString();
    }

    /**
     * 判断是否是闰年
     */
    private boolean isLeapYear(int y) {
        return y % 4 == 0 || (y % 100 == 0 && y % 400 == 0);
    }
}
