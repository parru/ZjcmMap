package com.map.model.building;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.map.model.TBManager;
import com.map.core.update.sdcard.FileManager;

/*
 * 
 * @author Pingfu
 */
public class BuildingFacilityDao extends TBManager {
	public static final String TABLE_NAME = "buildingFacilities";
	public static final String FILE_NAME = "BuildingFacilities.xml";
	public static final String BUILDING_ID = "building_id";
	public static final String FACILITY_ID = "facility_id";	
	
    private SQLiteDatabase database;
    
    public BuildingFacilityDao(SQLiteDatabase database){
    	super(database);
    	this.database = database;
    }
    
	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		String sql = "create table " + TABLE_NAME + "(";
		sql += BUILDING_ID + " INTEGER,";
		sql += FACILITY_ID + " INTEGER,";
		sql += "primary key (" + BUILDING_ID + "," + FACILITY_ID + ")";  //设置主键
		sql += ")";
		database.execSQL(sql);
	}
	
	public void insertElem(BuildingFacility buildingFacility){
		if(buildingFacility == null){
			return;
		}
		ContentValues content = new ContentValues();
		content.put(BUILDING_ID, buildingFacility.getBuilding_id());
		content.put(FACILITY_ID, buildingFacility.getFacility_id());
		database.insert(TABLE_NAME, null, content);
	}
	
	@Override
	public void dropTable() {
		// TODO Auto-generated method stub
		if(tableIsExist(TABLE_NAME)){
		    Log.i("drop table",TABLE_NAME);
			String sql = "drop table " + TABLE_NAME;
			database.execSQL(sql);
		}		
	}

	@Override
	public void updateTable() {
		// TODO Auto-generated method stub
	    FileManager fileManager = new FileManager();
        InputStream input = fileManager.readFileFromRAM(fileManager.XML_DIR, FILE_NAME);
		if(input != null){
		    dropTable();
	        createTable();
	        parserXml(input); 
		}else{
		    Log.i(FILE_NAME,"null");
		}
	}
    
    /**
     * 根据Facility的id得到Building的id
     * @param facility_id Facility的id
     * @return Building的id
     */
    public int getBuildingIdByFacilityId(int facility_id){
    	int id = 0;
    	Cursor cursor = database.query(TABLE_NAME, null,null,null,null,null,null);
    	if(cursor.moveToFirst()){
    		do{
    			int b_id = Integer.parseInt(cursor.getString(0));
    			int f_id = Integer.parseInt(cursor.getString(1));
    			if(f_id == facility_id){
    				id = b_id;
    				break;
    			}
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return id;
    }
    
    /**
     * 根据Building的id得到Facility的id
     * @param  building_id Building的id
     * @return Facility的id
     */
    public List<Integer> getFacilityIdByBuildingId(int building_id){
    	List<Integer> list = new ArrayList<Integer>();
    	Cursor cursor = database.query(TABLE_NAME, null,null,null,null,null,null);
    	if(cursor.moveToFirst()){
    		do{
    			int b_id = Integer.parseInt(cursor.getString(0));
    			int f_id = Integer.parseInt(cursor.getString(1));
    			if(b_id == building_id){
    				list.add(f_id);
    			}
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return list;
    }
    
    public void parserXml(InputStream input){
    	// 得到文件流，并设置编码方式
		XmlPullParser xmlParser = Xml.newPullParser();
		
		try {
			xmlParser.setInput(input, "UTF-8");
			//这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			BuildingFacility buildingFacility = null;
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				
				case XmlPullParser.START_TAG:  //标签开始
					
					String tag = xmlParser.getName();
					if(tag.equals("buildingfacilities")){
						// 如果是user标签开始，则说明需要实例化对象了
					    buildingFacility = new BuildingFacility();
					}					
					if(tag.equalsIgnoreCase(BUILDING_ID)){
						// 取出User标签中的一些属性值
						String b_id = xmlParser.nextText();
						buildingFacility.setBuilding_id(Integer.parseInt(b_id));
					}
					if(tag.equalsIgnoreCase(FACILITY_ID)){
						String f_id = xmlParser.nextText();
						buildingFacility.setFacility_id(Integer.parseInt(f_id));
					}
					break;
					
				case XmlPullParser.END_TAG:
					// 如果遇到river标签结束，则把river对象添加进集合中
					if (xmlParser.getName().equals("buildingfacilities")) {
						if(buildingFacility != null){
							insertElem(buildingFacility);
						}
					}
					break;					
				default:
					break;
				}
				evtType = xmlParser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
