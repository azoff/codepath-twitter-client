package com.example.twitterclient.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.twitterclient.R;
import com.example.twitterclient.models.Tweet;
import com.example.twitterclient.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created on 10/20/13.
 */
public class TweetListAdapter extends ArrayAdapter<Tweet> {

	private int tweetItemResourceId;

	public TweetListAdapter(Activity context, int tweetItemResourceId, int tweetListResourceId) {
		super(context, tweetItemResourceId, tweetListResourceId);
		this.tweetItemResourceId = tweetItemResourceId;
		((ListView) context.findViewById(tweetListResourceId)).setAdapter(this);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Tweet tweet = this.getItem(position);

		if (convertView == null) // only create the view if not recycling
			convertView = LayoutInflater.from(getContext()).inflate(tweetItemResourceId, parent, false);

		if (convertView != null) {

			User user = tweet.getUser();

			ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
			ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfile);

			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvName.setText(user.getSpannedName());

			TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
			tvBody.setText(tweet.getText());

		}

		return convertView;
	}
}
