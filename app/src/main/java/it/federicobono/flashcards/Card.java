package it.federicobono.flashcards;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by federicobono on 10/03/18.
 */

public class Card extends DatabaseObject{
    String frontText, backText;

    public Card(){}

    public Card (String frontText, String backText) {
        this.frontText = frontText;
        this.backText = backText;
    }

    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("frontText", frontText);
        map.put("backText", backText);
        return map;
    }
}
