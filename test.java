
import java.util.*;

public class test {
    public static class cube {
        int start1;
        int end1;
        int start2;
        int end2;
        int difference;
        public cube(int start1, int end1, int start2, int end2, int difference){
            this.start1 = start1;
            this.end1 = end1;
            this.start2 = start2;
            this.end2 = end2;
            this.difference = difference;
        }
    }
    public static void main(String[] args) {
        int[][] matrix = {
            {3,20,14,24,1},
            {13,25,9,7,18},
            {2,8,11,5,6},
            {19,15,12,4,17},
            {22,16,23,21,10}
        };
        boolean choice = true;
        Scanner sc = new Scanner(System.in);
        while (choice && !isSorted(matrix)){
            ArrayList<cube> cubes = new ArrayList<>(); 
            ans(matrix,0,matrix.length,0,matrix.length,cubes);
            cubes.sort(Comparator.comparingInt(a -> a.difference));      
            rotate(matrix, cubes.get(0).start1, cubes.get(0).end1, cubes.get(0).start2, cubes.get(0).end2);
            matrixprint(matrix);
            System.out.println("Once more? (1/0)");
            choice = (sc.nextInt() == 1);
        }
        System.out.println("Sorted Matrix:");
        matrixprint(matrix);
    }
    
    private static boolean isSorted(int[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                if (matrix[i][j] < prev){
                    return false;
                }
                prev = matrix[i][j];
            }
        }
        return true;
    }   
    private static void matrixprint(int[][] matrix){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static void ans(int[][] matrix, int start1, int end1, int start2, int end2, ArrayList<cube> cubes){
        if ((end1 - start1) == 2 && (end2 - start2) == 2){
            cubes.add(new cube(start1, end1, start2, end2, find(matrix, start1, end1, start2, end2)));
            return;
        }
        ans(matrix, start1, end1 - 1, start2, end2-1, cubes);
        ans(matrix, start1, end1 -1, start2+1,end2, cubes);
        ans(matrix, start1 +1, end1, start2, end2 -1, cubes);
        ans(matrix, start1 + 1, end1, start2 +1, end2, cubes);

    }

    private static int difference(int[][] matrix, int start1, int end1, int start2, int end2){
        int count = 0;
        if (matrix[start1][start2] > matrix[start1][end2-1]) count++;
        if (matrix[start1][end2-1] > matrix[end1-1][start2]) count++;
        if (matrix[end1-1][start2] > matrix[end1-1][end2-1]) count++;
        return count;
    }
    private static int find(int[][] matrix, int start1, int end1, int start2, int end2){

        int rows = end1 - start1;
        int cols = end2 - start2;

        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = matrix[start1 + i][start2 + j];
            }
        }
        rotate(copy, 0, rows, 0, cols);
        return difference(copy, 0, rows, 0, cols);
    }
    private static void rotate(int[][] graph, int start1, int end1, int start2, int end2){
        int temp = graph[start1][start2];
        graph[start1][start2] = graph[start1][end2-1];
        graph[start1][end2-1] = graph[end1-1][end2-1];
        graph[end1-1][end2-1] = graph[end1-1][start2];
        graph[end1-1][start2] = temp;
    }
    
}
