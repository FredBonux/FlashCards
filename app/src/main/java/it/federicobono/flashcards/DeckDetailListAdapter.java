package it.federicobono.flashcards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by federicobono on 10/03/18.
 */

public class DeckDetailListAdapter extends RecyclerView.Adapter<DeckDetailListAdapter.ViewHolder> {
    private static final String TAG = "FBLOG - CardListAdapter";

    public List<Card> cardList;
    private Context context;

    public DeckDetailListAdapter(List<Card> list, Context context) {
        this.cardList = list;
        this.context = context;
    }

    @Override
    public DeckDetailListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DeckDetailListAdapter.ViewHolder holder, int position) {
        Card card = cardList.get(position);

        Log.d(TAG, "onBindViewHolder: cardAdded: " + card.frontText);

        holder.frontFace.setText(card.getFrontText());
        holder.backFace.setText(card.getBackText());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView frontFace, backFace;

        public ViewHolder(View itemView) {
            super(itemView);
            frontFace = (TextView) itemView.findViewById(android.R.id.text1);
            backFace = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeckDetailListAdapter.ViewHolder holder;
        Card card = getItem(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
            holder = new DeckDetailListAdapter.ViewHolder();
            holder.frontFace = (TextView) convertView.findViewById(android.R.id.text1);
            holder.backFace = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(holder);
        }else {
            holder = (DeckDetailListAdapter.ViewHolder) convertView.getTag();
        }
        holder.frontFace.setText(card.getFrontText());
        holder.backFace.setText(card.getBackText());
        return convertView;
    }*/
}
