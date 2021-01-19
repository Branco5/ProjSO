import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.System;

public class MyTest {

    public static Random rand;

    public static void main(String[] args) {

        int[][] m = convertArray((getMatrixFromFile("ex5.txt")));
        for(int i = 0; i<m.length; i++){
            System.out.println();
            for(int j = 0; j<m.length; j++){
                System.out.print(m[i][j]+", ");
            }
        }
        //System.out.println(Arrays.deepToString(getMatrixFromFile("ex6.txt")));
        /*int n = 10;
        int parent1[] = {9,8,4,5,6,7,1,2,3,10};
        int parent2[] = {8,7,1,2,3,10,9,5,4,6};
        int offSpring1 [] = new int[n];
        int offSpring2 [] = new int[n];

        //Random rand = new Random();
        pmxCrossover(parent1, parent2,offSpring1,offSpring2,n,rand);

        System.out.print("F1: ");
        for (int i=0; i< n; i++)
            System.out.printf("%2d ",offSpring1[i]);
        System.out.println();
        System.out.print("F2: ");
        for (int i=0; i< n; i++)
            System.out.printf("%2d ",offSpring2[i]);*/

    }

    public List<int[]> initPaths(int size){
        int[] path = initialPath(size);
        List<int[]> paths = new ArrayList<>();

        for(int i=0; i<size; i++){
            paths.add(i,randomPath(path));
        }
        return paths;
    }

    public List<int[]> getTop2Paths(List<int[]> paths, int matrix[][]){
        int[] distances = new int[paths.size()];
        for (int[] path : paths) {
            distance(path, matrix);
        }
        (...)
    }

    public int distance(int path[], int matrix[][]){
        int dist = 0;
        int size = path.length;

        for(int i = 0; i<size-1; i++){
            int cur = path[i]-1;
            int next = path[i+1]-1;
            dist+=matrix[cur][next];
        }
        int last = path[size-1]-1;
        int first = path[0]-1;
        dist += matrix[last][first];
        return dist;
    }

    public int[] initialPath(int size){
        int[] path = new int[size];

        for(int i=0; i<size;i++){
            path[i]=i+1;
        }
        return path;
    }

    public int[] randomPath(int[] path){
        int size = path.length;
        for (int i = 0; i<size; i++){
            int a = rand.nextInt() % size;
            int tmp = path[i];
            path[i]=path[a];
            path[a]=tmp;
        }
        return path;
    }

    int[] swap(int[] path){
        int size = path.length;
        if(rollDice()){
            int a = rand.nextInt() % size;
            int b = rand.nextInt() % size;
            int tmp = path[a];
            path[a]=path[b];
            path[b]=tmp;
        }
        return path;
    }

    /**
     * Método com 5% de chance de retornar true
     * @return
     */
    boolean rollDice(){
        return rand.nextInt(100) < 5;
    }

    static void pmxCrossover(int parent1[],int parent2[],
                             int offSpring1[],int offSpring2[],int n,Random rand) {
        int replacement1[] = new int[n+1];
        int replacement2[] = new int[n+1];
        int i, n1, m1, n2, m2;
        int swap;

        System.out.print("P1: ");
        for (i=0; i< n; i++)
            System.out.printf("%2d ",parent1[i]);
        System.out.println();
        System.out.print("P2: ");
        for (i=0; i< n; i++)
            System.out.printf("%2d ",parent2[i]);
        System.out.println();

        int cuttingPoint1 = rand.nextInt(n);
        int cuttingPoint2 = rand.nextInt(n);

        //int cuttingPoint1 = 3;
        //int cuttingPoint2 = 5;

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = rand.nextInt(n);
        }
        if (cuttingPoint1 > cuttingPoint2) {
            swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        System.out.printf("cp1 = %d cp2 = %d\n",cuttingPoint1,cuttingPoint2);

        for (i=0; i < n+1; i++) {
            replacement1[i] = -1;
            replacement2[i] = -1;
        }

        for (i=cuttingPoint1; i <= cuttingPoint2; i++) {
            offSpring1[i] = parent2[i];
            offSpring2[i] = parent1[i];
            replacement1[parent2[i]] = parent1[i];
            replacement2[parent1[i]] = parent2[i];
        }

        System.out.print("A1: ");
        for (i=0; i< n+1; i++)
            System.out.printf("%2d ",replacement1[i]);
        System.out.println();
        System.out.print("A2: ");
        for (i=0; i< n+1; i++)
            System.out.printf("%2d ",replacement2[i]);
        System.out.println();
        // fill in remaining slots with replacements
        for (i = 0; i < n; i++) {
            if ((i < cuttingPoint1) || (i > cuttingPoint2)) {
                n1 = parent1[i];
                m1 = replacement1[n1];
                n2 = parent2[i];
                m2 = replacement2[n2];
                while (m1 != -1) {
                    n1 = m1;
                    m1 = replacement1[m1];
                }
                while (m2 != -1) {
                    n2 = m2;
                    m2 = replacement2[m2];
                }
                offSpring1[i] = n1;
                offSpring2[i] = n2;
            }
        }
    }

    public static String[][] getMatrixFromFile(String filename) {
        String name = "tsp_testes/" + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            int size = Integer.parseInt(String.valueOf(br.readLine()));
            System.out.println("Matrix size: "+size);
            String[][] matrix = new String[size][size];

            int count = 0;
            while ((line = br.readLine()) != null) {
                //line = line.replace(" ", "xxx");
                String[] values = line.split("\\s+");
                //String[] arr = arraycopy(values)
                List<String> list = new ArrayList<String>(Arrays.asList(values));
                //int[] array = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                if(list.get(0).equals("")){
                    for(int i=0;i< values.length-1;i++){
                        list.set(i, list.get(i+1));
                    }
                    list.remove(size-1);
                }
                //System.out.println(list.toString());
                matrix[count] = list.toArray(new String[0]);
                count++;
            }
            return matrix;

        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }

    public static int[][] convertArray(String arr[][]){
        int size = arr.length;
        int[][] aux = new int[size][size];
        for (int i = 0; i < size; i++) {
            for(int j=0; j<size; j++){
                aux[i][j] = Integer.parseInt(arr[i][j]);
            }
        }
        return aux;
    }
}