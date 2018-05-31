/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model.hitter;

import ch.bbbaden.yatzy.model.Dice;
import ch.bbbaden.yatzy.model.Game;
import ch.bbbaden.yatzy.model.Points;
import ch.bbbaden.yatzy.model.StateManager;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;


/**
 *
 * @author andre
 */
public class Computer  implements Hitter{
    private short actualDice;

    public Computer() {
        System.out.println("now computer");
        this.actualDice = 0;
    }

    @Override
    public Hitter changeHitter() {
        System.out.println("now player");
        return new Player();
    }

    @Override
    public void hit() {
        try{
        boolean done=true;
        for(Dice d:Points.allDices){
            if(!d.isLocked()){
                done=false;
            }
        }
        try {
            if(!done)Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!StateManager.getInstance().canDice()) {
                    return;
                }
                Game.getInstance().disposeDices();
                Game.getInstance().updatePoints("computer");

            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                strategy();
                System.out.println("computerhit");
                System.out.println(actualDice);

                if (actualDice == 3) {
                    Game.getInstance().setPoints(getTheBest(), "computer");
                } else {
                    Game.getInstance().g.letHit();
                }
            }
        });
//        
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Computer.class.getName()).log(Level.SEVERE, null, ex);
//        }
        }catch(NullPointerException ex){
            System.err.println("Bot stop working");
        }
    }

    @Override
    public void setPoints() {
    }

    @Override
    public void nextDice() {
        actualDice++;
    }

    @Override
    public boolean hasNextDice() {
        if(actualDice<3)return true;
        return false;
    }
    private String getTheBest(){
        int best=0;String name="ones";
        
        try{
        for(Map.Entry<String,Label> m:Game.getInstance().points.get("computer").entrySet()){
            int cal=Points.calculatePoints(m.getKey());
            if(m.getKey().equals("chance"))continue;
            if(m.getKey().equals("total"))continue;
            if(m.getValue().isDisabled())continue;
            System.out.println(m.getKey()+" = "+cal);
            if(cal>=best){
                best=cal;
                name=m.getKey();
            }
        }
        }catch(Exception NullPointerException){
            return "";
        }
        if(best==0 && !Game.getInstance().points.get("computer").get("chance").isDisabled())return "chance";
        System.out.println("->>>>>>>>>>>>>"+name);
        return name;
    }
    private void strategy(){
        String best=getTheBest();
        switch(best){
            case "ones":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=1)d.unlock();
                    else d.lock();
                }
                break;
            case "twos":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=2)d.unlock();
                    else d.lock();
                }
                break;
            case "threes":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=3)d.unlock();
                    else d.lock();
                }
                break;
            case "fours":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=4)d.unlock();
                    else d.lock();
                }
                break;
            case "fives":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=5)d.unlock();
                    else d.lock();
                }
            case "sixes":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=6)d.unlock();
                    else d.lock();
                }
                break;
            case "one_pair":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=Points.one_pair()/2)d.unlock();
                    else d.lock();
                }
                break;
                
            case "three_of_a_kind":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=Points.three_of_a_kind()/3)d.unlock();
                    else d.lock();
                }
                break;
            case "four_of_a_kind":
                for(Dice d:Points.allDices){
                    if(d.getNum()!=Points.four_of_a_kind()/4)d.unlock();
                    else d.lock();
                }
                break;
            case "small_straight":
                for(Dice d:Points.allDices){
                    d.lock();
                }
                for(int i=0;i<Points.allDices.size()-1;i++){
                    if(Points.allDices.get(i).getNum()==Points.allDices.get(i+1).getNum()){
                        Points.allDices.get(i).unlock();
                        return;
                    }
                }
                if(Points.allDices.get(0).getNum()!=Points.allDices.get(1).getNum()-1){
                        Points.allDices.get(0).unlock();
                }else if(Points.allDices.get(3).getNum()!=Points.allDices.get(4).getNum()-1){
                    Points.allDices.get(4).unlock();
                }
                break;
            case "large_straight":
                for(Dice d:Points.allDices){
                    d.lock();
                }
                break;
            case "full_house":
                for(Dice d:Points.allDices){
                    d.lock();
                }
                break;
            case "yatzy":
                for(Dice d:Points.allDices){
                    d.lock();
                }
                break;
        }
        
    }

    @Override
    public boolean startedNow() {
        if(actualDice==0)return true;
        return false;
    }

    
}
