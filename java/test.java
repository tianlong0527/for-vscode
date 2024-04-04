public class test {
    public static void main(String[] args) {
        String line = "          Hello      World !!!! ::: ?      \n";
        String[] words = line.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for(String s : words){
            sb.append(s);
        }
        
        for(String s : words){
            System.out.println(s);
        }
        System.out.println(sb.toString());
    }
}
