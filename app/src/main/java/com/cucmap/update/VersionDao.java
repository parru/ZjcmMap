package com.cucmap.update;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.cucmap.data.TBManager;
import com.cucmap.http.HttpDownLoader;
import com.cucmap.memory.FileManager;

public class VersionDao extends TBManager {
    public static final String FILE_NAME = "Version.xml";
    public static final String TABLE_NAME = "version";
    public static final String ID = "id";
    public static final String BUILDING = "building";
    public static final String BUILDING_FACILITY = "buildingFacilities";
    public static final String BUILDING_POSITION = "buildingPositions";
    public static final String CATEGORY = "category";
    public static final String FACILITY = "facility";
    public static final String POSITION = "position";
    public static final String ROAD = "road";
    public static final String ROAD_POSITION = "roadPositions";
    public static final String VERSION = "dataVersion";
    
    private Context context;
    public Map<String, Boolean> updateMap;
    
    private SQLiteDatabase database;
    
    public VersionDao(SQLiteDatabase database,Context context){
        super(database);
        this.database = database;
        this.context = context;
        updateMap = new HashMap<String, Boolean>();
        updateMap.put(BUILDING, false);
        updateMap.put(BUILDING_FACILITY, false);
        updateMap.put(BUILDING_POSITION, false);
        updateMap.put(CATEGORY, false);
        updateMap.put(FACILITY, false);
        updateMap.put(POSITION, false);
        updateMap.put(ROAD, false);
        updateMap.put(ROAD_POSITION, false);
        updateMap.put(VERSION, false);
    }

    @Override
    public void createTable() {
        // TODO Auto-generated method stub
        String sql = "create table " + TABLE_NAME +"(";
        sql += ID + "integer primary key,";
        sql += BUILDING + " REAL,";
        sql += BUILDING_FACILITY + " REAL,";
        sql += BUILDING_POSITION + " REAL,";
        sql += CATEGORY + " REAL,";
        sql += FACILITY + " REAL,";
        sql += POSITION + " REAL,";
        sql += ROAD + " REAL,";
        sql += ROAD_POSITION + " REAL,";
        sql += VERSION + " REAL";
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
    
    /**
     * 判断当前数据库是否需要更新
     * @param newVersion 新的从服务器读取到Version
     * @param oldVersion 从数据库读取到的旧Version
     * @return 如果需要更新返回true，否则返回flase
     */
    public boolean ifShouldUpdate(Version newVersion, Version oldVersion){
        boolean flag = false;
        Log.i("checkedVersion-->","checked latest version");
        if(newVersion == null){
            Log.i("update-->","no new version");
            return false;
        }
        if(oldVersion == null){
            Log.i("update-->","oldversion is null");
            return true;
        }
        if(newVersion.getVersion() > oldVersion.getVersion()){
            Log.i("update-->","should update database");
            flag = true;
        }
        return flag;
    }
    
    @Override
    public void updateTable() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        Version version = getVersionFromXml();
        if(version != null){
            values.put(ID, version.getId());
            values.put(BUILDING, version.getBuilding());
            values.put(BUILDING_FACILITY, version.getBuilding_facility());
            values.put(BUILDING_POSITION, version.getBuilding_position());
            values.put(CATEGORY, version.getCategory());
            values.put(FACILITY, version.getFacility());
            values.put(POSITION, version.getPosition());
            values.put(ROAD, version.getRoad());
            values.put(ROAD_POSITION, version.getRoad_postion());
            values.put(VERSION, version.getVersion());
            String whereClause = "id=?";
            String whereArgs[] = {String.valueOf(1)};
            database.update(TABLE_NAME, values, whereClause, whereArgs);
        }
    }
    
    /**
     * 更新数据库
     * @param version
     */
    public void updateDatabase(Version newVersion, Version oldVersion){
        if(newVersion == null || oldVersion == null){
            return;
        }
        if(ifShouldUpdate(newVersion, oldVersion)){
            if(newVersion.getBuilding() > oldVersion.getBuilding()){
                updateMap.put(BUILDING, true);
            }
            if(newVersion.getBuilding_facility() > oldVersion.getBuilding_facility()){
                updateMap.put(BUILDING, true);
            }
            if(newVersion.getBuilding_position() > oldVersion.getBuilding_position()){
                updateMap.put(BUILDING_POSITION, true);
            }
            if(newVersion.getCategory() > oldVersion.getCategory()){
                updateMap.put(CATEGORY, true);
            }
            if(newVersion.getFacility() > oldVersion.getFacility()){
                updateMap.put(FACILITY, true);
            }
            if(newVersion.getPosition() > oldVersion.getPosition()){
                updateMap.put(POSITION, true);
            }
            if(newVersion.getRoad() > oldVersion.getRoad()){
                updateMap.put(ROAD, true);
            }
            if(newVersion.getRoad_postion() > oldVersion.getRoad_postion()){
                updateMap.put(ROAD_POSITION, true);
            }
        }
    }
    
