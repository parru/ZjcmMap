package com.cucmap.data.road;

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

import com.cucmap.data.TBManager;
import com.cucmap.memory.FileManager;

public class RoadDao extends TBManager {
	public static final String TABLE_NAME = "road";
	public static final String FILE_NAME = "Road.xml";
	public static final String ID = "road_id";
	public static final String NAME = "road_name";
	public static final String WEIGHT = "weight";
	public static final String TYPE = "road_type";
	public static final String CODE = "road_code";
    private SQLiteDatabase database;
    
    public RoadDao(SQLiteDatabase database){
    	super(database);
    	this.database = database;
    }
    
    @Override
    public void createTable() {
        // TODO Auto-generated method stub
        String sql = "create table " + TABLE_NAME + "(";
        sql += ID + " INTEGER primary key,";
        sql += NAME + " TEXT,";
        sql += WEIGHT + " REAL,";
        sql += TYPE + " INTEGER,";
        sql += CODE + " TEXT";
        sql += ")";
        database.execSQL(sql);
        
    }

    @Override
    public void dropTable() {
        // TODO Auto-generated method stub
        if(tableIsExist(TABLE_NAME)){
            String sql = "drop table " + TABLE_NAME;
            database.execSQL(sql);
        }
    }
    
    public void insertElem(Road road){
        if(road == null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ID, road.getId());
        values.put(NAME, road.getName());
        values.put(TYPE, road.getType());
        values.put(WEIGHT, road.getWeight());
        values.put(CODE, road.getCode());
        database.insert(TABLE_NAME, null, values);
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
    
    /**
     * 根据道路的ID查找道路信息
     * @param id 道路的id
     * @return road 一条道路
     */
    public Road findRoadById(int id){
    	Road road = null;
    	Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
    	if(cursor.moveToFirst()){
    		do{
    			int r_id = cursor.getInt(0);
    			String r_name = cursor.getString(1);
    			double r_weight = cursor.getDouble(2);
    			int r_type = Integer.parseInt(cursor.getString(3));
    			String r_code = cursor.getString(4);
    			if(r_id == id){
    				road = new Road(r_id, r_name, r_weight, r_type, r_code);
    				break;
    			}
    			
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return road;
    }
    
    /*
     * 根据道路的code查找道路信息
     * @param code 道路的编号
     * @return road 一条道路
     */
    public Road findRoadByCode(String code){
    	Road road = null;
    	Cursor cursor = database.query("road", null, null, null, null, null, null);
    	if(cursor.moveToFirst()){
    		do{
    			int r_id = cursor.getInt(0);
    			String r_name = cursor.getString(1);
    			double r_weight = cursor.getDouble(2);
    			int r_type = Integer.parseInt(cursor.getString(3));
    			String r_code = cursor.getString(4);
    			if(r_code.equals(code)){
    				road = new Road(r_id, r_name, r_weight, r_type, r_code);
    				break;
    			}
    			
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return road;
    }
    
    /*
     * 得到所有的道路
     * @return roads 道路的列表
     */
    public List<Road> findAllRoadsInMap(){
    	List<Road> roads = new ArrayList<Road>();
    	
    	if(database == null){
    		Log.i("tableInRoad-->","in roadDao, can not find database");
    	}
    	
    	Cursor cursor = database.query("road", null, null, null, null, null, null);
    	Log.i("FindRoad-->","#############");
    	if(cursor.moveToFirst()){
    		do{
    			int r_id = Integer.parseInt(cursor.getString(0));
    			String r_name = cursor.getString(1);
    			double r_weight = Double.parseDouble(cursor.getString(2));
    			int r_type = Integer.parseInt(cursor.getString(3));
    			String r_code = cursor.getString(4);
    			Road road = new Road(r_id, r_name, r_weight, r_type, r_code);
    			roads.add(road);
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return roads;
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
            Road road = null;
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                
                case XmlPullParser.START_TAG:  //标签开始
                    
                    String tag = xmlParser.getName();
                    if(tag.equals(TABLE_NAME)){
                        // 如果是user标签开始，则说明需要实例化对象了
                        road = new Road();
                    }
                    
                    if(tag.equalsIgnoreCase(ID)){
                        // 取出User标签中的一些属性值
                        String id = xmlParser.nextText();
                        road.setId(Integer.parseInt(id));
                    }
                    
                    if(tag.equalsIgnoreCase(NAME)){
                        String name = xmlParser.nextText();
                        road.setName(name);
                    }
                    
                    if(tag.equalsIgnoreCase(TYPE)){
                        String type = xmlParser.nextText();
                        road.setType(Integer.parseInt(type));
                    }
                    
                    if(tag.equalsIgnoreCase(WEIGHT)){
                        String weight = xmlParser.nextText();
                        road.setWeight(Double.parseDouble(weight));
                    }
                    
                    if(tag.equalsIgnoreCase(CODE)){
                        String code = xmlParser.nextText();
                        road.setCode(code);
                    }
                    
                    break;
                    
                case XmlPullParser.END_TAG:
                    // 如果遇到river标签结束，则把river对象添加进集合中
                    if (xmlParser.getName().equals(TABLE_NAME)) {
                        if(road != null){
                            insertElem(road);
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
