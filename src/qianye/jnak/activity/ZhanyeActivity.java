package qianye.jnak.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import qianye.jnak.R;
import qianye.jnak.adapter.FolderAdapter;
import qianye.jnak.util.FileUtil;
import qianye.jnak.util.OpenFileUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ZhanyeActivity extends BaseActivity implements OnItemClickListener {
	private ListView mGvRootFolder;
	private ArrayList<File> mFileList;
	private FolderAdapter mFilAdapter;
	private File mResDir;
	private File mCurrentFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		initVariables();
		initView();
		setListener();
		getFileList();
	}

	private void initVariables() {
		mFileList = new ArrayList<File>();
		mFilAdapter = new FolderAdapter(this, mFileList);
		mResDir = FileUtil.getResDir(this, FileUtil.EXCEL_PATH);
		mCurrentFile = mResDir;
	}

	private void initView() {
		mGvRootFolder = (ListView) findViewById(R.id.lv_newslist);
		mGvRootFolder.setAdapter(mFilAdapter);
	}

	private void setListener() {
		mGvRootFolder.setOnItemClickListener(this);
	}

	private void getFileList() {
		if (null == mCurrentFile) {
			return;
		}
		File[] files = mCurrentFile.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		if (files == null) {
			return;
		} else {
			mFileList.clear();
			mFileList.addAll(Arrays.asList(files));
			Collections.sort(mFileList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					if (o1.isDirectory() && o2.isFile())
						return -1;
					if (o1.isFile() && o2.isDirectory())
						return 1;
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		mFilAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		File tempFile = (File) parent.getAdapter().getItem(position);
		if (null == tempFile || !tempFile.exists()) {
			return;
		}
		if (tempFile.isFile()) {
			Intent intent = OpenFileUtil.openFile(tempFile, this);
			if (null != intent) {
				startActivity(intent);
			}
		}

	}
}
