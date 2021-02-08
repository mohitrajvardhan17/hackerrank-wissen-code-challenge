/*
##############NOTES##############Todo: Complete this.
0. What is state machine and what are the types of state machine.
1. What is Nondeterministic finite automaton.
2. What is Deterministic finite automaton.
3. How to convert regular expression string to Nondeterministic finite automaton.
4. How to convert Nondeterministic finite automaton to Deterministic finite automaton.
##############STEPS##############Todo: Complete this.
1. Write logic to convert/parse a regular expression string to Nondeterministic finite automaton.
2. Write logic to convert Nondeterministic finite automaton to Deterministic finite automaton.
3. Write logic for raising the matrix to power p.
##############QUESTION##############
https://www.hackerrank.com/contests/wissen-coding-challenge-2021/challenges/count-strings
A regular expression is used to describe a set of strings. For this problem the alphabet is limited to 'a' and 'b'.

We define  to be a valid regular expression if:
1)  is "" or "".
2)  is of the form "", where  and  are regular expressions.
3)  is of the form "" where  and  are regular expressions.
4)  is of the form "" where  is a regular expression.

Regular expressions can be nested and will always have have two elements in the parentheses. ('' is an element, '' is not; basically, there will always be pairwise evaluation) Additionally, '' will always be the second element; '' is invalid.

The set of strings recognized by  are as follows:
1) If  is "", then the set of strings recognized .
2) If  is "", then the set of strings recognized .
3) If  is of the form "" then the set of strings recognized = all strings which can be obtained by a concatenation of strings  and , where  is recognized by  and  by .
4) If  is of the form "" then the set of strings recognized = union of the set of strings recognized by  and .
5) If  is of the form "" then the the strings recognized are the empty string and the concatenation of an arbitrary number of copies of any string recognized by .

Task
Given a regular expression and an integer, , count how many strings of length  are recognized by it.

Input Format

The first line contains the number of test cases .  test cases follow.
Each test case contains a regular expression, , and an integer, .

Constraints

It is guaranteed that  will conform to the definition provided above.
Output Format

Print  lines, one corresponding to each test case containing the required answer for the corresponding test case. As the answers can be very big, output them modulo .

Sample Input

3
((ab)|(ba)) 2
((a|b)*) 5
((a*)(b(a*))) 100
Sample Output

2
32
100
*/
package com.wissen.hackerrank.fourth.countstrings;

import java.io.IOException;
import java.util.*;

public class Solution {

    /**
     * STEP 0: Define a Regex class
     */
    class Regex {
        char e = '\0';
        public final Node root;
        public Node[] nodes;

        Regex(String regex) {
            Parser parser = new Parser(regex);
            parser.parse();
            SubsetConstruction subset = new SubsetConstruction(parser.expression.start, parser.expression.end);
            this.nodes = subset.dfa();
            this.root = nodes[0];
        }

        /**
         * STEP 1: Define a Edge class
         * This class provides implementation for immutable storage of a character value and pointer to next node.
         */
        class Edge {
            public final char data;
            public final Node nextNode;

            public Edge(char data, Node nextNode) {
                this.data = data;
                this.nextNode = nextNode;
            }
        }

        /**
         * STEP 2: Define a Node class
         * This class provides implementation for storage of multiple Edge class objects and a boolean variable to represent
         * the end state and non-end state.
         */
        class Node {
            public final List<Edge> edges = new ArrayList<>();
            public boolean isFinal;
        }

        /**
         * STEP 3: Define a Parser class
         * This class provides implementation for parsing a regular expression and converting it to a
         * state machine (i.e. Nondeterministic finite automaton).
         */
        class Parser {
            /**
             * STEP 4: Define an Expression class
             * This class provides implementation for storage of start and end node of
             * state machine (i.e. Nondeterministic finite automaton).
             */
            private class Expression {
                Node start = new Node();
                Node end = start;
            }

            private final String regex;
            private int pos;
            public Expression expression;

