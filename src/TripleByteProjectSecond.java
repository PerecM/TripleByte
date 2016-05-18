/**
 * Created by Elena Aravina on 5/17/16.
 *
 * containsGene(String genome, String gene) is a function that takes a genome (string of ACTG legalChars)
 * and a gene sequence (another string), and returns a bool indicating whether the gene is found in the genome
 */
import java.util.*;

public class TripleByteProjectSecond {

    //if nothing except genome prints out, then code works correctly
    public static void main(String[] args) {
        char[] legalChars = {'A', 'C', 'T', 'G'};
        Random random = new Random(System.currentTimeMillis());
        StringBuilder genomeBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            genomeBuilder.append(legalChars[random.nextInt(4)]);
        }

        String genome = genomeBuilder.toString();
        System.out.println(genome);

        for (int i = 0; i < 1000; i++) {
            StringBuilder geneBuilder = new StringBuilder();
            for (int j = 1; j < random.nextInt(50); j++) {
                geneBuilder.append(legalChars[random.nextInt(4)]);
            }
            String gene = geneBuilder.toString();
            try {
                //compare brute force with the algorithm
                if(genome.contains(gene) != containsGene(genome, gene)) {
                    System.out.println(genome.contains(gene) + " for " + gene + " " + containsGene(genome, gene));
                }
            } catch (Exception e) {
                //comment off if want to see all cases when either gene or genome was generated empty
                //System.out.println(e);
            }
        }
    }

    //simple case check
//    public static void main(String[] args) {
//        try {
//            System.out.println(containsGene("GGGTACCA", "GGTACC"));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//
//    }

    protected static boolean containsGene(String genome, String gene) throws Exception{
        if(gene.length() < 1 || genome.length() < 1) {
            throw new IllegalArgumentException("Either the gene or genome is an empty string. Please check the arguments.");
        }
        StateMachine geneSeq;
        try {
            geneSeq = new StateMachine(gene);
            for(int i = 0; i < genome.length(); i++) {
                if(geneSeq.operateState(genome.charAt(i))) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    private static class StateMachine {
        char[] legalChars = new char[]{'A', 'C', 'T', 'G'};
        String gene;
        State[] states;

        int currentState = 0;

        private StateMachine(String gene) {
            this.gene = gene;
            init();
        }

        private void init() {
            states = new State[gene.length()];
            states[0] = new State(gene.charAt(0));
            for (int i = 1; i < states.length; i++) {
                char currentChar = gene.charAt(i);
                states[i] = new State(currentChar);

                for (String suffix : states[i - 1].suffixes) {
                    for (char ch : legalChars) {
                        if (ch != currentChar && !states[i].transitions.containsKey(ch) && gene.startsWith(suffix + ch)) {
                            states[i].transitions.put(ch, (suffix + ch).length());
                        }
                    }
                }

                List<String> newSuffixes = new ArrayList<String>();
                for (String suffix : states[i - 1].suffixes) {
                    newSuffixes.add(suffix + currentChar);
                }

                states[i].suffixes.addAll(newSuffixes);
                Collections.sort(states[i].suffixes);
                Collections.reverse(states[i].suffixes);
            }
        }

        protected boolean operateState(char d) {
            if (states[currentState].value == d) {
                if (currentState + 1 == gene.length()) {
                    return true;
                } else {
                    currentState++;
                }
            } else {
                if (states[currentState].transitions.containsKey(d)) {
                    currentState = states[currentState].transitions.get(d);
                } else {
                    currentState = 0;
                }
            }
            return false;
        }
    }

    private static class State {
        List<String> suffixes = new ArrayList<String>();
        Map<Character, Integer> transitions = new HashMap<Character, Integer>();
        char value;

        State(char value) {
            this.value = value;
            suffixes.add("");
        }
    }


}
