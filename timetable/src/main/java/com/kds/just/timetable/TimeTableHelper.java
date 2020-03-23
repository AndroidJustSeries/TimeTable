package com.kds.just.timetable;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kds.just.timetable.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTableHelper implements OnClickListener {
	private static final String TAG = TimeTableHelper.class.getSimpleName();
	
	public static final String WEEK[] = {
			"일","월","화","수","목","금","토"
	};
	
	public TimeTableItem mWeekArray[] = null;	//상단 요일 view Array
	public TimeTableItem mIndexArray[] = null;	//왼쪽 Index view Array
	public TimeTableItem mCellArray[][] = null;	//Cell view Array

	public int mWeekCount = 0;				//요일 카운트 (MAX:7)
	public int mLineCount = 0;				//row count

	public int mWidth = 0;						//TimeTable 전체 너비
	public int mHeight = 0;						//TimeTable 전체 높이

	public int mWeekHeight = 0;					//상단 요일 높이
	public int mIndexWidth = 0;					//왼쪽 index 너비

	public int mCellWidth = 0;					//Cell 너비(자동설정)
	public int mCellHeight = 0;					//Cell 높이

	private TimeTableAdapter mAdapter;		//Custom data View adapter

	private TimeTableView mTimeTableView;
	
	private Context mContext;

	public TimeTableHelper(Context context) {
		mContext = context;
	}

	public void setWeekCount(int c) {
		mWeekCount = c;
	}

	public void setLineCount(int c) {
		mLineCount = c;
	}

	public void setCellHeight(int h) {
		mCellHeight = h;
	}

	public void setIndexWidth(int w) {
		mIndexWidth = w;
	}

	public void setWeekHeight(int h) {
		mWeekHeight = h;
	}

	private int mWeekLayoutId;
	private int mIndexLayoutId;
	private int mCellLayoutId;
	public void setLayoutId(int weekId, int indexId, int cellId) {
		mWeekLayoutId = weekId;
		mIndexLayoutId = indexId;
		mCellLayoutId = cellId;
	}

	public void build(TimeTableView parent) {
		mTimeTableView = parent;
		if (mWeekArray == null) {
			mWeekArray = new TimeTableItem[mWeekCount];
			for (int i=0;i<mWeekCount;i++) {
				mWeekArray[i] = TimeTableItem.createItem(parent,TimeTableItem.TYPE_WEEK,mWeekLayoutId);
				mWeekArray[i].setLocation(i,0);
				if (mIndexLayoutId == R.layout.timetable_view_cell) {
					((TextView)mWeekArray[i].mView).setText(WEEK[i]);
				}
			}
		}
		if (mIndexArray == null) {
			mIndexArray = new TimeTableItem[mLineCount];
			for (int i=0;i<mLineCount;i++) {
				mIndexArray[i] = TimeTableItem.createItem(parent,TimeTableItem.TYPE_INDEX,mIndexLayoutId);
				mIndexArray[i].setLocation(0,i);
				if (mIndexLayoutId == R.layout.timetable_view_cell) {
					((TextView)mIndexArray[i].mView).setText(String.valueOf(i));
				}
			}
		}
		if (mCellArray == null) {
			mCellArray = new TimeTableItem[mWeekCount][mLineCount];
			for (int x=0;x<mWeekCount;x++) {
				for (int y=0;y<mLineCount;y++) {
					mCellArray[x][y] = TimeTableItem.createItem(null,TimeTableItem.TYPE_CELL,mCellLayoutId);
					mCellArray[x][y].setLocation(x,y);
				}
			}
		}
	}

	public void setAdapter(TimeTableAdapter adapter) {
		mAdapter = adapter;
		if (mAdapter != null) {
			mAdapter.setHelper(this);
		}
		notifyDataSetChanged();
	}

	public TimeTableAdapter getAdapter() {
		return mAdapter;
	}

	void notifyDataSetChanged() {
		if (mAdapter != null) {
			if (mAdapter.getWeekCount() != mWeekCount ||
				mAdapter.getIndexCount() != mLineCount ) {
				mWeekCount = mAdapter.getWeekCount();
				mLineCount = mAdapter.getIndexCount();
				mWeekArray = null;
				mIndexArray = null;
				mCellArray = null;
				build(mTimeTableView);
			}
			if (mWeekArray != null) {
				for (int i=0;i<mWeekArray.length;i++) {
					mAdapter.setBind(mWeekArray[i]);
				}
			}
			if (mIndexArray != null) {
				for (int i=0;i<mLineCount;i++) {
					mAdapter.setBind(mIndexArray[i]);
				}
			}
			if (mCellArray != null) {
				for (int x=0;x<mWeekCount;x++) {
					for (int y=0;y<mLineCount;y++) {
						//TODO View가 안만들어졌을 경우와 이미 만들었던적이 있는 경우 어떻게 처리하나???
						//TODO 다음 버전에서는 view를 삭제하지 않고 재활용할 방법을 생각해보자
						if (mAdapter.isCreate(mCellArray[x][y])) {
							if (mCellArray[x][y].mView == null) {
								View view = mCellArray[x][y].makeView(mTimeTableView);
							}
							mAdapter.setBind(mCellArray[x][y]);
						} else {
							mCellArray[x][y].removeView();
						}
					}
				}
			}
		}
	}

	public int setViewSize(int w) {
		mWidth = w;

		mCellWidth = (mWidth - mIndexWidth)/ mWeekCount;
//		Log.e(TAG,"KDS3393_TEST_mHeight = " + mHeight + " mCellHeight = " + mCellHeight + " mEtcItemH = " + mBreakTimeHeight + " total = " + ((mCellHeight * (mLineCount + 1)) + (mBreakTimeHeight * mBreakTimeCnt)));
		
		int diff = mHeight - ((mCellHeight * mLineCount) + mWeekHeight); //높이 오차 수정
		if (diff > 0) {
			mWeekHeight += diff;
		}
		diff = mWidth - ((mCellWidth * mWeekCount) + mIndexWidth); //너비 오차 수정
		if (diff > 0) {
			mIndexWidth += diff;
		}
		mHeight = mWeekHeight + (mLineCount * mCellHeight);
		initItemRect();
		return mHeight;
	}

	private void initItemRect() {
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		
		for (int x=0;x<mWeekCount;x++) {	//요일
			left = right;
			right = left + mCellWidth;
			mWeekArray[x].setRect(mIndexWidth + left,0,mCellWidth,mWeekHeight,0);
//			Log.e(TAG,"KDS3393_TEST_draw l = " + mWeekArray[x].mLeft + " t = " + mWeekArray[x].mTop + " r = " + mWeekArray[x].mRight + " b = " + mWeekArray[x].mBottom);
		}
		
		for (int y=0;y<mLineCount;y++) {	//Index
			top = bottom;
			bottom = top + mCellHeight;
			mIndexArray[y].setRect(0,mWeekHeight + top,mIndexWidth,mCellHeight,0);
		}
		left = 0;
		right = 0;
		for (int x=0;x<mWeekCount;x++) {
			left = right;
			right = left + mCellWidth;
			top = 0;
			bottom = 0;
			for (int y=0;y<mLineCount;y++) {
				top = bottom;
				bottom = top + mCellHeight;
				mCellArray[x][y].setRect(mIndexWidth + left,mWeekHeight + top,mCellWidth,mCellHeight,0);
//				Log.e(TAG,"KDS3393_TEST_draw l = " + mCellArray[x][y].mRect.left + " t = " + mCellArray[x][y].mRect.top + " r = " + mCellArray[x][y].mRect.right + " b = " + mCellArray[x][y].mRect.bottom);
			}
		}
	}
	
	public TimeTableItem contains(MotionEvent event) {
		for (int x=0;x<mWeekCount;x++) {
			for (int y=0;y<mLineCount;y++) {
				if (mCellArray[x][y].contains(event) != null) {
					return mCellArray[x][y];
				}
			}
		}
		return null;
	}
	
	@Override
	public void onClick(View v) {
		TimeTableItem item = (TimeTableItem) v.getTag();
		if (item != null) {
			mTimeTableView.onClickRect(item);
		}
	}
}