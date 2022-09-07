import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import org.w3c.dom.html.HTMLImageElement;
import svu.csc213.*;
import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Blackjack extends GraphicsProgram {

    //data about the game
    private double wager, balance = 10000, bank = 10000;

    //labels to display info to the player
    private GLabel bankLabel, wagerLabel, balanceLabel, blackjack, welcomeTo, streakLabel;

    //buttons for controls
    private JButton wagerButton, playbutton, hitButton, stayButton, quitButton;

    //objects we are playing with
    private Deck deck;
    private GHand player, dealer;

    //game counter
    private int end, streak;

    @Override
    public void init(){
        Color color = new Color(158, 158, 158);
        this.setBackground(color);

        deck = new Deck();

        //set up our buttons
        wagerButton = new JButton("Wager");
        playbutton = new JButton("Play");
        hitButton = new JButton("Hit");
        stayButton = new JButton("Stay");
        quitButton = new JButton("Quit");

        //add buttons to the screen
        add(wagerButton, SOUTH);
        add(playbutton, SOUTH);
        add(hitButton, SOUTH);
        add(stayButton, SOUTH);
        add(quitButton, SOUTH);

        //handle initial button visibility
        addActionListeners();
        playbutton.setVisible(false);
        hitButton.setVisible(false);
        stayButton.setVisible(false);

        generateCards();

        //set up your GLabels
        blackjack = new GLabel("Blackjack");
        blackjack.setFont("Old English Text MT-100");
        add(blackjack,getWidth()/2 - blackjack.getWidth()/2,getHeight()/2 + blackjack.getHeight()/4);
        welcomeTo = new GLabel("Welcome to");
        welcomeTo.setFont("Perpetua-30");
        add(welcomeTo, getWidth()/2 - welcomeTo.getWidth()/2,blackjack.getY() - (welcomeTo.getHeight() * 2.5));
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        switch(ae.getActionCommand()){
            case "Wager":
                wager();
                break;

            case "Play":
                play();
                break;

            case "Hit":
                hit();
                break;

            case "Stay":
                stay();
                break;

            case "Quit":
                quit();
                break;

            default:
                System.out.println("I do not recognize that action command. Check your button text.");
        }
    }

    public void setUp(){
        if(end == 0){
            bankLabel = new GLabel("Bank: " + bank);
            add(bankLabel,0,10);
            wagerLabel = new GLabel("Wager: " + wager);
            add(wagerLabel,0,20);
            balanceLabel = new GLabel("Balance: " + balance);
            add(balanceLabel,0,30);
            streakLabel = new GLabel("Streak: " + streak);
            add(streakLabel,getWidth() * .75,getY() + 10);
            blackjack.setVisible(false);
            welcomeTo.setVisible(false);
        }else if(balance == 0){
            Dialog.showMessage("You have gone bankrupt.");
            lose();
        }
    }

    private void updateLabels(){
        bankLabel.setLabel("Bank: " + bank);
        wagerLabel.setLabel("Wager: " + wager);
        balanceLabel.setLabel("Balance: " + balance);
        streakLabel.setLabel("Streak: " + streak);
    }

    public void generateCards(){
        dealer = new GHand(new Hand(deck, true));
        add(dealer,getWidth()*.6,getHeight()/9);
        dealer.setVisible(false);

        player = new GHand(new Hand(deck,false));
        add(player,3,getHeight()*.7);
        player.setVisible(false);
    }

    private void wager(){
        wager = Dialog.getDouble("Enter your wager");

        if(wager > balance){
            Dialog.showMessage("You're too broke to wager that much.");
            wager();
        }else if(wager == 0){
            wager();
        }else{
            playbutton.setVisible(true);
            wagerButton.setVisible(false);
            balance -= wager;
            setUp();
        }

    }

    private void play(){
        player.setVisible(true);
        dealer.setVisible(true);
        playbutton.setVisible(false);
        hitButton.setVisible(true);
        stayButton.setVisible(true);
    }

    private void hit(){
        player.hit();
        if(player.getTotal() > 21){
            lose();
        }
    }

    private void stay(){
        hitButton.setVisible(false);
        stayButton.setVisible(false);
        dealer.flipCard(0);
        runDealer();
    }

    public void runDealer(){
        if(dealer.getTotal() >= 17 && dealer.getTotal() <= 21){
            if(player.getTotal() > dealer.getTotal()){
                win();
            }else if(player.getTotal() == dealer.getTotal()){
                tie();
            }else{
                lose();
            }
        }else{
            dealer.hit();
            runDealer();
        }
    }

    public void tie(){
        Dialog.showMessage("You tied!");
        balance += wager;
        updateLabels();
        wager = 0;
        end();
    }

    public void win(){
        streak++;
        balance += (wager * 2);
        wager = 0;
        updateLabels();
        Dialog.showMessage("You won!");
        end();
    }

    public void lose(){
        streak = 0;
        bank += wager;
        wager = 0;
        updateLabels();
        Dialog.showMessage("You lost!");
        end();
    }

    private void end(){
        end++;
        boolean playAgain = Dialog.getYesOrNo("Play again?");
        if(playAgain) {
            wagerButton.setVisible(true);
            stayButton.setVisible(false);
            hitButton.setVisible(false);
            dealer.removeAll();
            player.removeAll();
            generateCards();
        }else{
            System.exit(0);
        }
    }

    private void quit(){
        System.exit(0);
    }

    public static void main(String[] args) {
        new Blackjack().start();
    }

}
