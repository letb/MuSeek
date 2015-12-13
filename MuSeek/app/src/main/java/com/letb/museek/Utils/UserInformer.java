package com.letb.museek.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.letb.museek.R;

/**
 * Created by eugene on 14.12.15.
 */
public class UserInformer {
    public static void showMessage (@NonNull final Activity activity, String message) {
        final View content = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (content == null)
            return;
        Snackbar snackbar = Snackbar.make(content, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#ECEC4C"));

//        Possible UI customization
        snackbar.show();
    }
}
