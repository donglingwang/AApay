package com.bishe.aapay.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.CategoryDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LvCategoryAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private CategoryDao categoryDao;
	private int type;
	private List<Map<String, String>> categoryList;
	public LvCategoryAdapter(Context context,CategoryDao categoryDao,int type) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.categoryDao = categoryDao;
		this.type = type;
		this.categoryList = this.categoryDao.getCategoryList(type);
	}
	@Override
	public int getCount() {
		return categoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return categoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.category_lv_item, null);
			holder.viewCategoryName = (TextView) convertView.findViewById(R.id.category_item_name);
			holder.imageRemove = (ImageView) convertView.findViewById(R.id.category_item_remove);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.viewCategoryName.setText(categoryList.get(position).get("category_name"));
		holder.imageRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				categoryDao.deleteCategoryById(categoryList.get(position).get("category_id"));
				categoryList.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	
	public void refreshCategoryList(int type) {
		this.categoryList = categoryDao.getCategoryList(type);
	}
	public class ViewHolder {
		public TextView viewCategoryName;
		public ImageView imageRemove;
	}
}
