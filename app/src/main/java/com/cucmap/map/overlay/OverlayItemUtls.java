package com.cucmap.map.overlay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Camera.Size;

import com.cucmap.data.building.BuildingMark;
import com.cucmap.data.facility.FacilityMark;
import com.cucmap.data.position.Position;
import com.cucmap.data.road.RoadMark;
import com.cucmap.map.GeoPoint;
import com.cucmap.map.MapView;
import com.cucmap.map.mark.ItemMark;
import com.cucmap.utils.SearchRoadUtil;

public class OverlayItemUtls {
    private MapView mapView;
    
    public OverlayItemUtls(MapView mapView){
    	this.mapView = mapView;
    }
	
	/*
	 * 根据道路生成Items对象，模仿Baidu样式
	 * @param roadMarks 道路的集合
	 * @return items    返回道路上的标志点列表（有问题）
	 */
    public List<OverlayItem> getItemsByRoadMarks(
    		List<RoadMark> roadMarks){
    	List<OverlayItem> items = new ArrayList<OverlayItem>();
 
    	int mapHeight = mapView.getMapHeight();
    	int mapWidth = mapView.getMapWidth();
    	int turn = 0;
    	for(int i=0; i < roadMarks.size(); i++){
    		RoadMark roadMark = roadMarks.get(i);
    		GeoPoint p1 = GeoPoint.getGeoPoint(roadMark.getBeginPosition());
    		GeoPoint p2 = GeoPoint.getGeoPoint(roadMark.getEndPosition());
    		
    		String title = roadMark.getRoad().getName();
    		String description = "";
    		Context context = mapView.getContext();
    		int weight = (int)Math.round(roadMark.getRoad().getWeight());
    		
    		if(i == 0){
    			description = "终点";
    			turn = OverlayItemConfig.TURN_END;
    		}else {
    			int dx = p2.getMapX(mapWidth) - p1.getMapX(mapWidth);
        		int dy = p2.getMapY(mapHeight) - p2.getMapY(mapHeight);
    			if(dy > 0 && Math.abs(dy) > Math.abs(dx)){
    				description = "向上走" + Integer.toString(weight) + "米";
    				turn = OverlayItemConfig.TURN_UP;
        		}else if( dy < 0 && Math.abs(dy) > Math.abs(dx)){
        			description = "向下走" + Integer.toString(weight) + "米";
        			turn = OverlayItemConfig.TURN_DOWN;
        		}else if(dx > 0 && Math.abs(dy) < Math.abs(dx)){
        			description = "向右走" + Integer.toString(weight) + "米";
        			turn = OverlayItemConfig.TURN_RIGHT;
        		}else{
        			description = "向左走" + Integer.toString(weight) + "米";
        			turn = OverlayItemConfig.TURN_LEFT;
        		}
    		}
    		OverlayItem item = new OverlayItem(p2, description, title, 0,context);
    		item.setClickable(true);
    		item.setItemType(OverlayItemConfig.ROAD_ITEM_TYPE);
    		item.setMarker(item.getMarkByDirection(turn));
    		items.add(item);
    		if(i == roadMarks.size() - 1){
    			description = "起点";
    			turn = OverlayItemConfig.TURN_START;
    			OverlayItem item2 = new OverlayItem(p1, description, title, 0,context);
    			item.setClickable(true);
        		item.setItemType(OverlayItemConfig.ROAD_ITEM_TYPE);
        		item.setMarker(item.getMarkByDirection(turn));
        		items.add(item2);
    		}
    	}
    	return items;
    }
    
