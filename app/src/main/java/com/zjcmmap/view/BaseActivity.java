package com.zjcmmap.view;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjcmmap.R;

/**
 * Created by pingfu on 14/11/6.
 */
public class BaseActivity extends FragmentActivity {
    private RelativeLayout titleLayout;
    private TextView titleTxt;
    private LinearLayout titleBarItems;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.setContentView(R.layout.common_title);
        getRootView();
    }

    /**
     * 添加title
     *
     * @param titleName
     */
    protected void setTitleBar(String titleName) {
        titleTxt = (TextView) findViewById(R.id.title_txt);
        titleTxt.setText(titleName);
        showTitleBar();
    }

    /**
     *
     * @param titleName
     * @param views
     */
    protected void setTitleBar(String titleName, View... views){

    }

    @Override
    public void setContentView(int layoutResId) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        setContentView(view, false);
    }

    /**
     *
     * @param contentView
     * @param isShowTitle
     */
    private void setContentView(View contentView, boolean isShowTitle) {
        ViewGroup.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        contentLayout.addView(contentView,layoutParams);

        if (isShowTitle) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    private void getRootView() {
        titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
    }

    /**
     *
     */
    private void showTitleBar() {
        titleLayout.setVisibility(View.VISIBLE);
    }
}
