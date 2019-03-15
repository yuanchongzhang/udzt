package com.xmrxcaifu.gesture;


import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.xmrxcaifu.R;


//一个手势行程的定时点，多个点形成一个手势行程。GesturePoint封装x,y轴和时间戳的值
public class GesturePoint {
	/**
	 * 锟斤拷锟絰锟斤拷值
	 */
	private int leftX;
	/**
	 * 锟揭憋拷x锟斤拷值
	 */
	private int rightX;
	/**
	 * 锟较憋拷y锟斤拷值
	 */
	private int topY;
	/**
	 * 锟铰憋拷y锟斤拷值
	 */
	private int bottomY;
	/**
	 * 锟斤拷锟斤拷锟斤拷应锟斤拷ImageView锟截硷拷
	 */
	private ImageView image;

	/**
	 * 锟斤拷锟斤拷x值
	 */
	private int centerX;

	/**
	 * 锟斤拷锟斤拷y值
	 */
	private int centerY;

	/**
	 * 状态值
	 */
	private int pointState;
	

	/**
	 * 锟斤拷锟斤拷锟斤拷Point锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷郑锟斤拷锟�锟斤拷始(直锟接感撅拷锟斤拷1锟斤拷始)
	 */
	private int num;

	public GesturePoint(int leftX, int rightX, int topY, int bottomY,
			ImageView image, int num) {
		super();
		this.leftX = leftX;
		this.rightX = rightX;
		this.topY = topY;
		this.bottomY = bottomY;
		this.image = image;

//		this.centerX = (leftX + rightX) / 2;
//		this.centerY = (topY + bottomY) / 2;
		this.centerX = (leftX + rightX)/2;
		this.centerY = (topY + bottomY)/2;
		this.num = num;
	}

	public int getLeftX() {
		return leftX;
	}

	public void setLeftX(int leftX) {
		this.leftX = leftX;
	}

	public int getRightX() {
		return rightX;
	}

	public void setRightX(int rightX) {
		this.rightX = rightX;
	}

	public int getTopY() {
		return topY;
	}

	public void setTopY(int topY) {
		this.topY = topY;
	}

	public int getBottomY() {
		return bottomY;
	}

	public void setBottomY(int bottomY) {
		this.bottomY = bottomY;
	}

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int getPointState() {
		return pointState;
	}
	
	@SuppressLint("NewApi")
	public void setPointState(int state) {
		pointState = state;
		switch (state) {
			/*v7gesture_node_normal.png
			v7gesture_node_pressed.png
			v7gesture_node_wrong.png*/
		case Constants.POINT_STATE_NORMAL:
//			this.image.setBackground(new BitmapDrawable(BitmapUtils.readBitMap(null, R.drawable.gesture_node_normal)));
//			this.image.setBackgroundResource(R.drawable.gesture_node_normal);
			this.image.setBackgroundResource(R.drawable.v7gesture_node_normal);
				break;
		case Constants.POINT_STATE_SELECTED:
//			this.image.setBackground(new BitmapDrawable(BitmapUtils.readBitMap(null, R.drawable.gesture_node_pressed)));
//			this.image.setBackgroundResource(R.drawable.gesture_node_pressed);
			this.image.setBackgroundResource(R.drawable.v7gesture_node_pressed);
				break;
		case Constants.POINT_STATE_WRONG:
//			this.image.setBackground(new BitmapDrawable(BitmapUtils.readBitMap(null, R.drawable.gesture_node_wrong)));
//			this.image.setBackgroundResource(R.drawable.gesture_node_wrong);
			this.image.setBackgroundResource(R.drawable.v7gesture_node_wrong);
				break;
		default:
			break;
		}
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bottomY;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + leftX;
		result = prime * result + rightX;
		result = prime * result + topY;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GesturePoint other = (GesturePoint) obj;
		if (bottomY != other.bottomY)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (leftX != other.leftX)
			return false;
		if (rightX != other.rightX)
			return false;
		if (topY != other.topY)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [leftX=" + leftX + ", rightX=" + rightX + ", topY="
				+ topY + ", bottomY=" + bottomY + "]";
	}
}
