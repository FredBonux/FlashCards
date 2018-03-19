package it.federicobono.flashcards;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeckDetailActivity extends AppCompatActivity {
    private static final String TAG = "FBLOG - DeckDetailAct";
    int index;
    RecyclerView cardListView;
    DeckDetailListAdapter listAdapter;
    List<Card> cardList = new ArrayList<>();
    Deck d;


    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        d = (Deck) DeckList.getLista().get(index);
        setContentView(R.layout.activity_deck_detail);


        final AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.addFab);


        cardListView = (RecyclerView) findViewById(R.id.cardList);
        cardListView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new DeckDetailListAdapter(cardList, this);
        cardListView.setAdapter(listAdapter);


        final int color = Color.parseColor(d.getColore());
        appBar.setBackgroundColor(color);
        getWindow().setStatusBarColor(color);
        Drawable icon = getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp).getConstantState().newDrawable();
        icon.mutate().setTint(color);
        fab.setImageDrawable(icon);

        addFab.setBackgroundTintList(ColorStateList.valueOf(color));


        toolbar.setTitle(d.getTitolo());
        setSupportActionBar(toolbar);

        final Intent addCardIntent = new Intent(this, NewCardActivity.class);

        final Intent playDeckIntent = new Intent(this, PlayDeckActivity.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardList.size() <= 0)
                    Snackbar.make(view, R.string.noCards, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                    ArrayList<String> fronts = new ArrayList<>();
                    ArrayList<String> backs = new ArrayList<>();
                    for (Card c: cardList) {
                        fronts.add(c.getFrontText());
                        backs.add(c.getBackText());
                    }
                    Bundle b = new Bundle();
                    b.putStringArrayList("fronts", fronts);
                    b.putStringArrayList("backs", backs);
                    b.putString("title", d.getTitolo());
                    playDeckIntent.putExtra("cards", b);
                    startActivity(playDeckIntent);
                }
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCardIntent.putExtra("index", index);
                startActivityForResult(addCardIntent , 0);
            }
        });

        bindListener();
    }

    private void bindListener() {
        firestoreListener = d.getReference().collection("Cards").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                Log.d(TAG, "Cards updated!");
                cardList.clear();
                for (DocumentSnapshot document: documentSnapshots) {
                    Card card = document.toObject(Card.class).withRef(document.getReference());
                    cardList.add(card);
                }
                listAdapter.cardList = cardList;
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindListener();
    }
}
