package com.surajbokankar.commonutil.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surajbokankar.commonutil.MainActivity;
import com.surajbokankar.commonutil.R;

import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suraj.bokankar on 4/8/17.
 */

public class CommonUtil<T> {

    private static final String TAG = "CommonUtil";
    public static CommonUtil commonUtil = null;
    ProgressDialog myProgressDialog = null;

    public static CommonUtil getInstance() {
        if (commonUtil == null) {
            commonUtil = new CommonUtil();
        }
        return commonUtil;
    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    public static DatePickerDialog createDialogWithoutDateField(Context context) {
        DatePickerDialog dpd = new DatePickerDialog(context, null, 2017, 1, 12);


        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);

                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    public static boolean isEmailValid(Context context, String emailId) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailId;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;

    }

    public static void showToast(Context context, String message) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyBoard(Context context, View view) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static long getDateDiff(long timeUpdate, long timeNow, TimeUnit timeUnit) {
        long diffInMillies = Math.abs(timeNow - timeUpdate);
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String compareTwoTimeStamps(Timestamp currentTime, Timestamp oldTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffTime, minuteTime = 0, hourTime = 0;
        String suffix = null;
        diffTime = diff / 1000;
        //diffTime=0;
        suffix = "Just Now";

        if (diffTime > 60) {
            minuteTime = diff / (60 * 1000);
            diffTime = minuteTime;
            suffix = "minutes ago";
        }

        if (minuteTime > 60) {
            hourTime = diff / (60 * 60 * 1000);
            diffTime = hourTime;
            suffix = "hours ago";
        }

        if (hourTime > 24) {
            diffTime = diff / (24 * 60 * 60 * 1000);
            if (diffTime > 7) {

                float week = diffTime / 7;
                diffTime = Math.round(week);
                suffix = "weeks ago";

            } else {
                suffix = "days ago";
            }

        }

//diffMinutes
        String time = null;
        if (diffTime == 0) {
            time = suffix;
        } else {
            time = diffTime + " " + suffix;
        }
        return time;
    }

    public static String getBottomValue(String bottomVal) {
        if (bottomVal.length() > 50)
            bottomVal.substring(0, 50);
        return bottomVal;
    }

    public static void showSnackBar(Context context, String message, View view) {

        if(context!=null&&view!=null){
            Snackbar snackbar = Snackbar
                    .make(view, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

    }

    public Typeface getCustomFont(Context context, String assestFontName) {
        Typeface mTfRegular = null;

        return mTfRegular;
    }

    public SpannableString generateCenterSpannableText(Context context,String name) {
        SpannableString s = new SpannableString(name);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_2DCC70)), s.length() - 14, s.length(), 0);
        return s;
    }

    public int[] getColors() {
        int[] colors = {rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")};
        return colors;
    }

    public ObjectMapper getMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;

    }

    public void finishActivity(Context context) {
        Activity activity = (Activity) context;
        activity.finish();
    }

    public void changeMarginsDynamically(View view, int left, int right, int top, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(marginLayoutParams);
    }

    public void hideProgressBar() {
        if (myProgressDialog != null)
            myProgressDialog.dismiss();
    }

    public LinearLayoutManager getHorizontalLayoutManager(Context context) {
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        return horizontalLayoutManagaer;
    }

    public LinearLayoutManager getVerticalLayoutManager(Context context) {
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        return horizontalLayoutManagaer;
    }

    public GridLayoutManager getGridLayoutManager(Context context, int numberOfRow) {
        return new GridLayoutManager(context, numberOfRow);
    }

    public DatePickerDialog creatMonthPicker(Context context) {

        // Get Current Date


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);

                            //txtdate.setText(formatter.format(date).toString());

                            formatter = new SimpleDateFormat("dd/MMM/yyyy");

                            // txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                            formatter = new SimpleDateFormat("dd-MM-yyyy");

