package com.acronis.loaderdemo;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

class CachingPreciseTimeLoader extends AsyncTaskLoader<Long> {
	@Nullable private Long lastLoadedTime;

	private static final String NTP_SERVER = "0.pool.ntp.org";
	private static final int TIMEOUT = 20000;

	private static final String TAG = MainActivity.TAG + '/' + CachingPreciseTimeLoader.class.getSimpleName();

	CachingPreciseTimeLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		if (lastLoadedTime == null) {
			forceLoad();
		} else {
			deliverResult(lastLoadedTime);
		}
	}

	@Override
	public void deliverResult(Long data) {
		if (BuildConfig.DEBUG) Log.d(TAG, "Delivering " + data);
		lastLoadedTime = data;
		super.deliverResult(data);
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