package com.example.twitterclient.handlers;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created on 10/21/13.
 */
public class PostTweetActionHandler implements TextView.OnEditorActionListener {

	public static interface CanUpdateStatus {
		public void updateStatus(View view);
	}

	private CanUpdateStatus updates;

	public PostTweetActionHandler(CanUpdateStatus updates) {
		this.updates = updates;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		CharSequence text = v.getText();
		if (text == null || text.length() <= 0)
			return true;
		updates.updateStatus(v);
		return false;
	}

}
