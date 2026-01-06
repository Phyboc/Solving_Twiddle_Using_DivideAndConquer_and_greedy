import java.util.*;

public class test {

    public static class cube {
        int start1;
        int end1;
        int start2;
        int end2;
        int difference;

        public cube(int start1, int end1, int start2, int end2, int difference) {
            this.start1 = start1;
            this.end1 = end1;
            this.start2 = start2;
            this.end2 = end2;
            this.difference = difference;
        }
    }

    static HashSet<String> tabuList = new HashSet<>(); // stores 3x3 submatrix states

    public static void main(String[] args) {
        int[][] matrix = {
            {3, 20, 14, 24, 1},
            {13, 25, 9, 7, 18},
            {2, 8, 11, 5, 6},
            {19, 15, 12, 4, 17},
            {22, 16, 23, 21, 10}
        };

        int[][] finalMatrix = getSortedMatrix(matrix);

        boolean choice = true;
        Scanner sc = new Scanner(System.in);

        while (choice && !isSorted(matrix)) {
            ArrayList<cube> cubes = new ArrayList<>();
            ans(matrix, 0, matrix.length, 0, matrix[0].length, cubes, finalMatrix);

            // Sort cubes by difference (smaller difference is better)
            cubes.sort(Comparator.comparingInt(a -> a.difference));

            boolean moveMade = false;
            for (cube c : cubes) {
                if (!checkAndAdd(matrix, c.start1, c.start2)) {
                    // rotate 3x3 and mark as used
                    rotate3x3(matrix, c.start1, c.end1, c.start2, c.end2);
                    moveMade = true;
                    break;
                }
            }

            // If no move was made, undo rotations (rotate 3 more times to restore)
            if (!moveMade) {
                for (cube c : cubes) {
                    rotate3x3(matrix, c.start1, c.end1, c.start2, c.end2);
                    rotate3x3(matrix, c.start1, c.end1, c.start2, c.end2);
                    rotate3x3(matrix, c.start1, c.end1, c.start2, c.end2);
                }
            }

            matrixprint(matrix);
            System.out.println("Once more? (1/0)");
            choice = (sc.nextInt() == 1);
        }

        System.out.println("Sorted Matrix:");
        matrixprint(matrix);
    }

    // generate the final sorted matrix
    private static int[][] getSortedMatrix(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[] arr = new int[n * m];
        int idx = 0;
        for (int[] row : matrix) {
            for (int val : row) arr[idx++] = val;
        }
        Arrays.sort(arr);
        int[][] sorted = new int[n][m];
        idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sorted[i][j] = arr[idx++];
            }
        }
        return sorted;
    }

    private static boolean isSorted(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (matrix[i][j] < prev) return false;
                else prev = matrix[i][j];
        return true;
    }

    private static void matrixprint(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) System.out.print(val + " ");
            System.out.println();
        }
    }

    private static void ans(int[][] matrix, int start1, int end1, int start2, int end2, ArrayList<cube> cubes, int[][] finalMatrix) {
        if (end1 - start1 == 3 && end2 - start2 == 3) {
            cubes.add(new cube(start1, end1, start2, end2, find(matrix, start1, end1, start2, end2, finalMatrix)));
            return;
        }

        if (end1 - start1 < 3 || end2 - start2 < 3) return;

        ans(matrix, start1, end1 - 1, start2, end2 - 1, cubes, finalMatrix);
        ans(matrix, start1, end1 - 1, start2 + 1, end2, cubes, finalMatrix);
        ans(matrix, start1 + 1, end1, start2, end2 - 1, cubes, finalMatrix);
        ans(matrix, start1 + 1, end1, start2 + 1, end2, cubes, finalMatrix);
    }

    private static int find(int[][] matrix, int start1, int end1, int start2, int end2, int[][] finalMatrix) {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                copy[i][j] = matrix[start1 + i][start2 + j];

        rotate3x3(copy, 0, 3, 0, 3); // rotate copy
        return difference(copy, finalMatrix, start1, start2);
    }

    // difference compared with final matrix
    private static int difference(int[][] block, int[][] finalMatrix, int start1, int start2) {
        int diff = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (block[i][j] != finalMatrix[start1 + i][start2 + j]) diff++;
        return diff;
    }

    private static void rotate3x3(int[][] mat, int start1, int end1, int start2, int end2) {
        // rotate outer corners
        int temp = mat[start1][start2];
        mat[start1][start2] = mat[start1 + 2][start2];
        mat[start1 + 2][start2] = mat[start1 + 2][start2 + 2];
        mat[start1 + 2][start2 + 2] = mat[start1][start2 + 2];
        mat[start1][start2 + 2] = temp;

        // rotate inner edges
        temp = mat[start1][start2 + 1];
        mat[start1][start2 + 1] = mat[start1 + 1][start2];
        mat[start1 + 1][start2] = mat[start1 + 2][start2 + 1];
        mat[start1 + 2][start2 + 1] = mat[start1 + 1][start2 + 2];
        mat[start1 + 1][start2 + 2] = temp;
    }

    // check tabu list and store
    private static boolean checkAndAdd(int[][] matrix, int start1, int start2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                sb.append(matrix[start1 + i][start2 + j]).append(",");
        String key = sb.toString();
        if (tabuList.contains(key)) return true; // already visited
        tabuList.add(key);
        return false;
    }
}
