package com.example.twitterclient.models;

import android.text.Html;
import android.text.Spanned;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.example.twitterclient.utils.TwitterDateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created on 10/20/13.
 */
@Table(name = "tweets")
public class Tweet extends Model {

	@Column(name = "text")
	public String text;

	@Column(name = "created_date")
	public Date created_date;

	@Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public Long tweet_id;

	@Column(name = "user_id")
	public Long user_id;
	private User user;

	@Column(name = "mentioned")
	public Boolean mentioned;

	public Tweet() {
		super();
	}

	public User getUser() {
		return user != null ? user : User.fromUserId(user_id);
	}

	public Spanned getSpannedText() {
		return Html.fromHtml(text);
	}

	public String getFormattedDate(String formatString) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatString);
		return formatter.format(created_date);
	}

	public Spanned getSpannedDate(String dateFormatString) {
		String formatString = "<small style\"color:#888\"><em>%s</em></small>";
		String formattedDate = getFormattedDate(dateFormatString);
		return Html.fromHtml(String.format(formatString, formattedDate));
	}

	public static Tweet fromJsonObject(JSONObject jsonObject)
			throws JSONException, ParseException {
		return fromJsonObject(jsonObject, null);
	}

	public static Tweet fromJsonObject(JSONObject jsonObject, User toCheckForMentions)
			throws JSONException, ParseException {

		Tweet tweet = new Tweet();
		tweet.user = User.fromJsonObject(jsonObject.getJSONObject("user"));
		tweet.user_id = tweet.user.user_id;
		tweet.text = jsonObject.getString("text");
		tweet.tweet_id = jsonObject.getLong("id");
		tweet.created_date = TwitterDateUtil.toDate(jsonObject.getString("created_at"));
		tweet.mentioned = false;

		if (toCheckForMentions != null) {
			JSONArray userMentions = jsonObject.getJSONObject("entities").getJSONArray("user_mentions");
			for (int i = 0; i < userMentions.length(); i++) {
				JSONObject userMention = userMentions.getJSONObject(i);
				if (toCheckForMentions.user_id.equals(userMention.getInt("id"))) {
					tweet.mentioned = true;
					break;
				}
			}
		}

		tweet.save();
		return tweet;

	}

	public static List<Tweet> fromJsonArray(JSONArray jsonArray)
			throws JSONException, ParseException {
		return fromJsonArray(jsonArray, null);
	}

	public static List<Tweet> fromJsonArray(JSONArray jsonArray, User toCheckForMentions)
			throws JSONException, ParseException {
		List<Tweet> tweets = new ArrayList<Tweet>();
		ActiveAndroid.beginTransaction();
		for (int i = 0; i < jsonArray.length(); i++)
			tweets.add(fromJsonObject(jsonArray.getJSONObject(i), toCheckForMentions));
		ActiveAndroid.setTransactionSuccessful();
		ActiveAndroid.endTransaction();
		return tweets;
	}

	public static From getRecentFrom(Integer limit) {
		return new Select()
				.from(Tweet.class)
				.orderBy("created_date DESC")
				.limit(limit.toString());
	}

	public static List<Tweet> getRecentTweets(Integer limit) {
		return getRecentFrom(limit).execute();
	}

	public static List<Tweet> getRecentMentions(Integer limit) {
		return getRecentFrom(limit).where("mentioned = ?", true).execute();
	}

	public static List<Tweet> getRecentUserTweets(Integer limit, User currentUser) {
		return getRecentFrom(limit).where("user_id = ?", currentUser.user_id).execute();
	}

	public static class ByDateCreatedDesc implements Comparator<Tweet> {

		@Override
		public int compare(Tweet lhs, Tweet rhs) {
			return rhs.created_date.compareTo(lhs.created_date);
		}

	}

}

/*
{
	"coordinates": null,
	"truncated": false,
	"created_date": "Tue Aug 28 21:16:23 +0000 2012",
	"favorited": false,
	"id_str": "240558470661799936",
	"in_reply_to_user_id_str": null,
	"entities": {
	  "urls": [

	  ],
	  "hashtags": [

	  ],
	  "user_mentions": [

	  ]
	},
	"text": "just another test",
	"contributors": null,
	"id": 240558470661799936,
	"retweet_count": 0,
	"in_reply_to_status_id_str": null,
	"geo": null,
	"retweeted": false,
	"in_reply_to_user_id": null,
	"place": null,
	"source": "<a href=\"http://realitytechnicians.com\" rel=\"nofollow\">OAuth Dancer Reborn</a>",
	"user": {
	  "name": "OAuth Dancer",
	  "profile_sidebar_fill_color": "DDEEF6",
	  "profile_background_tile": true,
	  "profile_sidebar_border_color": "C0DEED",
	  "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
	  "created_date": "Wed Mar 03 19:37:35 +0000 2010",
	  "location": "San Francisco, CA",
	  "follow_request_sent": false,
	  "id_str": "119476949",
	  "is_translator": false,
	  "profile_link_color": "0084B4",
	  "entities": {
	    "url": {
	      "urls": [
	        {
	          "expanded_url": null,
	          "url": "http://bit.ly/oauth-dancer",
	          "indices": [
	            0,
	            26
	          ],
	          "display_url": null
	        }
	      ]
	    },
	    "description": null
	  },
	  "default_profile": false,
	  "url": "http://bit.ly/oauth-dancer",
	  "contributors_enabled": false,
	  "favourites_count": 7,
	  "utc_offset": null,
	  "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
	  "id": 119476949,
	  "listed_count": 1,
	  "profile_use_background_image": true,
	  "profile_text_color": "333333",
	  "followers_count": 28,
	  "lang": "en",
	  "protected": false,
	  "geo_enabled": true,
	  "notifications": false,
	  "description": "",
	  "profile_background_color": "C0DEED",
	  "verified": false,
	  "time_zone": null,
	  "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
	  "statuses_count": 166,
	  "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
	  "default_profile_image": false,
	  "friends_count": 14,
	  "following": false,
	  "show_all_inline_media": false,
	  "screen_name": "oauth_dancer"
	},
	"in_reply_to_screen_name": null,
	"in_reply_to_status_id": null
}
 */