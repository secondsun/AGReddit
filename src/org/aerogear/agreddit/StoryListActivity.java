package org.aerogear.agreddit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class StoryListActivity extends Activity
        implements StoryListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        
        
        if (findViewById(R.id.story_detail_container) != null) {
            mTwoPane = true;
            ((StoryListFragment) getFragmentManager()
                    .findFragmentById(R.id.story_list))
                    .setActivateOnItemClick(true);
            ((StoryListFragment) getFragmentManager()
                    .findFragmentById(R.id.story_list))
                    .setRetainInstance(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (((StoryListApplication)getApplication()).isLoggedIn()) {
        	inflater.inflate(R.menu.logout, menu);
        } else {
        	inflater.inflate(R.menu.login, menu);
        }
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.menu_login:
			LoginDialogFragment dialog = new LoginDialogFragment();
			dialog.show(getFragmentManager(), "Dialog");
			break;

		default:
			break;
		}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(StoryDetailFragment.ARG_ITEM_ID, id);
            StoryDetailFragment fragment = new StoryDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.story_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, StoryDetailActivity.class);
            detailIntent.putExtra(StoryDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
