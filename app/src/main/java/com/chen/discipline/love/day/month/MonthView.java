package com.chen.discipline.love.day.month;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chen.discipline.love.day.R;
import com.chen.discipline.love.day.utils.CalendarUtils;
import com.chen.discipline.love.day.utils.LunarCalendarUtils;

import java.util.Calendar;

/**
 * Created by Jimmy on 2016/10/6 0006.
 */
public class MonthView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mPaint;
    private Paint mLunarPaint;
    private Paint mTypePaint;
    private Paint mLinePaint;
    private int mNormalDayColor;
    private int mSelectDayColor;
    private int mSelectBGColor;
    private int mSelectBGTodayColor;
    private int mCurrentDayColor;
    private int mHintCircleColor;
    private int mLunarTextColor;
    private int mHolidayTextColor;
    private int mLastOrNextMonthTextColor;
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int mDaySize;
    private int mLunarTextSize;
    private int mWeekRow; // 当前月份第几周
    private int mCircleRadius = 6;
    private int[][] mDaysText;
    private int[] mHolidays;
    private String[][] mHolidayOrLunarText;
    //是否显示农历
    private boolean mIsShowLunar;
    //是否显示节假日
    private boolean mIsShowHolidayHint;
    private DisplayMetrics mDisplayMetrics;
    private OnMonthClickListener mDateClickListener;
    private GestureDetector mGestureDetector;
    private Bitmap mRestBitmap, mWorkBitmap;
    private Context mContext;
    public MonthView(Context context, int year, int month) {
        this(context, null, year, month);
    }

    public MonthView(Context context, TypedArray array, int year, int month) {
        this(context, array, null, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int year, int month) {
        this(context, array, attrs, 0, year, month);
    }

    public MonthView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, int year, int month) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(array, year, month);
        initPaint();
        initMonth();
        initGestureDetector();
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    private void initAttrs(TypedArray array, int year, int month) {
        if (array != null) {
            mSelectDayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_color, Color.parseColor("#E8E8E8"));
            mSelectBGTodayColor = array.getColor(R.styleable.MonthCalendarView_month_selected_circle_today_color, Color.parseColor("#FF8594"));
            mNormalDayColor = array.getColor(R.styleable.MonthCalendarView_month_normal_text_color, Color.parseColor("#575471"));
            mCurrentDayColor = array.getColor(R.styleable.MonthCalendarView_month_today_text_color, Color.parseColor("#FF8594"));
            mHintCircleColor = array.getColor(R.styleable.MonthCalendarView_month_hint_circle_color, Color.parseColor("#FE8595"));
            mLastOrNextMonthTextColor = array.getColor(R.styleable.MonthCalendarView_month_last_or_next_month_text_color, Color.parseColor("#ACA9BC"));
            mLunarTextColor = array.getColor(R.styleable.MonthCalendarView_month_lunar_text_color, Color.parseColor("#ACA9BC"));
            mHolidayTextColor = array.getColor(R.styleable.MonthCalendarView_month_holiday_color, Color.parseColor("#A68BFF"));
            mDaySize = array.getInteger(R.styleable.MonthCalendarView_month_day_text_size, 13);
            mLunarTextSize = array.getInteger(R.styleable.MonthCalendarView_month_day_lunar_text_size, 8);
            mIsShowLunar = array.getBoolean(R.styleable.MonthCalendarView_month_show_lunar, true);
            mIsShowHolidayHint = array.getBoolean(R.styleable.MonthCalendarView_month_show_holiday_hint, true);
        } else {
            mSelectDayColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#E8E8E8");
            mSelectBGTodayColor = Color.parseColor("#FF8594");
            mNormalDayColor = Color.parseColor("#575471");
            mCurrentDayColor = Color.parseColor("#FF8594");
            mHintCircleColor = Color.parseColor("#FE8595");
            mLastOrNextMonthTextColor = Color.parseColor("#ACA9BC");
            mHolidayTextColor = Color.parseColor("#A68BFF");
            mDaySize = 13;
            mLunarTextSize = 8;
            mIsShowLunar = true;
            mIsShowHolidayHint = true;
        }
        mSelYear = year;
        mSelMonth = month;
