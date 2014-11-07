package com.cucmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cucmap.data.DBManager;
import com.cucmap.data.building.BuildingMark;
import com.cucmap.data.facility.FacilityMark;
import com.cucmap.map.MapManager;
import com.cucmap.map.mark.ItemMark;
import com.cucmap.map.mark.ItemMarkDao;
import com.cucmap.mark.MyItemAdapter;
import com.cucmap.data.SearchBuildingUtil;
import com.cucmap.data.SearchFacilityUtil;
import com.zjcmmap.R;

@SuppressLint("UseSparseArrays")
public class BuildingSearchResultActivity  extends Activity{
	private ListView listView = null;
    private Button lastPageButton = null;
    private Button nextPageButton = null;
    private TextView textView;
    private int MAXNUM = 4;           //每一页最多显示4条记录
    private int page = 1;
    
    private MyItemAdapter adapter;
    private String key[];
    private int value[];
    
    private SQLiteDatabase database;
    private Bundle bundle;
    private Context context;
    
    private List<ItemMark> list_marks;
    private List<Map<String, Object>> mData = null;
    private ArrayList<Map<String, Object>> showData = null;
    private MapManager manager;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.check_searched_building);
        
        /** 得到界面上的控件 **/
        context = getApplicationContext();
        listView = (ListView)findViewById(R.id.building_list);   
        lastPageButton = (Button)findViewById(R.id.last_page_button);
        nextPageButton = (Button)findViewById(R.id.next_page_button);
        textView = (TextView)findViewById(R.id.page_num_text);
        bundle = this.getIntent().getExtras();
        manager = (MapManager)getApplication();
        
        /** 得到数据库  */
        DBManager dbManager = new DBManager(context);
		dbManager.openDatabase();
		database = dbManager.getDatabase();
        
		String type = "";
		String name = "";
		list_marks = new ArrayList<ItemMark>();
		
		if(bundle != null){
			type = bundle.getString("type");
			name = bundle.getString("name");
		}
		
		/** 得到查询的信息	 */
		List<BuildingMark> buildingMarks = new ArrayList<BuildingMark>();
		List<FacilityMark> facilityMarks = new ArrayList<FacilityMark>();
		if(!type.equals("")){
			/** 按照类型查询目前仅仅针对Facility表 */
			Log.i("Type-->",type);
			int t = Integer.parseInt(type);
			facilityMarks = SearchFacilityUtil.getFacilityMarkByType(t, database);
			list_marks = ItemMarkDao.getMarksByFacilityMarks(facilityMarks);
			
		}else if(!name.equals("")){
			/** 将Building表和Facility表中所有和name类似的字段都列出来 */
			facilityMarks = SearchFacilityUtil.getFacilityMarkByName(name, database);
			if(facilityMarks.size() < 1){
				buildingMarks = SearchBuildingUtil.getBuildingMarkByName(name, database);
				List<ItemMark> mark_list2 = ItemMarkDao.getMarksByBuildingMarks(buildingMarks);
				list_marks.addAll(mark_list2);
			}			
			List<ItemMark> mark_list1 = ItemMarkDao.getMarksByFacilityMarks(facilityMarks);
			list_marks.addAll(mark_list1);
			
		}
		
		/** 保存查询记录  */
		manager.itemMarks = list_marks;
		
		/** 将查询结果在界面显示出来 */
		showData = new ArrayList<Map<String,Object>>();
		mData = getMyData(list_marks);
		
		for(int i = 0; i < MAXNUM && i < mData.size(); i++){
			Map<String, Object> data = new HashMap<String, Object>();
			data = mData.get(i);
			showData.add(data);
		}
		
		key = new String[]{
		        "mark",    //标志的图标
		        "title",   //标志的名称
		        "score",   //标志的得分
		        "item",    //标志所属的对象
		};
		
		value = new int[]{
		        R.id.building_image_mark,
		        R.id.building_title,
		        R.id.building_image_score,
		        R.id.show_building_in_map,
		        R.id.start_from_here,
		        R.id.goto_here_button
		};
		
		adapter = new MyItemAdapter(context, showData, key, value);
		listView.setAdapter(adapter);
		setTextView();
        
		lastPageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(page > 1){
					showData.clear();
					page--;
					for(int i = 0; i < MAXNUM; i++){
						Map<String, Object> data = new HashMap<String, Object>();
						data = mData.get(i + (page - 1) * MAXNUM);
						showData.add(data);
					}
					adapter.notifyDataSetChanged();
					setTextView();
				}
			}
		});
		
		nextPageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int n = 0;
				if(mData.size() % MAXNUM == 0){
					n = mData.size() / MAXNUM;
				}else{
					n = mData.size() / MAXNUM + 1;
				}				
				if(page < n){
					showData.clear();
					page ++;
					for(int i = 0; i < MAXNUM; i++){
						if((page - 1) * MAXNUM + i >= mData.size()){
							break;
						}
						Map<String, Object> data = new HashMap<String, Object>();
						data = mData.get(i + (page - 1) * MAXNUM);
						showData.add(data);
					}
					adapter.notifyDataSetChanged();
					setTextView();
				}
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
			map.put("mark", id);
			map.put("title", mark.getName());
			map.put("score",70);
			map.put("item", mark);
			list.add(map);
		}
		return list;
    }   
    
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
    		id = R.drawable.market;
    	}
    	return id;
    }
    
    protected void setTextView() {
    	int n = 0;
		if(mData.size() % MAXNUM == 0){
			n = mData.size() / MAXNUM;
		}else{
			n = mData.size() / MAXNUM + 1;
		}
		Log.i("MAXSIZE", Integer.toString(mData.size()));
		textView.setText("第" + Integer.toString(page) + "页"
		        + ", 共" + Integer.toString(n) + "页");
	}
    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		database.close();
		super.onStop();
	}    
}
