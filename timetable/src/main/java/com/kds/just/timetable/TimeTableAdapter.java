package com.kds.just.timetable;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TimeTableAdapter<T1,T2,T3> {
    private static final String TAG = "TimeTableAdapter";

    private T1 cellData;
    private List<T2> weekData;
    private List<T3> indexData;

    public void setCellData(T1 data) {
        cellData = data;
        notifyDataSetChanged();
    }

    public T1 getCellData() {
        return cellData;
    }

    public void setWeekData(List<T2> data) {
        weekData = data;
        notifyDataSetChanged();
    }

    public List<T2> getWeekData() {
        return weekData;
    }

    public int getWeekCount() {
        return weekData!=null?weekData.size():0;
    }

    public void setIndexData(List<T3> data) {
        indexData = data;
        notifyDataSetChanged();
    }

    public List<T3> getIndexData() {
        return indexData;
    }

    public int getIndexCount() {
        return indexData!=null?indexData.size():0;
    }

    boolean isCreate(TimeTableItem tti) {
        if (tti.mType == TimeTableItem.TYPE_CELL) {
            if (cellData != null) {
                return isCreateCell(cellData,tti.mX,tti.mY);
            }
        }
        return false;
    }

    public boolean isCreateCell(T1 data, int x, int y) {
        return false;
    }

    void setBind(TimeTableItem tti) {
        if (tti.mType == TimeTableItem.TYPE_CELL) {
            if (tti.mView != null && cellData != null) {
                setCellBind(tti.mView,cellData,tti.mX,tti.mY);
            }
        } else if (tti.mType == TimeTableItem.TYPE_WEEK) {
            if (weekData != null) {
                setWeekBind(tti.mView,weekData.get(tti.mX),tti.mX);
            }
        } else if (tti.mType == TimeTableItem.TYPE_INDEX) {
            if (indexData != null) {
                setIndexBind(tti.mView,indexData.get(tti.mY),tti.mY);
            }
        }
    }

    protected void setCellBind(View v, T1 data, int x, int y) {

    }

    protected void setWeekBind(View v, T2 data, int position) {

    }

    protected void setIndexBind(View v, T3 data, int position) {

    }

    private TimeTableHelper mTimeTableHelper;
    public void setHelper(TimeTableHelper helper) {
        mTimeTableHelper = helper;
    }

    public void notifyDataSetChanged() {
        if (mTimeTableHelper != null) {
            mTimeTableHelper.notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged(int x, int y) {
        if (mTimeTableHelper != null) {
            mTimeTableHelper.notifyDataSetChanged(x,y);
        }
    }
}
