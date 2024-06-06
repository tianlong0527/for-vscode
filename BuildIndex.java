import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class BuildIndex {
    public static void main(String[] args) throws IOException {
        String filename = args[0].replaceAll("[^0-9]", " ");
        String[] numInFilename = filename.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append("corpus" + numInFilename[numInFilename.length - 1] + ".ser");
        Indexer idx = new Indexer(args[0]);
        try {
	        FileOutputStream fos = new FileOutputStream(sb.toString());
	        ObjectOutputStream oos = new ObjectOutputStream(fos);
	        oos.writeObject(idx);
	
	        oos.close();
	        fos.close();
        } catch (IOException e) {
	        e.printStackTrace();	
        }
    }
}
