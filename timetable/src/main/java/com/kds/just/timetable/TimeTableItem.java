package com.kds.just.timetable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.Random;

public class TimeTableItem {
	private static final String TAG = TimeTableItem.class.getSimpleName();
	
	public static final int TYPE_EMPTY = 0;
	public static final int TYPE_CELL = 1;
	public static final int TYPE_WEEK = 2;
	public static final int TYPE_INDEX = 3;

	public int mType = TYPE_EMPTY;
	
	public View mView;

	public int mLayoutId = R.layout.timetable_view_cell;

	public int mX;			//주 0:월 ~ 6:일)
	public int mY;			//index

	public Rect mRect = null;

	public int mCellDisplayWidth = 0;
	public int mCellDisplayHeight = 0;

	public Object mCustomData;
	public void setCustomData(Object data) {
		mCustomData = data;
	}
	public Object getCustomData() {
		return mCustomData;
	}
	public static TimeTableItem createItem(TimeTableView parent, int type, int layoutId) {
		TimeTableItem item = new TimeTableItem();
		item.mType = type;
		item.mLayoutId = layoutId;
		if (parent != null) {
			item.makeView(parent);
		}
		return item;
	}

	public TimeTableItem() {
		init();
	}

	public void setLocation(int x, int y) {
		mX = x;
		mY = y;
	}

	private void init() {
		mRect = new Rect();
	}

	public void setType(int t) {
		mType = t;
	}

	public void setRect(int l,int t,int w,int h) {
		mRect.set(l, t, l + w, t + h);
		mCellDisplayWidth = w;
		mCellDisplayHeight = h;
	}

	public void setLayout() {
		if (mView != null) {
			mView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
			TimeTableView.LayoutParams params = (TimeTableView.LayoutParams) mView.getLayoutParams();
			params.width = mCellDisplayWidth;
			params.height = mCellDisplayHeight;
		}
	}

	public void drawNormal(Canvas canvas, Paint paint) {
		canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, paint);
	}

	public void drawNormal(Canvas canvas, Paint bgPaint, Paint strokePaint) {
		canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, bgPaint);
		canvas.drawRect(mRect.left, mRect.top, mRect.right, mRect.bottom, strokePaint);
	}

	public void drawWeek(Canvas canvas, Paint paint, int right) {
		canvas.drawRect(0, 0, right, mRect.bottom, paint);
	}

	public View makeView(ViewGroup parent) {
		if (mView == null) {
			mView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
			parent.addView(mView);
		}
		mView.setTag(this);
		return mView;
	}

	public TimeTableItem contains(MotionEvent event) {
		if (mRect != null && mRect.contains((int)event.getX(), (int)event.getY())) {
			return this;
		} else {
			return null;
		}
		 
	}

	public void removeView() {
		if (mView != null) {
			((ViewGroup)mView.getParent()).removeView(mView);
			mView = null;
		}
	}

	@NonNull
	@Override
	public String toString() {
		String type = "CELL";
		if (mType == TYPE_WEEK) {
			type = "WEEK";
		} else if (mType == TYPE_INDEX) {
			type = "INDEX";
		}

		return type + ":" + mX + "," + mY;
	}
}
