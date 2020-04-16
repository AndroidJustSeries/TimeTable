package com.kds.just.timetable.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kds.just.timetable.TimeTableAdapter;
import com.kds.just.timetable.TimeTableView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TimeAdapter mTimeAdapter;
    ColorDrawable validColor = new ColorDrawable(Color.WHITE);
    ColorDrawable selectedColor = new ColorDrawable(Color.parseColor("#ffdf88"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TimeTableView timeTableView = findViewById(R.id.timetableview);

        mTimeAdapter = new TimeAdapter();
        timeTableView.setAdapter(mTimeAdapter);
        setData();

        timeTableView.setOnCellClickListener(new TimeTableView.OnCellClickListener() {
            @Override
            public void OnClick(View v, int x, int y) {
                if (v != null) {
                    if ('1' == mTimeAdapter.getCellData()[x].charAt(y)) {
                        if (v.getBackground() == selectedColor) {
                            v.setBackground(validColor);
                        } else {
                            v.setBackground(selectedColor);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this,"[" + x + "," + y + "] " + mTimeAdapter.getCellData()[x].charAt(y), Toast.LENGTH_SHORT).show();
                    StringBuilder build = new StringBuilder(mTimeAdapter.getCellData()[x]);
                    build.setCharAt(y, '1');
                    mTimeAdapter.getCellData()[x] = build.toString();
                    mTimeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setData() {
        ArrayList<TTWeekData> weeks = new ArrayList<>();
        weeks.add(new TTWeekData(0,14));
        weeks.add(new TTWeekData(1,15));
        weeks.add(new TTWeekData(2,16));
        weeks.add(new TTWeekData(3,17));
        weeks.add(new TTWeekData(4,18));
        weeks.add(new TTWeekData(5,19));
        weeks.add(new TTWeekData(6,20));
        mTimeAdapter.setWeekData(weeks);

        ArrayList<String> times = new ArrayList<>();
        for (int i=12;i<24;i++) {
            if (i > 9) {
                times.add(i + ":00");
            } else {
                times.add("0" + i + ":00");
            }
        }

        mTimeAdapter.setIndexData(times);

        String[] amCellData = {
            "000100000001",
            "000000000101",
            "000000100001",
            "000000010001",
            "000000001001",
            "000010000001",
            "001000000001"
        };
        String[] pmCellData = {
                "101111100000",
                "110111100000",
                "111111100000",
                "101110000000",
                "101111100000",
                "111011100000",
                "111011100000"
        };
        mTimeAdapter.setCellData(amCellData);
    }


    public class TimeAdapter extends TimeTableAdapter<String[], TTWeekData, String> {

        @Override
        public boolean isCreateCell(String[] data, int x, int y) {
            return '0' != data[x].charAt(y);
        }

        @Override
        protected void setCellBind(View v, String[] data, int x, int y) {
            TextView tv = (TextView) v;
            tv.setText(x + "," + y + "\n" + data[x].charAt(y));
            if ('1' == data[x].charAt(y)) {
                tv.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        protected void setWeekBind(View v, TTWeekData data, int position) {
            ((TextView)v.findViewById(R.id.timetable_week)).setText(data.weekDay);
            TextView date = (TextView)v.findViewById(R.id.timetable_week_date);
            date.setText(String.valueOf(data.day));
            if (position == 0) {
                date.setTextColor(Color.parseColor("#e02020"));
            } else if (position == 6) {
                date.setTextColor(Color.parseColor("#0091ff"));
            }
        }

        @Override
        protected  void setIndexBind(View v, String data, int position) {
            ((TextView)v).setText(data);
        }
    }

    public static final String WEEK[] = {
            "일","월","화","수","목","금","토"
    };

    public class TTWeekData {
        int weekValue;
        String weekDay;
        int day;

        public TTWeekData(int weekValue,int day) {
            this.weekValue = weekValue;
            this.weekDay = WEEK[weekValue];
            this.day = day;
        }
    }
}
