package com.example.twitterclient.models;

import android.text.Html;
import android.text.Spanned;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created on 10/20/13.
 */
@Table(name = "users")
public class User extends Model {

	@Column(name = "name")
	public String name;

	@Column(name = "profile_image_url")
	public String profile_image_url;

	@Column(name = "screen_name")
	public String screen_name;

	@Column(name = "description")
	public String description;

	@Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public Long user_id;

	@Column(name = "followers_count")
	public Integer followers_count;

	@Column(name = "friends_count")
	public Integer friends_count;

	public User() {
		super();
	}

	public String getOriginalProfileImageUrl() {
		return profile_image_url.replace("_normal", "");
	}

	public Spanned getSpannedName() {
		String formatString = "<strong>%s</strong> <small style=\"color:#777\">@%s</small>";
		return Html.fromHtml(String.format(formatString, name, screen_name));
	}

	public Spanned getSpannedFollowing() {
		NumberFormat formatter = NumberFormat.getInstance();
		String formatString = "Followers: <strong>%s</strong> Following: <strong>%s</strong>";
		return Html.fromHtml(
				String.format(formatString,
						formatter.format(followers_count),
						formatter.format(friends_count)
				)
		);
	}

	public static User fromJsonObject(JSONObject jsonObject) throws JSONException {
		User user = new User();
		user.user_id = jsonObject.getLong("id");
		user.name = jsonObject.getString("name");
		user.screen_name = jsonObject.getString("screen_name");
		user.description = jsonObject.getString("description");
		user.friends_count = jsonObject.getInt("friends_count");
		user.followers_count = jsonObject.getInt("followers_count");
		user.profile_image_url = jsonObject.getString("profile_image_url");
		user.save();
		return user;
	}

	public static User fromUserId(Long userId) {
		return new Select()
				.from(User.class)
				.where("user_id = ?", userId)
				.limit(1).executeSingle();
	}

}
/*
{
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
	}
 */
