package com.jacquessmuts.bakingforudacity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Jacques Smuts on 2017/08/23.
 * Until they find a new home, random utils in here
 */

public class Util {

    public static int getDPI(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int)(metrics.density * 160f);
    }

    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean getConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void errorMessageInternet(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.error_message_title)
                .content(R.string.error_message_content)
                .positiveText(R.string.error_message_button)
                .show();

    }

}
