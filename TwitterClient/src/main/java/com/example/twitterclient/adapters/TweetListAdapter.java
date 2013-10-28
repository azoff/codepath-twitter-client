package com.example.twitterclient.adapters;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.twitterclient.R;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.example.twitterclient.utils.HandlesErrors;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created on 10/20/13.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {

	private static final int tweetResource = R.layout.view_tweet;

	private static final String tweetDateFormat = "h:ma, MMMMM d yyyy";

	public static interface HandlesTweet extends HandlesErrors {
		public User getCurrentUser();

		public void onProfileClick(User user);

		public void onReplyClick(Tweet tweet);
	}

	final HandlesTweet tweetHandler;

	public TweetListAdapter(Context context, HandlesTweet tweetHandler) {
		super(context, tweetResource);
		this.tweetHandler = tweetHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Tweet tweet = this.getItem(position);

		if (convertView == null) // only create the view if not recycling
			convertView = LayoutInflater.from(getContext()).inflate(tweetResource, parent, false);

		if (convertView == null) {
			tweetHandler.onError(new InflateException("Unable to inflate tweet view"));
			return null;
		}

		final User user = tweet.getUser();

		ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(user.profile_image_url, ivProfile);
		ivProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tweetHandler.onProfileClick(user);
			}
		});

		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		tvName.setText(user.getSpannedName());

		TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
		tvDate.setText(tweet.getSpannedDate(tweetDateFormat));

		TextView tvReply = (TextView) convertView.findViewById(R.id.tvReply);
		if (tweetHandler.getCurrentUser().user_id.equals(user.user_id)) {
			tvReply.setVisibility(View.INVISIBLE);
		} else {
			tvReply.setVisibility(View.VISIBLE);
			tvReply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					tweetHandler.onReplyClick(tweet);
				}
			});
		}

		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		tvBody.setText(tweet.getSpannedText());

		return convertView;

	}
}
