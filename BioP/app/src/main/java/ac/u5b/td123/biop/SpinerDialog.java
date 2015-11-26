package ac.u5b.td123.biop;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by amimeyaY on 2015/11/20.
 */
public class SpinerDialog extends DialogFragment {
	private ProgressDialog progressDialog;

	public static SpinerDialog newInstance(int title, int message) {
		SpinerDialog fragment = new SpinerDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		args.putInt("message", message);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle safedInstanceState) {
		int title = getArguments().getInt("title");
		int message = getArguments().getInt("message");

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle(title);
		progressDialog.setMessage(getResources().getText(message));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		return progressDialog;
	}
}

