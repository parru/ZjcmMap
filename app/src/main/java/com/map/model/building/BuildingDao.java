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
import com.map.model.ld.LD;
import com.map.core.sdcard.FileManager;

public class BuildingDao extends TBManager {
	public static final String TABLE_NAME = "building";
	public static final String FILE_NAME = "Building.xml";
	
	public static final String ID = "building_id";
	public static final String NAME = "building_name";
	public static final String NUMBER = "building_number";
	public static final String ALIAS = "building_alias";
	public static final String DESCRIPTION = "building_description";
	
	private SQLiteDatabase database;
	/*
	 * 初始化成员变量
	 */
    public BuildingDao(SQLiteDatabase database){
    	super(database);
    	this.database = getDatabase();
    }
    
    @Override
    public void createTable(){
    	String sql = "create table " + TABLE_NAME + "(";
    	sql += ID + " INTEGER primary key,";
    	sql += NAME + " VARCHAR(50),";
    	sql += NUMBER + " INTEGER,";
    	sql += ALIAS + " VARCHAR(50),";
    	sql += DESCRIPTION + " VARCHAR(200)";
    	sql += ")";
    	database.execSQL(sql);
    }
    
    /**
     * 插入一条记录
     */
    public void insertElem(Building building){
    	//如果builing为空，则停止插入
    	if(building == null){
    		return;
    	}
    	ContentValues content = new ContentValues();
    	content.put(ID, building.getId());
    	content.put(NAME, building.getName());
    	content.put(NUMBER, building.getNumber());
    	content.put(ALIAS, building.getAlias());
    	content.put(DESCRIPTION, building.getDescription());
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
            Log.i(FILE_NAME,"file is null");
        }
	    
	}    
    
    /**
     * 根据名称查找建筑
     * @param name 要查询的建筑名
     * @return 一组Building对象
     */
    public List<Building> getBuildingByName(String name){
    	List<Building> buildings = new ArrayList<Building>();
    	Cursor cursor = database.query("building", null,null,null,null,null,null);
    	
    	//游标回到初始位置
    	if(cursor.moveToFirst()){
    		Log.i("countNum-->",Integer.toString(cursor.getCount()));
    		try{
    		    do{
    		    	int b_id = cursor.getInt(cursor.getColumnIndex(ID));
    		    	String b_name = cursor.getString(cursor.getColumnIndex(NAME));
    			    int b_number = cursor.getInt(cursor.getColumnIndex(NUMBER));
    			    String b_alias = cursor.getString(cursor.getColumnIndex(ALIAS));
    			    String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
    			    
    			    Building building=new Building(b_id,b_name,b_number,b_alias,description);
    			    
    			    //判断待查的建筑名称和数据库中的建筑名称相似度
    			    double d = LD.sim(name, b_name);
    			    if(d > LD.SIMILARITY){
    			    	Log.i("LD-->",Double.toString(d));
    			    	buildings.add(building);
    			    }
    			    //Log.i("BuildingNum",b_num);
    		    }while(cursor.moveToNext());
    		}catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	cursor.close();
    	return buildings;
    }
    
    /**
     * 根据建筑的编号查询建筑
     * @param number 建筑的编号
     * @return 一个Building对象
     */
    public Building getBuildingByNumber(int number){
    	Cursor cursor = database.query("building", null,null,null,null,null,null);
    	Building building = null;
    	if(cursor.moveToFirst()){
    		do{
    			int b_id = cursor.getInt(cursor.getColumnIndex(ID));
		    	String b_name = cursor.getString(cursor.getColumnIndex(NAME));
			    int b_number = cursor.getInt(cursor.getColumnIndex(NUMBER));
			    String b_alias = cursor.getString(cursor.getColumnIndex(ALIAS));
			    String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
    			if(b_number == number){
    				building = new Building(b_id,b_name,b_number,b_alias,description);
    				break;
    			}
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return building;
    }
    
    /**
     * <br/>
     * <根据建筑的id查询建筑
     * <br/>
     * @param id 建筑的id
     * @return building 一个Building对象
     */
    public Building getBuildingById(int id){
    	Cursor cursor = database.query(TABLE_NAME, null,null,null,null,null,null);
    	Building building = null;
    	if(cursor.moveToFirst()){
    		do{
    			int b_id = cursor.getInt(cursor.getColumnIndex(ID));
		    	String b_name = cursor.getString(cursor.getColumnIndex(NAME));
			    int b_number = cursor.getInt(cursor.getColumnIndex(NUMBER));
			    String b_alias = cursor.getString(cursor.getColumnIndex(ALIAS));
			    String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
    			if(b_id == id){
    				building = new Building(b_id,b_name,b_number,b_alias,description);
    				break;
    			}
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return building;
    }
    
    /**
     * 根据XML数据更新表
     * @param in    XML文件输入流
     */
	public void parserXml(InputStream in) {
	    if(in == null){
	        return;
	    }
		try {
			// 得到文件流，并设置编码方式
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setInput(in, "UTF-8");
			
			//这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			Building building = null;
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				
				case XmlPullParser.START_TAG:  //标签开始
					
					String tag = xmlParser.getName();
					if(tag.equals(TABLE_NAME)){
						// 如果是user标签开始，则说明需要实例化对象了
					    building = new Building();
					}					
					if(tag.equalsIgnoreCase(ID)){
						// 取出User标签中的一些属性值
						String id = xmlParser.nextText();
						building.setId(Integer.parseInt(id));
					}
					if(tag.equalsIgnoreCase(NAME)){
						String name = xmlParser.nextText();
						building.setName(name);
						
					}
					if(tag.equalsIgnoreCase(NUMBER)){
						String number = xmlParser.nextText();
						building.setNumber(Integer.parseInt(number));
					}
					
					if(tag.equalsIgnoreCase(ALIAS)){
						String alias = xmlParser.nextText();
						building.setAlias(alias);
					}
					
					if(tag.equalsIgnoreCase(DESCRIPTION)){
						String description = xmlParser.nextText();
						building.setDescription(description);
					}
					break;
					
				case XmlPullParser.END_TAG:
					// 如果遇到river标签结束，则把river对象添加进集合中
					if (xmlParser.getName().equals(TABLE_NAME)) {
						if(building != null){
							insertElem(building);
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
