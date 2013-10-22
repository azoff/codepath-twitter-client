package com.example.twitterclient.apps;

import com.activeandroid.app.Application;
import com.example.twitterclient.net.TwitterClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created on 10/20/13.
 */
public class TwitterApp extends Application {

	private static TwitterApp context;

	@Override
	public void onCreate() {

		super.onCreate();
		context = this;

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

	public static TwitterClient getClient() {
		return TwitterClient.getInstance(context);
	}

}
