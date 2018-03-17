package it.federicobono.flashcards;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by federicobono on 22/02/18.
 */

public class HomeListAdapter extends ArrayAdapter<Deck> implements AdapterView.OnItemClickListener{

    private static final String TAG = "FBLOG - HomeListAdapter";

    public HomeListAdapter(Context context, int textViewResourceId,
                         List<Deck> objects) {
        super(context, textViewResourceId, DeckList.getLista());
    }

    static class ViewHolder {
        TextView titolo;
        TextView cont;
        TextView materia;
        ImageView imageView;
        int size;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Deck deck = getItem(position);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_list_item, null);
            holder = new ViewHolder();
            holder.titolo = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.cont = (TextView) convertView.findViewById(R.id.itemCount);
            holder.imageView = (ImageView) convertView.findViewById(R.id.itemIcon);
            holder.materia = (TextView) convertView.findViewById(R.id.materia);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Resources res = getContext().getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        final LetterTileProvider tileProvider = new LetterTileProvider(getContext());
        final Bitmap letterTile = tileProvider.getLetterTile(deck.getMateria(), deck.getColore(), tileSize, tileSize, true);

        try {
            holder.size = deck.getCardSize();
        }catch (Exception e) {
            e.printStackTrace();
            holder.size = 0;
        }

        holder.imageView.setImageBitmap(letterTile);
        holder.cont.setText(String.valueOf(holder.size));
        holder.titolo.setText(deck.getTitolo());
        holder.materia.setText(deck.getMateria());
        holder.materia.setTextColor(Color.parseColor(deck.getColore()));
        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getContext(), DeckDetailActivity.class);
        intent.putExtra("index", position);
        getContext().startActivity(intent);
    }
}
