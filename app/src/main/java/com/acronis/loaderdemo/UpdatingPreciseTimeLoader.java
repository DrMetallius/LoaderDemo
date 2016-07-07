package com.acronis.loaderdemo;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class UpdatingPreciseTimeLoader extends AsyncTaskLoader<Long> {
	@Nullable private Long lastLoadedTime;
	@Nullable private Timer timer;

	private static final String NTP_SERVER = "0.pool.ntp.org";
	private static final int TIMEOUT = 20000;
	private static final long UPDATE_PERIOD = 10000;

	private static final String TAG = MainActivity.TAG + '/' + UpdatingPreciseTimeLoader.class.getSimpleName();

	UpdatingPreciseTimeLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		if (lastLoadedTime == null || takeContentChanged()) {
			forceLoad();
		} else {
			deliverResult(lastLoadedTime);
		}

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					onContentChanged();
				}
			}, UPDATE_PERIOD, UPDATE_PERIOD);
		}
	}

	@Override
	protected void onStopLoading() {
		if (timer != null) {
			timer.cancel();
			timer = null;
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