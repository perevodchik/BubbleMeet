package com.perevodchik.bubblemeet.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.perevodchik.bubblemeet.R;

public class Heart extends AppCompatImageView {
    private int rotate = 0;
    private int translateX = 0;
    private int translateY = 0;
    public Heart(Context context) {
        super(context);
    }

    public Heart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Heart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public void setTranslate(int tX, int tY) {
        this.translateX = tX;
        this.translateY = tY;
    }

    @SuppressLint({"DrawAllocation", "ResourceAsColor"})
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Path path = new Path();

        @SuppressLint("DrawAllocation") Paint paint = new Paint(Paint.EMBEDDED_BITMAP_TEXT_FLAG);

        paint.setShader(null);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        float width = getWidth() * 0.9f;
        float height = getHeight() * 0.9f;

        canvas.rotate(rotate);
        canvas.translate(translateX, translateY);

        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
        path.moveTo(width / 2, height / 5);

        path.cubicTo(5 * width / 14, 0,
                0, height / 15,
                width / 28, 2 * height / 5);

        path.cubicTo(width / 14, 2 * height / 3,
                3 * width / 7, 5 * height / 6,
                width / 2, height);

        path.cubicTo(4 * width / 7, 5 * height / 6,
                13 * width / 14, 2 * height / 3,
                27 * width / 28, 2 * height / 5);

        path.cubicTo(width, height / 15,
                9 * width / 14, 0,
                width / 2, height / 5);

        paint.setColor(getContext().getResources().getColor(R.color.colorBackground));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }
}
