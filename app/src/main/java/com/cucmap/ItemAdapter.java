package com.cucmap;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjcmmap.R;

public class ItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Picture> pictures;
	
	private int screenWidth;
	private int screenHeight;
	
	public ItemAdapter(String[] titles, int[]images, Context context) {
		super();
		pictures = new ArrayList<Picture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < images.length; i++) {
			Picture picture = new Picture(titles[i], images[i]);
			pictures.add(picture);
		}
		WindowManager window = (WindowManager)
                (context.getSystemService(Context.WINDOW_SERVICE));
        Display display = window.getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
	}

	@Override
	public int getCount() {
		if (null != pictures) {
			return pictures.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.items, null);
			viewHolder = new ViewHolder();
			
			viewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.item_text);
			
			int h = screenWidth / 5;
			RelativeLayout.LayoutParams layoutParams =
					new RelativeLayout.LayoutParams(h, h);
			viewHolder.image.setLayoutParams(layoutParams);
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView title;
	}

	class Picture {
		private String title;
		private int imageId;

		public Picture() {
			super();
		}

		public Picture(String title, int imageId) {
			super();
			this.title = title;
			this.imageId = imageId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}
	}

}
