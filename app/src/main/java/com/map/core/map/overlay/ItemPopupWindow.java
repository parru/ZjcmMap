package com.map.core.map.overlay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.map.core.BuildingDetailActivity;
import com.map.core.PathFindingActivity;
import com.map.core.map.mark.ItemMark;
import com.map.R;

public class ItemPopupWindow {
    private ItemMark itemMark;
    private int height;
    private int width;
    private String text;
    private Context context;
    public ItemPopupWindow(Context context,ItemMark itemMark, String text){
        this.context = context;
        this.itemMark = itemMark;
        this.text = text;
        Paint paint = new Paint();
        paint.setTextSize(14);
        this.height = 30;
        this.width = 20 + (int)paint.measureText(text) + 20;
    }
    
    /**
     * 绘制popup层，位置参照与父View的左上角
     * @param parent    popup对应的父View
     * @param x         popup相对于父View的X轴坐标
     * @param y         popup相对于父View的Y轴坐标
     */
    public  void showItemPopupWindow(View parent, int x, int y){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.pop_building_item, null);
        
        PopupWindow popupWindow = new PopupWindow(view, 
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        
        ImageView imageView1 = (ImageView) view.findViewById(R.id.building_pop_mark);
        imageView1.setOnClickListener(listener);
        TextView textView = (TextView)view.findViewById(R.id.building_pop_text);
        textView.setText(text);
        textView.setOnClickListener(listener);
        ImageView imageView2 = (ImageView)view.findViewById(R.id.building_pop_path);
        imageView2.setOnClickListener(listener);
        
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
    
    /**
     * 
     * @param parent
     * @param x
     * @param y
     */    
    public void showNavPopupWindow(View parent, int x, int y){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.pop_nav_item, null);
        PopupWindow popupWindow = new PopupWindow(view, 
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView)view.findViewById(R.id.nav_pop_item_text);
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
    
    private OnClickListener listener = new OnClickListener() {
        private Intent intent;
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
            case R.id.building_pop_path:
                intent = new Intent(context,PathFindingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(itemMark != null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itemMark", itemMark);
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
                break;
            default:
                intent = new Intent(context,BuildingDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(itemMark != null){
                    Bundle bundle = new Bundle();
                    intent.putExtra("beginPosition", itemMark.getName());
                    bundle.putSerializable("itemMark", itemMark);
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
                break;
            }
        }
    };
}
