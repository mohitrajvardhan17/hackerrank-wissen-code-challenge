/*
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/tree-huffman-decoding
Input characters are only present in the leaves. Internal nodes have a character value of ϕ (NULL).
We can determine that our values for characters are:

A - 0
B - 111
C - 1100
D - 1101
R - 10
Our Huffman encoded string is:

A B    R  A C     A D     A B    R  A
0 111 10 0 1100 0 1101 0 111 10 0
or
01111001100011010111100
To avoid ambiguity, Huffman encoding is a prefix free encoding technique.
No codeword appears as a prefix of any other codeword.

To decode the encoded string, follow the zeros and ones to a leaf and return the character there.

You are given pointer to the root of the Huffman tree and a binary coded string to decode.
You need to print the decoded string.
 */
package com.wissen.hackerrank.first.huffmanencoding;


import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

abstract class Node implements Comparable<Node> {
    public int frequency;
    public char data;
    public Node left, right;

    public Node(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Node tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends Node {

    public HuffmanLeaf(int frequency, char val) {
        super(frequency);
        data = val;
    }
}

class HuffmanNode extends Node {

    public HuffmanNode(Node l, Node r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

class Decoding {
    private static Node original = null;
    private static boolean first = true;

    public void decode(String s, Node root) {
        assert root != null || s !=null;

        if (first) {
            original = root;
            first = false;
        }

        if (root instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) root;
            if (s.charAt(0) == '0') {
                decode(s.substring(1), root.left);
            } else if (s.charAt(0) == '1') {
                decode(s.substring(1), root.right);
            } else {
                new IllegalArgumentException("The Encoded String should only contain 0 and 1.");
            }
        } else if (root instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) root;
            System.out.print(leaf.data);
            if (s == null || s.length() == 0) {
                return;
            }
            decode(s, original);
        }
    }
}

public class Solution {

    public static Map<Character, String> mapA = new HashMap<Character, String>();

    public static void printCodes(Node tree, StringBuffer prefix) {
        assert tree != null;

        if (tree instanceof HuffmanLeaf) {

            HuffmanLeaf leaf = (HuffmanLeaf) tree;
            // print out character, frequency, and code for this leaf (which is just the prefix)
            //System.out.println(leaf.data + "\t" + leaf.frequency + "\t" + prefix);
            mapA.put(leaf.data, prefix.toString());
        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            //Todo: What is the purpose of below statement
            prefix.deleteCharAt(prefix.length() - 1);

            //traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            //Todo: What is the purpose of below statement
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


    // input is an array of frequencies, indexed by character code
    public static Node buildTree(int charFreq[]) {
        PriorityQueue<Node> trees = new PriorityQueue<Node>();

        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreq.length; i++) {
            if (charFreq[i] > 0) {
                trees.offer(new HuffmanLeaf(charFreq[i], (char) i));
            }
        }

        assert trees.size() > 0;

        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            Node a = trees.poll();
            Node b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }

        return trees.poll();
    }

    public static void main(String[] args) {
        //Scanner input = new Scanner(System.in);

        //String test= input.next();
        String test = "ABACA"; //Expected Output = ABACA

        // we will assume that all our characters will have
        // code less than 256, for simplicity
        int charFreqs[] = new int[256];

        // read each character and record the frequencies
        for (char c : test.toCharArray()) {
            charFreqs[c]++;
        }

        // build tree
        Node tree = buildTree(charFreqs);

        // print out results
        printCodes(tree, new StringBuffer());
        StringBuffer s = new StringBuffer();

        for (int i = 0; i < test.length(); i++) {
            char c = test.charAt(i);
            s.append(mapA.get(c));
        }

        Decoding d = new Decoding();
        d.decode(s.toString(), tree);
    }
}
