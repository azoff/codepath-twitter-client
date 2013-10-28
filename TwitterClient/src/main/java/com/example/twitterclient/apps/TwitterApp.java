package com.example.twitterclient.apps;

import android.content.Context;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;
import com.example.twitterclient.models.User;
import com.example.twitterclient.net.TwitterClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created on 10/20/13.
 */
public class TwitterApp extends Application {

	private static Context context;
	private static User user;

	@Override
	public void onCreate() {

		super.onCreate();

		// start the database
		ActiveAndroid.initialize(context = this);

		// create default options, enabling caching...
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.build();

		// create configuration for ImageLoader.displayImage() out of default options
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.build();

		// set the ImageLoader configuration
		ImageLoader.getInstance().init(config);

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	public static TwitterClient getClient() {
		return TwitterClient.getInstance(context);
	}

	public static Context getContext() {
		return context;
	}

	public static User getCurrentUser() {
		return user;
	}

	public static void setUser(User user) {
		TwitterApp.user = user;
	}

}
