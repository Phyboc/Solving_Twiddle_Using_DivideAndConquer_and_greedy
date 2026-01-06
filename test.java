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

    // Tabu list storing 2x2 blocks as strings
    static Set<String> tabuList = new HashSet<>();

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
            ans(matrix,0,matrix.length,0,matrix[0].length,cubes);

            // Sort by difference (smaller is better)
            cubes.sort(Comparator.comparingInt(a -> a.difference));

            boolean moveMade = false;
            for (cube c : cubes) {
                if (checkAndAdd(matrix, c.start1, c.start2)) {
                    rotate(matrix, c.start1, c.end1, c.start2, c.end2);
                    moveMade = true;
                    break;
                }
            }

            // Undo if no move was possible
            if (!moveMade) {
                // Rotate first cube 3 more times to restore (since it's 2x2)
                rotate(matrix, cubes.get(0).start1, cubes.get(0).end1, cubes.get(0).start2, cubes.get(0).end2);
                rotate(matrix, cubes.get(0).start1, cubes.get(0).end1, cubes.get(0).start2, cubes.get(0).end2);
                rotate(matrix, cubes.get(0).start1, cubes.get(0).end1, cubes.get(0).start2, cubes.get(0).end2);
            }

            matrixprint(matrix);
            System.out.println("Once more? (1/0)");
            choice = (sc.nextInt() == 1);
        }

        System.out.println("Sorted Matrix:");
        matrixprint(matrix);
    }

    // Check if fully sorted
    private static boolean isSorted(int[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                if (matrix[i][j] < prev) return false;
                prev = matrix[i][j];
            }
        }
        return true;
    }   

    // Print matrix
    private static void matrixprint(int[][] matrix){
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Generate all 2x2 cubes
    private static void ans(int[][] matrix, int start1, int end1, int start2, int end2, ArrayList<cube> cubes){
        if ((end1 - start1) == 2 && (end2 - start2) == 2){
            cubes.add(new cube(start1, end1, start2, end2, find(matrix, start1, end1, start2, end2)));
            return;
        }
        if ((end1 - start1) > 2 && (end2 - start2) > 2) {
            ans(matrix, start1, end1 - 1, start2, end2-1, cubes);
            ans(matrix, start1, end1 -1, start2+1,end2, cubes);
            ans(matrix, start1 +1, end1, start2, end2 -1, cubes);
            ans(matrix, start1 + 1, end1, start2 +1, end2, cubes);
        }
    }

    // Calculate global difference after rotating 2x2 block
    private static int difference(int[][] matrix, int start1, int end1, int start2, int end2){
        // Copy matrix and rotate 2x2 block
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] copy = new int[n][m];
        for (int i = 0; i < n; i++)
            System.arraycopy(matrix[i], 0, copy[i], 0, m);

        rotate(copy, start1, end1, start2, end2);

        // Count inversions in full matrix
        int count = 0;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                if (copy[i][j] < prev) count++;
                prev = copy[i][j];
            }
        }
        return count;
    }

    // Copy 2x2 block into string for tabu list
    private static boolean checkAndAdd(int[][] matrix, int start1, int start2){
        String key = matrix[start1][start2] + "," + matrix[start1][start2+1] + "," +
                     matrix[start1+1][start2] + "," + matrix[start1+1][start2+1];
        if (tabuList.contains(key)) return false;
        tabuList.add(key);
        return true;
    }

    // Rotate 2x2 block
    private static void rotate(int[][] graph, int start1, int end1, int start2, int end2){
        int temp = graph[start1][start2];
        graph[start1][start2] = graph[start1][end2-1];
        graph[start1][end2-1] = graph[end1-1][end2-1];
        graph[end1-1][end2-1] = graph[end1-1][start2];
        graph[end1-1][start2] = temp;
    }

    // Copy 2x2 block and calculate difference
    private static int find(int[][] matrix, int start1, int end1, int start2, int end2){
        return difference(matrix, start1, end1, start2, end2);
    }
}