                            //txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                            formatter = new SimpleDateFormat("dd.MMM.yyyy");

//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());

                        } catch (Exception ex) {

                        }


                    }
                }, year, month, day);

        return dd;

    }

    public void getDate(long dateValue, DatePicker datePicker) {
        dateValue = dateValue * 1000;
        Timestamp timestamp = new Timestamp(dateValue);
        Date date = new Date(timestamp.getTime());
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        Log.i(TAG, "getDate: Date=" + dateValue);

    }

    public void getEndDate(long dateValue, DatePicker datePicker) {
        dateValue = dateValue * 1000;
        Timestamp timestamp = new Timestamp(dateValue);
        Date date = new Date(timestamp.getTime());
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));


        Log.i(TAG, "getDate: Date=" + dateValue);

    }

    public String getRangeValue(long startDate, long endDate) {
        StringBuilder startBuilder = new StringBuilder();
        StringBuilder endBuilder = new StringBuilder();


        endDate = endDate * 1000;
        startDate = startDate * 1000;

        Format formatter = new SimpleDateFormat("MMM, yyyy");

        Timestamp startTimStamp = new Timestamp(startDate);

        Timestamp endTimStamp = new Timestamp(endDate);

        Date from = new Date(startTimStamp.getTime());

        Date To = new Date(endTimStamp.getTime());
        String starting = formatter.format(from);
        String ending = formatter.format(To);

        String rangeValue = starting + "-" + ending;

        return rangeValue;

    }

    public void changeStatusBar(Context context, int color, boolean shouldChangeStatusBarTintToDark, int type) {
        if (Build.VERSION.SDK_INT >= 21) {

            Activity activity = (Activity) context;
            Window window = activity.getWindow();
            View view = window.getDecorView();
            if (shouldChangeStatusBarTintToDark)
                view.setSystemUiVisibility(view.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //
            else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                view.setSystemUiVisibility(0);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public String getUserName(String name) {
        String value = "";
        if (!TextUtils.isEmpty(name)) {
            String[] listOfName = name.split(" ");
            StringBuilder builder = new StringBuilder();
            if (listOfName.length >= 2) {
                String charSequence = listOfName[0].substring(0, 1);
                String lastChar = (listOfName[listOfName.length - 1]).substring(0, 1);
                builder = builder.append(charSequence).append(lastChar);
                value = builder.toString();
            } else if (listOfName.length == 1) {
                String str = listOfName[0];
                String firstChar = listOfName[0].substring(0, 1);
                String lastChar = str.substring((str.length()) - 1, (str.length()));
                builder.append(firstChar).append(lastChar);
                value = builder.toString();
            }
        }
        return value.toUpperCase();
    }

    public void changePasswordStyle(AppCompatEditText input) {

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    public String getFormatedDate(long timeStamp, String format) {
        StringBuilder stringBuilder = new StringBuilder();

        timeStamp = timeStamp * 1000;
        Date from = new Date(timeStamp);
        Calendar cal = new GregorianCalendar();
        cal.setTime(from);
        Format formatter = new SimpleDateFormat(format);
        String today = formatter.format(from);
        stringBuilder.append(today);

        return today;
    }

    public String getFormattedDate(String stringDate, String currentFormat, String expectedFormat) {
        String dateString = null;

        SimpleDateFormat format1 = new SimpleDateFormat(currentFormat);
        SimpleDateFormat format2 = new SimpleDateFormat(expectedFormat);
        Date date = null;
        try {
            date = format1.parse(stringDate);
            System.out.println(format2.format(date));
            dateString = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public String lastCharSkip(String demoString, String charString) {

        if (demoString.endsWith(charString)) {
            demoString = demoString.substring(0, demoString.length() - 1);
        }
        return demoString;
    }

    public String getPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator;
        return path;
    }

    public void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
if(count>1000){
            count=998;
        }


        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        //intent.putExtra("badge_count", "100+");
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public String getTimeFromStamp(Long timeStamp) {
        long timestamp2 = System.currentTimeMillis();
        Timestamp oldTime = new Timestamp(timeStamp);
        Timestamp currentTime = new Timestamp(timestamp2);
        String time = compareTwoTimeStamps(currentTime, oldTime);
        return time;
    }

    public void setCount(AppCompatTextView countView, String count) {
        if (count != null && Integer.valueOf(count) != 0) {
            countView.setVisibility(View.VISIBLE);
            countView.setText(" ");
            countView.setText(count);
        } else {
            countView.setVisibility(View.GONE);
        }
    }

    public String toCamelCase(final String init) {
        final StringBuilder ret = new StringBuilder(init.length());
        try {
            if (init == null)
                return null;

            for (final String word : init.split(" ")) {
                if (!word.isEmpty()) {
                    ret.append(word.substring(0, 1).toUpperCase());
                    ret.append(word.substring(1).toLowerCase());
                }
                if (!(ret.length() == init.length()))
                    ret.append(" ");
            }
        } catch (Exception e) {
            Log.i(TAG, "toCamelCase: Eror=" + e.getMessage());
        }


        return ret.toString();
    }

    public String convertListToString(ArrayList<T> list) {
        Gson gson = new Gson();
        String response = gson.toJson(list);
        return response;
    }

    public ArrayList<T> getListFromString(String input) {
        Gson gson = new Gson();
        ArrayList<T> list = null;
        list = gson.fromJson(input, new TypeToken<ArrayList<T>>() {
        }.getType());
        return list;
    }

    public void addSwipeTheme(SwipeRefreshLayout swipeLayout, Integer color){
        swipeLayout.setColorSchemeResources(color,color,color,color);
    }



    public Date setDefaultDate(int year,int month,int day){
        Date initialDate = new Date();
        initialDate.setYear(year);
        initialDate.setMonth(month);
        initialDate.setDate(day);
        return initialDate;
    }


    public Date setStartDate(int year){
        Date startDate = new Date();
        startDate.setYear(year);
        startDate.setMonth(1);
        startDate.setDate(1);
        return startDate;
    }

    public Date setEndDate(int year){
        Date endDate = new Date();
        endDate.setYear(year);
        endDate.setMonth(1);
        endDate.setDate(1);
        return endDate;
    }


    public long getDateFromString(String startDateString){
        long dateValue=0;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate;
        try {
            startDate = df.parse(startDateString);
            dateValue=startDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateValue;
    }

    public  void showProgressBar(Context context){
        if(context!=null){
            myProgressDialog = ProgressDialog.show(context, null, null, true, false);
            myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            myProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            myProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            myProgressDialog.setCancelable(true);
            myProgressDialog.setIndeterminate(true);
            myProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            myProgressDialog.setContentView(R.layout.layout_progress);  //R.layout.layout_progress_dialog
            myProgressDialog.show();
        }

    }




    public String getUrl(String url){
        // String url=null;
        // url="https://facelift.kplexus.net/#!/lite/kols/powermap?mode=0&category=pharma&tas=%22Oncology%22&indications=%7B%22values%22:%5B%22Breast%20Cancer%22%5D,%22isAnd%22:true%7D&centrality_criteria=%22betweenness%22&resources=%22Publications%22&filters=%7B%22kol_info%22:%5B%7B%22name%22:%22Publications%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Clinical%20Trials%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Congresses%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Regulatory%20Bodies%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22HTA%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Societies%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Advocacy%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Top%20Hospital%20KOLs%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Guidelines%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D,%7B%22name%22:%22Diagnostic%20Centers%22,%22value%22:10,%22showDate%22:false,%22startDate%22:0,%22endDate%22:0%7D%5D,%22showScore%22:false,%22kolType%22:%22powermap%22,%22Indications%22:%7B%22values%22:%5B%22Breast%20Cancer%22%5D,%22isAnd%22:true%7D%7D&networkView=%7B%22Publications%22:true,%22Clinical%20Trials%22:true,%22Congresses%22:true,%22Regulatory%20Bodies%22:true,%22HTA%22:true,%22Societies%22:true,%22Advocacy%22:true,%22Top%20Hospital%20KOLs%22:true,%22Guidelines%22:true,%22Diagnostic%20Centers%22:true%7D&selectedUserId=null";
        try {
            url= URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            Log.i(TAG, "getUrl: Error="+e.getMessage());
            e.printStackTrace();
        }
        return  url;
    }

    public void changeDrawable(Context context,View view){
        int solidColor=context.getResources().getColor(R.color.layout_bgColor);
        int textColor=context.getResources().getColor(R.color.card_title_color);
        float[] cornerRadii=new float[]{100,100,100,100,100,100,100,100};
        GradientDrawable drawable= CustomDrawable.getInstance().setDrawableSolidColor(solidColor).setDrawableShape(GradientDrawable.RECTANGLE).setDrawableCornerRadius(cornerRadii).getDrawable();
        view.setBackgroundDrawable(drawable);

    }


    public void singleViewBackPressed(Context context, final FragmentManager manager){
        Activity activity= (Activity) context;
        int fragments = manager.getBackStackEntryCount();
        if (fragments == 1) {

            activity.finish();
        } else {

            if (manager.getBackStackEntryCount() > 1) {

                manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        int index = manager.getBackStackEntryCount() - 1;
                        FragmentManager.BackStackEntry backEntry = manager.getBackStackEntryAt(index);
                        String tag = backEntry.getName();
                    }
                });
                manager.popBackStack();
            } else {
                //super.onBackPressed();
            }
        }
    }

    public void setTextSize(AppCompatTextView textContentView, int size) {
        textContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                16);
    }

   /* public void setTextViewHeightWidth(Context mContext, String dimension, AppCompatImageView researchImageView) {

        try {

            float rate = DimensionConverter.stringToDimension(dimension, mContext.getResources().getDisplayMetrics());
            int intRate = (int) rate;
            researchImageView.getLayoutParams().height = intRate;
            researchImageView.getLayoutParams().width = intRate;
        } catch (Exception e) {
            Log.i(TAG, "setTextViewHeightWidth: error="+e.getMessage());
        }
    }

*/



    public int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();   // Gets the current date and time
        //int year = now.get(Calendar.YEAR);
        calendar.setTimeZone(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        return year;
    }


    public int getYearFromStamp(long dateValue) {
        // dateValue = dateValue * 1000;
        Timestamp timestamp = new Timestamp(dateValue);
        Date date = new Date(timestamp.getTime());
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        //  datePicker.updateDate(, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        return  cal.get(Calendar.YEAR);
    }


    public long getTimeStampFromYear(int year, int month, int day, int  hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }


    public void getMainActivity(Context context){

        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }



}
