class TrieNode {
    TrieNode[] children = new TrieNode[26];
    int count = 0; // how many words or letters that pass through this node
    boolean isEndOfWord = false;
}

public class Trie {
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

    public int appearTimes(String word) {
        if(word == null) {
            return root.count;
        }
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return -1;
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
}
