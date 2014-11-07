package com.cucmap.data.road;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import com.cucmap.data.TBManager;
import com.cucmap.memory.FileManager;

public class RoadPositonDao extends TBManager {
	public static final String TABLE_NAME = "roadPositions";
	public static final String FILE_NAME = "RoadPositions.xml";
	public static final String ROAD_ID = "road_id";
	public static final String POS_ID = "pos_id";
	public static final String START = "start";
	
	private SQLiteDatabase database;
	public RoadPositonDao(SQLiteDatabase database) {
		super(database);
		// TODO Auto-generated constructor stub
		this.database = database;
	}

	@Override
	public void createTable() {
		// TODO Auto-generated method stub
		String sql = "create table " + TABLE_NAME + "(";
		sql += ROAD_ID + " INTEGER,";
		sql += POS_ID + " INTEGER,";
		sql += START + " INTEGER,";
		sql += "primary key (" + ROAD_ID + "," + POS_ID + ")";  //设置主键
		sql += ")";
		database.execSQL(sql);
	}

	@Override
	public void dropTable() {
		// TODO Auto-generated method stub
		if(tableIsExist(TABLE_NAME)){
		    String sql = "drop table " + TABLE_NAME;
		    Log.i("drop-->",sql);
		    database.execSQL(sql);
		}
	}
	
	public void insertElem(RoadPosition roadPosition){
	    if(roadPosition == null){
	        return;
	    }
	    ContentValues values = new ContentValues();
	    values.put(ROAD_ID, roadPosition.getRoad_id());
	    values.put(POS_ID, roadPosition.getPos_id());	    
	    values.put(START, roadPosition.getStart());
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
	 * 判断道路上的点是否存在
	 * @param roadPosition
	 * @return
	 */
	public boolean isElemExists(RoadPosition roadPosition){
	    boolean flag = false;
	    if(roadPosition != null){
	        Cursor cursor = database.query(TABLE_NAME,null,null,null,null,null,null);
	        if(cursor.moveToFirst()){
	            do{
	                int r_id = cursor.getInt(cursor.getColumnIndex(ROAD_ID));
	                int p_id = cursor.getInt(cursor.getColumnIndex(POS_ID));
	                if(r_id==roadPosition.getRoad_id()&&p_id==roadPosition.getPos_id()){
	                    flag = true;
	                    break;
	                }
	            }while(cursor.moveToNext());
	        }
	        cursor.close();
	    }
	    return flag;
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
            RoadPosition roadPosition = null;
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                
                case XmlPullParser.START_TAG:  //标签开始
                    
                    String tag = xmlParser.getName();
                    if(tag.equals("roadpositions")){
                        // 如果是user标签开始，则说明需要实例化对象了
                        roadPosition = new RoadPosition();
                    }
                    
                    if(tag.equalsIgnoreCase(POS_ID)){
                        // 取出User标签中的一些属性值
                        String pos_id = xmlParser.nextText();
                        roadPosition.setPos_id(Integer.parseInt(pos_id));
                    }
                    
                    if(tag.equalsIgnoreCase(ROAD_ID)){
                        String road_id = xmlParser.nextText();
                        roadPosition.setRoad_id(Integer.parseInt(road_id));
                    }
                    
                    if(tag.equalsIgnoreCase(START)){
                        String start = xmlParser.nextText();
                        roadPosition.setStart(Integer.parseInt(start));
                    }                    
                    break;
                    
                case XmlPullParser.END_TAG:
                    // 如果遇到river标签结束，则把river对象添加进集合中
                    if (xmlParser.getName().equals("roadpositions")) {
                        if(roadPosition != null){
                            insertElem(roadPosition);
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
