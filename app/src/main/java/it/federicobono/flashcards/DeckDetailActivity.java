package it.federicobono.flashcards;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DeckDetailActivity extends AppCompatActivity {
    private static final String TAG = "FBLOG - DeckDetailAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        Deck d = (Deck) DeckList.getLista().get(intent.getIntExtra("index", 0));
        Log.d(TAG, d.getTitolo());

        setContentView(R.layout.activity_deck_detail);

        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
        appBar.setBackgroundColor(Color.parseColor(d.getColore()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(d.getTitolo());
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }


}
