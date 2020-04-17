package com.kds.just.timetable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

	public int mStrokeWidth = 0;
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

	public void setStrokeWidth(int w) {
		mStrokeWidth = w;
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

	void notifyDataSetChanged(int x, int y) {
		if (mAdapter.isCreate(mCellArray[x][y])) {
			if (mCellArray[x][y].mView == null) {
				View view = mCellArray[x][y].makeView(mTimeTableView);
			}
			mAdapter.setBind(mCellArray[x][y]);
		} else {
			mCellArray[x][y].removeView();
		}
	}

	void notifyDataSetChanged() {
		if (mAdapter != null) {
			if (mAdapter.getWeekCount() != mWeekCount ||
				mAdapter.getIndexCount() != mLineCount ) {
				//TODO 사용중인 View를 재활용할수 있는 방법 고민
				mWeekCount = mAdapter.getWeekCount() > 0?mAdapter.getWeekCount():7;
				mLineCount = mAdapter.getIndexCount() > 0?mAdapter.getIndexCount():12;
				removeViewAll();
				mWeekArray = null;
				mIndexArray = null;
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

	private void removeViewAll() {
		if (mWeekArray != null) {
			for (TimeTableItem item:mWeekArray) {
				item.removeView();
			}
		}
		if (mIndexArray != null) {
			for (TimeTableItem item:mIndexArray) {
				item.removeView();
			}
		}
		if (mCellArray != null) {
			for (int x=0;x<mCellArray.length;x++) {
				for (int y=0;y<mCellArray[x].length;y++) {
					mCellArray[x][y].removeView();
				}
			}
		}
		mWeekArray = null;
		mIndexArray = null;
		mCellArray = null;
	}

	public int setViewSize(int w) {
		mWidth = w;
        if (mWeekCount == 0) {
            return 0;
        }
		mCellWidth = (mWidth - mIndexWidth) / mWeekCount;

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
			mWeekArray[x].setRect(mIndexWidth + left,0,mCellWidth,mWeekHeight);
			mWeekArray[x].setLayout();
		}
		
		for (int y=0;y<mLineCount;y++) {	//Index
			top = bottom;
			bottom = top + mCellHeight;
			mIndexArray[y].setRect(0,mWeekHeight + top,mIndexWidth,mCellHeight);
			mIndexArray[y].setLayout();
		}
		right = 0;
		int cellWidth = 0;
		int cellHeight = 0;
		for (int x=0;x<mWeekCount;x++) {
			left = right;
			right = left + mCellWidth;
			bottom = 0;
			if (x == mWeekCount - 1) {
				cellWidth = mWidth - (mIndexWidth + left + (mStrokeWidth / 2));
			} else {
				cellWidth = mCellWidth;
			}
			for (int y=0;y<mLineCount;y++) {
				top = bottom;
				bottom = top + mCellHeight;
				if (y == mLineCount - 1) {
					cellHeight = mHeight - (mWeekHeight + top + (mStrokeWidth / 2));
				} else {
					cellHeight = mCellHeight;
				}
				mCellArray[x][y].setRect(mIndexWidth + left + (mStrokeWidth / 2),
										 mWeekHeight + top + (mStrokeWidth / 2),
										cellWidth - mStrokeWidth,
										cellHeight - mStrokeWidth);
				mCellArray[x][y].setLayout();
			}
		}
	}

	public void drawBackground(Canvas canvas, Paint bgPaint, Paint strokePaint) {

		canvas.drawRect(mIndexWidth, mWeekHeight, mWidth - (mStrokeWidth/2), mHeight - (mStrokeWidth/2), bgPaint);
		canvas.drawRect(mIndexWidth, mWeekHeight, mWidth - (mStrokeWidth/2), mHeight - (mStrokeWidth/2), strokePaint);

		int width = mWidth - mIndexWidth;
		int height = mHeight - mWeekHeight;
		int wOffset = width / mWeekCount;
		int hOffset = height / mLineCount;
		for (int x=1;x<mWeekCount;x++) {
			canvas.drawLine(mIndexWidth + (wOffset * x),mWeekHeight,mIndexWidth + (wOffset * x),mHeight,strokePaint);
		}
		for (int y=1;y<mLineCount;y++) {
			canvas.drawLine(mIndexWidth,mWeekHeight + hOffset * y,mWidth,mWeekHeight + hOffset * y,strokePaint);
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