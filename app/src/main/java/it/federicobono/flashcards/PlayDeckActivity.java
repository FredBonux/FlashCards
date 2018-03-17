package it.federicobono.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class PlayDeckActivity extends AppCompatActivity {

    EasyFlipView easyFlipView;
    CardView frontCard;
    CardView backCard;
    TextView frontText, backText;
    Button nextBtn;

    String deckTitle;
    ArrayList<String> fronts, backs;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_deck);

        Bundle b = getIntent().getBundleExtra("cards");
        fronts = b.getStringArrayList("fronts");
        backs = b.getStringArrayList("backs");
        deckTitle = b.getString("title");
        setTitle(deckTitle);

        easyFlipView = (EasyFlipView) findViewById(R.id.easyFlipView);
        frontCard = (CardView) findViewById(R.id.frontCard);
        backCard = (CardView) findViewById(R.id.backCard);
        frontText = (TextView) findViewById(R.id.frontSideText);
        backText = (TextView) findViewById(R.id.backSideText);
        nextBtn = (Button) findViewById(R.id.nextBtn);

        easyFlipView.setFlipOnTouch(false);

        easyFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frontCard.setCardElevation(0);
                backCard.setCardElevation(0);
                easyFlipView.flipTheView();
            }
        });

        easyFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                if(newCurrentSide == EasyFlipView.FlipState.BACK_SIDE) {
                    frontCard.setCardElevation(0);
                    backCard.setCardElevation(4);
                }else {
                    frontCard.setCardElevation(4);
                    backCard.setCardElevation(0);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(easyFlipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE){
                    frontCard.setCardElevation(0);
                    backCard.setCardElevation(0);
                    easyFlipView.flipTheView();
                }
                moveNext();
            }
        });

        moveNext();


    }

    private void moveNext() {
        index++;
        if(index < 0) index = 0;
        if(index >= fronts.size()) index = 0;

        frontText.setText(fronts.get(index));
        backText.setText(backs.get(index));
    }
}
