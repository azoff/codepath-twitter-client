package com.example.twitterclient.handlers;

import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.utils.HandlesErrors;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created on 10/22/13.
 */
public class AsyncStatusUpdateHandler extends JsonHttpResponseHandler {

	public static interface CanReceiveTweet extends HandlesErrors {
		public void receiveTweet(Tweet tweet);
	}

	private CanReceiveTweet receiver;

	public AsyncStatusUpdateHandler(CanReceiveTweet receiver) {
		this.receiver = receiver;
	}

	@Override
	public void onSuccess(JSONObject jsonObject) {
		try {
			receiver.receiveTweet(Tweet.fromJsonObject(jsonObject));
		} catch (JSONException e) {
			onFailure(e, jsonObject);
		} catch (ParseException e) {
			onFailure(e, jsonObject);
		}
	}

	@Override
	public void onFailure(Throwable throwable, JSONObject jsonObject) {
		receiver.onError(throwable);
	}
}
