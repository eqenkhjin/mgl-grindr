package mn.eq.health4men.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import mn.eq.health4men.R;


public class Utils {

    public static String MAIN_HOST = "http://health4men.mn/service/";
//    public static String MAIN_HOST = "http://admin-pc:8080/1dor/";


    public Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static float density;
    public static AsyncHttpClient client = new AsyncHttpClient();
    private static final int CONNECTION_TIMEOUT = 10000;
    public static int PAGINATION_COUNT = 50;

    public Utils() {
    }

    public Utils(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("NegDor", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        density = context.getResources().getDisplayMetrics().density;
        client.setTimeout(CONNECTION_TIMEOUT);
        client.setMaxRetriesAndTimeout(1, CONNECTION_TIMEOUT);
    }

    public static Typeface setCustomFont(Context context, String path) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);

        return typeface;
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    public static ProgressDialog getProgressDialog(Context context) {
        return getProgressDialog(context, "Уншиж байна");
    }

    public static ProgressBar getProgressBar(Context context) {
        ProgressBar progressBar = new ProgressBar(context);
        return progressBar;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

    @SuppressLint("NewApi")
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public String formatUne(String text) {
        String retString = "";
        int beginIndex = 0, length, sum;
        length = text.length();
        while (beginIndex < text.length()) {
            if (length % 3 == 0) {
                sum = 3;
            } else {
                sum = length % 3;
            }
            retString = retString + text.substring(beginIndex, beginIndex + sum);
            retString = retString + "'";
            length = length - sum;
            beginIndex = beginIndex + sum;
        }
        return retString.substring(0,retString.length() - 1);
    }

    public void showToast(String title){
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    public void showNoInternetAlert(Context context){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name_mn))
                .setMessage(context.getString(R.string.no_internet))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.alert_logo)
                .show();
    }

    public void showAlert(Context context,String text){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name_mn))
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.alert_logo)
                .show();
    }
}