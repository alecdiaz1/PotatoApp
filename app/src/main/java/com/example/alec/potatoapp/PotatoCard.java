package com.example.alec.potatoapp;

public class PotatoCard {
    public String cardName, imageUrl, result, id;

    public PotatoCard() {}

    public PotatoCard(String cardName, String imageUrl, String result, String id) {
        this.cardName = cardName;
        this.imageUrl = imageUrl;
        this.result = result;
        this.id = id;
    }

    public String getId() {return id;}
    public String getCardName() {return cardName;}
    public String getImageUrl() {return imageUrl;}
    public String getResult() {return result;}
}
