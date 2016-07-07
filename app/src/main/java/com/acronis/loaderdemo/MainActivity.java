package com.acronis.loaderdemo;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends Activity {
	@NonNull private TextView preciseTimeText;

	@NonNull private final LoaderCallbacks<Long> callbacks = new LoaderCallbacks<Long>() {
		@Override
		public Loader<Long> onCreateLoader(int id, Bundle args) {
			return new SimplePreciseTimeLoader(MainActivity.this);
		}

		@Override
		public void onLoadFinished(Loader<Long> loader, Long data) {
			if (data == null) {
				preciseTimeText.setText(R.string.time_loading_failed);
			} else {
				preciseTimeText.setText(DateFormat.getDateTimeInstance().format(new Date(data)));
			}
		}

		@Override
		public void onLoaderReset(Loader<Long> loader) {}
	};

	private static final int LOADER_TIME = 1;

	public static final String TAG = "LoaderDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preciseTimeText = (TextView) findViewById(R.id.main_time);
		findViewById(R.id.main_btn_reload).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				getLoaderManager().restartLoader(LOADER_TIME, null, callbacks);
			}
		});
		getLoaderManager().initLoader(LOADER_TIME, null, callbacks);
	}
}
