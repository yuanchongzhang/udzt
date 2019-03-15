package com.xmrxcaifu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmrxcaifu.R;


public class CustomDialog_view_new extends Dialog {

	public CustomDialog_view_new(Context context) {
		super(context);
	}

	public CustomDialog_view_new(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context; // 上下文对象

		private String message,message2; // 对话框内容
		 CustomDialog_view_new dialog;
		private String confirm_btnText; // 按钮名称“确定”
		private String cancel_btnText; // 按钮名称“取消”

		private View contentView; // 对话框中间加载的其他布局界面
		/* 按钮坚挺事件 */
		private OnClickListener confirm_btnClickListener;
		private OnClickListener cancel_btnClickListener;


		public Builder(Context context) {
			this.context = context;
		}

		/* 设置对话框信息 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage2(String message) {
			this.message2 = message;
			return this;
		}
		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}
		public Builder setMessage2(int message) {
			this.message2 = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {

			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {

			return this;
		}

		/**
		 * 设置对话框界面
		 * 
		 * @param v
		 *            View
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setPositiveButton(int confirm_btnText,
				OnClickListener listener) {
			this.confirm_btnText = (String) context.getText(confirm_btnText);
			this.confirm_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setPositiveButton(String confirm_btnText,
				OnClickListener listener) {
			this.confirm_btnText = confirm_btnText;
			this.confirm_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNegativeButton(int cancel_btnText,
				OnClickListener listener) {
			this.cancel_btnText = (String) context.getText(cancel_btnText);
			this.cancel_btnClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button and it's listener
		 * 
		 * @param confirm_btnText
		 * @return
		 */
		public Builder setNegativeButton(String cancel_btnText,
				OnClickListener listener) {
			this.cancel_btnText = cancel_btnText;
			this.cancel_btnClickListener = listener;
			return this;
		}




		public CustomDialog_view_new create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			if(dialog==null){
				dialog	 = new CustomDialog_view_new(context,
						R.style.mystyle);
			}
		
			View layout = inflater.inflate(R.layout.customdialog_new, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title



			// set the confirm button
			if (confirm_btnText != null) {
				((Button) layout.findViewById(R.id.confirm_btn))
						.setText(confirm_btnText);
				if (confirm_btnClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									confirm_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				} else {

				}
			} else {

				layout.findViewById(R.id.cancel_btn).setBackgroundResource(
						R.drawable.single_btn_select);
			}
			// set the cancel button
			if (cancel_btnText != null) {
				((Button) layout.findViewById(R.id.cancel_btn))
						.setText(cancel_btnText);
				if (cancel_btnClickListener != null) {
					((Button) layout.findViewById(R.id.cancel_btn))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									cancel_btnClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				} else {
					((Button) layout.findViewById(R.id.cancel_btn))
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			} else {
				// if no cancel button just set the visibility to GONE
				layout.findViewById(R.id.cancel_btn).setVisibility(View.GONE);


			}
			// set the content message
			if(message2!=null){
				((TextView) layout.findViewById(R.id.message2)).setText(message2);
			}
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.message))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.message)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}
	public void mdissmis(){
		this.dismiss();
	}
}
