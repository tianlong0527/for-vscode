public class ABC {
    public static int run(int a){
        a += 5;
        return a;
    }
    public static void main(String[] args) {
        int a=5;
        a *= a^2;
        a = run(a);
        System.out.println(a);
    }
}