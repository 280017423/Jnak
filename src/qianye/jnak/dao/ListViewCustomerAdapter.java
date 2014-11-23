package qianye.jnak.dao;

import java.util.ArrayList;

import qianye.jnak.R;
import qianye.jnak.model.Customer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewCustomerAdapter extends BaseAdapter {

	int count = 10; /* starting amount */
	private ArrayList<Customer> items;
	private LayoutInflater inflater;

	public ListViewCustomerAdapter(ArrayList<Customer> items, int lineSize, Context context) {
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
			return new Customer();
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
			contentView = inflater.inflate(R.layout.listviewcustomer_item, null);
		
		}	
		
		if(!this.items.isEmpty()&&position<this.items.size()){
		TextView text = (TextView) contentView
					.findViewById(R.id.list_item_name);
			TextView text2 = (TextView) contentView
					.findViewById(R.id.list_item_phone);
			Customer customer = items.get(position);
			text.setText(customer.getName());
			text2.setText(customer.getMobile());
		}
		return contentView;
		

	}

}