    /*
	 * 根据最短路径生成Items对象，模仿Baidu样式
	 * @param  path    使用最短路径算法获取的信息
	 * @return items   返回道路上的标志列表
	 */
    public List<OverlayItem> getItemsByPath(List<Position> path){
    	List<OverlayItem> items = new ArrayList<OverlayItem>();
    	int size = path.size();
    	int mapWidth = mapView.getMapWidth();
    	int mapHeight = mapView.getMapHeight();
    	int weight = 0;
    	for(int i=0; i < size; i++){
    		int turn = 0;
    		String description = "";
    		if(i == 0){
    			description = "起点";
    			turn = OverlayItemConfig.TURN_START;
    			weight = 100;
    		}else if(i == size - 1){
    			turn = OverlayItemConfig.TURN_END;
    			description = "终点";
    			weight = 100;
    		}else{
    			Position p1 = path.get(i);
    			Position p2 = path.get( i + 1);
    			weight = SearchRoadUtil.getDistance(p1, p2);
    			GeoPoint gp1 = GeoPoint.getGeoPoint(p1);
    			GeoPoint gp2 = GeoPoint.getGeoPoint(p2);
    			int dx = gp2.getMapX(mapWidth) - gp1.getMapX(mapWidth);
    			int dy = gp2.getMapY(mapHeight) - gp1.getMapY(mapHeight);
    			if(Math.abs(dx) > Math.abs(dy)){
    				if(dx > 0){
    					description = "向东走" + Integer.toString(weight) + "米";
    					turn = OverlayItemConfig.TURN_RIGHT;
    				}else{
    					description = "向西走" + Integer.toString(weight) + "米";
    					turn = OverlayItemConfig.TURN_LEFT;
    				}
    			}else{
    				if(dy > 0){
    					description = "向南走" + Integer.toString(weight) + "米";
    					turn = OverlayItemConfig.TURN_DOWN;
    				}else{
    					description = "向北走" + Integer.toString(weight) + "米";
    					turn = OverlayItemConfig.TURN_UP;
    				}
    			}
    		}//else
    		GeoPoint point = GeoPoint.getGeoPoint(path.get(i));
    		OverlayItem item = new OverlayItem(point, description, 
        			"", 0, mapView.getContext());
    		if(weight > 20){
				item.setClickable(true);
    			item.setItemType(OverlayItemConfig.ROAD_ITEM_TYPE);
    			item.setMarker(item.getMarkByDirection(turn));
			}    		
    		items.add(item);
    	}
    	return items;
    }
    
    /**
     * 根据Itemmarks生成Overlay的Mark类型
     * @param itemMarks
     * @return
     */
    public List<OverlayItem> getItemsByItemMarks(List<ItemMark> itemMarks){
    	List<OverlayItem> items = new ArrayList<OverlayItem>();
    	Context context = mapView.getContext();
    	for(ItemMark itemMark:itemMarks){
    		BuildingMark buildingMark = itemMark.getBuildingMark();
    		GeoPoint point = GeoPoint.getGeoPoint(buildingMark.getPosition());
    		String description = itemMark.getDescription();
    		String title = itemMark.getName();
    		int item_type = 0;
    		OverlayItem item = new OverlayItem(point, description, title, 
    		        item_type, context);
    		items.add(item);
    	}
    	return items;
    }
    
    /**
     * 根据BuildingMarks生成图层上的标志列表
     * @param buildingMarks   建筑的列表
     * @return 生成的图层上的标志列表
     */
    public List<OverlayItem> getItemsByBuildingMarks(List<BuildingMark> buildingMarks){
        List<OverlayItem> items = new ArrayList<OverlayItem>();
        Context context = mapView.getContext();
        for(BuildingMark buildingMark: buildingMarks){
            GeoPoint point = GeoPoint.getGeoPoint(buildingMark.getPosition());
            String description = buildingMark.getBuilding().getDescription();
            String title = buildingMark.getBuilding().getName();
            int item_type = OverlayItemConfig.BUILDING_ITEM_TYPE;
            OverlayItem item = new OverlayItem(point, description, title, 
                    item_type, context);
            items.add(item);
        }
        return items;
    }
    
    /**
     * 根据FacilityMarks生成图层上的标志列表
     * @param facilityMarks   部门的列表
     * @return 生成的图层上的标志列表
     */
    public List<OverlayItem> getItemsByFacilityMarks(List<FacilityMark> facilityMarks){
        List<OverlayItem> items = new ArrayList<OverlayItem>();
        Context context = mapView.getContext();
        for(FacilityMark facilityMark: facilityMarks){
            GeoPoint point = GeoPoint.getGeoPoint(
                    facilityMark.getBuildingMark().getPosition());
            String description = facilityMark.getFacility().getName();
            String title = facilityMark.getFacility().getName();
            int item_type = OverlayItemConfig.FACILITY_ITEM_TYPE;
            OverlayItem item = new OverlayItem(point, description, title, 
                    item_type, context);
            items.add(item);
        }
        return items;
    }
}
