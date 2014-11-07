package com.map.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.map.model.DBManager;
import com.map.model.building.Building;
import com.map.model.position.Position;
import com.map.model.road.RoadMark;
import com.map.core.map.MapManager;
import com.map.core.map.mark.ItemMark;
import com.map.core.common.Dijkstra;
import com.map.core.common.SearchBuildingUtil;
import com.map.core.common.SearchRoadUtil;
import com.map.R;

@SuppressLint("UseSparseArrays")
public class PathFindingResultActivity extends Activity {
    private ListView beginPositionListView = null;  
    private ListView endPositionListView = null;
    
    private Button doSearchButton = null;   
    private Button calcelSearchButton = null;  
    private SimpleAdapter begin_adapter;
    private SimpleAdapter end_adapter;
    
    private SQLiteDatabase database;
    private Bundle bundle;
    private Context context;
    
    private List<ItemMark> beginItemMarks;
    private List<ItemMark> endItemMarks;
    private ItemMark beginCheckedItemMark;
    private ItemMark endCheckedItemMark;
    private List<Map<String, Object>>beginData;
    private List<Map<String, Object>>endData;

    private MapManager mapManager;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.check_searched_path);
        
        /** 得到数据库  */
        DBManager dbManager = new DBManager(this);
		dbManager.openDatabase();
		database = dbManager.getDatabase();
		mapManager = (MapManager)getApplication();
		mapManager.initRoadMap(database);
        
        /** 得到界面上的控件     */
        beginPositionListView = (ListView) this.findViewById(R.id.beginposition_list);
        endPositionListView = (ListView) this.findViewById(R.id.endposition_list);  
        doSearchButton = (Button) findViewById(R.id.do_search_road);    
        calcelSearchButton = (Button) findViewById(R.id.cancel_search_road);  
        context = getApplicationContext();
        
        bundle = this.getIntent().getExtras();         
		
		/** 得到查询的建筑信息	 */ 
        boolean flag = false;
		if(bundle != null){
			beginItemMarks = (List<ItemMark>)bundle.getSerializable("beginList");
			endItemMarks = (List<ItemMark>) bundle.getSerializable("endList");
			
			/** 初始化ListView数据 */
			beginData = new ArrayList<Map<String,Object>>();
			endData = new ArrayList<Map<String,Object>>();
			beginData = getMyData(beginItemMarks);
			endData = getMyData(endItemMarks);
			begin_adapter = new SimpleAdapter(this, beginData, R.layout.listview_item,
			          new String[]{"img","title","isChecked"},
			          new int[]{R.id.item_img,R.id.item_text,R.id.item_check});
			end_adapter = new SimpleAdapter(this, endData, R.layout.listview_item,
			          new String[]{"img","title","isChecked"},
			          new int[]{R.id.item_img,R.id.item_text,R.id.item_check});
			
//			if(beginData.size() == 1){
//				beginData.get(0).put("isChecked", true);
//			}
//			if(endData.size() == 1){
//				endData.get(0).put("isChecked", true);
//			}
			
			/** 显示界面  */
			beginPositionListView.setAdapter(begin_adapter);
			endPositionListView.setAdapter(end_adapter);
		}else{
			beginCheckedItemMark = beginItemMarks.get(0);
			endCheckedItemMark = endItemMarks.get(0);
		}
		
		beginPositionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				for(int i = 0; i < beginData.size(); i++){
					if(i != arg2){
						beginData.get(i).put("isChecked",false);
					}
				}
				beginData.get(arg2).put("isChecked", true);
				beginCheckedItemMark = beginItemMarks.get(arg2);
				begin_adapter.notifyDataSetChanged();
			}
		});
		
		endPositionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				for(int i = 0; i < endData.size(); i++){
					if(i != arg2){
						endData.get(i).put("isChecked",false);
					}
				}
				endData.get(arg2).put("isChecked", true);
				endCheckedItemMark = endItemMarks.get(arg2);
				end_adapter.notifyDataSetChanged();
			}
		});
		
        //开始寻路
		doSearchButton.setOnClickListener(new OnClickListener(){  
            @Override  
            public void onClick(View arg0) {
            	if(beginCheckedItemMark == null || endCheckedItemMark == null){
            		Toast toast = Toast.makeText(context.getApplicationContext(), 
    						"请正确选择起点和终点", Toast.LENGTH_LONG); 
    				toast.setGravity(Gravity.CENTER, 0, 0); 
    				toast.show();
    				return;
            	}
            	Building building1 = beginCheckedItemMark.getBuildingMark().getBuilding();
            	Building building2 = endCheckedItemMark.getBuildingMark().getBuilding();
            	Position beginPosition = SearchBuildingUtil.getNearestPositionInRoad(
            			building1, database);
            	Position endPosition = SearchBuildingUtil.getNearestPositionInRoad(
            			building2,	database);
            	/** 求出最短路径  */
            	Dijkstra dijkstra = new Dijkstra();
            	int start = endPosition.getId();
            	dijkstra.getShortestDistance(mapManager.roadMap.getRoadMap(), start);
            	String path = dijkstra.getPath()[beginPosition.getId()];
            	Log.i("Path-->", path);
            	String temp[] = path.split("_");
            	for(int i = 0; i < temp.length / 2; i ++){
            		String t = temp[temp.length - 1 - i];
            		temp[temp.length - 1 - i] = temp[i];
            		temp[i] = t;
            	}
            	List<Position> positions=SearchRoadUtil.getPositionsInRoute(temp,database);
            	List<RoadMark> roadMarks = SearchRoadUtil.getRoadMarksByListPosition(
            			positions, database);
            	//Collections.sort(positions,Collections.reverseOrder());
            	mapManager.positions = positions;
            	mapManager.searchedRoadMarks = roadMarks;
            	Intent intent = new Intent(PathFindingResultActivity.this,MapActivity.class);
            	startActivity(intent);
            }
        });   
		
		calcelSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beginCheckedItemMark = null;
				endCheckedItemMark = null;
				for(int i = 0; i < beginData.size(); i++){
					beginData.get(i).put("isChecked",false);
				}
				for(int i = 0; i < endData.size(); i++){
					endData.get(i).put("isChecked",false);
				}
				begin_adapter.notifyDataSetChanged();
				end_adapter.notifyDataSetChanged();
			}
		});
    }
 
    // 显示带有checkbox的listview  
    public List<Map<String, Object>> getMyData(List<ItemMark> marks){  
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map;
		for(int i = 0; i < marks.size(); i++){
			ItemMark mark = marks.get(i);
			map = new HashMap<String, Object>();
			int id = getImageByType(mark.getType());
			map.put("img", id);
			map.put("title", mark.getName());
			map.put("isChecked",false);
			list.add(map);
		}
		return list;
    }   
    
    /**
     * 根据建筑的类型的到建筑的图标
     * @param type
     * @return
     */
    protected int getImageByType(int type){
    	int id = 0;
    	
    	HashMap<Integer, Integer>item_images = new HashMap<Integer, Integer>();

    	item_images.put(1, R.drawable.classroom);
        item_images.put(2, R.drawable.college);
        item_images.put(3, R.drawable.hospital);
        item_images.put(4, R.drawable.dorm);
        item_images.put(5, R.drawable.dining);
        item_images.put(6, R.drawable.school);
        item_images.put(7, R.drawable.coffee);
        item_images.put(8, R.drawable.library);
        item_images.put(9, R.drawable.wc);
        item_images.put(10, R.drawable.atm);
        item_images.put(11, R.drawable.bookshop);
        item_images.put(12, R.drawable.market);
    	
    	if(item_images.containsKey(type)){
    		id = item_images.get(type);
    	}else{
    		id = R.drawable.classroom;
    	}
    	return id;
    }
    
    @Override
    public void onStop(){
    	database.close();
    	super.onStop();
    }
}
