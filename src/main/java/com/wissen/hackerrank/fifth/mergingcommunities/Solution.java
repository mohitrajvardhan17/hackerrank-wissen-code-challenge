package com.wissen.hackerrank.fifth.mergingcommunities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/*
##############NOTES##############Todo: Complete this.
##############STEPS##############Todo: Complete this.
##############QUESTION##############
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/merging-communities
People connect with each other in a social network. A connection between Person I and Person J is represented as MIJ.
When two persons belonging to different communities connect, the net effect is the merger of both communities which I
 and J belongs to.

At the beginning, there are N people representing N communities. Suppose person 1 and 2 connected and later 2 and 3
connected, then 1,2, and 3 will belong to the same community.

There are two type of queries:
 1. MIJ -> communities containing person I and J merged (if they belong to different communities).
 2. QI -> print the size of the community to which person I belongs.

Input Format :
The first line of input will contain integers N and Q, i.e. the number of people and the number of queries.
The next Q lines will contain the queries.

Constraints :
1 <= N <= 10^5
1 <= Q <= 2 * 10^5

Output Format :
The output of the queries.

Sample Input :
3 6
Q 1
M 1 2
Q 2
M 2 3
Q 3
Q 2

Sample Output :
1
2
3
3

Explanation :
Initial size of each of the community is 1.
 */
public class Solution {
    private List<Community<Person>> communities = new ArrayList<>();

    private class Person {
        private final int personId;

        public Person(int personId) {
            this.personId = personId;
        }

        public int getPersonId() {
            return personId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return personId == person.personId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(personId);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "personId=" + personId +
                    '}';
        }
    }

    private class Community<T> {
        private List<T> collections = new ArrayList<>();

        public boolean isEmpty() {
            return !isNull() && collections.isEmpty();
        }

        public boolean isNull() {
            return collections != null;
        }

        public List<T> returnAllOrReturnEmpty() {
            return isNull() ? collections : Collections.<T>emptyList();
        }

        public List<T> returnAllOrCreateEmpty() {
            if (!isNull()) {
                setAll(new ArrayList<T>());
            }
            return collections;
        }

        public void setAll(List<T> collections) {
            this.collections = collections;
        }

        public void add(T record) {
            returnAllOrCreateEmpty().add(record);
        }

        public void addAll(List<T> records) {
            returnAllOrCreateEmpty().addAll(records);
        }

        public void addCommunity(Community<T> community) {
            returnAllOrCreateEmpty().addAll(community.returnAllOrReturnEmpty());
        }

        public void addCommunities(List<Community<T>> communities) {
            for (Community<T> community : communities) {
                addCommunity(community);
            }
        }

        public boolean isPresent(T record) {
            return collections.stream().anyMatch(i -> i.equals(record));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Community<?> community = (Community<?>) o;
            return collections.equals(community.collections);
        }

        @Override
        public int hashCode() {
            return Objects.hash(collections);
        }

        @Override
        public String toString() {
            return "Community{" +
                    "collections=" + collections +
                    '}';
        }
    }

    private void addPersonToCommunity(Person person, Community<Person> community) {
        community.add(person);
        this.communities.add(community);
    }

    private void mergeCommunities(Community<Person> newCommunity, Community<Person> oldCommunity) {
        newCommunity.addCommunity(oldCommunity);
        this.communities.remove(oldCommunity);
    }

    private List<Community<Person>> getCommunityOfPerson(Person firstPerson, Person secondPerson) {
        List<Community<Person>> communities = new ArrayList<>();
        Community<Person> firstPersonCommunity = null;
        Community<Person> secondPersonCommunity = null;
        for (Community<Person> community : this.communities) {
            if (firstPersonCommunity == null) {
                firstPersonCommunity = (community.isPresent(firstPerson)) ? community : null;
            }
            if (secondPersonCommunity == null) {
                secondPersonCommunity = (community.isPresent(secondPerson)) ? community : null;
            }
            if (firstPersonCommunity != null && secondPersonCommunity != null) {
                break;
            }
        }
        communities.add(firstPersonCommunity);
        communities.add(secondPersonCommunity);
        return communities;
    }

    private int processPersonCommunitySizeQuery(Person person) {
        int size = -1;
        for (Community<Person> community : this.communities) {
            boolean result = community.isPresent(person);
            if (result) {
                size = community.returnAllOrReturnEmpty().size();
                break;
            }
        }
        return size;
    }

    private void processMergeCommunityQuery(Person firstPerson, Person secondPerson) {
        List<Community<Person>> output = getCommunityOfPerson(firstPerson, secondPerson);
        if (!output.get(0).equals(output.get(1))) {
            mergeCommunities(output.get(0), output.get(1));
        }
    }

    public void processQuery(String query) {
        if (query == null || query.length() == 0) {
            return;
        }
        String[] args = query.split(" ");
        switch (args.length) {
            case 2:
                int personId = Integer.parseInt(args[1]);
                Person person = new Person(personId);
                int result = processPersonCommunitySizeQuery(person);
                System.out.println(result);
                break;
            case 3:
                int firstPersonId = Integer.parseInt(args[1]);
                Person firstPerson = new Person(firstPersonId);
                int secondPersonId = Integer.parseInt(args[2]);
                Person secondPerson = new Person(secondPersonId);
                processMergeCommunityQuery(firstPerson, secondPerson);
                break;
            default:
        }
    }

    public void initializeCommunityWithPerson(int numberOfPerson) {
        for (int i = 1; i <= numberOfPerson; i++) {
            addPersonToCommunity(new Person(i), new Community<Person>());
        }
    }

    private final static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Solution solution = new Solution();
        String input = SCANNER.nextLine().trim();
        String[] inputs = input.split(" ");
        int numberOfPerson = Integer.parseInt(inputs[0]);
        int numberOfQuery = Integer.parseInt(inputs[1]);
        solution.initializeCommunityWithPerson(numberOfPerson);
        for (int i = 1; i <= numberOfQuery; i++) {
            String query = SCANNER.nextLine().trim();
            solution.processQuery(query);
        }
    }
}