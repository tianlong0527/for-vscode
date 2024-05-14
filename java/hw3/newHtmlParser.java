import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class newHtmlParser {    
    static String Title() {
        String Title = "";
        try {
            Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
            Elements title = doc.select("th");
            for(Element s : title){
                Title += s.text() + " ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Title;
    }

    static Map< String, String > stockPrice() throws IOException {
        Map< String, String > stockPrice = new HashMap< String, String >();
        Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
        stockPrice.put(doc.title(), "");
        Elements price = doc.select("td");
        for(Element s : price){
            stockPrice.put( doc.title() , stockPrice.get(doc.title()) + s.text() + " ");
        }
        return stockPrice;
    }

    static String[] findStock(String targetStock) throws IOException {
        String[] stockData = new String[31];
        String filename = "data.csv";
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String rl = "";
        int location = -1; // 股票名稱在location，價格在location+1
        int count = 0;
        while((rl = br.readLine()) != null) {
            String[] data = rl.split(" ");
            if(!rl.contains("day")) { // the first line ( the names of stocks )
                for(int i = 0;i < data.length;i++) {
                    if(data[i].equals(targetStock)) {
                        stockData[count] = targetStock;
                        location = i;
                        break;
                    }
                }
            } else {
                stockData[count] = data[location + 1];
            }
            count++;
        }
        br.close();
        return stockData;
    }

    static double Rounding(double num) {
        if(num >= 0) {
            return ((int) (num * 100 + 0.5)) / 100.0; // -3.456
        }
        else {
            return ((int) (num * 100 - 0.5)) / 100.0; // -3.456 
        }
    }

    static double average(String[] data, int days, int start, int end) {
        double sum = 0;
        for(int i = start;i <= end;i++) {
            sum += Double.parseDouble(data[i]);
        }
        sum /= days; 
        return sum;
    }

    static double absolutely(double num) {
        if(num < 0) {
            return -num;
        }
        return num;
    }

    static double squareRoot(double num, double guess) {
        double newGuess = (guess + num / guess) / 2; // 牛頓迭代法
        if (absolutely(guess - newGuess) < 0.0000001) {
            return newGuess;
        } else {
            return squareRoot(num, newGuess); 
        }
    }

    static double standardDeviation(String[] data, int start, int end) { 
        int days = end - start + 1;
        double average = average(data, days, start, end);
        double sum = 0;
        for(int i = start;i <= end;i++) {
            double num = Double.parseDouble(data[i]);
            num -= average;
            num *= num;
            sum += num;
        }
        sum /= (days - 1);
        double guess = sum / 2;
        sum = squareRoot(sum, guess);
        sum = Rounding(sum);
        return sum;
    }
    
    static void modeZero() throws IOException {
        String stockTitle = Title(); // the stocks' names
        Map< String, String > stockPrice = stockPrice(); // the prices of stocks
        String filename = "data.csv";
        File file = new File(filename);
        String output = "";

        if(!file.exists()) {
            file.createNewFile();
        }
        if(Files.readAllLines(file.toPath()).isEmpty()) {
            output = stockTitle + "\n";
        }
        for(String s : stockPrice.keySet()){
            output += s + " " + stockPrice.get(s) + "\n";
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv", true))){
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void taskZero() throws IOException {
        String filename = "data.csv";
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String rl = "";
        String content = "";
        while((rl = reader.readLine()) != null) {
            if(rl.contains("day")) { // day, the prices of stocks
                int s = rl.indexOf(" "); // delete day
                String price = rl.substring(s + 1);
                price = price.replace(" ", ",");
                content += price;
            }
            else { // the names of stocks
                rl = rl.replace(" ", ",");
                content += rl;
            }
            if(content.endsWith(",")) { // delete the last space
                content = content.substring(0, content.length() - 1);
            }
            content += "\n";
        }
        content = content.substring(0, content.length() - 2); // delete the last \n
        reader.close();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv", true))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void taskOne(String targetStock, int start, int end) throws IOException {
        String[] stockData = findStock(targetStock);
        int endStart = end - 4; // the beginning of the last five days
        double[] averageFiveDays = new double[endStart - start + 1];
        for(int i = start;i <= endStart;i++) {
            double sum = 0;
            for(int j = 0;j < 5;j++) {
                sum += Double.parseDouble(stockData[i + j]);
            }
            sum /= 5;
            sum = Rounding(sum); // 四捨五入
            averageFiveDays[i - start] = sum;
        }
        String output = "";
        output += targetStock + "," + start + "," + end + "\n";
        for(int i = 0;i < averageFiveDays.length - 1;i++) {
            output += averageFiveDays[i] + ",";
        }
        output += averageFiveDays[averageFiveDays.length - 1] + "\n";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv", true))) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void taskTwo(String targetStock, int start, int end) throws IOException {
        String[] data = findStock(targetStock);
        double sd = standardDeviation(data, start, end); // standard deviation
        String output = targetStock + "," + start + "," + end + "\n";
        if((String.valueOf(sd)).endsWith("0")) {
            output += (int) sd + "\n";
        } else {
            output += sd + "\n";
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv", true))) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void taskThree(int start, int end) throws IOException {
        String filename = "data.csv";
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = br.readLine();
        String[] stockName = st.split(" ");
        br.close();
        String[] Top = new String[3];
        double[] TopSD = new double[3];
        for(String s : stockName) {
            String[] data = findStock(s);
            double sd = standardDeviation(data, start, end);
            if(sd > TopSD[0]) {
                TopSD[2] = TopSD[1];
                TopSD[1] = TopSD[0];
                TopSD[0] = sd;
                Top[2] = Top[1];
                Top[1] = Top[0];
                Top[0] = s;
            } else {
                if(sd > TopSD[1]) {
                    TopSD[2] = TopSD[1];
                    TopSD[1] = sd;
                    Top[2] = Top[1];
                    Top[1] = s;
                } else {
                    if(sd > TopSD[2]) {
                        TopSD[2] = sd;
                        Top[2] = s;
                    }
                }
            }
        }
        String output = Top[0] + "," + Top[1] + "," + Top[2] + "," + start + "," + end + "\n";
        for(int i = 0;i < 3;i++) {
            if((String.valueOf(TopSD[i])).endsWith("0")) {
                output += (int) TopSD[i] + ",";
            } else {
                output += TopSD[i] + ",";
            }
        }
        output = output.substring(0, output.length() - 1) + "\n";
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv", true))) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void taskFour(String targetStock, int start, int end) throws IOException {
        String[] data = findStock(targetStock);
        int days = end - start + 1;
        double Paverage = average(data, days, start, end);
        double Daverage = 0;
        for(int i = start;i <= end;i++) {
            Daverage += i;
        }
        Daverage /= days;
        double sumS = 0; // 分子
        double sumM = 0; // 分母
        for(int i = start;i <= end;i++) {
            sumS += (i - Daverage) * (Double.parseDouble(data[i]) - Paverage);
            sumM += (i - Daverage) * (i - Daverage);
        }
        double b1 = sumS / sumM;
        double b0 = Paverage - b1 * Daverage;
        b1 = Rounding(b1);
        b0 = Rounding(b0);
        String output = targetStock + "," + start + "," + end + "\n";
        if((String.valueOf(b1)).endsWith("0")) {
            output += (int) b1 + ",";
        } else {
            output += b1 + ",";
        }
        if((String.valueOf(b0)).endsWith("0")) {
            output += (int) b0 + "\n";
        } else {
            output += b0 + "\n";
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv", true))) {
            bw.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        if(args[0].equals("0")) {
            modeZero();
        }
        else { // mode 1
            if(args[1].equals("0")) { // task 0
                taskZero();
            }
            else if(args[1].equals("1")) { // task 1
                taskOne(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }
            else if(args[1].equals("2")) { // task 2
                taskTwo(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }
            else if(args[1].equals("3")) {  // task 3
                taskThree(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }
            else { // task 4
                taskFour(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }
        }
    } // main end
}