            public Parser(String regex) {
                this.regex = regex;
                this.pos = 0;
            }

            /**
             * STEP 5: Define a parse() method in Regex$Parse class
             * This method initiate parsing of regular expression.
             *
             * @return the starting Node of the generated state machine (i.e. Nondeterministic finite automaton).
             */
            public Node parse() {
                this.expression = sequence();
                return expression.start;
            }

            /**
             * STEP 6: Define a skipWhitespaces() method in Regex$Parse class
             * This method increments the position counter and skips one or more consecutive whitespaces until a
             * non-whitespace character is encountered.
             *
             * @return true if the position counter is not at end of the string else false.
             */
            private boolean skipWhitespaces() {
                while (pos < regex.length() && Character.isWhitespace(regex.charAt(pos))) {
                    pos++;
                }
                return pos < regex.length();
            }

            /**
             * STEP 7: Define a getCharAtPosition() method in Regex$Parse class
             * This method gives non-whitespace character at which position counter points or a null character if
             * position counter is at the end of the regex string.
             *
             * @return non-whitespace character at which position counter points or null character.
             */
            private char getCharAtPosition() {
                return skipWhitespaces() ? regex.charAt(pos++) : e;
            }

            /**
             * STEP 8: Define a sequence() method in Regex$Parse class
             * This method processes each non-whitespace character part of regex string and creates the
             * state machine (i.e. Nondeterministic finite automaton) from same character.
             *
             * @return Expression object containing start and end node of generated state machine.
             */
            private Expression sequence() {
                Expression expression = new Expression();
                for (; ; ) {
                    char data = getCharAtPosition();
                    switch (data) {
                        case 'a':
                        case 'b':
                            literal(expression, data);
                            break;
                        case '(':
                            expression = parenthesis(expression);
                            break;
                        case '|':
                            expression = pipe(expression);
                            break;
                        case '*':
                            expression = star(expression);
                            break;
                        default:
                            putback();
                            return expression;
                    }
                }
            }

            /**
             * STEP 9: Define a literal() method in Regex$Parse class
             * This method processes each literal character part of regex string and updates the
             * state machine (i.e. Nondeterministic finite automaton) by creating a new end node
             * and adds the newly created data edge in the new end node.
             *
             * @param expression Expression object containing start and end node of generated state machine.
             * @param data       non-whitespace character which will be inserted as Node in state machine.
             */
            private void literal(Expression expression, char data) {
                /**/
                expression.end.edges.add(new Edge(data, expression.end = new Node()));
                /**/
                //OR
                /**
                 Node newEndNode = new Node();
                 Edge newDataEdge = new Edge(data, newEndNode);
                 expression.end.edges.add(newDataEdge);
                 expression.end = newEndNode;
                 /**/
            }

            /**
             * STEP 10: Define a parenthesis() method in Regex$Parse class
             * This method processes each nested regular expression which is between opening and closing
             * parenthesis character in the regex string and updates the state machine (i.e. Nondeterministic
             * finite automaton) by recursively calling the sequence() method.
             *
             * @param expression Expression object containing start and end node of generated state machine.
             */
            private Expression parenthesis(Expression expression) {
                Expression nested = sequence();
                if (getCharAtPosition() != ')') {
                    throw new IllegalStateException("syntax error: " + ") expected");
                }
                //Todo: What is the purpose of the below condition.
                if (expression.start == expression.end) {
                    return nested;
                }
                expression.end.edges.add(new Edge(e, nested.start));
                expression.end = nested.end;
                return expression;
            }

            /**
             * STEP 11: Define a pipe() method in Regex$Parse class
             * This method processes each pipe character part of regex string and updates the
             * state machine (i.e. Nondeterministic finite automaton) by recursively calling
             * the sequence() method.
             *
             * @param first Expression object containing start and end node of generated state machine.
             */
            private Expression pipe(Expression first) {
                Expression second = sequence();
                Expression expression = new Expression();
                expression.start.edges.add(new Edge(e, first.start));
                expression.start.edges.add(new Edge(e, second.start));
                first.end.edges.add(new Edge(e, expression.end = new Node()));
                second.end.edges.add(new Edge(e, expression.end));
                return expression;
            }

