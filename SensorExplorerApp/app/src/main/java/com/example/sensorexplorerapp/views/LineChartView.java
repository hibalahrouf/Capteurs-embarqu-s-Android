package com.example.sensorexplorerapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {

    private static final int MAX_SAMPLES = 90;

    private final List<Float> samples = new ArrayList<>();
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String label = "Live signal";

    public LineChartView(Context context) {
        super(context);
        setMinimumHeight(dp(220));
        gridPaint.setColor(Color.rgb(210, 216, 226));
        gridPaint.setStrokeWidth(dp(1));

        linePaint.setColor(Color.rgb(25, 118, 210));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dp(3));

        labelPaint.setColor(Color.rgb(92, 102, 120));
        labelPaint.setTextSize(dp(12));
    }

    public void setChartLabel(String label) {
        this.label = label;
        invalidate();
    }

    public void pushSample(float value) {
        samples.add(value);
        while (samples.size() > MAX_SAMPLES) {
            samples.remove(0);
        }
        invalidate();
    }

    public void clearSamples() {
        samples.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int left = dp(16);
        int top = dp(18);
        int right = width - dp(16);
        int bottom = height - dp(26);

        canvas.drawColor(Color.WHITE);
        for (int i = 0; i <= 4; i++) {
            float y = top + (bottom - top) * i / 4f;
            canvas.drawLine(left, y, right, y, gridPaint);
        }
        canvas.drawText(label, left, height - dp(8), labelPaint);

        if (samples.size() < 2) {
            canvas.drawText("Waiting for samples...", left, top + dp(30), labelPaint);
            return;
        }

        float min = samples.get(0);
        float max = samples.get(0);
        for (float value : samples) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        if (Math.abs(max - min) < 0.01f) {
            max += 1f;
            min -= 1f;
        }

        Path path = new Path();
        for (int i = 0; i < samples.size(); i++) {
            float x = left + (right - left) * i / (float) (MAX_SAMPLES - 1);
            float normalized = (samples.get(i) - min) / (max - min);
            float y = bottom - normalized * (bottom - top);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, linePaint);
        canvas.drawText(String.format("min %.2f   max %.2f", min, max), left, top + dp(14), labelPaint);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
