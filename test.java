import java.util.*;

public class test {

    public static class Cube {
        int start1, end1, start2, end2, difference;

        public Cube(int start1, int end1, int start2, int end2, int difference) {
            this.start1 = start1;
            this.end1 = end1;
            this.start2 = start2;
            this.end2 = end2;
            this.difference = difference;
        }
    }

    // Tabu list stores small 2x2 rotations applied
    private static final List<String> tabuList = new ArrayList<>();

    public static void main(String[] args) {
        int[][] matrix = {
            {3, 20, 14, 24, 1},
            {13, 25, 9, 7, 18},
            {2, 8, 11, 5, 6},
            {19, 15, 12, 4, 17},
            {22, 16, 23, 21, 10}
        };

        Scanner sc = new Scanner(System.in);
        boolean choice = true;

        while (choice && !isSorted(matrix)) {
            ArrayList<Cube> cubes = new ArrayList<>();
            ans(matrix, 0, matrix.length, 0, matrix[0].length, cubes);

            // Sort cubes by difference (best first)
            cubes.sort(Comparator.comparingInt(a -> a.difference));

            boolean moveMade = false;
            for (Cube c : cubes) {
                rotate(matrix, c.start1, c.end1, c.start2, c.end2);
                if (!checkAndAdd(matrix, c.start1, c.start2)) {
                    // Move already seen, undo by rotating 3 more times
                    for (int i = 0; i < 3; i++) {
                        rotate(matrix, c.start1, c.end1, c.start2, c.end2);
                    }
                } else {
                    moveMade = true;
                    break; // accept first unseen move
                }
            }

            matrixPrint(matrix);
            System.out.println("Once more? (1/0)");
            choice = (sc.nextInt() == 1);
        }

        System.out.println("Sorted Matrix:");
        matrixPrint(matrix);
    }

    // Generate all 2x2 subcubes
    private static void ans(int[][] matrix, int start1, int end1, int start2, int end2, ArrayList<Cube> cubes) {
        if ((end1 - start1) == 2 && (end2 - start2) == 2) {
            cubes.add(new Cube(start1, end1, start2, end2, find(matrix, start1, end1, start2, end2)));
            return;
        }
        if (end1 - start1 > 2 && end2 - start2 > 2) {
            ans(matrix, start1, end1 - 1, start2, end2 - 1, cubes);
            ans(matrix, start1, end1 - 1, start2 + 1, end2, cubes);
            ans(matrix, start1 + 1, end1, start2, end2 - 1, cubes);
            ans(matrix, start1 + 1, end1, start2 + 1, end2, cubes);
        }
    }

    // Count number of inversions in 2x2
    private static int difference(int[][] matrix, int start1, int end1, int start2, int end2) {
        int count = 0;
        if (matrix[start1][start2] > matrix[start1][end2 - 1]) count++;
        if (matrix[start1][end2 - 1] > matrix[end1 - 1][start2]) count++;
        if (matrix[end1 - 1][start2] > matrix[end1 - 1][end2 - 1]) count++;
        return count;
    }

    // Copy 2x2 submatrix and compute difference after rotation
    private static int find(int[][] matrix, int start1, int end1, int start2, int end2) {
        int[][] copy = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                copy[i][j] = matrix[start1 + i][start2 + j];
            }
        }
        rotate(copy, 0, 2, 0, 2);
        return difference(copy, 0, 2, 0, 2);
    }

    // Rotate 2x2 submatrix clockwise
    private static void rotate(int[][] graph, int start1, int end1, int start2, int end2) {
        int temp = graph[start1][start2];
        graph[start1][start2] = graph[start1][end2 - 1];
        graph[start1][end2 - 1] = graph[end1 - 1][end2 - 1];
        graph[end1 - 1][end2 - 1] = graph[end1 - 1][start2];
        graph[end1 - 1][start2] = temp;
    }

    // Print matrix
    private static void matrixPrint(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) System.out.print(val + " ");
            System.out.println();
        }
    }

    // Check if matrix is sorted
    private static boolean isSorted(int[][] matrix) {
        int prev = Integer.MIN_VALUE;
        for (int[] row : matrix) {
            for (int val : row) {
                if (val < prev) return false;
                prev = val;
            }
        }
        return true;
    }

    // ---------------- Tabu list methods ----------------
    private static String matrixKey(int[][] matrix, int x, int y) {
        // Convert 2x2 starting at (x,y) to a string key
        return matrix[x][y] + "," + matrix[x][y + 1] + "," + matrix[x + 1][y] + "," + matrix[x + 1][y + 1];
    }

    private static boolean checkAndAdd(int[][] matrix, int x, int y) {
        String key = matrixKey(matrix, x, y);
        if (tabuList.contains(key)) {
            return false; // Already seen
        }
        tabuList.add(key);
        if (tabuList.size() > 50) tabuList.remove(0); // Limit tabu list size
        return true;
    }
}
