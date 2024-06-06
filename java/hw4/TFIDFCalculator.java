import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TFIDFCalculator {
    static ArrayList<String> readAndParse(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> article = new ArrayList<String>();
        int count1 = 0, count2 = 0;

        while((line = br.readLine()) != null) {
            if(count1 < 5) {
                count1++;
                if(article.isEmpty() || article.size() == count2){
                    article.add(line);
                } else {
                    article.set(count2, article.get(count2) + line);
                }
            } else {
                count1 = 1;
                count2++;
                if(article.isEmpty() || article.size() == count2){
                    article.add(line);
                } else {
                    article.set(count2, article.get(count2) + line);
                }
            }
        }
        br.close();
        return article;
    }

    static Trie buildTrieTree(String article, TrieForIDF countDocTrie) {
        Trie trie = new Trie();
        article = article.toLowerCase();
        article = article.replaceAll("[^a-z]+", " ");
        String[] words = article.trim().split("\\s+");
        for(String s : words) {
            trie.insert(s);
        }
        return trie;
    }

    static TrieForIDF buildTrieForIDF(String article, TrieForIDF countDocTrie) {
        article = article.toLowerCase();
        article = article.replaceAll("[^a-z]+", " ");
        String[] words = article.trim().split("\\s+");
        for(String s : words) {
            countDocTrie.insert(s);
        }
        for(String s : words) {
            countDocTrie.setNewArticle(s);
        }
        return countDocTrie;
    }

    static String[] readTestCaseKeyWord(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        String[] keyWord = line.trim().split("\\s+");
        br.close();
        return keyWord;
    }

    static String[] readTestCaseArticle(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int count = 0;
        String[] article = null;
        while((line = br.readLine()) != null) {
            if(count == 0) {
                count++;
                continue;
            }
            article = line.trim().split("\\s+");
        }
        br.close();
        return article;
    }

    static double tf(String keyWord, Trie trie) {
        int appearTimes = trie.appearTimes(keyWord);
        if(appearTimes == -1) {
            appearTimes = 0;
        }
        int totalWords = trie.appearTimes(null);
        return (double) appearTimes / totalWords;
    }

    static double idf(int totalDocs, TrieForIDF trie, String keyWord) {
        int docsHasKeyWord = trie.countDoc(keyWord);
        return Math.log(totalDocs) - Math.log(docsHasKeyWord);
    }
    
    public static void main(String[] args) throws IOException{
        // every five lines is an article
        ArrayList<String> article = readAndParse(args[0]);
        Trie[] trie = new Trie[article.size()];
        TrieForIDF countDocTrie = new TrieForIDF();

        // build the trie tree for every article
        for(int i = 0; i < trie.length; i++) {
            trie[i] = buildTrieTree(article.get(i), countDocTrie);
            countDocTrie = buildTrieForIDF(article.get(i), countDocTrie, trie[i]);
        }

        // store the testcases in two arrays
        String[] testCaseKeyWord = readTestCaseKeyWord(args[1]);
        String[] testCaseArticle = readTestCaseArticle(args[1]);

        double[] tFrequency = new double[testCaseKeyWord.length];
        double[] idFrequency = new double[testCaseKeyWord.length];
        double[] tfIdf_d = new double[testCaseKeyWord.length];
        String[] tfIdf_s = new String[testCaseKeyWord.length];

        for(int i = 0;i < testCaseKeyWord.length;i++) {
            // to calculate the tf-idf of every testcases
            tFrequency[i] = tf(testCaseKeyWord[i], trie[Integer.parseInt(testCaseArticle[i])]);
            if(tFrequency[i] != 0) {
                idFrequency[i] = idf(article.size(), countDocTrie, testCaseKeyWord[i]);
            } else {
                idFrequency[i] = 0;
            }
            /*System.out.println("tFrequency: " + tFrequency[i]);
            System.out.println("idFrequency: " + idFrequency[i]);*/         //debug
            tfIdf_d[i] = tFrequency[i] * idFrequency[i];
            tfIdf_s[i] = String.format("%.5f", tfIdf_d[i]);
        }
        // write to file
        StringBuilder content = new StringBuilder();
        for(int i = 0;i < tfIdf_s.length;i++) {
            if(i != tfIdf_s.length - 1) {
                content.append(tfIdf_s[i] + " ");
            } else {
                content.append(tfIdf_s[i]);
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            bw.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}