package org.aerogear.agreddit;

import java.net.MalformedURLException;
import java.net.URL;

import org.aerogear.agreddit.authentication.RedditAuthenticationModule;
import org.aerogear.agreddit.gson.ListingTypeAdapter;
import org.aerogear.agreddit.reddit.Listing;
import org.aerogear.android.Callback;
import org.aerogear.android.Pipeline;
import org.aerogear.android.core.HeaderAndBody;
import org.aerogear.android.impl.pipeline.PipeConfig;
import org.aerogear.android.pipeline.Pipe;

import android.app.Application;

import com.google.gson.GsonBuilder;

public class StoryListApplication extends Application {

	Pipeline pipeline;

	RedditAuthenticationModule module;

	@Override
	public void onCreate() {
		super.onCreate();
		URL redditURL;
		try {
			redditURL = new URL(getString(R.string.reddit_base));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		pipeline = new Pipeline(redditURL);
		module = new RedditAuthenticationModule(getApplicationContext());
		
		PipeConfig config = new PipeConfig(redditURL, Listing.class);
		config.setGsonBuilder(new GsonBuilder().registerTypeAdapter(Listing.class, new ListingTypeAdapter()));
		config.setEndpoint(".json");
		config.setAuthModule(module);
		
		pipeline.pipe(Listing.class, config);
		
	}
	
	public boolean isLoggedIn() {
		return false;
	}

	public Pipe<Listing> getListing() {
		return pipeline.get(Listing.class.getSimpleName().toLowerCase());
	}

	public void login(String username, String password, Callback<HeaderAndBody> callback) {
		module.login(username, password, callback);
	}
	
}
