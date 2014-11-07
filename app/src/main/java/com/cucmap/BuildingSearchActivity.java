package com.cucmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zjcmmap.R;

public class BuildingSearchActivity extends Activity {
	private int screenHeight;
	private int screenWidth;
	
	private GridView gridView = null;
	private ArrayList<HashMap<String, Object>> categoryList = null;

	private HashMap<String, Integer> itemImages;
	private EditText searchEdit;
	private ImageButton imageButton;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.building_search_activity);

		/** 初始化分类的数据 */
		itemImages = new HashMap<String, Integer>();

		itemImages.put("教室", R.drawable.classroom);
		itemImages.put("学院", R.drawable.college);
		itemImages.put("医院", R.drawable.hospital);
		itemImages.put("宿舍", R.drawable.dorm);
		itemImages.put("餐厅", R.drawable.dining);
		itemImages.put("学校", R.drawable.school);
		itemImages.put("咖啡厅", R.drawable.coffee);
		itemImages.put("图书馆", R.drawable.library);
		itemImages.put("厕所", R.drawable.wc);
		itemImages.put("ATM机", R.drawable.atm);
		itemImages.put("书店", R.drawable.bookshop);
		itemImages.put("超市", R.drawable.market);

		/*
		 * 获得XML中的控件
		 */
		getAllWidgets();

		categoryList = new ArrayList<HashMap<String, Object>>();
		for (Map.Entry<String, Integer> entry : itemImages.entrySet()) {
			String mark = entry.getKey();
			int id = entry.getValue();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("Image", id);
			hashMap.put("Text", mark);
			categoryList.add(hashMap);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, categoryList,
				R.layout.items, 
				new String[] { "Image", "Text" }, 
				new int[] {R.id.item_image, R.id.item_text });
		
		gridView.setAdapter(simpleAdapter);
		//ItemAdapter adapter = new ItemAdapter(titles, images, this);
		//gridView.setAdapter(simpleAdapter);
		
		gridView.setOnItemClickListener(new ItemClickListener());
		imageButton.setOnClickListener(new MyOnClickListener());
	}

	protected void getAllWidgets(){
		/*
		 * 获得XML中的控件
		 */
		RelativeLayout.LayoutParams layoutParams =
				new RelativeLayout.LayoutParams(20, 20);
		screenWidth  = getWindowManager().getDefaultDisplay().getWidth();		
	    screenHeight = getWindowManager().getDefaultDisplay().getHeight();
	    
		searchEdit = (EditText) findViewById(R.id.search_content_text);
		imageButton = (ImageButton) findViewById(R.id.do_search_button);
		text = (TextView) findViewById(R.id.result_content);
		gridView = (GridView) findViewById(R.id.gridview);
		
		
		int columnWidth = screenWidth / 5;		
		gridView.setColumnWidth(columnWidth);
	}
	
	private class ItemClickListener implements OnItemClickListener {
		/**
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// arg0就是那个DateList
			Log.i("temp-->","###############");
			HashMap<String, Object> item = categoryList.get(arg2);
			// 显示应用标题的那个TextView
			searchEdit.setText((String) item.get("Text"));
		}
	}

	private class MyOnClickListener implements OnClickListener {
		private Map<String, Integer> category_type;

		public MyOnClickListener() {
			category_type = new HashMap<String, Integer>();

			category_type.put("教室", 1);
			category_type.put("学院", 2);
			category_type.put("医院", 3);
			category_type.put("宿舍", 4);
			category_type.put("餐厅", 5);
			category_type.put("学校", 6);
			category_type.put("咖啡厅", 7);
			category_type.put("图书馆", 8);
			category_type.put("厕所", 9);
			category_type.put("ATM机", 10);
			category_type.put("书店", 11);
			category_type.put("超市", 12);
		}

		/**
		 * 根据搜索内容判定是否是按类型搜索
		 * @param categoryString 分类的名称
		 * @return i 分类的编号，如果为0，表示没有该分类
		 */
		public int getBuildingType(String categoryString) {
			int i = 0;
			if (category_type.containsKey(categoryString)) {
				i = category_type.get(categoryString);
			}
			return i;
		}

		@Override
		public void onClick(View v) {
			// 得到查询信息
			String search_content = searchEdit.getText().toString();
			Intent intent = new Intent(BuildingSearchActivity.this, 
					BuildingSearchResultActivity.class);
			int i = getBuildingType(search_content);
			
			Bundle bundle = new Bundle();
			boolean flag = false;
			// 安装类型查询
			if (i != 0) {
				bundle.putString("type", Integer.toString(i));
				bundle.putString("name", "");
				flag = true;
			}
			else {
				bundle.putString("type", "");
				bundle.putString("name", search_content);
				if(search_content.length() >= 1){
				    flag = true;
				}
			}
			
			if(flag){
			    /*
	             * 转到地图界面
	             */
	            intent.putExtras(bundle);
	            startActivity(intent);
			}else{
			    Toast toast = Toast.makeText(getApplicationContext(), 
			            "请输入查询名称", Toast.LENGTH_LONG); 
			    toast.setGravity(Gravity.CENTER, 0, 0); 
			    toast.show(); 
			}
			text.setText(search_content);
		}
	}
}
