package com.itsolution.trafficbot;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class MyAccessibilityService extends AccessibilityService {


    public Boolean touch,up,down,left,right;
    SharedPreferences sharedPreferences;
    public int x,y;
    AccessibilityNodeInfo accessibilityNodeInfo;
    SharedPreferences click;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {


        click=getSharedPreferences("click",MODE_PRIVATE);
        Boolean touch=click.getBoolean("click",false);
        SharedPreferences.Editor editor=click.edit();

        if(touch==true){


            int midx= this.getResources().getDisplayMetrics().widthPixels/2;
            int midy= this.getResources().getDisplayMetrics().heightPixels/2;
            Log.e("loop","okay");
            performGlobalAction(touchTo(midx,midy));
            editor.putBoolean("click",false);
            editor.apply();

            Log.e("x and y is ",String.valueOf(midx)+" "+String.valueOf(midy));
            //performGlobalAction(swipe(1));




        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            accessibilityNodeInfo = getRootInActiveWindow();
        }

        //accessibilityNodeInfo.performAction(touchTo(260,600));



    }
    @Override
    public void onInterrupt() {

    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    public int touchTo(int x, int y) {

        int midx= this.getResources().getDisplayMetrics().widthPixels/2;
        int midy= this.getResources().getDisplayMetrics().heightPixels/2;

        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path, 0, 50));
        boolean click=dispatchGesture(gestureBuilder.build(), null , null);
        if(click==true){
            Log.e("btn","CLICKED");
            return 1;
        }else
        {
            Log.e("btn","not CLICKED");
            return 0;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int swipe(int dir){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int middleYValue = displayMetrics.heightPixels / 2;
        final int leftSideOfScreen = displayMetrics.widthPixels / 4;
        final int rightSizeOfScreen = leftSideOfScreen * 3;

        final int height = displayMetrics.heightPixels;
        final int top = (int) (height * .25);
        final int bottom = (int) (height * .75);
        final int midX = displayMetrics.widthPixels / 2;


        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        Path path = new Path();

        if(dir==1)
        {
            //down
            path.moveTo(midX, bottom);
            path.lineTo(midX, top);

            Log.e("Down","going");
        }
        if(dir==2)
        {
            //up
            path.moveTo(midX, top);
            path.lineTo(midX, bottom);

            Log.e("UP","going");
        }
        if (dir==3)
        {
            //Swipe left
            path.moveTo(rightSizeOfScreen, middleYValue);
            path.lineTo(leftSideOfScreen, middleYValue);

            Log.e("LEFT","going");
        }
        if(dir==4) {
            //Swipe right
            path.moveTo(leftSideOfScreen, middleYValue);
            path.lineTo(rightSizeOfScreen, middleYValue);
            Log.e("RIGHT","going");
        }

        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path, 100, 50));
        dispatchGesture(gestureBuilder.build(),null, null);
        return middleYValue;
    }














}
