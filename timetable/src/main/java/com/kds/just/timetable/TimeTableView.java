package com.kds.just.timetable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.kds.just.timetable.utils.Utils;

public class TimeTableView extends ViewGroup implements GestureDetector.OnGestureListener {
	private static final String TAG = TimeTableView.class.getSimpleName();

	protected TimeTableHelper mHelper = null;

	private GestureDetector mGestureDetector;

	private OnCellClickListener mOnCellClickListener;

	private int mStrokeWidth = Utils.dp2px(0.33f);

	public interface OnCellClickListener {
		void OnClick(View v, int x, int y);
	}

	public void setOnCellClickListener(OnCellClickListener l) {
		mOnCellClickListener = l;
	}

	public TimeTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHelper = new TimeTableHelper(getContext());

//		        <attr name="weekCount" format="integer" />
//        <attr name="lineCount" format="integer" />

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeTableView);
		mHelper.setWeekCount(ta.getInt(R.styleable.TimeTableView_weekCount,7));
		mHelper.setLineCount(ta.getInt(R.styleable.TimeTableView_lineCount,10));

		mHelper.setCellHeight(ta.getDimensionPixelSize(R.styleable.TimeTableView_cellHeight,Utils.dp2px(30)));
		mHelper.setWeekHeight(ta.getDimensionPixelSize(R.styleable.TimeTableView_weekHeight,Utils.dp2px(74)));
		mHelper.setIndexWidth(ta.getDimensionPixelSize(R.styleable.TimeTableView_indexWidth,Utils.dp2px(27)));

		setWeekPaint(ta.getColor(R.styleable.TimeTableView_weekBGColor,Color.TRANSPARENT));
		setIndexPaint(ta.getColor(R.styleable.TimeTableView_indexBGColor,Color.TRANSPARENT));

		setCellBGPaint(ta.getColor(R.styleable.TimeTableView_cellBGColor,Color.TRANSPARENT));
		mStrokeWidth = ta.getDimensionPixelSize(R.styleable.TimeTableView_cellStrokeWidth,Utils.dp2px(0.33f));
		setCellStrokePaint(ta.getColor(R.styleable.TimeTableView_cellStrokeColor,Color.GRAY), mStrokeWidth);
		mHelper.setStrokeWidth(mStrokeWidth);

		int weekLayoutId = ta.getResourceId(R.styleable.TimeTableView_weekLayout,R.layout.timetable_view_cell);
		int indexLayoutId = ta.getResourceId(R.styleable.TimeTableView_indexLayout,R.layout.timetable_view_cell);
		int cellLayoutId = ta.getResourceId(R.styleable.TimeTableView_cellLayout,R.layout.timetable_view_cell);
		mHelper.setLayoutId(weekLayoutId, indexLayoutId, cellLayoutId);

		mGestureDetector = new GestureDetector(context, this);

		init();

	}

	private void init() {
		setWillNotDraw(false);
        mHelper.build(this);
        requestLayout();
        invalidate();
	}

	public void setAdapter(TimeTableAdapter adapter) {
		mHelper.setAdapter(adapter);
	}

	public TimeTableAdapter getAdapter() {
		return mHelper.getAdapter();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		TimeTableItem item = mHelper.contains(e);
		playSoundEffect(0);
		if (item != null) {
			onClickRect(item);
		}
		return false;
	}

	public void onClickRect(TimeTableItem item) {
		if (mOnCellClickListener != null) {
			mOnCellClickListener.OnClick(item.mView, item.mX,item.mY);
		}
	}

	public void setWeekPaint(int color) {
		mWeekPaint.setColor(color);
	}

	public void setIndexPaint(int color) {
		mIndexPaint.setColor(color);
	}

	public void setCellStrokePaint(int color, int strokeWidth) {
		mCellStrokePaint.setColor(color);
		mCellStrokePaint.setStyle(Paint.Style.STROKE);
		mCellStrokePaint.setStrokeWidth(strokeWidth);
		mCellStrokePaint.setAntiAlias(true);

	}

	public void setCellBGPaint(int color) {
		mCellBGPaint.setColor(color);
	}

	@Override
	public TimeTableView.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new TimeTableView.LayoutParams(getContext(), attrs);
	}

	protected TimeTableView.LayoutParams generateDefaultLayoutParams() {
		return new TimeTableView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);

		if (mHelper != null) {
			layoutHeight = mHelper.setViewSize(layoutWidth);
		}

		measureChildren(MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY));

		setMeasuredDimension(layoutWidth, layoutHeight);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getChildCount() <= 0) {
			return;
		}
		final int height = mHelper.setViewSize(r - l);
		if (height > 0) {
			getLayoutParams().height = height;
		}
		for (int x=0;x<mHelper.mIndexArray.length;x++) {
			mHelper.mIndexArray[x].setLayout();
		}

		for (int x=0;x<mHelper.mWeekArray.length;x++) {
			mHelper.mWeekArray[x].setLayout();
		}

		for (int x=0;x<mHelper.mWeekCount;x++) {
			for (int y=0;y<mHelper.mLineCount;y++) {
				mHelper.mCellArray[x][y].setLayout();
			}
		}

		if (height > 0 && getLayoutParams().height != height) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
			lp.height = height;
			return;
		}
	}


	@Override
	protected void onDraw(Canvas canvas) {
		drawTimeTable(canvas);

	}

	Paint mWeekPaint = new Paint();
	Paint mIndexPaint = new Paint();
	Paint mCellStrokePaint = new Paint();
	Paint mCellBGPaint = new Paint();
	private void drawTimeTable(Canvas canvas) {
		mHelper.mWeekArray[0].drawWeek(canvas, mWeekPaint, getWidth());

		for (int x=0;x<mHelper.mIndexArray.length;x++) {
			mHelper.mIndexArray[x].drawNormal(canvas, mIndexPaint);
		}

		mHelper.drawBackground(canvas,mCellBGPaint,mCellStrokePaint);
	}

	public static class LayoutParams extends FrameLayout.LayoutParams {
		public LayoutParams(int width, int height) {
			super(width, height);
		}
		public LayoutParams(@NonNull Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
			super(source);
		}

		public LayoutParams(@NonNull FrameLayout.LayoutParams source) {
			super(source);
		}

		public int mIndex = 0;
	}

}
