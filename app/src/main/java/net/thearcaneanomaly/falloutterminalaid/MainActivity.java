package net.thearcaneanomaly.falloutterminalaid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public final static String SELECTED_WORD = "net.thearcaneanomaly.falloutterminalaid.SELECTED_WORD";
    public final static String SELECTED_INDEX = "net.thearcaneanomaly.falloutterminalaid.SELECTED_INDEX";
    public final static String WORD_LENGTH = "net.thearcaneanomaly.falloutterminalaid.WORD_LENGTH";
    public final static int REQUEST_ADD = 1;
    public final static int REQUEST_EDIT = 2;
    public final static int RESULT_DELETE = 1;
    public final static int RESULT_EDITED = 2;

    static final String WORD_ARRAY = "WORD_ARRAY";

    ArrayList<Word> arrayWords;
    ListView lvWords;
    int wordLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            arrayWords = savedInstanceState.getParcelableArrayList(WORD_ARRAY);
        }

        if (arrayWords == null) arrayWords = new ArrayList<Word>();

        lvWords = (ListView) findViewById(R.id.listView);
        updateList();

        lvWords.setClickable(true);
        lvWords.setOnItemClickListener(this);

        /* was for testing purposes
        String[] s = {"VEGGIES", "SIPHONS", "SCALPEL", "MEANING", "HEAVENS",
            "BECOMES", "SHARPER", "BLAZING", "LENDING", "LEADING", "LEADERS"};

        for (String str : s)
        {
            arrayWords.add(new Word(str));
        }
        wordLength = 7;
        updateList(); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.action_add) {
            //Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WordSelectActivity.class);
            intent.putExtra(WORD_LENGTH, wordLength);
            startActivityForResult(intent, REQUEST_ADD);
        }

        if (id == R.id.action_reset_match)
        {
            for(Word w : arrayWords)
            {
                w.setMatch(true);
            }

            updateList();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddWordClick(View v)
    {
        EditText et = (EditText) findViewById(R.id.etNewWord);
        Word newWord = new Word(et.getText().toString().toUpperCase().trim());

        if( !validate(newWord.getWord(), "0") ) return;

        arrayWords.add(newWord);
        updateList();

        if(wordLength == 0) wordLength = newWord.getWord().length();

        Toast.makeText(this, et.getText(), Toast.LENGTH_SHORT).show();
    }

    protected boolean validate(String word, String numCorrect)
    {
        if(word.isEmpty()) {
            Toast.makeText(this, "You must enter a word.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(numCorrect.isEmpty()) {
            Toast.makeText(this, "You must enter a value for Correct Letters.", Toast.LENGTH_SHORT).show();
            return false;
        }

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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Word w = arrayWords.get(position);
        //Toast.makeText(this, "You selected: " + w.getWord(),
        //        Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(this, WordSelectActivity.class);

        //i.putExtra(SELECTED_WORD, w);
        //i.putExtra(SELECTED_INDEX, position);
        //i.putExtra(WORD_LENGTH, wordLength);
        //startActivityForResult(i, REQUEST_EDIT);
        EditWordDialogFragment ewdf = new EditWordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("word", w);
        ewdf.setArguments(bundle);
        ewdf.show(getFragmentManager(), "EditWordDialogFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {
                Word newWord = data.getParcelableExtra(SELECTED_WORD);

                arrayWords.add(newWord);

                updateList();

                if (wordLength == 0) wordLength = newWord.getWord().length();
            }
        } else if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_EDITED) {
                int selected = data.getIntExtra(SELECTED_INDEX, 0);
                Word w = data.getParcelableExtra(SELECTED_WORD);

                arrayWords.set(selected, w);
                int numCharacters = w.getCorrect();
                String editedWord = w.getWord();
                int numMatches = 0;

                String lastMatch = null;

                for(Word word : arrayWords)
                {
                    //Skip ones already ruled out
                    if(!word.isMatch()) continue;

                    //Skip the one we just changed
                    if(w.getWord().equals(word.getWord())) continue;

                    try
                    {
                        word.setMatch( (word.numSameChars(editedWord) == numCharacters) );
                    }
                    catch (InvalidArgumentException e)
                    {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if(word.isMatch())
                    {
                        lastMatch = word.getWord();
                        numMatches++;
                    }
                }

                updateList();
                if(numMatches == 1)
                {
                    Toast.makeText(this, "Possible match: " + lastMatch, Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_DELETE) {
                int selected = data.getIntExtra(SELECTED_INDEX, 0);

                arrayWords.remove(selected);
                updateList();

                if (arrayWords.isEmpty()) wordLength = 0;
            }
        }
    }

    protected void updateList() {
        ListWordsAdapter lwa = new ListWordsAdapter(this, arrayWords);
        lvWords.setAdapter(lwa);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(WORD_ARRAY, arrayWords);
    }
}