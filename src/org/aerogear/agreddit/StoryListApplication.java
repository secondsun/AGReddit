package org.aerogear.agreddit;

import java.net.MalformedURLException;
import java.net.URL;

import org.aerogear.agreddit.authentication.RedditAuthenticationModule;
import org.aerogear.agreddit.gson.ListingTypeAdapter;
import org.aerogear.agreddit.reddit.Listing;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.Pipeline;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.impl.pipeline.PipeConfig;
import org.jboss.aerogear.android.pipeline.Pipe;
import org.jboss.aerogear.android.pipeline.paging.PageConfig;

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
		
		PageConfig pageConfig = new PageConfig();
		pageConfig.setLimitValue(25);
		pageConfig.setMetadataLocation(PageConfig.MetadataLocations.BODY);
		pageConfig.setNextIdentifier("data.after");
		pageConfig.setPreviousIdentifier("data.before");
		pageConfig.setPageHeaderParser(new PageConsumer());
		
		config.setPageConfig(pageConfig);
		
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
