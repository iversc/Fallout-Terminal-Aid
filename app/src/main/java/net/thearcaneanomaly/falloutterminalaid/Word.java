package net.thearcaneanomaly.falloutterminalaid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chris on 6/19/2015.
 */
public class Word implements Parcelable {
    private String m_word;
    private int m_numCorrect;
    private boolean m_isMatch;
    private int m_drawableId;

    public Word(String word, int numCorrect, boolean isMatch, int drawableId)
    {
        m_word = word.toUpperCase();
        m_numCorrect = numCorrect;
        m_isMatch = isMatch;
        m_drawableId = drawableId;
    }

    public Word(Parcel in)
    {
        readFromParcel(in);
    }

    public Word(String word)
    {
        this(word, 0, true, 0);
    }
/*
    public Word(String word, int numCorrect)
    {
        this(word, numCorrect, true);
    }*/

    public int getDrawableId()
    {
        return m_drawableId;
    }

    public void setDrawableId(int dId)
    {
        m_drawableId = dId;
    }

    public String getWord()
    {
        return m_word;
    }

    public int getCorrect()
    {
        return m_numCorrect;
    }

    public void setCorrect(int numCorrect)
    {
        m_numCorrect = numCorrect;
    }

    public boolean isMatch()
    {
        return m_isMatch;
    }

    public void setMatch(boolean isMatch)
    {
        m_isMatch = isMatch;
    }

    public int numSameChars(String b) throws InvalidArgumentException
    {
        InvalidArgumentException ex = new InvalidArgumentException();
        if(b == null) ex.add("b cannot be null");
        if(!ex.isEmpty()) throw ex;

        if(m_word.length() == 0) ex.add("word cannot be empty");
        if(b.length() == 0) ex.add("b cannot be empty");
        if(m_word.length() != b.length()) ex.add("word and b must be the same length");
        if(!ex.isEmpty()) throw ex;

        int sameChars = 0;
        for(int x = 0; x < m_word.length(); x++)
        {
            if(m_word.charAt(x) == b.charAt(x))
            {
                sameChars++;
            }
        }

        setCorrect(sameChars);
        return sameChars;
    }

    public int numSameChars(Word w) throws InvalidArgumentException
    {
        return numSameChars(w.getWord());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Word createFromParcel(Parcel in ) {
            return new Word( in );
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(m_drawableId);
        dest.writeInt( (m_isMatch) ? 1 : 0 );
        dest.writeInt(m_numCorrect);
        dest.writeString(m_word);
    }

    private void readFromParcel(Parcel in)
    {
        m_drawableId = in.readInt();
        m_isMatch = (in.readInt() == 1);
        m_numCorrect = in.readInt();
        m_word = in.readString();
    }
}

