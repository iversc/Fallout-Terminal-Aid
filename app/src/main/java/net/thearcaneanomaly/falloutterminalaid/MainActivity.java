package net.thearcaneanomaly.falloutterminalaid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    public final static String SELECTED_WORD = "net.thearcaneanomaly.falloutterminalaid.SELECTED_WORD";
    public final static String SELECTED_INDEX = "net.thearcaneanomaly.falloutterminalaid.SELECTED_INDEX";
    public final static String WORD_LENGTH = "net.thearcaneanomaly.falloutterminalaid.WORD_LENGTH";
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

        et.getText().clear();
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

        EditWordDialogFragment ewdf = new EditWordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("word", w);
        bundle.putInt("index", position);
        ewdf.setArguments(bundle);
        ewdf.show(getFragmentManager(), "EditWordDialogFragment");
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

    public void onEditWordResult(Word word, int selectedIndex, int resultCode) {

        if (resultCode == RESULT_EDITED) {
            arrayWords.set(selectedIndex, word);
            int numCharacters = word.getCorrect();
            String editedWord = word.getWord();
            int numMatches = 0;

            String lastMatch = null;

            for(Word w : arrayWords)
            {
                //Skip ones already ruled out
                if(!w.isMatch()) continue;

                //Skip the one we just changed
                if(w.getWord().equals(word.getWord())) continue;

                try
                {
                    w.setMatch( (w.numSameChars(editedWord) == numCharacters) );
                }
                catch (InvalidArgumentException e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                }

                if(w.isMatch())
                {
                    lastMatch = w.getWord();
                    numMatches++;
                }
            }

            updateList();
            if(numMatches == 1)
            {
                Toast.makeText(this, "Possible match: " + lastMatch, Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == RESULT_DELETE) {
            arrayWords.remove(selectedIndex);
            updateList();

            if (arrayWords.isEmpty()) wordLength = 0;
        }
    }
}