public class Card {

    public enum Suit{
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }

    public enum Face{
        DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
    }

    private final Face FACE;
    private final Suit SUIT;
    private boolean faceUp;

    public Card(Face face, Suit suit){
        this.FACE = face;
        this.SUIT = suit;
        this.faceUp = true;
    }

    public Suit getSuit(){
        return SUIT;
    }

    public Face getFace() {
        return FACE;
    }

    public int getValue(){
        switch (FACE){
            case ACE:
                return 11;

            case JACK, QUEEN, KING:
                return 10;

            default:
                return FACE.ordinal()+2;
        }
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flip(){
        this.faceUp = !faceUp;
    }

    @Override
    public String toString(){
        return FACE + "of" + SUIT;
    }
}