            /**
             * STEP 12: Define a star() method in Regex$Parse class
             * This method processes each star character part of regex string and updates the
             * state machine (i.e. Nondeterministic finite automaton) by creating a new sub-expression
             * and inserting it into the original expression.
             *
             * @param inner Expression object containing start and end node of generated state machine.
             */
            private Expression star(Expression inner) {
                Expression expression = new Expression();
                expression.start.edges.add(new Edge(e, inner.start));
                inner.end.edges.add(new Edge(e, inner.start));
                inner.end.edges.add(new Edge(e, expression.end = new Node()));
                expression.start.edges.add(new Edge(e, expression.end));
                return expression;
            }

            /**
             * STEP 13: Define a putback() method in Regex$Parse class
             * This method decrements the position counter by one unless position counter is pointing
             * at the starting of the regular expresion string.
             */
            private void putback() {
                if (pos >= 0) {
                    --pos;
                }
            }
        }

        /**
         * STEP 14: Define a SubsetConstruction class
         * This class provides implementation for converting Nondeterministic finite automaton to
         * a Deterministic finite automaton.
         */
        class SubsetConstruction {

            /**
             * STEP 15: Define a SubsetConstruction$State class
             * This class provides implementation for storage of set of node representing Nondeterministic finite
             * automaton and another node representing Deterministic finite automaton.
             */
            private class State {
                public final Set<Node> nfa;
                public final Node dfa = new Node();

                public State(Set<Node> nfa) {
                    this.nfa = nfa;
                }
            }

            private final Node nfaEnd;
            private final Queue<State> queue;
            private final Map<Set<Node>, Node> dfaNodeByNfaNodes;
            private final Node dfaRoot;

            SubsetConstruction(Node nfaRoot, Node nfaEnd) {
                this.nfaEnd = nfaEnd;
                this.queue = new ArrayDeque<>();
                this.dfaNodeByNfaNodes = new HashMap<>();
                this.dfaRoot = addState(eClosure(nfaRoot)).dfa;
            }

            /**
             * STEP 16: Define a addState() method in Regex$SubsetConstruction class.
             * This method converts set of node representing Nondeterministic finite automaton to State Object while
             * updating the dfaNodeByNfaNodes Map and adding the state to queue.
             *
             * @param nfa Set of node representing Nondeterministic finite automaton.
             * @return State class object containing set of node representing Nondeterministic finite automaton
             * and another node representing Deterministic finite automaton.
             */
            private State addState(Set<Node> nfa) {
                State state = new State(nfa);
                state.dfa.isFinal = nfa.contains(nfaEnd);
                dfaNodeByNfaNodes.put(state.nfa, state.dfa);
                queue.add(state);
                return state;
            }

            /**
             * STEP 17: Define a eClosure() method in Regex$SubsetConstruction class.
             * This method takes the root node of Nondeterministic finite automaton and converts them to
             * set of distinct nodes representing Nondeterministic finite automaton.
             *
             * @param node Root node of Nondeterministic finite automaton
             * @return Set of distinct nodes representing Nondeterministic finite automaton.
             */
            private Set<Node> eClosure(Node node) {
                return eClosure(Collections.singletonList(node));
            }

            /**
             * STEP 18: Define a eClosure() method in Regex$SubsetConstruction class.
             * This method takes the collection of root node of Nondeterministic finite automaton and converts them to
             * set of distinct nodes representing Nondeterministic finite automaton.
             *
             * @param nodes Collection of nodes representing Nondeterministic finite automaton.
             * @return Set of distinct nodes  representing Nondeterministic finite automaton.
             */
            private Set<Node> eClosure(Collection<Node> nodes) {
                Set<Node> closure = new HashSet<>();
                Stack<Node> stack = new Stack<>();
                stack.addAll(nodes);
                while (!stack.isEmpty()) {
                    Node node = stack.pop();
                    if (closure.add(node)) {
                        stack.addAll(next(node, e));
                    }
                }
                return closure;
            }

