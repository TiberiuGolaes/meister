package com.nalatarate.meister.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;

import com.nalatarate.meister.R;

/**
 * Created by Tiberiu on 6/19/2016.
 */
public class CircularImageView extends MeasuredImageView{

    // Border configuration variables
    protected int borderWidth;
    protected final RectF borderRect;

    // Objects used for the actual drawing
    protected final Paint paintVector;
    protected final Paint paintBorder;
    protected final Paint paintBackground;

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CircularImageViewStyle_circularImageViewDefault);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Initialize paint objects

        paintVector = new Paint();
        paintVector.setAntiAlias(true);
        paintBackground = new Paint();
        paintBackground.setAntiAlias(true);
        paintBackground.setStyle(Paint.Style.FILL);

        // Load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyleAttr, 0);

        // Check for extra features being enabled
        boolean hasBorder = attributes.getBoolean(R.styleable.CircularImageView_civ_border, false);

        // Set border properties, if enabled
        if(hasBorder) {
            paintBorder = new Paint();
            paintBorder.setAntiAlias(true);
            paintBorder.setStyle(Paint.Style.STROKE);
            int defaultBorderSize = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
            setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircularImageView_civ_borderWidth, defaultBorderSize));
            setBorderColor(attributes.getColor(R.styleable.CircularImageView_civ_borderColor, Color.WHITE));
            borderRect = new RectF();
        } else {
            paintBorder = null;
            borderRect = null;
        }

        setBackgroundColor(attributes.getColor(R.styleable.CircularImageView_civ_backgroundColor, Color.BLACK));

        // We no longer need our attributes TypedArray, give it back to cache
        attributes.recycle();
    }

    /**
     * Sets the CircularImageView's border width in pixels.
     * @param borderWidth Width in pixels for the border.
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        if(paintBorder != null) paintBorder.setStrokeWidth(borderWidth);
        requestLayout();
        invalidate();
    }

    /**
     * Sets the CircularImageView's basic border color.
     * @param borderColor The new color (including alpha) to set the border.
     */
    public void setBorderColor(int borderColor) {
        if (paintBorder != null) paintBorder.setColor(borderColor);
        this.invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (paintBackground != null) paintBackground.setColor(backgroundColor);
        this.invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);

    }

    @Override
    public void onDraw(Canvas canvas) {
        int canvasSize = Math.min(getWidth(), getHeight());
        setCanvasSize(canvasSize);

        // Get the exact X/Y axis of the view
        int center = canvasSize / 2;

        canvas.drawCircle(center, center, center, paintBackground);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getDrawable();
            if (d instanceof VectorDrawable) {
                Bitmap image = drawableToBitmap(d);
                BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                if (canvasSize != image.getWidth() || canvasSize != image.getHeight()) {
                    Matrix matrix = new Matrix();
                    float scale = (float) canvasSize / (float) image.getWidth();
                    matrix.setScale(scale, scale);
                    shader.setLocalMatrix(matrix);
                }
                paintVector.setShader(shader);
                canvas.drawCircle(center, center, center, paintVector);
            } else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
        if (borderRect != null) { // If no selector was drawn, draw a border and clear the filter instead... if enabled
            int outerWidth = borderWidth/2;

            borderRect.set(outerWidth, outerWidth, canvasSize - outerWidth, canvasSize - outerWidth);
            canvas.drawArc(borderRect, 360, 360, false, paintBorder);
        }

    }

}