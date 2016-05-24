import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class algo {
 static public  ArrayList<int[][]> matrixes = new ArrayList<>();

    static boolean equal(int[][] m1, int[][] m2, int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (m1[i][j] != m2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean nonZero(int[][] matrix, int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean there(int[][] matrix, ArrayList<int[][]>matr, int m, int n) {
        for (int[][] i:matr)
        {
            if (equal( i,matrix, m, n))
            {
                return true;
            }
        }
        return false;
    }

    static void function(int[][] matrix, int m, int n) {
        if (nonZero(matrix, m, n)) {
            int[][] matrix1 = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    matrix1[i][j] = matrix[i][j];
                    if (matrix1[i][j] == 9) {
                        matrix1[i][j] = 0;
                    }
                }
            }
            //if (!there(matrix1, matrixes, m, n)) {
            matrixes.add(matrix1);
            //}
            return;
        } else {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrix[i][j] == 0) {
                        //1
                        if (i <= m - 2 && j <= n - 2 && matrix[i + 1][j] == 0 && matrix[i + 1][j + 1] == 0) {
                            matrix[i][j] = 1;
                            matrix[i + 1][j] = 1;
                            matrix[i + 1][j + 1] = 1;
                            function(matrix, m, n);
                            matrix[i + 1][j] = 0;
                            matrix[i + 1][j + 1] = 0;
                            matrix[i][j] = 0;
                        }
                        //2
                        if (i <= m - 2 && j <= n - 2 && matrix[i][j + 1] == 0 && matrix[i + 1][j + 1] == 0) {
                            matrix[i][j] = 2;
                            matrix[i][j + 1] = 2;
                            matrix[i + 1][j + 1] = 2;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i][j + 1] = 0;
                            matrix[i + 1][j + 1] = 0;
                        }
                        //3
                        if (i <= m - 2 && j >= 1 && matrix[i + 1][j] == 0 && matrix[i + 1][j - 1] == 0) {
                            matrix[i][j] = 3;
                            matrix[i + 1][j] = 3;
                            matrix[i + 1][j - 1] = 3;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i + 1][j] = 0;
                            matrix[i + 1][j - 1] = 0;
                        }
                        //4
                        if (i <= m - 2 && j <= n - 2 && matrix[i + 1][j] == 0 && matrix[i][j + 1] == 0) {
                            matrix[i][j] = 4;
                            matrix[i + 1][j] = 4;
                            matrix[i][j + 1] = 4;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i + 1][j] = 0;
                            matrix[i][j + 1] = 0;
                        }
                        //5
                        if (i <= m - 3 && j <= n - 2 && matrix[i + 1][j] == 0 && matrix[i + 2][j] == 0 && matrix[i + 1][j + 1] == 0) {
                            matrix[i][j] = 5;
                            matrix[i + 1][j] = 5;
                            matrix[i + 2][j] = 5;
                            matrix[i + 1][j + 1] = 5;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i + 1][j] = 0;
                            matrix[i + 2][j] = 0;
                            matrix[i + 1][j + 1] = 0;
                        }
                        //6
                        if (i <= m - 3 && j >= 1 && matrix[i + 1][j] == 0 && matrix[i + 2][j] == 0 && matrix[i + 1][j - 1] == 0) {
                            matrix[i][j] = 6;
                            matrix[i + 1][j] = 6;
                            matrix[i + 2][j] = 6;
                            matrix[i + 1][j - 1] = 6;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i + 1][j] = 0;
                            matrix[i + 2][j] = 0;
                            matrix[i + 1][j - 1] = 0;
                        }
                        //7
                        if (i <= m - 2 && j <= n - 3 && matrix[i][j + 1] == 0 && matrix[i][j + 2] == 0 && matrix[i + 1][j + 1] == 0) {
                            matrix[i][j] = 7;
                            matrix[i][j + 1] = 7;
                            matrix[i][j + 2] = 7;
                            matrix[i + 1][j + 1] = 7;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i][j + 1] = 0;
                            matrix[i][j + 2] = 0;
                            matrix[i + 1][j + 1] = 0;
                        }
                        //8
                        if (i <= m - 2 && j >= 1 && j <= m - 2 && matrix[i + 1][j - 1] == 0 && matrix[i + 1][j] == 0 && matrix[i + 1][j + 1] == 0) {
                            matrix[i][j] = 8;
                            matrix[i + 1][j - 1] = 8;
                            matrix[i + 1][j] = 8;
                            matrix[i + 1][j + 1] = 8;
                            function(matrix, m, n);
                            matrix[i][j] = 0;
                            matrix[i + 1][j - 1] = 0;
                            matrix[i + 1][j] = 0;
                            matrix[i + 1][j + 1] = 0;
                            return;
                        } else {
                            return;
                        }
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(new File("input.txt"));
            int n, m;
            n = scan.nextInt();
            m = scan.nextInt();
            scan.nextLine();
            int[][] matrix = new int[m][n];
            for (int i = 0; i < m; i++) {
                String t;
                t = scan.nextLine();
                for (int j = 0; j < n; j++) {
                        matrix[i][j] = t.toCharArray()[j] - 48;
                    if (matrix[i][j] == 1) {
                        matrix[i][j] = 9;
                    }
                }
            }
            scan.close();
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(matrix[i][j]);
                }
                System.out.println();
            }
            function(matrix, m, n);
            FileWriter fout = new FileWriter(new File("output.txt"));
            fout.write(matrixes.size());
            fout.write('\n');
            for (int i = 0; i < matrixes.size(); i++) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k < n; k++) {
                        fout.write( matrixes.get(i)[j][k]);
                    }
                    fout.write('\n');
                }
                fout.write('\n');
            }
            fout.close();
            matrixes.clear();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}