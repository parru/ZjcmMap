package com.map.model.position;

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
import com.map.core.sdcard.FileManager;

/*
 *@author Pingfu
 * 
 */
public class PositionDao extends TBManager {
	public static final String TABLE_NAME = "position";
	public static final String FILE_NAME = "PositionOfInterest.xml";
	
	public static final String ID = "pos_id";
	public static final String NAME = "position_name";
	public static final String LON = "longitude";
	public static final String LAT = "latitude";
	
	private SQLiteDatabase database;
    
    public PositionDao(SQLiteDatabase database){
        super(database);
    	this.database = database;
    }
    
    @Override
    public void createTable() {
        // TODO Auto-generated method stub
        String sql = "create table " + TABLE_NAME + "(";
        sql += ID + " INTEGER primary key,";
        sql += NAME + " VARCHAR(50),";
        sql += LON + " FLOAT(10,6),";
        sql += LAT + " FLOAT(10,6)";
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
    
    public void insertElem(Position position){
        if(position == null){
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ID, position.getId());
        values.put(NAME, position.getName());
        values.put(LON, position.getLongitude());
        values.put(LAT, position.getLatitude());
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
            Log.i(FILE_NAME,"file is null");
        }
        
    }
    
    /**
     * 根据Position的id查找兴趣点
     * @param id Position的坐标点id
     * @return position 返回一个Position对象
     */
    public Position getPositionById(int id){
    	Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
    	Position position = null;
    	if(cursor.moveToFirst()){
    		do{
    			int p_id = cursor.getInt(0);
    			double longitude = cursor.getDouble(cursor.getColumnIndex(LON));
    			double latitude = cursor.getDouble(cursor.getColumnIndex(LAT));
    			String name = cursor.getString(cursor.getColumnIndex(NAME)); 
    			if(p_id == id){
    				position = new Position(id, longitude, latitude, name);
    				break;
    			}
    		}while(cursor.moveToNext());
    	}
    	cursor.close();
    	return position;
    }
    
    /*
     * 根据坐标点的名称查找
     * @param name 坐标点的名称
     * @return positions 返回名称类似的坐标点列表
     */
    public List<Position> getPostionByName(String name){
    	Cursor cursor = database.query("position", null, null, null, null, null, null);
    	List<Position> positions = new ArrayList<Position>();
    	cursor.close();
    	return positions;
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
            Position position = null;
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                
                case XmlPullParser.START_TAG:  //标签开始
                    
                    String tag = xmlParser.getName();
                    if(tag.equals("PositionOfinterest")){
                        // 如果是user标签开始，则说明需要实例化对象了
                        position = new Position();
                    }
                    
                    if(tag.equalsIgnoreCase(ID)){
                        // 取出User标签中的一些属性值
                        String id = xmlParser.nextText();
                        position.setId(Integer.parseInt(id));
                    }
                    
                    if(tag.equalsIgnoreCase(NAME)){
                        String name = xmlParser.nextText();
                        position.setName(name);
                    }
                    
                    if(tag.equalsIgnoreCase(LON)){
                        String longitude = xmlParser.nextText();
                        position.setLongitude(Double.parseDouble(longitude));
                    }
                    
                    if(tag.equalsIgnoreCase(LAT)){
                        String latitude = xmlParser.nextText();
                        position.setLatitude(Double.parseDouble(latitude));
                    }
                    
                    break;
                    
                case XmlPullParser.END_TAG:
                    // 如果遇到river标签结束，则把river对象添加进集合中
                    if (xmlParser.getName().equals("PositionOfinterest")) {
                        if(position != null){
                            insertElem(position);
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
