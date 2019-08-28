package com.allein.freund.authapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.allein.freund.authapp.R;
import com.allein.freund.authapp.remote.APIUtils;

public class ChangeServerUrlDialogFragment extends DialogFragment {

    public interface ChangeServerUrlDialogListener {
        void onUrlChange(DialogFragment dialog);
        void onBadUrl(DialogFragment dialog);
    }

    private ChangeServerUrlDialogListener mListener;
    private EditText serverUrlTextEdit;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ChangeServerUrlDialogListener)activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                + " must implement ChangeServerUrlDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_change_server_url, null);
        serverUrlTextEdit = v.findViewById(R.id.server_url);
        serverUrlTextEdit.setText(APIUtils.getBaseUrl());
        builder.setView(v)
            // Add action buttons
            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if(Patterns.WEB_URL.matcher(serverUrlTextEdit.getText().toString()).matches()) {
                        APIUtils.setCustomBaseUrl(serverUrlTextEdit.getText().toString());
                        mListener.onUrlChange(ChangeServerUrlDialogFragment.this);
                    } else {
                        mListener.onBadUrl(ChangeServerUrlDialogFragment.this);
                    }

                }
            })
            .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ChangeServerUrlDialogFragment.this.getDialog().cancel();
                }
            });
        return builder.create();
    }
}