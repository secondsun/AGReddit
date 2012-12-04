package org.aerogear.agreddit;

import org.aerogear.android.Callback;
import org.aerogear.android.core.HeaderAndBody;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginDialogFragment extends DialogFragment {

	private View root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.login, container);
		root.findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					public void onClick(View v) {
						String username = ((EditText) root
								.findViewById(R.id.username)).getText()
								.toString();
						String password = ((EditText) root
								.findViewById(R.id.password)).getText()
								.toString();
						((StoryListApplication) getActivity().getApplication())
								.login(username, password,
										new Callback<HeaderAndBody>() {

											public void onSuccess(
													HeaderAndBody data) {
												((StoryListFragment) getActivity()
														.getSupportFragmentManager()
														.findFragmentById(
																R.id.story_list))
														.reload();
												LoginDialogFragment.this.dismiss();
											}

											public void onFailure(Exception e) {
												Log.e("LoginDialog", e.getMessage(), e);
												Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
											}
										});
					}
				});
		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

}
