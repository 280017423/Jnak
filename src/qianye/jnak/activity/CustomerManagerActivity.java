package qianye.jnak.activity;

import java.util.Date;

import qianye.jnak.R;
import qianye.jnak.dao.CustomerDAO;
import qianye.jnak.model.Customer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerManagerActivity extends BaseActivity {
	long id;
	EditText et_name;
	EditText et_company;
	EditText et_post;
	EditText et_mobile;
	EditText et_phone;
	EditText et_email;
	EditText et_address;
	Button btn;
	CustomerDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_manager);
		btn = (Button) this.findViewById(R.id.btn_ok);
		et_name = (EditText) this.findViewById(R.id.et_name);
		et_company = (EditText) this.findViewById(R.id.et_company);
		et_post = (EditText) this.findViewById(R.id.et_post);
		et_mobile = (EditText) this.findViewById(R.id.et_mobile);
		et_phone = (EditText) this.findViewById(R.id.et_phone);
		et_email = (EditText) this.findViewById(R.id.et_email);
		et_address = (EditText) this.findViewById(R.id.et_address);
		Intent i = getIntent();
		id = i.getLongExtra("id", 0);
		Log.d("customerid", String.valueOf(id));

		dao = new CustomerDAO(this);
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_manager, menu);
		return true;
	}

	void loadData() {

		if (id > 0) {
			btn.setText("修改");
			Customer c = dao.get(id);
			if (c.get_id() > 0) {
				et_name.setText(c.getName());
				et_company.setText(c.getCompany());
				et_post.setText(c.getPost());
				et_mobile.setText(c.getMobile());
				et_phone.setText(c.getPhone());
				et_email.setText(c.getEmail());
				et_address.setText(c.getAddress());
			}
		} else

		{

			btn.setText("添加");
		}
	}

	public void btnok(View v) {
		String name = et_name.getText().toString().trim();
		String company = et_company.getText().toString().trim();
		String post = et_post.getText().toString().trim();
		String mobile = et_mobile.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		String address = et_address.getText().toString().trim();
		String msg = "";
		Customer cus = dao.get(id);
		cus.setName(name);
		cus.setCompany(company);
		cus.setPost(post);
		cus.setMobile(mobile);
		cus.setPhone(phone);
		cus.setEmail(email);
		cus.setAddress(address);
		cus.set_id(id);
		if (cus.get_id() > 0) {
			dao.update(cus);
			msg = "修改成功";
		} else {
			cus.setUsername(mPubUserName);
			cus.setCreateon(new Date().toString());
			dao.Add(cus);
			msg = "添加成功";
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		btncancel(v);
	}

	public void btncancel(View v) {
		Intent i = new Intent(this, CustomerListActivity.class);
		startActivity(i);
		finish();
	}
}
