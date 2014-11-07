package com.cucmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cucmap.data.DBManager;
import com.cucmap.data.building.BuildingMark;
import com.cucmap.data.facility.FacilityMark;
import com.cucmap.map.MapManager;
import com.cucmap.map.mark.ItemMark;
import com.cucmap.map.mark.ItemMarkDao;
import com.cucmap.data.SearchBuildingUtil;
import com.cucmap.data.SearchFacilityUtil;
import com.zjcmmap.R;

/**
 * 道路查询界面，
 * 该界面处理查询请求，并根据查询请求获取最短路径
 * 最短路径最终保存在MapManager全局变量中
 * @author Administrator
 */
public class PathFindingActivity extends Activity {
    private EditText beginPosition;
    private EditText endPosition;
    private Button doSearchButton;
    private Button cancleButton;
	private Context context;
	private List<ItemMark> beginItemMarks;
    private List<ItemMark> endItemMarks;
    
    private SQLiteDatabase database;
    private MapManager mapManager;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.path_find_activity);
		
		/** 得到界面上的控件对象 */
		getAllWidgets();
		
		/** 得到数据库  */
        DBManager dbManager = new DBManager(this);
		dbManager.openDatabase();
		database = dbManager.getDatabase();
		mapManager = (MapManager)getApplication();
		mapManager.initRoadMap(database);
		
		/** 接受其他界面的传值 **/
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null){
			String beginPositionStr = bundle.getString("beginPosition");
			String endPositionStr = bundle.getString("endPosition");
			beginPosition.setText(beginPositionStr);
			endPosition.setText(endPositionStr);
		}
	}
    
    protected void getAllWidgets(){
    	context = getApplicationContext();
    	beginPosition = (EditText)findViewById(R.id.start_position);
		endPosition = (EditText)findViewById(R.id.end_position);
		doSearchButton = (Button)findViewById(R.id.do_path_finding);
		cancleButton = (Button)findViewById(R.id.cancel_path_finding);
		
		/** 添加监听 **/
		doSearchButton.setOnClickListener(new MyOnclickListener());
		cancleButton.setOnClickListener(new MyOnclickListener());
    }
    
    protected class MyOnclickListener implements OnClickListener{
        private Intent intent;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.do_path_finding:
				String begin = beginPosition.getText().toString();
				String end = endPosition.getText().toString();
				intent = new Intent(PathFindingActivity.this,
						PathFindingResultActivity.class);
				
				if(begin.length() < 1){
					String alert = "请输入起点";
					showToastInfo(alert);
					
				}else if(end.length() < 1){
					String alert = "请输入终点";
					showToastInfo(alert);
					
				}else{
					List<BuildingMark> beginBuildingMarks = new ArrayList<BuildingMark>();
				    List<FacilityMark> beginFacilityMarks = new ArrayList<FacilityMark>();
					List<BuildingMark> endBuildingMarks = new ArrayList<BuildingMark>();
					List<FacilityMark> endFacilityMarks = new ArrayList<FacilityMark>();
					
					beginBuildingMarks = SearchBuildingUtil.getBuildingMarkByName(begin, 
							database);
					if(beginBuildingMarks.size() < 1){
						beginFacilityMarks = SearchFacilityUtil.getFacilityMarkByName(begin, 
								database);
					}					
					List<ItemMark> list_1 = ItemMarkDao.getMarksByBuildingMarks(
							beginBuildingMarks);
					List<ItemMark> list_2 = ItemMarkDao.getMarksByFacilityMarks(
							beginFacilityMarks);
					beginItemMarks = new ArrayList<ItemMark>();
					beginItemMarks.addAll(list_1);
					beginItemMarks.addAll(list_2);
					
					endBuildingMarks = 
							SearchBuildingUtil.getBuildingMarkByName(end, database);
					if(endBuildingMarks.size() < 1){
						endFacilityMarks = 
							SearchFacilityUtil.getFacilityMarkByName(end, database);
					}
					
					List<ItemMark> list_3 = 
							ItemMarkDao.getMarksByBuildingMarks(endBuildingMarks);
					List<ItemMark> list_4 = 
							ItemMarkDao.getMarksByFacilityMarks(endFacilityMarks);
					endItemMarks = new ArrayList<ItemMark>();
					endItemMarks.addAll(list_3);
					endItemMarks.addAll(list_4);
					
					if(beginItemMarks.size() < 1 || endBuildingMarks.size() < 1){
						//得不到起点或终点
						String info = "不能完全获取起点和终点";
						showToastInfo(info);
						
					}else{
						//获取的起点或终点，转到起点、终点结果确认界面
						Bundle bundle = new Bundle();
						bundle.putSerializable("beginList", (Serializable)beginItemMarks);
						bundle.putSerializable("endList", (Serializable)endItemMarks);
						intent.putExtras(bundle);
						startActivity(intent);	
					}
				}
				break;
				
			case R.id.cancel_path_finding:
				beginPosition.setText("");
				endPosition.setText("");
				break;
			default:
				break;
			}
		}
    	
    }
    
    /**
     * 
     * @param itemMarks
     */
    public void getPathItemMark(List<ItemMark> itemMarks){
    	
    }
    
    /**
     * 使用Toast向用户提示错误信息
     * @param info  提示的信息
     */
    protected void showToastInfo(String info){
		Toast toast = Toast.makeText(context.getApplicationContext(), 
				info, Toast.LENGTH_LONG); 
		toast.setGravity(Gravity.CENTER, 0, 0); 
		toast.show();
    }
}
