package com.cucmap;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class ListSearchResult extends ListView {
	
	private ScrollView scrollView;
	private int maxHeight;
	
	public ListSearchResult(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ScrollView getScrollView() {
		return scrollView;
	}

	public void setScrollView(ScrollView scrollView) {
		this.scrollView = scrollView;
	}
	
	public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub

        if (maxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, 
            		MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        System.out.println(getChildAt(0));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	//当指触到listview的时候，让父ScrollView交出ontouch权限，
            	//也就是让父scrollview 停住不能滚动
                setParentScrollAble(false);
            case MotionEvent.ACTION_CANCEL:
            	//当手指松开时，让父ScrollView重新拿到onTouch权限
                setParentScrollAble(true);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {

        scrollView.requestDisallowInterceptTouchEvent(!flag);
    }
	
}
