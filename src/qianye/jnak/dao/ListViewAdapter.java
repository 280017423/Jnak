package qianye.jnak.dao;

import java.util.ArrayList;
import qianye.jnak.R;
import qianye.jnak.model.Article;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	int count = 10; /* starting amount */
	private ArrayList<Article> items;
	private LayoutInflater inflater;

	public ListViewAdapter(ArrayList<Article> items, int lineSize, Context context) {
		super();
		this.items = items;
		this.count = lineSize;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public Object getItem(int arg0) {
		if(!this.items.isEmpty()&&arg0<this.items.size()){
		return items.get(arg0);
		}
		else
		{
			return new Article();
		}
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// contentView=items.get(arg0);
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.listview_item, null);
		
		}	
		if(!this.items.isEmpty()&&position<this.items.size()){
		TextView text = (TextView) contentView
					.findViewById(R.id.list_item_text);
			TextView text2 = (TextView) contentView
					.findViewById(R.id.list_item_zhaiyao);
			Article article =items.get(position);
			text.setText(article.getTitle());
			text2.setText(article.getZhaiyao());
		}
		return contentView;

	}

}
