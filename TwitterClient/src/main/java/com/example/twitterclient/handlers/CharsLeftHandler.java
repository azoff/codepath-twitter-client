package com.example.twitterclient.handlers;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created on 10/21/13.
 */
public class CharsLeftHandler implements View.OnKeyListener {

	private TextView view;
	private Button button;
	private int maxChars;

	public CharsLeftHandler(TextView tvCharsLeft, Button btnPost) {
		view = tvCharsLeft;
		button = btnPost;
		maxChars = 140;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		CharSequence text = ((TextView) v).getText();
		if (text == null) return false;
		Integer usedChars = text.length();
		Integer remainingChars = maxChars - usedChars;
		view.setText(remainingChars.toString());
		button.setEnabled(usedChars > 0);
		return remainingChars <= 0;
	}

}
