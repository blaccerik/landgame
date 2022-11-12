package com.example.landgame;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ToString
public class MoveStats {

    @Override
    public String toString() {
        return this.lowest + " " + this.total;
    }

    public static final int MAX_SIZE = 400;
    private static final int IF_MAX = 1000;

    //    private int score;
    private int lowest;
    private int count;
    private int total;

    // index is distance
    // value is count
    private int[] queue;

    public MoveStats() {
        this.count = 0;
        this.total = 0;
        this.lowest = IF_MAX;
        this.queue = new int[MAX_SIZE];
    }

    public void addDistance(int distance) {
        if (distance < lowest) {
            lowest = distance;
        }
        this.count++;
        this.total += distance;
        this.queue[distance]++;
//        this.score += (float) 1 / (sum * sum);
    }

    public void removeDistance(int distance) {
        // there might be new lowest
        this.count--;
        this.total -= distance;
        this.queue[distance]--;
        if (distance == this.lowest && this.queue[distance] == 0) {

            int best = IF_MAX;
            for (int i = distance; i < MAX_SIZE; i++) {
                int n = this.queue[i];
                if (n < best && n != 0) {
                    best = i;
                    break;
                }
            }
            this.lowest = best;
        }
    }


    public float getScore() {
        int a = this.lowest * this.total;
        if (a == 0) {
            return 0;
        }
//        System.out.println(this);
        return (float) 1 / a;
    }
}
