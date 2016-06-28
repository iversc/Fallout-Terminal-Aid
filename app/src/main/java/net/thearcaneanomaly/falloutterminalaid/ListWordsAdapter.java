package net.thearcaneanomaly.falloutterminalaid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chris on 6/19/2015.
 */
public class ListWordsAdapter extends BaseAdapter{
    Context context;
    protected List<Word> list;
    LayoutInflater inflater;

    public ListWordsAdapter(Context context, List<Word> listCars) {
        this.list = listCars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount()
    {
        return list.size();
    }

    public Word getItem(int item)
    {
        return list.get(item);
    }

    public long getItemId(int item)
    {
        return list.get(item).getDrawableId();
    }

    public View getView(int position, View convertView, ViewGroup parent )
    {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = this.inflater.inflate(R.layout.layout_words_list,
                    parent, false);

            holder = new ViewHolder();

            holder.txtWord = (TextView) convertView.findViewById(R.id.txtWord);
            holder.cbMatch = (CheckBox) convertView.findViewById(R.id.cbMatches);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Word w = list.get(position);
        holder.txtWord.setText(w.getWord());
        holder.cbMatch.setChecked(w.isMatch());

        return convertView;
    }

    private class ViewHolder
    {
        TextView txtWord;
        CheckBox cbMatch;
    }
}
