package com.cucmap.animation;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

@SuppressLint("NewApi")
public class Animation {

	/** 上一帧播放时间 **/
	private long mLastPlayTime = 0;
	/** 播放当前帧的ID **/
	private int mPlayID = 0;
	/** 动画frame数量 **/
	private int mFrameCount = 0;
	/** 用于储存动画资源图片 **/
	private Bitmap[] mframeBitmap = null;
	/** 是否循环播放 **/
	private boolean mIsLoop = false;
	/** 播放结束 **/
	private boolean mIsend = false;
	/** 动画播放间隙时间 **/
	private static final int ANIM_TIME = 100;

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param frameBitmapID
	 * @param isloop
	 */
	public Animation(Context context, int[] frameBitmapID, boolean isloop) {
		mFrameCount = frameBitmapID.length;
		mframeBitmap = new Bitmap[mFrameCount];
		for (int i = 0; i < mFrameCount; i++) {
			mframeBitmap[i] = readBitMap(context, frameBitmapID[i]);
		}
		mIsLoop = isloop;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param frameBitmap
	 * @param isloop
	 */
	public Animation(Context context, Bitmap[] frameBitmap, boolean isloop) {
		mFrameCount = frameBitmap.length;
		mframeBitmap = frameBitmap;
		mIsLoop = isloop;
	}

	/**
	 * 绘制动画中的其中一帧
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 * @param frameID
	 */
	public void drawFrame(Canvas Canvas, Paint paint, int x, int y, int frameID) {
		Canvas.drawBitmap(mframeBitmap[frameID], x, y, paint);
	}

	/**
	 * 绘制动画
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void drawAnimation(Canvas canvas, Paint paint, int x, int y) {
		// 如果没有播放结束则继续播放
		if (!mIsend) {
			canvas.drawBitmap(mframeBitmap[mPlayID], x, y, paint);
			long time = System.currentTimeMillis();
			if (time - mLastPlayTime > ANIM_TIME) {
				mPlayID++;
				mLastPlayTime = time;
				if (mPlayID >= mFrameCount) {
					// 标志动画播放结束
					mIsend = true;
					if (mIsLoop) {
						// 设置循环播放
						mIsend = false;
						mPlayID = 0;
					}
				}
			}
		}
	}
	
	public void upDateAnimation(){
		//this.mPlayID++;
		this.mPlayID = this.mPlayID % this.mFrameCount;
	}

	/**
	 * 读取图片资源
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.DONUT)
	@SuppressLint("NewApi")
	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
}