//        mRestBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rest_day);
//        mWorkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_work_day);
        mHolidays = CalendarUtils.getInstance(getContext()).getHolidays(mSelYear, mSelMonth + 1);
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();
        float textSize = mDaySize * mDisplayMetrics.scaledDensity;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(textSize);

        mLunarPaint = new Paint();
        mLunarPaint.setAntiAlias(true);
        mLunarPaint.setTextSize(textSize);
        mLunarPaint.setColor(mLunarTextColor);

        mTypePaint = new Paint();
        mTypePaint.setAntiAlias(true);
        mTypePaint.setTextSize(textSize);
        mTypePaint.setTextAlign(Paint.Align.CENTER);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#EEEEEE"));
        mLinePaint.setStrokeWidth((float) 2.0);
    }

    private void initMonth() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        if (mSelYear == mCurrYear && mSelMonth == mCurrMonth) {
            setSelectYearMonth(mSelYear, mSelMonth, mCurrDay);
        } else {
//            setSelectYearMonth(mSelYear, mSelMonth, 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        clearData();
        drawLins(canvas);
        drawLastMonth(canvas);
        int selected[] = drawThisMonth(canvas);
        drawNextMonth(canvas);
        drawLunarText(canvas, selected);
//        drawHoliday(canvas);
    }

    private void drawLins(Canvas canvas){
        for (int i = 0; i < NUM_COLUMNS; i++){
            canvas.drawLine(mColumnSize * i,0,mColumnSize * i,getHeight(),mLinePaint);
        }
        for (int i = 0; i <NUM_ROWS; i++){
            canvas.drawLine(0,mRowSize * i,getWidth(),mRowSize * i,mLinePaint);
        }
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
        mSelectCircleSize = (int) (mColumnSize / 3.5);
        while (mSelectCircleSize > mRowSize / 2) {
            mSelectCircleSize = (int) (mSelectCircleSize / 1.3);
        }
    }

    private void clearData() {
        mDaysText = new int[6][7];
        mHolidayOrLunarText = new String[6][7];
    }

    private void drawLastMonth(Canvas canvas) {
        int lastYear, lastMonth;
        if (mSelMonth == 0) {
            lastYear = mSelYear - 1;
            lastMonth = 11;
        } else {
            lastYear = mSelYear;
            lastMonth = mSelMonth - 1;
        }
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(lastYear, lastMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < weekNumber - 1; day++) {
            mDaysText[0][day] = monthDays - weekNumber + day + 2;
            String dayString = String.valueOf(mDaysText[0][day]);
            float centerX = (float) (mColumnSize * day + mColumnSize * 0.5);
            float centerY = (float) (mRowSize * 0.45);
            canvas.drawText(dayString, centerX, centerY, mPaint);
            mHolidayOrLunarText[0][day] = CalendarUtils.getHolidayFromSolar(lastYear, lastMonth, mDaysText[0][day]);
        }
    }

    private int[] drawThisMonth(Canvas canvas) {
        String dayString;
        int selectedPoint[] = new int[2];
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < monthDays; day++) {
            dayString = String.valueOf(day + 1);
            int col = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            mDaysText[row][col] = day + 1;
            float centerX = (float) (mColumnSize * col + mColumnSize * 0.5);
            float centerY = (float) (mRowSize * row + mRowSize * 0.45);
            if (dayString.equals(String.valueOf(mSelDay)) || (mCurrMonth == mSelMonth && dayString.equals(String.valueOf(mCurrDay)))) {
                int startRecX = mColumnSize * col;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize;
                int endRecY = (int)(startRecY + mRowSize * 0.75);
                if (mSelYear == mCurrYear && mCurrMonth == mSelMonth && day + 1 == mCurrDay) {
                    mPaint.setColor(mSelectBGTodayColor);
                } else {
                    mPaint.setColor(mSelectBGColor);
                }
                canvas.drawCircle((startRecX + endRecX) / 2, (startRecY + endRecY) / 2, mSelectCircleSize, mPaint);
                mWeekRow = row + 1;
            }
            if (dayString.equals(String.valueOf(mSelDay))) {
                selectedPoint[0] = row;
                selectedPoint[1] = col;
                mPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(String.valueOf(mCurrDay)) && mCurrDay != mSelDay && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayColor);
            } else {
                mPaint.setColor(mNormalDayColor);
            }
            if (dayString.equals("1")){
                String month = (mSelMonth +1) + "月";
                canvas.drawText(month, centerX, centerY - mRowSize * 0.3f, mPaint);
            }
            canvas.drawText(dayString, centerX, centerY, mPaint);
            mHolidayOrLunarText[row][col] = CalendarUtils.getHolidayFromSolar(mSelYear, mSelMonth, mDaysText[row][col]);
        }
        return selectedPoint;
    }

    private void drawNextMonth(Canvas canvas) {
        mPaint.setColor(mLastOrNextMonthTextColor);
        int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
        int nextMonthDays = 42 - monthDays - weekNumber + 1;
        int nextMonth = mSelMonth + 1;
        int nextYear = mSelYear;
        if (nextMonth == 12) {
            nextMonth = 0;
            nextYear += 1;
        }
        for (int day = 0; day < nextMonthDays; day++) {
            int column = (monthDays + weekNumber - 1 + day) % 7;
            int row = 5 - (nextMonthDays - day - 1) / 7;
            try {
                mDaysText[row][column] = day + 1;
                mHolidayOrLunarText[row][column] = CalendarUtils.getHolidayFromSolar(nextYear, nextMonth, mDaysText[row][column]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String dayString = String.valueOf(mDaysText[row][column]);
            float centerX = (float) (mColumnSize * column + mColumnSize * 0.5);
            float centerY = (float) (mRowSize * row + mRowSize * 0.45);
            canvas.drawText(dayString, centerX, centerY, mPaint);
        }
    }

    /**
     * 绘制农历
     *
     * @param canvas
     * @param selected
     */
    private void drawLunarText(Canvas canvas, int[] selected) {
        if (mIsShowLunar) {
            int firstYear, firstMonth, firstDay;
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            if (weekNumber == 1) {
                firstYear = mSelYear;
                firstMonth = mSelMonth + 1;
                firstDay = 1;
            } else {
                int monthDays;
                if (mSelMonth == 0) {
                    firstYear = mSelYear - 1;
                    firstMonth = 11;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = 12;
                } else {
                    firstYear = mSelYear;
                    firstMonth = mSelMonth - 1;
                    monthDays = CalendarUtils.getMonthDays(firstYear, firstMonth);
                    firstMonth = mSelMonth;
                }
                firstDay = monthDays - weekNumber + 2;
            }
            LunarCalendarUtils.Lunar lunar = LunarCalendarUtils.solarToLunar(new LunarCalendarUtils.Solar(firstYear, firstMonth, firstDay));
            int days;
            int day = lunar.lunarDay;
            int leapMonth = LunarCalendarUtils.leapMonth(lunar.lunarYear);
            days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
            for (int i = 0; i < 42; i++) {
                int column = i % 7;
                int row = i / 7;
                if (day > days) {
                    day = 1;
                    if (lunar.lunarMonth == 12) {
                        lunar.lunarMonth = 1;
                        lunar.lunarYear = lunar.lunarYear + 1;
                    }
                    if (lunar.lunarMonth == leapMonth) {
                        days = LunarCalendarUtils.daysInMonth(lunar.lunarYear, lunar.lunarMonth, lunar.isLeap);
                    } else {
                        lunar.lunarMonth++;
                        days = LunarCalendarUtils.daysInLunarMonth(lunar.lunarYear, lunar.lunarMonth);
                    }
                }
                if (row == 0 && mDaysText[row][column] >= 23 || row >= 4 && mDaysText[row][column] <= 14) {
                    mLunarPaint.setColor(mLunarTextColor);
                } else {
                    mLunarPaint.setColor(mHolidayTextColor);
                }
                String dayString = mHolidayOrLunarText[row][column];
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, day);
                }
                if ("".equals(dayString)) {
                    dayString = LunarCalendarUtils.getLunarDayString(day);
                    mLunarPaint.setColor(mLunarTextColor);
                }
                if (selected[0] == row && selected[1] == column) {
                    mLunarPaint.setColor(mSelectDayColor);
                }
                int startX = (int) (mColumnSize * column + (mColumnSize - mLunarPaint.measureText(dayString)) / 2);
                int startY = (int) (mRowSize * row + mRowSize * 0.82 - (mLunarPaint.ascent() + mLunarPaint.descent()) / 2);
                if (dayString.equals("初一")){
                    String month = (mSelMonth +1) + "月";
                    canvas.drawText(month, startX + 1, startY - mRowSize * 0.2f, mLunarPaint);
                }
                canvas.drawText(dayString, startX, startY, mLunarPaint);
                day++;
            }
        }
    }

    private void drawHoliday(Canvas canvas) {
        if (mIsShowHolidayHint) {
            Rect rect = new Rect(0, 0, mRestBitmap.getWidth(), mRestBitmap.getHeight());
            Rect rectF = new Rect();
            int distance = (int) (mSelectCircleSize / 2.5);
            for (int i = 0; i < mHolidays.length; i++) {
                int column = i % 7;
                int row = i / 7;
                rectF.set(mColumnSize * (column + 1) - mRestBitmap.getWidth() - distance, mRowSize * row + distance, mColumnSize * (column + 1) - distance, mRowSize * row + mRestBitmap.getHeight() + distance);
                if (mHolidays[i] == 1) {
                    canvas.drawBitmap(mRestBitmap, rect, rectF, null);
                } else if (mHolidays[i] == 2) {
                    canvas.drawBitmap(mWorkBitmap, rect, rectF, null);
                }
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight())
            return;
        int row = y / mRowSize;
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        int clickYear = mSelYear, clickMonth = mSelMonth;
        if (row == 0) {
            if (mDaysText[row][column] >= 23) {
                if (mSelMonth == 0) {
                    clickYear = mSelYear - 1;
                    clickMonth = 11;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth - 1;
                }
                if (mDateClickListener != null) {
                    mDateClickListener.onClickLastMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        } else {
            int monthDays = CalendarUtils.getMonthDays(mSelYear, mSelMonth);
            int weekNumber = CalendarUtils.getFirstDayWeek(mSelYear, mSelMonth);
            int nextMonthDays = 42 - monthDays - weekNumber + 1;
            if (mDaysText[row][column] <= nextMonthDays && row >= 4) {
                if (mSelMonth == 11) {
                    clickYear = mSelYear + 1;
                    clickMonth = 0;
                } else {
                    clickYear = mSelYear;
                    clickMonth = mSelMonth + 1;
                }
                if (mDateClickListener != null) {
                    mDateClickListener.onClickNextMonth(clickYear, clickMonth, mDaysText[row][column]);
                }
            } else {
                clickThisMonth(clickYear, clickMonth, mDaysText[row][column]);
            }
        }
    }

    /**
     * 跳转到某日期
     *
     * @param year
     * @param month
     * @param day
     */
    public void clickThisMonth(int year, int month, int day) {
        if (mDateClickListener != null) {
            mDateClickListener.onClickThisMonth(year, month, day);
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取当前选择年
     *
     * @return
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     *
     * @return
     */
    public int getSelectMonth() {
        return mSelMonth;
    }

    /**
     * 获取当前选择日
     *
     * @return
     */
    public int getSelectDay() {
        return this.mSelDay;
    }

    public int getRowSize() {
        return mRowSize;
    }

    public int getWeekRow() {
        return mWeekRow;
    }

    /**
     * 设置点击日期监听
     *
     * @param dateClickListener
     */
    public void setOnDateClickListener(OnMonthClickListener dateClickListener) {
        this.mDateClickListener = dateClickListener;
    }
}

