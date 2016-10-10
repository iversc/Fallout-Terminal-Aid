package net.thearcaneanomaly.falloutterminalaid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Chris on 6/27/2016.
 */
public class EditWordDialogFragment  extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Word w = getArguments().getParcelable("word");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = getActivity().getLayoutInflater();

        builder.setView(li.inflate(R.layout.layout_word_edit, null));

        builder.setTitle(w.getWord());
        builder.setPositiveButton(R.string.edit_word_dialog_button_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "Edit Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancel pressed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton(R.string.edit_word_dialog_button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Delete pressed", Toast.LENGTH_SHORT).show();
            }
        });

        Dialog d = builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return d;
    }
}
