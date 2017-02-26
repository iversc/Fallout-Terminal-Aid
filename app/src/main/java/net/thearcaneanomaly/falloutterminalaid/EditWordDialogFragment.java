package net.thearcaneanomaly.falloutterminalaid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Chris on 6/27/2016.
 */
public class EditWordDialogFragment  extends DialogFragment {

    private Word selectedWord;
    private int resultCode;
    private int selectedIndex;
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        selectedWord = getArguments().getParcelable("word");
        selectedIndex = getArguments().getInt("index");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater li = getActivity().getLayoutInflater();

        final View v = li.inflate(R.layout.layout_word_edit, null);
        builder.setView(v);

        builder.setTitle(selectedWord.getWord());
        builder.setPositiveButton(R.string.edit_word_dialog_button_edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String s = ((EditText)v.findViewById(R.id.etNumCorrect)).getText().toString();
                int numCorrect = (s.equals("")) ? 0 : Integer.parseInt(s);
                selectedWord.setCorrect(numCorrect);

                selectedWord.setMatch( ((CheckBox)v.findViewById(R.id.cbPossible)).isChecked() );

                resultCode = MainActivity.RESULT_EDITED;
                mainActivity.onEditWordResult(selectedWord, selectedIndex, resultCode);
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultCode = MainActivity.RESULT_CANCELED;
                mainActivity.onEditWordResult(selectedWord, selectedIndex, resultCode);
            }
        });

        builder.setNeutralButton(R.string.edit_word_dialog_button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resultCode = MainActivity.RESULT_DELETE;
                mainActivity.onEditWordResult(selectedWord, selectedIndex, resultCode);
            }
        });

        Dialog d = builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return d;
    }

    public Word getSelectedWord()
    {
        return selectedWord;
    }

    public int getResultCode()
    {
        return resultCode;
    }
}
