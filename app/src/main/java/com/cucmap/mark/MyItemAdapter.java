package com.cucmap.mark;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cucmap.BuildingDetailActivity;
import com.cucmap.BuildingSearchActivity;
import com.cucmap.MapActivity;
import com.cucmap.PathFindingActivity;
import com.cucmap.data.building.BuildingMark;
import com.cucmap.map.MapManager;
import com.cucmap.map.mark.ItemMark;
import com.zjcmmap.R;

/**
 * 自定义适配器，用来显示建筑
 * @author Eddy
 */
public class MyItemAdapter extends BaseAdapter {
    private class buttonViewHolder {
        public ImageButton markButton;
        public TextView titleText;
        public ScoreImage scoreImage;
        public Button showInMap;
        public Button startFromHere;
        public Button goToHere;        
    }
    private List<Map<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private buttonViewHolder holder;

    public MyItemAdapter(Context context, List<Map<String, Object>> appList,
            String[]key, int[] value) {
        mAppList = appList;
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = key;
        valueViewID = value;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override 
    public View getView(int position, View convertView, ViewGroup parent) { 
        if ( convertView != null ){ 
            holder = (buttonViewHolder)convertView.getTag(); 
        } else { 
            //得到ListViewItem上的控件
            convertView = mInflater.inflate(R.layout.building_listview_item, null ) ;
            holder = new buttonViewHolder( ) ; 
            holder.markButton = (ImageButton)convertView.findViewById(valueViewID[0]); 
            holder.titleText = (TextView) convertView.findViewById(valueViewID[1]);
            holder.scoreImage = (ScoreImage)convertView.findViewById(valueViewID[2]);
            holder.showInMap = (Button) convertView.findViewById(valueViewID[3]);
            holder.startFromHere = (Button) convertView.findViewById(valueViewID[4]);
            holder.goToHere = (Button) convertView.findViewById(valueViewID[5]);
            convertView.setTag(holder) ; 
        }
        
        Map < String , Object > appInfo = mAppList.get(position); 
        if (appInfo != null ){
            int mark = (Integer)appInfo.get(keyString[0]);
            String title = (String)appInfo.get(keyString[1]);
            int score = (Integer)appInfo.get(keyString[2]);
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    mark);
            holder.markButton.setImageBitmap(bitmap);
            holder.titleText.setText(title);
            holder.scoreImage.setScore(score);  //这一部分没有数据，先写死
            
            //添加监听
            holder.markButton.setOnClickListener(new lvButtonListener(position));
            holder.showInMap.setOnClickListener(new lvButtonListener(position));
            holder.goToHere.setOnClickListener(new lvButtonListener(position));
            holder.startFromHere.setOnClickListener(new lvButtonListener(position));
        }
        return convertView; 
    }    
    class lvButtonListener implements OnClickListener {
        private int position;
        private Intent intent;
        private ItemMark mark;
        public lvButtonListener(int pos) {
            position = pos;
            mark = (ItemMark)mAppList.get(position).get(keyString[3]);
        }

        @Override 
        public void onClick( View v) { 
            int vid= v.getId ( ) ; 
            if ( vid == holder.showInMap.getId()){
                //转到地图界面，将建筑在地图上显示
                intent = new Intent(mContext, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("building_name", mark.getName());
                mContext.startActivity(intent);
                
            }else if(vid == holder.startFromHere.getId()){
                //转到道路查询界面，从这儿出发
                Log.i("ItemMark","#################");
                intent = new Intent(mContext, PathFindingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("beginPosition", mark.getBuildingMark().
                        getBuilding().getName());
                mContext.startActivity(intent);
                
            }else if(vid == holder.goToHere.getId()){
                //转到道路查询界面，到这儿去
                intent = new Intent(mContext, PathFindingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("endPosition", mark.getBuildingMark().getBuilding().getName());
                mContext.startActivity(intent);
                
            }else{
                //转到建筑描述详情页面
                intent = new Intent(mContext, BuildingDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemMark", (Serializable)mark);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }    
    }
}