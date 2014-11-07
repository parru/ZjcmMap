package com.map.core.map.overlay;

import com.map.core.map.MapView;

public class OverlayFactory {
    public static final int ITEMIZED_OVERLAYER = 1;
    public static final int ROUTE_OVERLAYER = 2;
    
    public static Overlay getOverLayer(int overlayer_type,MapView mapView){
    	switch (overlayer_type) {
		case ITEMIZED_OVERLAYER:
			return new ItemizedOverlay(mapView);
		case ROUTE_OVERLAYER:
			return new RouteOverlay(mapView);
		default:
			return null;
		}
    }
}