            /**
             * STEP 19: Define a next() method in Regex$SubsetConstruction class.
             * This method takes a root Node and character as input then search the character in data of the edges
             * of each Nodes and stores next Node of edges containing the character in a list.
             *
             * @param node Root node of Nondeterministic finite automaton
             * @param data character which needs to be searched in the LinkedList.
             * @return Collection of nodes
             */
            private Collection<Node> next(Node node, char data) {
                Collection<Node> list = new ArrayList<>();
                for (Edge edge : node.edges) {
                    if (edge.data == data) {
                        list.add(edge.nextNode);
                    }
                }
                return list;
            }

            /**
             * STEP 20: Define a next() method in Regex$SubsetConstruction class.
             * This method takes a collection of root Node and character as input then search the character in data of
             * the edges of each Nodes and stores next Node of edges containing the character in a list.
             *
             * @param nodes Collection of root node of Nondeterministic finite automaton
             * @param data  Character which needs to be searched in the LinkedList.
             * @return Collection of nodes
             */
            private Collection<Node> next(Collection<Node> nodes, char data) {
                Collection<Node> list = new ArrayList<>();
                for (Node node : nodes) {
                    list.addAll(next(node, data));
                }
                return list;
            }

            /**
             * STEP 21: Define a dfa() method in Regex$SubsetConstruction class.
             * This method initiate the processing of all State objected inserted in the queue so that
             * Nondeterministic finite automaton Nodes could be converted to Deterministic finite automaton.
             *
             * @return Collection of nodes representing Deterministic finite automaton
             */
            public Node[] dfa() {
                while (!queue.isEmpty()) {
                    State state = queue.poll();
                    for (char data : new char[]{'a', 'b'}) {
                        Set<Node> nfaNextNodes = eClosure(next(state.nfa, data));
                        if (nfaNextNodes.isEmpty()) {
                            continue;
                        }
                        Node dfaNextNode = dfaNodeByNfaNodes.get(nfaNextNodes);
                        if (dfaNextNode == null) {
                            dfaNextNode = addState(nfaNextNodes).dfa;
                        }
                        state.dfa.edges.add(new Edge(data, dfaNextNode));
                    }
                }
                return getNodes();
            }

            /**
             * STEP 22: Define a getNodes() method in Regex$SubsetConstruction class.
             * This method converts all the values part of dfaNodeByNfaNodes Map to a List and returns.
             *
             * @return Collection of nodes representing Deterministic finite automaton.
             */
            private Node[] getNodes() {
                List<Node> nodes = new ArrayList<>();
                nodes.add(this.dfaRoot);
                for (Node node : dfaNodeByNfaNodes.values()) {
                    if (node != dfaRoot) {
                        nodes.add(node);
                    }
                }
                return nodes.toArray(new Node[nodes.size()]);
            }
        }
    }

    /**
     * STEP 23: Define a MatrixPower class
     * This class provides implementation for converting Nondeterministic finite automaton to
     * a Deterministic finite automaton.
     */
    class MatrixPower {
        private final long[][] matrix;
        private final Map<Integer, long[][]> powers;


        MatrixPower(long[][] matrix) {
            this.matrix = matrix;
            this.powers = new HashMap<>();
        }

        /**
         * STEP 24: Define a power() method in Regex$MatrixPower class.
         * This method uses recursive approach to raise a matrix to any power.
         *
         * @param p the power to which matrix needs to be raised.
         * @return matrix after raising it to input power.
         */
        public long[][] power(int p) {
            if (p == 1) {
                return this.matrix;
            }
            long[][] result = this.powers.get(p);
            if (result != null) {
                return result;
            }
            result = power(p / 2);
            powers.put(p / 2 * 2, result = multiply(result, result));
            if (p % 2 > 0) {
                powers.put(p, result = multiply(result, power(p % 2)));
            }
            return result;
        }

