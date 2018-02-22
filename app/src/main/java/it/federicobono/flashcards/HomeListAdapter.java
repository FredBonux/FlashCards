package it.federicobono.flashcards;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by federicobono on 22/02/18.
 */

public class HomeListAdapter extends ArrayAdapter<FlashCard>{

    public HomeListAdapter(Context context, int textViewResourceId,
                         List<FlashCard> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.home_list_item, null);

        TextView titolo = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView desc = (TextView) convertView.findViewById(R.id.itemDesc);
        TextView cont = (TextView) convertView.findViewById(R.id.itemCount);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.itemIcon);

        FlashCard flashCard = getItem(position);

        titolo.setText(flashCard.getTitolo());
        desc.setText(flashCard.getDescrizione());
        cont.setText("10");


        final Resources res = getContext().getResources();
        final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

        final LetterTileProvider tileProvider = new LetterTileProvider(getContext());
        final Bitmap letterTile = tileProvider.getLetterTile(flashCard.getTitolo(), "#eee",tileSize , tileSize, true);

        imageView.setImageBitmap(letterTile);


        return convertView;
    }
}
