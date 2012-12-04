package org.aerogear.agreddit.authentication;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.aerogear.agreddit.R;
import org.aerogear.android.Callback;
import org.aerogear.android.authentication.AbstractAuthenticationModule;
import org.aerogear.android.authentication.AuthorizationFields;
import org.aerogear.android.core.HeaderAndBody;
import org.aerogear.android.core.HttpProvider;
import org.aerogear.android.impl.core.HttpRestProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RedditAuthenticationModule extends AbstractAuthenticationModule {

	private final URL baseURL;
	private final URL loginURL;

	private String authToken = "";
	private boolean isLoggedIn = false;
	private String modHash;

	
	public RedditAuthenticationModule(Context context) {
		try {
			baseURL = new URL(context.getString(R.string.reddit_base) + "api");
			loginURL = new URL(baseURL.toString() + "/login");
			
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}		
	}
	
	public URL getBaseURL() {
		return baseURL;
	}

	public String getLoginEndpoint() {
		return "/login";
	}

	public String getLogoutEndpoint() {
		return "/logout";
	}

	public String getEnrollEndpoint() {
		return null;
	}

	public void login(final String username, final String password,
		    final Callback<HeaderAndBody> callback) {
        new AsyncTask<Void, Void, Void>() {
            private Exception exception;
            private HeaderAndBody result;

            @Override
            protected Void doInBackground(Void... params) {
                
                try {
                	HttpProvider provider = new HttpRestProvider(getLoginURL(username));
                	provider.setDefaultHeader("User-Agent", "AeroGear StoryList Demo /u/secondsun");
                	provider.setDefaultHeader("Content-Type", "application/x-www-form-urlencoded");
                    String loginData = buildLoginData(username, password);
                    result = provider.post(loginData);
                    Log.d("Auth", new String(result.getBody()));
                    String json = new String(result.getBody());
                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject().get("json").getAsJsonObject();
                    modHash = obj.get("data").getAsJsonObject().get("modhash").getAsString();
                    authToken = obj.get("data").getAsJsonObject().get("cookie").getAsString();
                    isLoggedIn = true;
                } catch (Exception e) {
                    Log.e(RedditAuthenticationModule.class.getSimpleName(), 
                    	  "Error with Login", e);
                    exception = e;
                }
                return null;
            }

            private String buildLoginData(String username, String password) {
            	StringBuilder builder = new StringBuilder();
            	builder.append("user=").append(URLEncoder.encode(username))
            	.append("&api_type=json&passwd=").append(URLEncoder.encode(password));
                 return builder.toString();
			}

			@Override
            protected void onPostExecute(Void ignore) {
                super.onPostExecute(ignore);
                if (exception == null) {
                    callback.onSuccess(this.result);
                } else {
                    callback.onFailure(exception);
                }
            }

        }.execute((Void) null);

	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public AuthorizationFields getAuthorizationFields() {

		AuthorizationFields fields = new AuthorizationFields();
		fields.addHeader("User-Agent", "AeroGear StoryList Demo /u/secondsun");
		if (isLoggedIn) {
			fields.addHeader("Cookie", "reddit_session="+authToken);
			fields.addQueryParameter("uh", modHash);
		}
		return fields;
	}

	private URL getLoginURL(String userName) {
		try {
			return new URL(loginURL.toString() + "/" + userName);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
