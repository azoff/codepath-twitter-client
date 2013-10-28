package com.example.twitterclient.handlers;

import com.example.twitterclient.models.User;
import com.example.twitterclient.utils.HandlesErrors;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 10/22/13.
 */
public class AsyncUserVerificationHandler extends JsonHttpResponseHandler {

	public static interface CanReceiveUser extends HandlesErrors {
		public void receiveUser(User user);
	}

	private CanReceiveUser receiver;

	public AsyncUserVerificationHandler(CanReceiveUser receiver) {
		this.receiver = receiver;
	}

	@Override
	public void onSuccess(JSONObject jsonObject) {
		try {
			receiver.receiveUser(User.fromJsonObject(jsonObject));
		} catch (JSONException e) {
			onFailure(e, jsonObject);
		}
	}

	@Override
	public void onFailure(Throwable throwable, JSONObject jsonObject) {
		receiver.onError(throwable);
	}
}
