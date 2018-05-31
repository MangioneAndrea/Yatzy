/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.yatzy.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author andre
 */
public class Points {

    final static public ArrayList<Dice> allDices = new ArrayList<>();

    public static int calculatePoints(String val) {
        sortList();
        switch (val) {
            case "ones":
                return Points.ones();
            case "twos":
                return Points.twos();
            case "threes":
                return Points.threes();
            case "fours":
                return Points.fours();
            case "fives":
                return Points.fives();
            case "sixes":
                return Points.sixes();
            case "one_pair":
                return Points.one_pair();
            case "three_of_a_kind":
                return Points.three_of_a_kind();
            case "four_of_a_kind":
                return Points.four_of_a_kind();
            case "small_straight":
                return Points.small_straight();
            case "large_straight":
                return Points.large_straight();
            case "full_house":
                return Points.full_house();
            case "chance":
                return Points.chance();
            case "yatzy":
                return Points.yatzy();
        }
        return 0;
    }

    public static int ones() {
        return getSum(1);
    }

    public static int twos() {
        return getSum(2);
    }

    public static int threes() {
        return getSum(3);
    }

    public static int fours() {
        return getSum(4);
    }

    public static int fives() {
        return getSum(5);
    }

    public static int sixes() {
        return getSum(6);
    }

    private static int getSum(int n) {
        int res = 0;
        for (Dice d : allDices) {
            if (d.getNum() == n) {
                res += d.getNum();
            }
        }
        return res;
    }

    public static int one_pair() {
        int tmp = 0;
        for (Dice d : allDices) {
            if (d.getNum() == tmp) {
                return tmp * 2;
            }
            tmp = d.getNum();
        }
        return 0;
    }

    public static int yatzy() {
        int tmp = allDices.get(0).getNum();
        for (Dice d : allDices) {
            if (d.getNum() != tmp) {
                return 0;
            }
        }
        return 50;
    }

    public static int three_of_a_kind() {
        int tmp = 0, tmp2 = 0;
        for (Dice d : allDices) {
            if (d.getNum() == tmp) {
                if (d.getNum() == tmp2) {
                    return d.getNum() * 3;
                }
                tmp2 = d.getNum();
                continue;
            }
            tmp = d.getNum();
        }
        return 0;
    }

    public static int four_of_a_kind() {
        int tmp = 0, tmp2 = 0, tmp3 = 0;
        for (Dice d : allDices) {
            if (d.getNum() == tmp) {
                if (d.getNum() == tmp2) {
                    if (d.getNum() == tmp3) {
                        return d.getNum() * 4;
                    }
                    tmp3 = d.getNum();
                    continue;
                }
                tmp2 = d.getNum();
                continue;
            }
            tmp = d.getNum();
        }
        return 0;
    }

    public static int small_straight() {
        int tmp = 0;
        int count = 0;
        for (Dice d : allDices) {
            if (d.getNum() != tmp - 1) {
                count++;
            }
            tmp = d.getNum();
        }
        return count > 2 ? 0 : 30;
    }

    public static int large_straight() {
        int tmp = 0;
        int count = 0;
        for (Dice d : allDices) {
            if (d.getNum() != tmp - 1) {
                count++;
            }
            tmp = d.getNum();
        }
        return count > 1 ? 0 : 40;
    }

    public static int full_house() {
        int a = allDices.get(0).getNum();
        int b = allDices.get(1).getNum();
        int c = allDices.get(2).getNum();
        int d = allDices.get(3).getNum();
        int e = allDices.get(4).getNum();
        if (a == b && c == d && d == e && a != c) {
            return 25;
        }
        if (a == b && b == c && d == e && a != d) {
            return 25;
        }
        return 0;
    }

    public static int chance() {
        int res = 0;
        for (Dice d : allDices) {
            res += d.getNum();
        }
        return res;
    }

    public static void sortList() {
        Collections.sort(
                allDices, (Dice d, Dice d2) -> d2.getNum() - d.getNum()
        );
    }
}
