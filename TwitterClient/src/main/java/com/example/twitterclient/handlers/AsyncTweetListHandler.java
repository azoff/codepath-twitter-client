package com.example.twitterclient.handlers;

import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.example.twitterclient.utils.HandlesErrors;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created on 10/20/13.
 */
public class AsyncTweetListHandler extends JsonHttpResponseHandler {

	public static interface OnTweetListListener extends HandlesErrors {
		public void onTweetList(List<Tweet> tweets);

		public User getCurrentUser();
	}

	private OnTweetListListener handler;

	public AsyncTweetListHandler(OnTweetListListener handler) {
		this.handler = handler;
	}

	@Override
	public void onSuccess(JSONArray jsonArray) {
		try {
			handler.onTweetList(Tweet.fromJsonArray(jsonArray, handler.getCurrentUser()));
		} catch (JSONException ex) {
			handler.onError(ex);
		} catch (ParseException ex) {
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
