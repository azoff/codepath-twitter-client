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

	final HandlesErrors errorHandler;

	public TweetListAdapter(Context context, HandlesErrors errorHandler) {
		super(context, tweetResource);
		this.errorHandler = errorHandler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Tweet tweet = this.getItem(position);

		if (convertView == null) // only create the view if not recycling
			convertView = LayoutInflater.from(getContext()).inflate(tweetResource, parent, false);

		if (convertView == null) {
			errorHandler.onError(new InflateException("Unable to inflate tweet view"));
			return null;
		}

		User user = tweet.user;

		ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(user.profile_image_url, ivProfile);

		TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
		tvName.setText(user.getSpannedName());

		TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
		tvDate.setText(tweet.getSpannedDate(tweetDateFormat));

		TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
		tvBody.setText(tweet.getSpannedText());

		return convertView;
	}
}