        /**
         * STEP 25: Define a power() method in Regex$MatrixPower class.
         * This method is used for multiplying two matrix.
         *
         * @param a first matrix which is used in multiplication.
         * @param b second matrix which is used in multiplication.
         * @return the resultant matrix after the multiplication.
         */
        private long[][] multiply(long[][] a, long[][] b) {
            long[][] m = new long[a.length][a.length];
            for (int i = 0; i < a.length; ++i) {
                for (int j = 0; j < a.length; ++j) {
                    long sum = 0;
                    for (int k = 0; k < a.length; ++k)
                        sum = (sum + a[i][k] * b[k][j]) % 1000000007L;//Todo: what is the uses of " % 1000000007L"
                    m[i][j] = sum;
                }
            }
            return m;
        }
    }

    /**
     * STEP 27: Define a RegexCounter class
     * This class provides implementation for counting the possible number of input which matches the
     * regular expression .
     */
    class RegexCounter {
        private final long[][] adjacencyMatrix;
        private final List<Integer> finalStates;

        /**
         * STEP 28: Define a RegexCounter constructor
         * This constructor initializes adjacency matrix and final state by iterating the through the nodes and its
         * associated edges.
         */
        public RegexCounter(Regex regex) {
            Map<Regex.Node, Integer> indexes = new HashMap<>();
            this.adjacencyMatrix = new long[regex.nodes.length][regex.nodes.length];//Todo: what is the adjacencyMatrix
            this.finalStates = new ArrayList<>();
            for (Regex.Node node : regex.nodes) {
                int index = getIndex(indexes, node);
                for (Regex.Edge edge : node.edges) {
                    int next = getIndex(indexes, edge.nextNode);
                    adjacencyMatrix[index][next] = 1;
                }
            }
        }

        /**
         * STEP 29: Define a getIndex() method in RegexCounter class.
         * This method updates the finalStates by adding the input node if the input node is final and
         * calculates index of a node from a given input Map of nodes which has mapping of nodes by its
         * index. If the node is new then it add the node to the map with a new index which is the size
         * of the Map.
         *
         * @param indexByNode Mapping of nodes by its associated index.
         * @param node        the node for which index is required.
         * @return index of input node in the input Map.
         */
        private int getIndex(Map<Regex.Node, Integer> indexByNode, Regex.Node node) {
            Integer index = indexByNode.get(node);
            if (index == null) {
                indexByNode.put(node, index = indexByNode.size());
                if (node.isFinal) {
                    finalStates.add(index);
                }
            }
            return index;
        }

        /**
         * STEP 30: Define a count() method in RegexCounter class.
         * This method raise the adjacency matrix to a power and loops over the finalStates to sum up the count
         * of possible input for a specific regular expression using the resultant matrix after raising it to a
         * power.
         *
         * @param len length of input string for which associated regular expression combination count is required.
         * @return the count of possible input for a regex.
         */
        long count(int len) {
            long[][] matrix = new MatrixPower(adjacencyMatrix).power(len);
            long count = 0;
            for (int finalState : finalStates) {
                count = (count + matrix[0][finalState]) % 1000000007L;//Todo: what is the uses of " % 1000000007L"
            }
            return count;
        }
    }

    /*
     * Complete the countStrings function below.
     */
    public long countStrings(String regex, int len) {
        /*
         * Write your code here.
         */
        return len == 0 ? 0 : new RegexCounter(new Regex(regex)).count(len);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        int t = Integer.parseInt(scanner.nextLine().trim());

        for (int tItr = 0; tItr < t; tItr++) {
            String[] rl = scanner.nextLine().split(" ");

            String r = rl[0];

            int l = Integer.parseInt(rl[1].trim());

            long result = new Solution().countStrings(r, l);

            System.out.println(String.valueOf(result));
        }
    }
}