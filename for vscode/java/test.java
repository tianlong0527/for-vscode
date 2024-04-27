public class test {
    public static void main(String[] args) {
        // 建立原始二維陣列
        int[][] original = {{1, 2, 3}, {4, 5, 6}};

        // 創建原始陣列的淺拷貝
        int[][] copied = original.clone();

        // 輸出原始陣列和複製陣列的第一個元素
        System.out.println("Before modification:");
        System.out.println("original[0][0] = " + original[0][0]);
        System.out.println("copied[0][0] = " + copied[0][0]);

        // 修改複製陣列的第一個元素
        copied[0][0] = 100;

        // 再次輸出原始陣列和複製陣列的第一個元素
        System.out.println("After modification:");
        System.out.println("original[0][0] = " + original[0][0]);
        System.out.println("copied[0][0] = " + copied[0][0]);
    }
}