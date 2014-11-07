package com.cucmap.mark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zjcmmap.R;


public class ScoreImage extends ImageView {
    private double score;
    public ScoreImage(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        // TODO Auto-generated constructor stub
        this.score = 0;
    }
    
    /**
     * 当前建筑得到的分数
     * @param score  建筑的评分（0--100）,最终转换成满分5分的double类型
     */
    public void setScore(int score){
        double d = (double)score / 20.0;
        this.score = d;
        invalidate();
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        drawScore(canvas, new Paint());
        super.onDraw(canvas);
    }

    /**
     * 根据当前的评分生成图像
     * @param canvas  当前画布
     * @param paint   当前画笔
     */
    protected void drawScore(Canvas canvas, Paint paint){
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), 
                R.drawable.place_ratingbar_full_filled);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), 
                R.drawable.place_ratingbar_half_full);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.place_ratingbar_full_empty);
        double d = score;
        for(int i = 0; i < 5; i++){
            RectF rectf = new RectF(i * 20, 0, i * 20 + 16, 16);
            if(d >= 1){
                canvas.drawBitmap(bitmap1, null, rectf, paint);
            }else if(d > 0 && d < 1){
                canvas.drawBitmap(bitmap2, null, rectf, paint);
            }else{
                canvas.drawBitmap(bitmap3, null, rectf, paint);
            }
            d --;
        }
    }
    

}
