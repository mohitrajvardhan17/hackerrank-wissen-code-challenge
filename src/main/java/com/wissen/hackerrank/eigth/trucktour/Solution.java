package com.wissen.hackerrank.eigth.trucktour;

import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
https://codecurse.medium.com/truck-tour-hackerrank-problem-f793501e9522
https://stackoverflow.com/questions/8651965/java-array-traversal-in-circular-manner
##############STEPS##############Todo: Complete this.
##############QUESTION##############Todo: Complete this.
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/truck-tour
Suppose there is a circle. There are N petrol pumps on that circle. Petrol pumps are numbered 0 to (N-1) (both
inclusive). You have two pieces of information corresponding to each of the petrol pump: (1) the amount of petrol
that particular petrol pump will give, and (2) the distance from that petrol pump to the next petrol pump.

Initially, you have a tank of infinite capacity carrying no petrol. You can start the tour at any of the petrol pumps.
Calculate the first point from where the truck will be able to complete the circle. Consider that the truck will stop
at each of the petrol pumps. The truck will move one kilometer for each litre of the petrol.

Input Format
The first line will contain the value of N.
The next N lines will contain a pair of integers each, i.e. the amount of petrol that petrol pump will give and the
distance between that petrol pump and the next petrol pump.

Constraints:
1 ≤ N ≤ 10^5
1 ≤ amount of petrol, distance ≤ 10^9

Output Format:
An integer which will be the smallest index of the petrol pump from which we can start the tour.

Sample Input:
3
1 5
10 3
3 4

Sample Output:
1

Explanation:
We can start the tour from the second petrol pump.
*/
public class Solution {

    /**
     * STEP 1: Define a PetrolPump class
     * This class provides implementation for storage of amount of petrol in a petrol pump and distance to the next
     * petrol pump. Also it computes and stores the balance petrol at every petrol pump.
     */
    class PetrolPump {
        final long nextPetrolPumpDistance;
        final long amountOfPetrol;
        final long balance;

        public PetrolPump(long amountOfPetrol, long nextPetrolPumpDistance) {
            this.nextPetrolPumpDistance = nextPetrolPumpDistance;
            this.amountOfPetrol = amountOfPetrol;
            this.balance = amountOfPetrol - nextPetrolPumpDistance;
        }
    }

    /**
     * STEP 2: Define a PetrolPumpTour class
     * This class provides implementation for inserting all the petrol pumps which are part of tour and computing the
     * starting petrol pump for the tour.
     */
    class PetrolPumpTour {

        private final PetrolPump[] petrolPumps;
        private final int size;
        private int index = 0;

        public PetrolPumpTour(int petrolPumpCount) {
            this.petrolPumps = new PetrolPump[petrolPumpCount];
            this.size = petrolPumpCount;
        }

        /**
         * STEP 3: Define a add() method in PetrolPumpTour class
         * This method takes one petrol pump and adds to the tour.
         *
         * @param petrolPump the petrol pump which needs to be inserted in the tour.
         */
        public void add(PetrolPump petrolPump) {
            if (index > size - 1) {
                return;
            }
            this.petrolPumps[this.index] = petrolPump;
            index++;
        }

        /**
         * STEP 4: Define a getIndexOfEntryPetrolPump() method in PetrolPumpTour class
         * This method computes the starting petrol pump index or -1 if the tour is not possible.
         *
         * @return The starting petrol pump index to start the tour. If the tour is not possible then it returns -1.
         */
        public int getIndexOfEntryPetrolPump() {
            if (index != size) {
                throw new IllegalStateException("Need all the petrol pumps");
            }
            long sum = 0;
            for (int i = 0; i < this.size; i++) {
                sum = 0;
                for (int j = 0; j < this.size; j++) {
                    sum = sum + this.petrolPumps[(i + j) % size].balance;
                    if (sum < 0) {
                        i = (i + j);
                        break;
                    }
                }
                if (sum >= 0) {
                    return i;
                }
            }
            return -1;
        }
    }

    public void init(Scanner SCANNER) {
        int petrolPumpCount = SCANNER.nextInt();
        PetrolPumpTour petrolPumpTour = new PetrolPumpTour(petrolPumpCount);
        for (int i = 0; i < petrolPumpCount; i++) {
            long amountOfPetrol = SCANNER.nextLong();
            long nextPetrolPumpDistance = SCANNER.nextLong();
            petrolPumpTour.add(new PetrolPump(amountOfPetrol, nextPetrolPumpDistance));
            SCANNER.nextLine();
        }
        System.out.println(petrolPumpTour.getIndexOfEntryPetrolPump());
    }

    private final static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        new Solution().init(SCANNER);
    }
}
