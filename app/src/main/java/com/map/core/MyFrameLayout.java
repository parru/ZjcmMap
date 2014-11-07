package com.map.core;

/**
 * Created by pingfu on 14/11/7.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {
    private final String TAG = "MyFrameLayout";
    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, TAG);
    }

//     @Override
//     public boolean onInterceptTouchEvent(MotionEvent ev) {
//         int action = ev.getAction();
//         switch(action){
//         case MotionEvent.ACTION_DOWN:
//              Log.e(TAG,"onInterceptTouchEvent action:ACTION_DOWN");
////              return true; 在这就拦截了，后面的就不会得到事件
//              break;
//		case MotionEvent.ACTION_MOVE:
//              Log.e(TAG,"onInterceptTouchEvent action:ACTION_MOVE");
//              break;
//         case MotionEvent.ACTION_UP:
//              Log.e(TAG,"onInterceptTouchEvent action:ACTION_UP");
//              break;
//         case MotionEvent.ACTION_CANCEL:
//              Log.e(TAG,"onInterceptTouchEvent action:ACTION_CANCEL");
//              break;
//         }
//         return false;
//     }
//
//     @Override
//     public boolean onTouchEvent(MotionEvent ev) {
//         int action = ev.getAction();
//         switch(action){
//         case MotionEvent.ACTION_DOWN:
//              Log.e(TAG,"onTouchEvent action:ACTION_DOWN");
//              break;
//         case MotionEvent.ACTION_MOVE:
//              Log.e(TAG,"onTouchEvent action:ACTION_MOVE");
//              break;
//         case MotionEvent.ACTION_UP:
//              Log.e(TAG,"onTouchEvent action:ACTION_UP");
//              break;
//         case MotionEvent.ACTION_CANCEL:
//              Log.e(TAG,"onTouchEvent action:ACTION_CANCEL");
//              break;
//         }
////         return true;
//         return false;
//     }
//
//     @Override
//     protected void onLayout(boolean changed, int l, int t, int r, int b) {
//         // TODO Auto-generated method stub
//         super.onLayout(changed, l, t, r, b);
//     }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 基本的规则是：
     * 1.down事件首先会传递到onInterceptTouchEvent()方法
     *
     * 2.如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return false(不拦截)，
     * 那么后续的move, up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
     *
     * 3.如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return true(拦截，那么后面的move,up事件不需要在看因为已经拦截了
     * 我们直接拿去处理onTouchEvent()就可以了)，那么后续的move, up等事件将不再传递给onInterceptTouchEvent()，
     * 而是和down事件一样传递给该ViewGroup的onTouchEvent()处理，注意，目标view将接收不到任何事件。
     * 1:LayoutView1(31375): onInterceptTouchEvent action:ACTION_DOWN
     * 2:LayoutView2(31375): onInterceptTouchEvent action:ACTION_DOWN
     * 3:LayoutView2(31375): onTouchEvent action:ACTION_DOWN
     * 4:LayoutView1(31375): onInterceptTouchEvent action:ACTION_MOVE
     * 5:LayoutView2(31375): onTouchEvent action:ACTION_MOVE
     * 6:LayoutView1(31375): onInterceptTouchEvent action:ACTION_MOVE
     * 7:LayoutView2(31375): onTouchEvent action:ACTION_MOVE
     * 8:LayoutView1(31375): onInterceptTouchEvent action:ACTION_UP
     * 9:LayoutView2(31375): onTouchEvent action:ACTION_UP
     * 该设置为：
     * onInterceptTouchEvent：LayoutView1为false,LayoutView2为true
     * onTouchEvent：LayoutView2为true
     * 故而事件在LayoutView2时被拦截并处理，根据上面说法就是LayoutView2后续的MOVE,UP操作都不在经过onInterceptTouchEvent，直接
     * 交给onTouchEvent处理，结果也的确如此。（见：LayoutView2的3,5,7,9，第一次是onInterceptTouchEvent处理如1，以后交给onTouchEvent）
     * 而LayoutView1都还是要经过onInterceptTouchEvent(见LayoutView1的4,6,8)
     *
     * 4.如果最终需要处理事件的view的onTouchEvent()返回了false(没能处理这个事件，不能丢在传回来让父继续)，
     * 那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
     * **************************************************************************
     * 感觉向一个圈，然后一直在找一个能处理这个消息的人，如果找到了就结束，没找到就循环，知道回到发出消息的那个人
     * 没有标注的DOWN表示拦截事件onInterceptTouchEvent，标注了onTouchEvent就是处理事件
     * 如果都没处理: A(DOWN)-->B(DOWN)-->C(onTouchEvent DOWN)-->B(onTouchEvent DOWN)-->A(onTouchEvent DOWN),没有执行UP事件，注意有MOVE的话，在DOWN和UP之间,下面的都一样。
     * B处理：A(DOWN)-->B(DOWN)-->B(onTouchEvent)-->A(onTouchEvent UP)-->B(onTouchEvent UP)-->(over)
     * 例子：如果父亲不拦截消息就传给儿子，如果儿子要这个消息就处理(DOWN)，结束,然后有父亲1--父亲2--儿子以此释放消息(UP)。
     * 然是如果儿子对这个消息置之不理，那这个消息又传回父亲，由父亲来处理即
     * 11** 父亲1(LayoutView1不拦截false)---父亲2(LayoutView2不拦截false)--儿子(MyTextView，onTouchEvent return true)--结束
     * 22** 父亲1(LayoutView1不拦截false)---父亲2(LayoutView2不拦截false)--儿子(MyTextView，onTouchEvent return false)--回传给父亲2(onTouchEvent return true)--结束
     * 33** 父亲1(LayoutView1不拦截false)---父亲2(LayoutView2不拦截false)--儿子(MyTextView，onTouchEvent return false)--回传给父亲2(onTouchEvent return false)--父亲1(onTouchEvent return true)--结束(如果都没处理不在执行UP ACTION)
     * 44** 父亲1(LayoutView1拦截true)--父亲1(onTouchEvent return true)--结束          (DOWN--DOWN(onTouchEvent)--UP(onTouchEvent))
     * 55** 父亲1(LayoutView1拦截false)--父亲2(LayoutView2拦截true)--父亲2(onTouchEvent return false)--父亲1(onTouchEvent return true)--结束      (DOWN1--DOWN2--DOWN(2 onTouchEvent)--DOWN(1 onTouchEvent)--UP(1 onTouchEvent))(1:父亲2,2：父亲2)
     *
     * ***************************************************************************
     * 5.如果最终需要处理事件的view 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理。
     */
}
