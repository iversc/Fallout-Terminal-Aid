package net.thearcaneanomaly.falloutterminalaid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class WordSelectActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_select);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Word w = i.getParcelableExtra(MainActivity.SELECTED_WORD);

        if(w == null)
        {
            //Add mode
            findViewById(R.id.bAdd).setVisibility(View.VISIBLE);
            findViewById(R.id.bDelete).setVisibility(View.INVISIBLE);
            findViewById(R.id.bSave).setVisibility(View.INVISIBLE);

            findViewById(R.id.txtWord).setEnabled(true);
            ((TextView)findViewById(R.id.txtNumCorrect)).setText("0");
        }
        else
        {
            findViewById(R.id.bAdd).setVisibility(View.INVISIBLE);
            findViewById(R.id.bDelete).setVisibility(View.VISIBLE);
            findViewById(R.id.bSave).setVisibility(View.VISIBLE);

            TextView tv = (TextView) findViewById(R.id.txtWord);
            tv.setEnabled(false);
            tv.setText(w.getWord());

            tv = (TextView) findViewById(R.id.txtNumCorrect);
            tv.setText(Integer.toString(w.getCorrect()));

            CheckBox cb = (CheckBox)findViewById(R.id.cbMatches);
            cb.setChecked(w.isMatch());
            cb.setEnabled(true);
            cb.setClickable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddClick(View v)
    {
        String word = ((TextView)findViewById(R.id.txtWord)).getText().toString();
        String s = ((TextView) findViewById(R.id.txtNumCorrect)).getText().toString();

        if(!validate(word, s)) return;

        int numCorrect = Integer.parseInt(s);
        boolean bMatch = ((CheckBox)findViewById(R.id.cbMatches)).isChecked();

        Word w = new Word(word, numCorrect, bMatch, 0);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.SELECTED_WORD, w);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    protected boolean validate(String word, String numCorrect)
    {
        if(word.isEmpty()) {
            Toast.makeText(this, "You must enter a word", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(numCorrect.isEmpty()) {
            Toast.makeText(this, "You must enter a value for Correct Letters", Toast.LENGTH_SHORT).show();
            return false;
        }

        int wordLength = getIntent().getIntExtra(MainActivity.WORD_LENGTH, 0);


        if( (wordLength != 0) && (word.length() != wordLength) )
        {
            Toast.makeText(this, "Word must be " + Integer.toString(wordLength) + " characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(Integer.parseInt(numCorrect) > wordLength)
        {
            Toast.makeText(this, "Correct characters can't be longer than word length.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onDeleteClick(View v)
    {
        Intent i = getIntent();
        Word w = i.getParcelableExtra(MainActivity.SELECTED_WORD);
        int selected = i.getIntExtra(MainActivity.SELECTED_INDEX, 0);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.SELECTED_WORD, w);
        returnIntent.putExtra(MainActivity.SELECTED_INDEX, selected);
        setResult(MainActivity.RESULT_DELETE, returnIntent);
        finish();
    }

    public void onSaveClick(View v)
    {
        String word = ((TextView)findViewById(R.id.txtWord)).getText().toString();
        String s = ((TextView) findViewById(R.id.txtNumCorrect)).getText().toString();

        if(!validate(word, s)) return;

        int numCorrect = Integer.parseInt(s);
        boolean bMatch = ((CheckBox)findViewById(R.id.cbMatches)).isChecked();

        Intent i = getIntent();
        Word original = i.getParcelableExtra(MainActivity.SELECTED_WORD);
        int originalNumCorrect = original.getCorrect();
        boolean originalMatch = original.isMatch();

        Intent returnIntent = new Intent();
        int result = RESULT_OK;
        if(originalNumCorrect != numCorrect || originalMatch != bMatch) {
            Word w = new Word(word, numCorrect, bMatch, 0);
            int selected = getIntent().getIntExtra(MainActivity.SELECTED_INDEX, 0);

            returnIntent.putExtra(MainActivity.SELECTED_WORD, w);
            returnIntent.putExtra(MainActivity.SELECTED_INDEX, selected);
            result = MainActivity.RESULT_EDITED;
        }

        setResult(result, returnIntent);
        finish();
    }
}
