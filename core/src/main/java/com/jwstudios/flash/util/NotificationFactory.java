package com.jwstudios.flash.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * User: johnwright
 * Date: 4/2/14
 * Time: 10:52 PM
 */
public class NotificationFactory {
    private NotificationFactory() {
        // static factory
    }
    public static void standardNotification(final int message, String title, final Context context) {
        // no words yet, should popup with sync now alert dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void standardNotification(final String message, String title, final Context context) {
        // no words yet, should popup with sync now alert dialog
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void multipleChoicePopup(String title,
                                           CharSequence[] values,
                                           DialogInterface.OnClickListener onClickListener,
                                           Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(values, onClickListener);
        builder.show();
    }
}
