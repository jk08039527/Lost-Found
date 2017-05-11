package com.jerry.zhoupro.pop;

import com.jerry.zhoupro.R;
import com.jerry.zhoupro.widget.RefreshingView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class RefreshDialog extends Dialog {

	private Context mContext;
    private RefreshingView mImageView;

	public RefreshDialog(Context context) {
		super(context, R.style.refresh_dialog);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_refresh);
		setCanceledOnTouchOutside(false);
		mImageView = (RefreshingView) findViewById(R.id.loading_image);
	}

	@Override
	public void show() {
		if (!((Activity) mContext).isFinishing()) {
			super.show();
			mImageView.post(new Runnable() {
				@Override
				public void run() {
					mImageView.start();
				}
			});
		}
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			mImageView.stop();
			super.dismiss();
		}
	}
}