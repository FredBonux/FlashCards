package it.federicobono.flashcards;

import android.app.ActionBar;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class NewDeckActivity extends AppCompatActivity {

    HSLColorPicker colorPicker;
    ImageView imageView;
    FloatingActionButton fab;
    EditText mDeckName, mMateria;
    int selectedColor;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deck);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.newDeck);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        colorPicker = (HSLColorPicker) findViewById(R.id.colorPicker);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mDeckName = (EditText) findViewById(R.id.newDeckTitle);
        mMateria = (EditText) findViewById(R.id.newDeckMateria);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                selectedColor = color;
                imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                appBar.setBackgroundColor(color);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deckName = mDeckName.getText().toString();
                String materia = mMateria.getText().toString();
                if(materia.length() <= 0) materia = "Altro";
                if(deckName.length() <= 0) return;
                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
                Deck deck = new Deck(hexColor, deckName, materia);
                if(deck.save()) {
                    finish();
                }else {
                    Toast.makeText(NewDeckActivity.this, "Errore!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
