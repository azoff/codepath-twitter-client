package com.example.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 10/20/13.
 */
@Table(name = "tweets")
public class Tweet extends Model {

	@Column(name = "text")
	private String text;

	private User user;

	public Tweet() {
		super();
	}

	public String getText() {
		return text;
	}

	public User getUser() {
		return user;
	}

	public static Tweet fromJsonObject(JSONObject jsonObject) throws JSONException {
		Tweet tweet = new Tweet();
		tweet.text = jsonObject.getString("text");
		tweet.user = User.fromJsonObject(jsonObject.getJSONObject("user"));
		return tweet;
	}

	public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (int i = 0; i < jsonArray.length(); i++)
			tweets.add(fromJsonObject(jsonArray.getJSONObject(i)));
		return tweets;
	}

}

/*
{
	"coordinates": null,
	"truncated": false,
	"created_at": "Tue Aug 28 21:16:23 +0000 2012",
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
	  "created_at": "Wed Mar 03 19:37:35 +0000 2010",
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