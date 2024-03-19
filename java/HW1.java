import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HW1 {
    //判斷是否為迴文
    public static boolean palindrome(String line) {
        int left = 0;
        int right = line.length() - 1;
        while(left < right){
            if(line.charAt(left) != line.charAt(right)){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    //判斷是否存在str1
    /*public static boolean exist(String line, String str1){
        for(int i = 0;i <= (line.length() - str1.length());++i){
            for(int j = 0;j < str1.length(); ++j){
                if(line.charAt(i+j) == str1.charAt(j)){

                }
            }
        }
        return false;
    }*/

    //判斷line中是否存在str2，且超過times次
    public static boolean exist_times(String line, String str2, int times){
        for(int i = 0;i <= (line.length() - str2.length());++i){
            for(int j = 0;j < str2.length();++j){
                if(line.charAt(i+j) == str2.charAt(j)){
                    if(j == str2.length() - 1){
                        times--;
                        if(times == 0){
                            return true;
                        }
                    }
                }
                else{
                    break;
                }
            }
        }
        return false;
    }

    //尋找line中是否存在m個a和2m個b的字串形式
    public static boolean find(String line){
        int a = findA(line);
        if(a != 100){
            if(findBB(line,a)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    //從頭開始找到第一個a
    public static int findA(String line){
        for(int i = 0;i < line.length();++i){
            if(line.charAt(i) == 'a'){
                return i;
            }
        }
        return 100;
    }

    //從i(找到的第一個a的位置)開始找到第一組bb
    public static boolean findBB(String line,int i){
        for(int j = i;j < line.length() - 1;++j){
            if(line.charAt(j) == 'b' && line.charAt(j+1) == 'b'){
                return true;
            }
        }
        return false;
    }

    //main function
    public static void main(String[] args) {
        String str1 = args[1];
        String str2 = args[2];
        int s2Count = Integer.parseInt(args[3]);

        //For your testing of input correctness
        /*System.out.println("The input file:"+args[0]);
        System.out.println("str1="+str1);
        System.out.println("str2="+str2);
        System.out.println("num of repeated requests of str2 = "+s2Count);*/

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line;
            //System.out.println((line = reader.readLine()) != null);
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                line = line.toLowerCase();
                //You main code should be invoked here
                if(palindrome(line)){
                    System.out.print("Y,");
                }
                else{
                    System.out.print("N,");
                }
                if(exist_times(line,str1,1)){
                    System.out.print("Y,");
                }
                else{
                    System.out.print("N,");
                }
                if (exist_times(line, str2, s2Count)) {
                    System.out.print("Y,");
                }
                else{
                    System.out.print("N,");
                }
                if(find(line)){
                    System.out.print("Y\n");
                }
                else{
                    System.out.print("N\n");
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}