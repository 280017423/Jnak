package qianye.jnak.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import qianye.jnak.R;
import qianye.jnak.adapter.YuangongAdapter;
import qianye.jnak.util.ConstantSet;
import qianye.jnak.util.FileUtil;
import qianye.jnak.util.OpenFileUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class YuangongActivity extends BaseActivity implements OnItemClickListener {
	private GridView mGvRootFolder;
	private ArrayList<File> mFileList;
	private YuangongAdapter mFilAdapter;
	private File mResDir;
	private File mCurrentFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yuangong);
		initVariables();
		initView();
		setListener();
		getFileList();
	}

	private void initVariables() {
		mFileList = new ArrayList<File>();

		mResDir = FileUtil.getResDir(this, FileUtil.YUANGONG_PATH);
		mCurrentFile = mResDir;
	}

	private void initView() {
		mGvRootFolder = (GridView) findViewById(R.id.gv_newslist);
		mFilAdapter = new YuangongAdapter(this, mFileList, mGvRootFolder);
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
				return OpenFileUtil.FILE_ENDING_IMAGE == OpenFileUtil.getFileEnding(file, YuangongActivity.this);
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
			Intent intent = new Intent(YuangongActivity.this, ImageDetailActivity.class);
			ArrayList<File> imageFileList = new ArrayList<File>();
			int size = mFileList.size();
			for (int i = 0; i < size; i++) {
				File file = mFileList.get(i);
				if (!file.isDirectory()
						&& OpenFileUtil.FILE_ENDING_IMAGE == OpenFileUtil.getFileEnding(file, YuangongActivity.this)) {
					imageFileList.add(file);
					if (tempFile.getAbsolutePath().equals(file.getAbsolutePath())) {
						intent.putExtra(ConstantSet.KEY_INTENT_IMG_POSITION, imageFileList.size() - 1);
					}
				}
			}
			intent.putExtra(ConstantSet.KEY_INTENT_IMGS_LIST, imageFileList);
			startActivity(intent);
		}

	}
}
