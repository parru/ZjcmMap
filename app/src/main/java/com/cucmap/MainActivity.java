package com.cucmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cucmap.data.DBManager;
import com.cucmap.data.TBFactory;
import com.cucmap.data.TBManager;
import com.cucmap.http.HttpDownLoader;
import com.cucmap.progress.ProgressDialog;
import com.cucmap.update.Version;
import com.cucmap.update.VersionDao;
import com.zjcmmap.R;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private SQLiteDatabase database;
	private Version newVersion;
	private Version oldVersion;
	private VersionDao versionDao;
	private Context context;
	
    private ProgressDialog mProgressDialog;
    private int index = 0;
    private int max = 20;
    private boolean flag = true;
    private boolean shouldUpdate = false;
    private boolean getReadyToUpdate = false;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
            	//检测更新
            	if(shouldUpdate){
        		    newVersion = versionDao.getVersionFromXml();
        	        oldVersion = versionDao.getVersionFromDatabase();
        	        if(versionDao.ifShouldUpdate(newVersion, oldVersion)){
        	            versionDao.updateDatabase(newVersion, oldVersion);
        	            Log.i("createDialog-->", "##########");
        	            createDialog();        	            
        	        }else{
        	        	gotoMapActivity();
        	        }
        	        shouldUpdate = false;
        	        
        		}else{
        			gotoMapActivity();	
        		}
            	break;
            	
            case 1:
                if (index <= 8 ) {
                	mProgressDialog.setMessage("已经下载");
                	mProgressDialog.setProgress(index);
                }else if(index > 8 && index <=16){
                	mProgressDialog.setTitle("开始更新数据库");
                    mProgressDialog.setDynamicStyle(
                            ProgressDialog.STYLE_SPINNER, "正在更新。。。");
                    int i = index - 8;
                    //更新数据库
                    TBManager tbManager = getTbManager(i, database);
                    tbManager.updateTable();
                }else if(index > 16 && index <= 20 ){
                	mProgressDialog.setTitle("更新完成");
                    mProgressDialog.setDynamicStyle(
                             ProgressDialog.STYLE_SPINNER, "更新完成。。。");
                }else{
                    if(mProgressDialog != null){
                        mProgressDialog.dismiss();
                    }        
                    flag = false;
                    index = 0;
                    versionDao.updateTable();
                    Log.i("updateVersion-->", "updataver");
                    gotoMapActivity();
                }  
                getReadyToUpdate = true;
                Log.i("Handle-->", "" + index);
                Log.i("Thread-->", "" + getReadyToUpdate);
                index ++;
                break;
                
            default:
                break;
            }
        };
	};
	
    private String[] files={
            "Building.xml", "BuildingFacilities.xml", "BuildingPositions.xml",
            "Category.xml", "Facility.xml", "PositionOfInterest.xml",
            "Road.xml", "RoadPositions.xml"
    };
    private String[] tables = {
    		"building","buildingFacilities","buildingPositions","category",
    		"facility","position","road","roadPositions"
    };
    
    private String base_url = "http://mom.cuc.edu.cn/cucmap/xml/";
    
    private MyThread thread;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);     
	    //全屏     
	    getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,       
	        WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.main_activity);
		
		/* 打开数据库 */
		context = getApplicationContext();
		DBManager dbManager = new DBManager(context);
		dbManager.openDatabase();
		database = dbManager.getDatabase();
		versionDao = new VersionDao(database, this);
		
		/* 开启线程 */
		thread = new MyThread();
		getReadyToUpdate = true;
		thread.start();
	}
    
    /**
     * 数据库中的表格更新管理器
     * @param i
     * @param database
     * @return
     */
    protected TBManager getTbManager(int i, SQLiteDatabase database) {
        TBManager tbManager = null;
        tbManager = TBFactory.createTBManager(tables[i], database);
        return tbManager;        
    }

    /**
     * 创建更新提示框
     */
    protected void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("检测到数据库有更新");
        builder.setMessage("最新版的数据库能更精确的定位和导航，是否更新数据库");
        
		builder.setPositiveButton("开始更新",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					createProgressDialog();
					getReadyToUpdate = true;
				}
			}
		);
		
        builder.setNegativeButton("取消更新",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    gotoMapActivity();
                }
            });
        builder.create().show();
    }
    
    /**
     * 创建更新对话框
     */
    protected void createProgressDialog(){
    	mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("开始");
		mProgressDialog
				.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMax(8);
		mProgressDialog.setButton("取消",
		    new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int whichButton) {
				    flag = false;
				    mProgressDialog.dismiss();
				    mProgressDialog = null;
				    index = 0;
			    }
		    }
		);
		mProgressDialog.show();
    }
    
    /**
     * 跳转到地图界面
     */
    protected void gotoMapActivity(){
        //转到地图界面
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(MainActivity.this,
                        MapActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, 
                        android.R.anim.fade_out);
            }
        }, 1500);
    }
    
    class MyThread extends Thread {
    	private int temp = 0;
        @Override
        public void run() {
            while (flag) {
                if(getReadyToUpdate){
                   	Log.i("index-->", " " + index);
                   	if(index == 0){
                   		shouldUpdate = versionDao.updateLocalXml();
                   		handler.obtainMessage(0).sendToTarget();
                   		index ++;
                   	}else if(index > 0 && index <= 8 ){
                   	    //下载XML文件
                        String url = base_url + files[index - 1];
                        int temp = HttpDownLoader.downloadFileToRom(url,
                        		"xml",files[index - 1]);
                        if(temp == -1){
                            flag = false;
                            index = 20;
                            Toast toast = Toast.makeText(context, 
                                        "网络错误，请检查网络连接", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        handler.obtainMessage(1).sendToTarget();
                   	}else if(index < 20){
                   		handler.obtainMessage(1).sendToTarget();
                   	}                    
                    getReadyToUpdate = false;
                }
                try {
                    Thread.sleep(1500);
                    Log.i("Thread-->", "Thread is RUN");
                    if(!getReadyToUpdate){
                    	temp ++;
                    	if(temp > 3){
                    		getReadyToUpdate = true;
                    		temp = 0;
                    	}
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    };
    
    @Override
    protected void onStop() {
        // 关闭数据库
        database.close();
        
        // 关闭线程
        flag = false;
        super.onStop();
    }	
}