package it.federicobono.flashcards;

/**
 * Created by federicobono on 22/02/18.
 */

public class FlashCard {
    private String titolo, descrizione;

    public FlashCard() {} //Per utilizzare la classe con Firebase.database

    public FlashCard(String titolo, String descrizione) {
        this.titolo = titolo;
        this.descrizione = descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