    /**
     * 从数据库中查看当前的版本信息
     * @return 当前的版本信息，保存在Version对象中
     */
    public Version getVersionFromDatabase(){
        List<Version> versions = new ArrayList<Version>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                Version version = new Version();
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                double b = cursor.getDouble(cursor.getColumnIndex(BUILDING));
                double b_f = cursor.getDouble(cursor.getColumnIndex(BUILDING_FACILITY));
                double b_p = cursor.getDouble(cursor.getColumnIndex(BUILDING_POSITION));
                double c = cursor.getDouble(cursor.getColumnIndex(CATEGORY));
                double f = cursor.getDouble(cursor.getColumnIndex(FACILITY));
                double p = cursor.getDouble(cursor.getColumnIndex(POSITION));
                double r = cursor.getDouble(cursor.getColumnIndex(ROAD));
                double r_p = cursor.getDouble(cursor.getColumnIndex(ROAD_POSITION));
                double v = cursor.getDouble(cursor.getColumnIndex(VERSION));
                
                version.setId(id);
                version.setBuilding(b);
                version.setBuilding_facility(b_f);
                version.setBuilding_position(b_p);
                version.setCategory(c);
                version.setFacility(f);
                version.setPosition(p);
                version.setRoad(r);
                version.setRoad_postion(r_p);
                version.setVersion(v);
                
                versions.add(version);
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(versions.size() > 0){
            return versions.get(0);
        }
        else{
            return null;
        }
    }
    
    /**
     * 下载服务器上的XML文件，更新本地的XML数据
     * @return 成功返回true，失败返回false
     */
    public boolean updateLocalXml(){
        String url = "http://mom.cuc.edu.cn/cucmap/xml/version.xml";
        int flag = HttpDownLoader.downloadFileToRom(url, "xml", FILE_NAME);
        if(flag == -1){
//            Toast toast = Toast.makeText(context, 
//                    "网络错误，请检查网络连接", Toast.LENGTH_LONG);
//            toast.show();
            return false;
        }
        return true;
    }
    
    /**
     * 从本地文件中得到当前的最新版本信息
     * @return
     */
    public Version getVersionFromXml(){
        Version version = null;
        FileManager fileManager = new FileManager();
        InputStream input = fileManager.readFileFromRAM(fileManager.XML_DIR, FILE_NAME);
        if(input == null){
            Log.i("NULL-->","input is null");
        }else{
            version = parseXml(input); 
        }
        return version;
    }
    
    /**
     * 读取XML中的Version信息
     * @param inputStream
     * @return 返回读取到的Version值
     */
    public Version parseXml(InputStream inputStream){
        List<Version> versions = new ArrayList<Version>();
        Version version = null;
        try {
            // 得到文件流，并设置编码方式
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "UTF-8");
            
            //这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
            int evtType = xmlParser.getEventType();
            // 一直循环，直到文档结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                case XmlPullParser.START_TAG:  //标签开始
                    String tag = xmlParser.getName();
                    
                    if(tag.equalsIgnoreCase(TABLE_NAME)){
                        // 如果是version标签开始，则说明需要实例化对象了
                        version = new Version();                        
                    }
                    
                    if(tag.equalsIgnoreCase(ID)){
                        String id = xmlParser.nextText();
                        version.setId(Integer.parseInt(id));
                    }
                    
                    if(tag.equalsIgnoreCase(BUILDING)){
                        String building = xmlParser.nextText();
                        version.setBuilding(Double.parseDouble(building));   
                    }
                    
                    if(tag.equalsIgnoreCase(BUILDING_FACILITY)){
                        String b_f = xmlParser.nextText();
                        version.setBuilding_facility(Double.parseDouble(b_f));
                    }
                    
                    if(tag.equalsIgnoreCase(BUILDING_POSITION)){
                        String b_p = xmlParser.nextText();
                        version.setBuilding_position(Double.parseDouble(b_p));
                    }
                    
                    if(tag.equalsIgnoreCase(CATEGORY)){
                        String category = xmlParser.nextText();
                        version.setCategory(Double.parseDouble(category));
                    }
                    
                    if(tag.equalsIgnoreCase(POSITION)){
                        String position = xmlParser.nextText();
                        version.setPosition(Double.parseDouble(position));
                    }
                    
                    if(tag.equalsIgnoreCase(ROAD)){
                        String road = xmlParser.nextText();
                        version.setRoad(Double.parseDouble(road));
                    }
                    
                    if(tag.equalsIgnoreCase(ROAD_POSITION)){
                        String r_p = xmlParser.nextText();
                        version.setRoad_postion(Double.parseDouble(r_p));
                    }
                                    
                    if(tag.equalsIgnoreCase(VERSION)){
                        String v = xmlParser.nextText();
                        version.setVersion(Double.parseDouble(v));
                    }
                    break;
                    
                case XmlPullParser.END_TAG:
                    // 如果遇到Version标签结束，则把Version对象添加进集合中
                    if (xmlParser.getName().equalsIgnoreCase(TABLE_NAME)) {
                        versions.add(version);
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
        return versions.get(0);
    }
}
