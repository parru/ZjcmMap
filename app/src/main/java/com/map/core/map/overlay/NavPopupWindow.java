package com.map.core.map.overlay;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.map.R;

public class NavPopupWindow {
    private Context context;
    private String text;
    private int width;         //pop的宽
    private int height;        //pop的高
    public NavPopupWindow(Context context, String text){
        this.context = context;
        this.text = text;
        Paint paint = new Paint();
        paint.setTextSize(14);
        this.width = (int)paint.measureText(text);
    }
    
    public void showPopupWindow(View parent, int x, int y){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.pop_building_item, null);
        
        PopupWindow popupWindow = new PopupWindow(view, 
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        
        TextView textView = (TextView)view.findViewById(R.id.building_pop_text);
        textView.setText(text);
        
        //必须设置背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置焦点
        popupWindow.setFocusable(true);
        //设置点击其他地方 就消失
        popupWindow.setOutsideTouchable(true);
        x = x - width / 2 - 7;
        y = y - 15;
        popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP, x, y);
    }
}
