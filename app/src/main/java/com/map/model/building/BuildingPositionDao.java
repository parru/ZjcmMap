package com.map.model.building;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.map.model.TBManager;
import com.map.model.position.Position;
import com.map.model.position.PositionDao;
import com.map.core.update.sdcard.FileManager;
import com.map.core.common.SearchRoadUtil;

public class BuildingPositionDao extends TBManager {
	public static final String TABLE_NAME = "buildingPositions";
	public static final String FILE_NAME = "BuildingPositions.xml";
	public static final String BUILDING_ID = "building_id";
	public static final String POSITION_ID = "pos_id";
	public static final String TYPE = "buildingPos_type";
	
    private SQLiteDatabase database;
    public int BUILDING_FOR_MARK = 1;    //用于标注建筑的位置
    public int BUILDING_ROR_ROUTE = 0;   //用于标注建筑的最近的点
    
    public BuildingPositionDao(SQLiteDatabase database){
    	super(database);
    	this.database = database;
    }
    
    @Override
	public void createTable() {
		// TODO Auto-generated method stub
	    String sql = "create table " + TABLE_NAME + "(";
	    sql += BUILDING_ID + " integer,";
	    sql += POSITION_ID + " integer,";
	    sql += TYPE + " integer,";
	    sql += "primary key (" + BUILDING_ID + "," + POSITION_ID + ")";  //设置主键
	    sql += ")";
	    database.execSQL(sql);
	}
    
    public void insertElem(BuildingPosition buildingPosition){
    	if(buildingPosition == null){
    		return;
    	}
    	ContentValues content = new ContentValues();
    	content.put(BUILDING_ID, buildingPosition.getBuilding_id());
    	content.put(POSITION_ID, buildingPosition.getPoint_id());
    	content.put(TYPE, buildingPosition.getType());
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
            Log.i(FILE_NAME, "file is null");
        }
	}
    
    /*
     * 查找id为building_id的建筑所对应类型为type的坐标点
     * @param building_id 建筑的编号
     * @param type 坐标点的类型
     * @return position 对应的坐标点
     */
    public Position getBuidingPosition(int building_id,int type){
    	Position position = null;
    	PositionDao positionDao = new PositionDao(database);
    	Cursor cursor=database.query(TABLE_NAME, null,null,null,null,null,null);
    	
    	if(cursor.moveToFirst()){
    		try{
				do {
					int b_id = cursor.getInt(cursor.getColumnIndex(BUILDING_ID));
					int p_id = cursor.getInt(cursor.getColumnIndex(POSITION_ID));
					int b_type = cursor.getInt(cursor.getColumnIndex(TYPE));
					if (b_id == building_id && b_type == type) {
						position = positionDao.getPositionById(p_id);
						break;
					}
				} while (cursor.moveToNext());
    		}catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	cursor.close();
    	return position;
    }
    
    /*
     * 找到距离建筑最近的道路上的点
     * @param building 要查找的建筑
     * @return Position 距离该建筑最近的道路上的点
     */
    public Position findNearestPosition(Building building){
    	Position position = null;
    	Cursor cursor=database.query("positionOfInterests",null,null,null,null,null,null);
    	if(cursor.moveToFirst()){
    		int min_distance = Integer.MAX_VALUE;
    		do{
    			int pos_id = cursor.getInt(0);
    			double longitude = cursor.getDouble(1);
    			double latitude = cursor.getDouble(2);
    			String name = cursor.getString(3);
    			Position position2 = new Position(pos_id, longitude, latitude, name);
    			if(min_distance > SearchRoadUtil.getDistance(position, position2)){
    				position = position2;
    			}    			
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return position;
    }
    
    /**
     * 根据XML数据更新表
     * @param in    XML文件输入流
     */
	public void parserXml(InputStream in) {
		try {
			// 得到文件流，并设置编码方式
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(in, "UTF-8");
			
			//这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			BuildingPosition buildingPosition = null;
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				
				case XmlPullParser.START_TAG:  //标签开始
					
					String tag = xmlParser.getName();
					if(tag.equals("buildingpositions")){
						// 如果是user标签开始，则说明需要实例化对象了
					    buildingPosition = new BuildingPosition();
					}					
					if(tag.equalsIgnoreCase(BUILDING_ID)){
						// 取出User标签中的一些属性值
						String id = xmlParser.nextText();
						buildingPosition.setBuilding_id(Integer.parseInt(id));
					}
					if(tag.equalsIgnoreCase(POSITION_ID)){
						String pos_id = xmlParser.nextText();
						buildingPosition.setPoint_id(Integer.parseInt(pos_id));
					}
					if(tag.equalsIgnoreCase(TYPE)){
						String type = xmlParser.nextText();
						buildingPosition.setType(Integer.parseInt(type));
					}
					break;
					
				case XmlPullParser.END_TAG:
					// 如果遇到river标签结束，则把river对象添加进集合中
					if (xmlParser.getName().equals("buildingpositions")) {
						if(buildingPosition != null){
							insertElem(buildingPosition);
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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}