class TrieIDF {
    TrieIDF[] children = new TrieIDF[26];
    int count = 0;
    boolean newArticle = true;
}

public class TrieForIDF {
    TrieIDF root = new TrieIDF();

    public void insert(String word) {
        TrieIDF node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieIDF();
            }
            node = node.children[c - 'a'];
        }
        if(node.newArticle) {
            node.newArticle = false;
            node.count++;
        }
    }

    public int countDoc(String word) {
        TrieIDF node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return -1;
            }
        }
        return node.count;
    }

    public void setNewArticle(String word) {
        TrieIDF node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
        }
        node.newArticle = true;
    }
}
