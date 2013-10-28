package com.example.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.activeandroid.Model;
import com.example.twitterclient.R;
import com.example.twitterclient.adapters.TweetListAdapter;
import com.example.twitterclient.apps.TwitterApp;
import com.example.twitterclient.fragments.TimelineFragment;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity implements
		TweetListAdapter.HandlesTweet {

	private User profileUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Long userId = getIntent().getLongExtra("id", 0);
		profileUser = Model.load(User.class, userId);
		TimelineFragment timeline = new TimelineFragment(profileUser);

		setTitle(profileUser.screen_name);

		ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(profileUser.getOriginalProfileImageUrl(), ivProfile);

		TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvDescription.setText(profileUser.description);

		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		tvFollowing.setText(profileUser.getSpannedFollowing());

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.timelineLayout, timeline)
				.commit();

	}

	@Override
	public void onError(Throwable error) {
		Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
		Log.e("FIXME", "TimelineType Error", error);
	}

	@Override
	public User getCurrentUser() {
		return TwitterApp.getCurrentUser();
	}

	@Override
	public void onProfileClick(User user) {
	}

	@Override
	public void onReplyClick(Tweet tweet) {
		Intent intent = new Intent(this, ComposeActivity.class);
		intent.putExtra("reply_to_id", tweet.getId());
		startActivity(intent);
	}

}
