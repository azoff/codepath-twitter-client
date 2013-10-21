package com.example.twitterclient.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created on 10/20/13.
 */
public class TwitterDateUtil {

	private static final String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

	public static Date toDate(String twitterDate) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE_FORMAT);
		sf.setLenient(true);
		return sf.parse(twitterDate);
	}

}
