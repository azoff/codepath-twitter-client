package com.example.twitterclient.handlers;

import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.utils.HandlesErrors;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created on 10/20/13.
 */
public class AsyncTweetListHandler extends JsonHttpResponseHandler {

	public static interface HandlesTweetList extends HandlesErrors {
		public void onTweetList(List<Tweet> tweets);
	}

	private HandlesTweetList handler;

	public AsyncTweetListHandler(HandlesTweetList handler) {
		this.handler = handler;
	}

	@Override
	public void onSuccess(JSONArray jsonArray) {
		try {
			handler.onTweetList(Tweet.fromJsonArray(jsonArray));
		} catch (JSONException ex) {
			handler.onError(ex);
		}
	}

	@Override
	public void onFailure(Throwable throwable, JSONArray jsonArray) {
		handler.onError(throwable);
	}

	@Override
	public void onFailure(Throwable throwable, JSONObject jsonObject) {
		handler.onError(throwable);
	}
}
