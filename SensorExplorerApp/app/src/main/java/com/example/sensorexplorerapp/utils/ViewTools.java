package com.example.sensorexplorerapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sensorexplorerapp.R;

public final class ViewTools {

    private ViewTools() {
    }

    public static ScrollView page(Context context) {
        ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        LinearLayout content = stack(context);
        content.setPadding(dp(context, 18), dp(context, 18), dp(context, 18), dp(context, 24));
        scrollView.addView(content);
        return scrollView;
    }

    public static LinearLayout stack(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return layout;
    }

    public static TextView heading(Context context, String text) {
        TextView view = text(context, text, 20, true);
        view.setPadding(0, 0, 0, dp(context, 8));
        return view;
    }

    public static TextView body(Context context, String text) {
        TextView view = text(context, text, 15, false);
        view.setLineSpacing(dp(context, 2), 1f);
        view.setPadding(0, dp(context, 4), 0, dp(context, 10));
        return view;
    }

    public static TextView panel(Context context, String text) {
        TextView view = body(context, text);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        view.setPadding(dp(context, 14), dp(context, 12), dp(context, 14), dp(context, 12));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, dp(context, 8), 0, dp(context, 12));
        view.setLayoutParams(params);
        return view;
    }

    public static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }

    private static TextView text(Context context, String text, int sp, boolean bold) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextSize(sp);
        view.setTextColor(ContextCompat.getColor(context, R.color.ink_primary));
        if (bold) {
            view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return view;
    }
}
