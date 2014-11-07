package com.cucmap.data.road;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RoadMap implements Runnable {
    private SQLiteDatabase database;
	private int roadMap[][];
	private boolean isFinished = false;
	
	public RoadMap(SQLiteDatabase database){
		this.database = database;
	}
    
    public void initRoadMap(SQLiteDatabase database){
    	roadMap = new int[200][200];
    	for(int i=0;i<200;i++){
    		for(int j=0;j<200;j++){
    			roadMap[i][j] = 1000;
    		}
    	}
    	List<Road> roads = new RoadDao(database).findAllRoadsInMap();
    	Log.i("RoadSize-->",Integer.toString(roads.size()));
    	for(int i = 0; i < roads.size(); i++){
    		Road road = roads.get(i);
    		String road_code = road.getCode();
    		String a1 = road_code.substring(0,3);
    		String a2 = road_code.substring(3,road_code.length());
    		int a = Integer.parseInt(a1);
    		int b = Integer.parseInt(a2);
    		int weight = (int) Math.round(road.getWeight());
    		roadMap[a][b] = weight;
    		roadMap[b][a] = weight;
    	}
    	Log.i("RoadFinished-->",Integer.toString(roads.size()));
    	isFinished = true;
    }
    
    public int[][] getRoadMap(){
    	return this.roadMap;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!isFinished){
			initRoadMap(database);
			Log.i("InitRoadMap-->","Finished");
		}
		isFinished = true;
	}
	
	public boolean isFinished(){
		return this.isFinished;
	}
}
