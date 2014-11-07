package com.cucmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cucmap.map.mark.ItemMark;
import com.zjcmmap.R;

public class BuildingDetailActivity extends Activity implements OnClickListener {
    private ImageView markImage;
    private TextView titleText;
    private Button showInMapButton;
    private TextView detailText;
    private Button startFromHereButton;
    private Button goToHereButton;
    private ImageView bigImageView;
    private ItemMark itemMark;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.building_detail);
        
        /*
         * 得到XML上的控件
         */
        getWidgets();
        
        /*
         * 得到其他Activity传过来的值
         */
        Intent intent = getIntent();
        if(intent != null){
            itemMark = (ItemMark)intent.getExtras().get("itemMark");
            if(itemMark != null){
                titleText.setText(itemMark.getName());
                detailText.setText(itemMark.getBuildingMark().getBuilding().getDescription());
            }
        }
    }
    
    /**
     * 得到XML上的控件
     */
    protected void getWidgets() {
        markImage = (ImageView) findViewById(R.id.building_type_mark);
        titleText = (TextView) findViewById(R.id.building_title_text);
        showInMapButton = (Button) findViewById(R.id.show_building_in_map);
        detailText = (TextView) findViewById(R.id.building_descript_text);
        startFromHereButton = (Button)findViewById(R.id.from_here_button);
        goToHereButton = (Button) findViewById(R.id.goto_here_button);
        bigImageView = (ImageView) findViewById(R.id.building_image);    
        
        showInMapButton.setOnClickListener(this);
        startFromHereButton.setOnClickListener(this);
        goToHereButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        
        if(id == showInMapButton.getId()){
            //在地图上显示建筑
            Intent intent = new Intent(this,MapActivity.class);
            if(itemMark != null){
                String building_name = itemMark.getName();
                intent.putExtra("building_name", building_name);
            }
            startActivity(intent);
        }else if(id == goToHereButton.getId()){
            //
            Intent intent = new Intent(this,PathFindingActivity.class);
            if(itemMark != null){
                String building_name = itemMark.getName();
                intent.putExtra("endPosition", building_name);
            }
            startActivity(intent);
        }else if(id == startFromHereButton.getId()){
            Intent intent = new Intent(this,PathFindingActivity.class);
            if(itemMark != null){
                String building_name = itemMark.getName();
                intent.putExtra("beginPosition", building_name);
            }
            startActivity(intent);
        }
    }

}
