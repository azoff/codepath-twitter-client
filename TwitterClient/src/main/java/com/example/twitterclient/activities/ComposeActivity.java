package com.example.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.twitterclient.R;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.handlers.AsyncStatusUpdateHandler;
import com.example.twitterclient.handlers.CharsLeftHandler;
import com.example.twitterclient.handlers.PostTweetActionHandler;
import com.example.twitterclient.models.Tweet;

public class ComposeActivity extends Activity implements
		PostTweetActionHandler.CanUpdateStatus,
		AsyncStatusUpdateHandler.CanReceiveTweet {

	public static final int RESULT_CODE_TWEET = 0;

	private EditText etTweetText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		etTweetText = (EditText) findViewById(R.id.etTweetText);
		etTweetText.setOnEditorActionListener(new PostTweetActionHandler(this));
		etTweetText.setOnKeyListener(new CharsLeftHandler(
				(TextView) findViewById(R.id.tvCharsLeft),
				(Button) findViewById(R.id.btnPost)
		));
	}

	public void updateStatus(View view) {
		Editable text = etTweetText.getText();
		String status = text == null ? "" : text.toString();
		if (status.length() > 0)
			TwitterApp.getClient().updateStatus(status, new AsyncStatusUpdateHandler(this));
	}

	@Override
	public void receiveTweet(Tweet tweet) {
		Intent intent = new Intent();
		intent.putExtra("tweet_id", tweet.getId());
		setResult(RESULT_CODE_TWEET, intent);
		finish();
	}

	@Override
	public void onError(Throwable error) {
		Log.e("FIXME", "Compose Error", error);
		Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		finish();
	}

}
