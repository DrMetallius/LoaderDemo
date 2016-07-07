package com.acronis.loaderdemo;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

class SimplePreciseTimeLoader extends AsyncTaskLoader<Long> {
	private static final String NTP_SERVER = "0.pool.ntp.org";
	private static final int TIMEOUT = 20000;

	private static final String TAG = MainActivity.TAG + '/' + SimplePreciseTimeLoader.class.getSimpleName();

	SimplePreciseTimeLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

	@Override
	public Long loadInBackground() {
		if (BuildConfig.DEBUG) Log.d(TAG, "Started loading time");
		try {
			return new SntpClient().getTime(NTP_SERVER, TIMEOUT);
		} catch (IOException e) {
			if (BuildConfig.DEBUG) Log.i(TAG, "Couldn't load precise time", e);
			return null;
		}
	}
}