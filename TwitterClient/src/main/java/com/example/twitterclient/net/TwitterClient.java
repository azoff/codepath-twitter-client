package com.example.twitterclient.net;

import android.content.Context;
import com.codepath.oauth.OAuthBaseClient;
import com.example.twitterclient.R;
import com.example.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created on 10/18/13.
 */
public class TwitterClient extends OAuthBaseClient {

	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "IAyNXfHb8xLcZgGKjhH1JA";       // Change this
	public static final String REST_CONSUMER_SECRET = "BwhB1M7pxwRstuyK55Z9987PwXqDNo357YBJLgT4"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(Tweet before, Tweet after, JsonHttpResponseHandler handler) {
		getTimeline("home", before, after, handler);
	}

	public void getMentionsTimeline(Tweet before, Tweet after, JsonHttpResponseHandler handler) {
		getTimeline("mentions", before, after, handler);
	}

	private void getTimeline(String timeline, Tweet before, Tweet after, JsonHttpResponseHandler handler) {
		String url = getApiUrl(String.format("statuses/%s_timeline.json", timeline));
		RequestParams params = new RequestParams();
		params.put("count", String.valueOf(R.integer.page_size));
		if (before != null) {
			params.put("since_id", before.tweet_id.toString());
		}
		if (after != null) {
			Long maxId = after.tweet_id - 1;
			params.put("max_id", maxId.toString());
		}
		client.get(url, params, handler);
	}

	public void updateStatus(String status, JsonHttpResponseHandler handler) {
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		client.post(url, params, handler);
	}

	public static TwitterClient getInstance(Context context) {
		return (TwitterClient) OAuthBaseClient.getInstance(TwitterClient.class, context);
	}

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.postTweet(apiUrl, params, handler);
     */
}
