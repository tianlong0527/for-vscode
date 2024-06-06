import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Indexer implements Serializable{
    private List<String> articles = new ArrayList<String>();

    // constructor
    Indexer(String filename) throws IOException {
        articles = dealFile(filename);
    }

    // read and deal with the file
    public List<String> dealFile(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        List<String> article = new ArrayList<String>();
        int count1 = 0, count2 = 0;

        while((line = br.readLine()) != null) {
            if(count1 < 5) {
                count1++;
                if(article.isEmpty() || article.size() == count2){
                    article.add(line);
                } else {
                    article.set(count2, article.get(count2) + line);
                }
                if(count1 == 5) {
                    article.set(count2, article.get(count2).toLowerCase().replaceAll("[^a-z]+", " "));
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

    // get articles (articles is private variable)
    public List<String> getArticles() {
        return articles;
    }

    
}