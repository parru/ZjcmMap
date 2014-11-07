package com.map.core;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TopLayout extends LinearLayout {
    
	private String TAG = "TopLayout";
	
	public TopLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	@Override 
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       int action = ev.getAction(); 
       switch(action){ 
       case MotionEvent.ACTION_DOWN: 
           Log.e(TAG,"onInterceptTouchEvent action:ACTION_DOWN");
//           //return true;
           break; 
       case MotionEvent.ACTION_MOVE: 
           Log.e(TAG,"onInterceptTouchEvent action:ACTION_MOVE"); 
           break; 
       case MotionEvent.ACTION_UP: 
           Log.e(TAG,"onInterceptTouchEvent action:ACTION_UP"); 
           break; 
       case MotionEvent.ACTION_CANCEL: 
           Log.e(TAG,"onInterceptTouchEvent action:ACTION_CANCEL"); 
           break; 
       } 
       return false; 
    } 

	@Override 
    public boolean onTouchEvent(MotionEvent ev) { 
       int action = ev.getAction(); 
       switch(action){ 
       case MotionEvent.ACTION_DOWN: 
           Log.e(TAG,"onTouchEvent action:ACTION_DOWN"); 
           break; 
       case MotionEvent.ACTION_MOVE: 
           Log.e(TAG,"onTouchEvent action:ACTION_MOVE"); 
           break; 
       case MotionEvent.ACTION_UP: 
           Log.e(TAG,"onTouchEvent action:ACTION_UP"); 
           break; 
       case MotionEvent.ACTION_CANCEL: 
           Log.e(TAG,"onTouchEvent action:ACTION_CANCEL"); 
           break; 
       } 
//       return true; 
       return false;
    }

}
