package com.example.twitterclient.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.activeandroid.Model;
import com.codepath.oauth.OAuthLoginActivity;
import com.example.twitterclient.R;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.handlers.AsyncUserVerificationHandler;
import com.example.twitterclient.models.User;
import com.example.twitterclient.net.TwitterClient;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> implements
		AsyncUserVerificationHandler.CanReceiveUser {

	private Button btnLogin;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
		super.onCreate(savedInstanceState);
	}

	public void login(View v) {
		btnLogin.setEnabled(false);
		this.getClient().connect();
	}

	@Override
	public void onLoginSuccess() {

		btnLogin.setText(getString(R.string.verifying_account));

		User user = TwitterApp.getCurrentUser();
		if (user != null) {
			receiveUser(user);
			return;
		}

		Long userId = prefs.getLong("id", 0);
		if (userId > 0) {
			user = Model.load(User.class, userId);
			if (user != null) {
				receiveUser(user);
				return;
			}
		}

		AsyncUserVerificationHandler handler = new AsyncUserVerificationHandler(this);
		TwitterApp.getClient().verifyCredentials(handler);

	}

	@Override
	public void receiveUser(User user) {
		TwitterApp.setUser(user);
		prefs.edit().putLong("id", user.getId()).commit();
		Intent intent = new Intent(this, TimelineActivity.class);
		startActivity(intent);
	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "TimelineType Error", error);
	}

	@Override
	public void onLoginFailure(Exception e) {
		onError(e);
	}

}
