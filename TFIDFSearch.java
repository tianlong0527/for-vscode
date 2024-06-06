import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TFIDFSearch {
    public static void main(String[] args) {
        String filename = args[0] + ".ser";
        List<Trie> articlesTrie = new ArrayList<>();
        Trie IDFTrie = new Trie();
        Indexer idx = deSerialize(filename);

        // build trie tree
        for(String s : idx.getArticles()) {
            articlesTrie.add(buildTrieTree(s));
            IDFTrie = buildIDFTree(s, IDFTrie);
        }

        // readTestCase
        dealTestCase(args[1], articlesTrie, IDFTrie);
    }

    // deserialize the object
    static Indexer deSerialize(String filename) {
        Indexer idx = null;
        try { 
	        FileInputStream fis = new FileInputStream(filename);
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        idx = (Indexer) ois.readObject();
	        ois.close();
	        fis.close();
        } catch (IOException e) {
	        e.printStackTrace();
        } catch (ClassNotFoundException c) {
	        c.printStackTrace();
        }
        return idx;
    }

    // build Trie tree for articles
    static Trie buildTrieTree(String article) {
        Trie trie = new Trie();
        String[] words = article.trim().split(" ");
        for(String s : words) {
            trie.insert(s);
        }
        return trie;
    }

    // build Trie tree for IDF
    static Trie buildIDFTree(String article, Trie trieForIDF) {
        String[] words = article.trim().split(" ");
        for(String s : words) {
            trieForIDF.insert(s, true);
        }
        for(String s : words) {
            trieForIDF.setNewArticle(s);
        }
        return trieForIDF;
    }

    static void dealTestCase(String filename, List<Trie> articlesTrie, Trie IDFTrie) {
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int index = 0, n = 0;
            int num = 0;
            boolean check = false;

            while((line = br.readLine()) != null) {
                if(index == 0) { // the first line of each testcase is the number of queries
                    index++;
                    n = Integer.parseInt(line);
                    continue;
                }
                num++;
                System.out.println("num: " + num);

                if (line.contains("A")) {
                    ANDPart(line, articlesTrie, IDFTrie, n);
                } else if (line.contains("O")) {
                    ORPart(line, articlesTrie, IDFTrie, n);
                } else {
                    ANDPart(line, articlesTrie, IDFTrie, n);
                } 
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void ANDPart(String testCase, List<Trie> articlesTrie, Trie IDFTrie, int n) throws IOException {
        String[] keywords = testCase.split(" ");
        Set<String> interArticles = intersection(keywords, articlesTrie);
        calculateTFIDF(keywords, interArticles, articlesTrie, IDFTrie, n);
    }

    // take the intersection of the articles that contain all the keywords
    static Set<String> intersection(String[] keywords, List<Trie> articleTrie) {
        Set<String> interArticle = new TreeSet<>();
        for (int i = 0;i < keywords.length;i += 2) { // skip the "AND"
            Set<String> articleTemp = new TreeSet<>();
            // find the articles that contain the current keyword
            if(i == 0){
                for(int j = 0;j < articleTrie.size();j++) {
                    if(articleTrie.get(j).search(keywords[i])) {
                        articleTemp.add(String.valueOf(j));
                    }
                }
            } else {
                for(String s : interArticle) {
                    if(articleTrie.get(Integer.parseInt(s)).search(keywords[i])) {
                        articleTemp.add(s);
                    }
                }
            }
            if(i == 0) { // take intersection
                interArticle.addAll(articleTemp);
            } else {
                interArticle.retainAll(articleTemp);
            }
            articleTemp.clear();
        }
        return interArticle;
    }

    static void ORPart(String testCase, List<Trie> articlesTrie, Trie IDFTrie, int n) throws IOException {
        String[] keywords = testCase.split(" ");
        Set<String> unionArticles = union(keywords, articlesTrie);
        calculateTFIDF(keywords, unionArticles, articlesTrie, IDFTrie, n);
    }

    static Set<String> union(String[] keywords, List<Trie> articleTrie) {
        Set<String> unionArticle = new TreeSet<>();

        for(int i = 0;i < keywords.length;i += 2) {
            for(int j = 0;j < articleTrie.size();j++) {
                if(articleTrie.get(j).search(keywords[i])) {
                    unionArticle.add(String.valueOf(j));
                }
            }
        }
        return unionArticle;
    }

    static void calculateTFIDF(String[] keywords, Set<String> interORunion_articles, List<Trie> articlesTrie, Trie IDFTrie, int n) throws IOException {
        double tf = 0.0, idf = 0.0, tfidf_d = 0.0;
        Map<Integer, Double> tfidfMap = new HashMap<>();
        Deque<Integer> articleNum = new ArrayDeque<>();

        for(String article : interORunion_articles) {
            double current = 0.0;
            Deque<Integer> articleNumTemp = new ArrayDeque<>();
            for(int i = 0;i < keywords.length;i += 2) { 
                tf = tf(keywords[i], articlesTrie.get(Integer.parseInt(article)));
                idf = idf(keywords[i], IDFTrie, articlesTrie.size());
                tfidf_d = tf * idf;
                current += tfidf_d;
            }
            // debug 6607 3903 13290 16374 19886 4074 9235 13106 1600 4157 6246 19686 532 494 7002 10199 4788 6071
            if(Integer.parseInt(article) == 6607 || Integer.parseInt(article) == 3903 || Integer.parseInt(article) == 13290 || Integer.parseInt(article) == 16374 || Integer.parseInt(article) == 19886 || Integer.parseInt(article) == 4074 || Integer.parseInt(article) == 9235 || Integer.parseInt(article) == 13106 || Integer.parseInt(article) == 1600 || Integer.parseInt(article) == 4157 || Integer.parseInt(article) == 6246 || Integer.parseInt(article) == 19686 || Integer.parseInt(article) == 532 || Integer.parseInt(article) == 494 || Integer.parseInt(article) == 7002 || Integer.parseInt(article) == 10199 || Integer.parseInt(article) == 4788 || Integer.parseInt(article) == 6071) {
                System.out.println("article: " + article);
                System.out.println("current: " + current);
                System.out.println("n : " + n);
            }
            tfidfMap.put(Integer.parseInt(article), current);
            // sort tfidf and the article number
            if (articleNum.isEmpty()) {
                articleNum.push(Integer.parseInt(article));
            } else {
                // not the first element
                if(current < tfidfMap.get(articleNum.peek())) {
                    if(articleNum.size() < n) {
                        articleNum.push(Integer.parseInt(article));
                    }
                } else if (current == tfidfMap.get(articleNum.peek())) {
                    while(!articleNum.isEmpty() && current == tfidfMap.get(articleNum.peek()) && Integer.parseInt(article) < articleNum.peek()) {
                        articleNumTemp.push(articleNum.pop());
                    }
                    if(articleNum.size() < n) {
                        articleNum.push(Integer.parseInt(article));
                    }
                    while(articleNum.size() < n && !articleNumTemp.isEmpty()) {
                        articleNum.push(articleNumTemp.pop());
                    }
                } else {
                    while(!articleNum.isEmpty() && current > tfidfMap.get(articleNum.peek())) {
                        articleNumTemp.push(articleNum.pop());
                    }
                    if(!articleNum.isEmpty()) {
                        if(current == tfidfMap.get(articleNum.peek())) {
                            while(!articleNum.isEmpty() && current == tfidfMap.get(articleNum.peek()) && Integer.parseInt(article) < articleNum.peek()) {
                                articleNumTemp.push(articleNum.pop());
                            }
                            if(articleNum.size() < n) {
                                articleNum.push(Integer.parseInt(article));
                            }
                            while(articleNum.size() < n && !articleNumTemp.isEmpty()) {
                                articleNum.push(articleNumTemp.pop());
                            }
                        } else {
                            articleNum.push(Integer.parseInt(article));
                        }
                    } else {
                        articleNum.push(Integer.parseInt(article));
                    }
                    while(articleNum.size() < n && !articleNumTemp.isEmpty()) {
                        int temp = articleNumTemp.pop();    
                        articleNum.push(temp);
                    }
                }
                if(!articleNumTemp.isEmpty()) {
                    System.out.println(articleNumTemp);
                    articleNumTemp.clear();
                }
            }
        }

        // reverse the order of the article number
        StringBuilder sb = new StringBuilder();
        Deque<Integer> ReArticleNum = new ArrayDeque<>();
        for(int i : articleNum) {
            ReArticleNum.push(i);
        }
        while(!ReArticleNum.isEmpty()) {
            sb.append(ReArticleNum.pop() + " ");
        }

        // complete n items
        if(n > articleNum.size()) { 
            for(int i = articleNum.size();i < n;i++) {
                sb.append("-1 "); 
            }
        }
        writeFile(sb.toString());
    }

    static double tf(String keyword, Trie trie) {
        int appearTimes = trie.appearTimes(keyword);
        int totalWords = trie.appearTimes(null);
        return (double) appearTimes / totalWords;
    }

    static double idf(String keyword, Trie trie, int totalDocs) {
        int docsHasKeyWord = trie.countDoc(keyword);
        if(docsHasKeyWord != 0) return Math.log(totalDocs) - Math.log(docsHasKeyWord);
        else return 0.0;
    }

    static void writeFile(String content) throws IOException {
        content = content.substring(0, content.length() -1);
        content += "\n";
        File file = new File("output.txt");
        if(!file.exists()) {
            file.createNewFile();
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true))) {
            bw.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int count = 0;
    int countIDF = 0;
    boolean newArticle = true;
}

class Trie {
    TrieNode root = new TrieNode();
    
    public void insert(String word) {
        TrieNode node = root;
        root.count++;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.count++;
        node.isEndOfWord = true;
    }

    public void insert(String word, boolean T) { // the extra boolean T is for the case of inserting a word in the trie for IDF, meaningless here
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        if(node.newArticle) {
            node.newArticle = false;
            node.count++;
        }
    }

    public int appearTimes(String word) {
        if(word == null) {
            return root.count;
        }
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return 0;
            }
        }
        return node.count;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }

    public int countDoc(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return 0;
            }
        }
        return node.count;
    }

    public void setNewArticle(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
        }
        node.newArticle = true;
    }
}