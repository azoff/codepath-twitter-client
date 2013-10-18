package com.example.twitterclient.activities;

import android.os.Bundle;
import com.codepath.oauth.OAuthLoginActivity;
import com.example.twitterclient.R;
import com.example.twitterclient.net.TwitterClient;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void onLoginSuccess() {

	}

	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}
}
