package com.example.boss.boss_demo.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.boss.boss_demo.MultiTypeActivity;
import com.example.boss.boss_demo.R;


public class SimpleUpdateDialog {
	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	Button cancleBtn, okBtn;
	TextView titleTv, versionTv, contentTv;
	boolean cancleable = true;
	ProgressBar progressBar;
	boolean isFoodSplit = false;
	boolean isPartSplit = false;
	boolean isShopSplit = false;

	/**
	 * init the dialog
	 * 
	 * @return
	 */
	public SimpleUpdateDialog(Context c, boolean canc) {
		this.context = c;
		cancleable = canc;
		dialog = new Dialog(context, R.style.common_dlg);
		dialog.setContentView(R.layout.dialog_layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		final CheckBox food = dialog.findViewById(R.id.checkbox_food);
		final CheckBox part = dialog.findViewById(R.id.checkbox_part);
		final CheckBox shop = dialog.findViewById(R.id.checkbox_shop);
		dialog.findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (food.isChecked()){
					isFoodSplit = true;
				}
				if (part.isChecked()){
					isPartSplit = true;
				}
				if (shop.isChecked()){
					isShopSplit = true;
				}
				MultiTypeActivity.showPartModule = isPartSplit;
				MultiTypeActivity.showFoodModule = isFoodSplit;
				MultiTypeActivity.showShopModule = isShopSplit;
				if (dialogcallback != null)
					dialogcallback.dialogCancle();
				dismiss();
			}
		});
	}

	public interface Dialogcallback {
		public void dialogOk();

		public void install();

		public void dialogCancle();
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}

	/**
	 * @category Set The Content of the TextView
	 * */
	public void setContent(String content) {
		contentTv.setText(content + "");
	}

	public void show() {
		if (!dialog.isShowing())
			dialog.show();
	}

	public void hide() {
		if (dialog.isShowing())
			dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}

	public String isShow() {
		// TODO Auto-generated method stub
		return dialog.isShowing() ? "yes" : "no";
	}
}
