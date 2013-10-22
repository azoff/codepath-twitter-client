package com.example.twitterclient.handlers;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;

/**
 * Created on 10/22/13.
 */
public class AppendTweetsHandler implements AbsListView.OnScrollListener {

	private final CanLoadTweets loader;
	private final ArrayAdapter adapter;

	public static interface CanLoadTweets {
		public void loadOlderTweets();
	}

	public AppendTweetsHandler(CanLoadTweets loader, ArrayAdapter adapter) {
		this.loader = loader;
		this.adapter = adapter;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getLastVisiblePosition() == adapter.getCount() - 1)
			loader.loadOlderTweets();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
