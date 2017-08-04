package com.jerry.zhoupro.widget;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.app.Key;
import com.jerry.zhoupro.presenter.listener.MyTextWatcherListener;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * 带清空按钮的EditText
 */
public class MyEditText extends AppCompatEditText {

	private Drawable dRight;
	private Rect rBounds;

	public MyEditText(Context context) {
		super(context);
		initEditText();
	}

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initEditText();
	}

	private void initEditText() {
		setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text_color));
		setEditTextDrawable(Key.NIL);
		addTextChangedListener(new MyTextWatcherListener() { // 对文本内容改变进行监听
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setEditTextDrawable(s);
			}
		});
	}

	// 控制图片的显示
	private void setEditTextDrawable(CharSequence s) {
		if (s.length() == 0) {
			setCompoundDrawables(null, null, null, null);
		} else {
			setCompoundDrawables(null, null, this.dRight, null);
		}
	}

	/**
	 * 隐藏删除按钮
	 */
	public void showDelBtn(boolean show) {
		if (show && getText().length() > 0) {
			setCompoundDrawables(null, null, this.dRight, null);
		} else {
			setCompoundDrawables(null, null, null, null);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.dRight = null;
		this.rBounds = null;
		super.finalize();
	}

	// 添加触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((this.dRight != null) && (event.getAction() == MotionEvent.ACTION_UP)) {
			this.rBounds = this.dRight.getBounds();
			int i = (int) event.getX();
			if (i > getRight() - 5 * this.rBounds.width()) {
				setText(Key.NIL);
			}
		}
		return super.onTouchEvent(event);
	}

	// 设置显示的图片资源
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if (right != null)
			this.dRight = right;
		super.setCompoundDrawables(left, top, right, bottom);
	}
}
