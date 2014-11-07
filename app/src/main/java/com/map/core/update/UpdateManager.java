package com.map.core.update;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.map.model.TBManager;
import com.map.model.building.BuildingDao;
import com.map.model.building.BuildingFacilityDao;
import com.map.model.building.BuildingPositionDao;
import com.map.core.http.HttpDownLoader;

/**
 * 更新管理
 * @author Eddy
 */
public class UpdateManager {
    public static final String BUILDING = "building";
    public static final String BUILDING_FACILITY = "buildingFacilties";
    public static final String BUILDING_POSITION = "buildingPositions";
    public static final String CATEGORY = "category";
    public static final String FACILITY = "facility";
    public static final String POSITION = "position";
    public static final String ROAD = "road";
    public static final String ROAD_POSITION = "roadPositions";
    
    public static final String BASE_URL = "http://mom.cuc.edu.cn/xml";
    public static final String FILE_PATH = "xml";
    
    private Map<String, Boolean>updateMap;
    
    public UpdateManager(Map<String, Boolean>updateMap){
        this.updateMap = updateMap;
    }
    
    /**
     * 更新本地的xml文件
     * @return 如果成功返回true，否则返回false
     */
    public boolean updateLocalXml(){
        boolean flag = true;
        for(Map.Entry<String, Boolean>entry: updateMap.entrySet()){
            if(entry.getValue()){
                String filename = entry.getKey() + ".xml";
                String urlStr = BASE_URL + "/" + filename;
                int i = HttpDownLoader.downloadFileToRom(urlStr, FILE_PATH, filename);
                if( i == -1){
                    flag = false;
                }
            }
        }
        return flag;
    }
    
    /**
     * 
     * @param table_name
     * @return
     */
    public void updateTable(SQLiteDatabase database){
        TBManager tbManager = null;
        for(Map.Entry<String, Boolean>entry: updateMap.entrySet()){
            String table_name = entry.getKey();
            if(entry.getValue()){
                if(table_name.equalsIgnoreCase(BUILDING)){
                    tbManager = new BuildingDao(database);
                }
                if(table_name.equalsIgnoreCase(BUILDING_FACILITY)){
                    tbManager = new BuildingFacilityDao(database);
                }
                if(table_name.equalsIgnoreCase(BUILDING_POSITION)){
                    tbManager = new BuildingPositionDao(database);
                }
                tbManager.updateTable();
            }
        }       
       
    }
  
}
