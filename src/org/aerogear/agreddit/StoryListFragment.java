package org.aerogear.agreddit;

import java.util.List;

import org.aerogear.agreddit.reddit.Listing;
import org.aerogear.agreddit.reddit.T3;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.http.HttpException;
import org.jboss.aerogear.android.impl.pipeline.paging.WrappingPagedList;
import org.jboss.aerogear.android.pipeline.Pipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StoryListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private WrappingPagedList<Listing> listings;
	
	private final Callback<List<Listing>> readCallback = new Callback<List<Listing>>() {

		public void onSuccess(List<Listing> data) {
			Log.d("Reddt", "success");
			Log.d("Reddit", data.toString());
			listings = (WrappingPagedList<Listing>) data;
			setListAdapter(new ArrayAdapter<T3>(getActivity(),
	                android.R.layout.simple_list_item_activated_1,
	                android.R.id.text1,
	                data.get(0).getData().getChildren()));
		}

		public void onFailure(Exception e) {
			Log.d("Reddt", "failure", e);
			if (e instanceof HttpException) {
				HttpException httpException = (HttpException) e;
				Log.d("Reddit", new String(httpException.getData()));
			}
		}
		
	};
	
	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onItemSelected(String id) {
		}
	};

	public StoryListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		reload();
		
	}

	public void reload() {
		StoryListApplication applicaiton = (StoryListApplication) getActivity().getApplication();
		Pipe<Listing> listing = applicaiton.getListing();
		listing.read(readCallback);
	}

	public void next() {
		listings.next(readCallback);
	}
	
	public void previous() {
		listings.previous(readCallback);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		
		mCallbacks.onItemSelected(((T3)listView.getAdapter().getItem(position)).getUrl());
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_next:
			next();
			return true;
		case R.id.menu_previous:
			previous();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
}
