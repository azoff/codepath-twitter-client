package com.example.twitterclient.activities;

import android.os.Bundle;
import android.view.View;
import com.codepath.oauth.OAuthLoginActivity;
import com.example.twitterclient.R;
import com.example.twitterclient.net.TwitterClient;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		lastClient = this.getClient();
	}

	public void login(View v) {
		this.getClient().connect();
	}

	@Override
	public void onLoginSuccess() {

	}

	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	private static TwitterClient lastClient;

	public static TwitterClient getLastClient() {
		return lastClient;
	}

}
