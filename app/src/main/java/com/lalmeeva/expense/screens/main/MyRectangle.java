package com.lalmeeva.expense.screens.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.lalmeeva.expense.R;

/**
 * Created by admin on 29.08.2017.
 */

public class MyRectangle extends View {
    private Paint paint;

    public MyRectangle(Context context) {
        super(context);
        init();
    }

    public MyRectangle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRectangle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyRectangle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect((getRight() - getLeft())/2 - getContext().getResources().getDimensionPixelOffset(R.dimen.my_rectangle_margin),
                (getBottom() - getTop())/2 - getContext().getResources().getDimensionPixelOffset(R.dimen.my_rectangle_margin),
                (getRight() - getLeft())/2 + getContext().getResources().getDimensionPixelOffset(R.dimen.my_rectangle_margin),
                (getBottom() - getTop())/2 + getContext().getResources().getDimensionPixelOffset(R.dimen.my_rectangle_margin),
                paint);
    }
}
