import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.System;

public class Algorithm {
    public Random rand;
    public List<Path> paths;
    public int[][] matrix;


    public Algorithm(String file, int nrPaths){
        rand = new Random();
        paths = new ArrayList<>();
        matrix = convertArray((getMatrixFromFile(file)));
        initPaths(nrPaths);
    }

    public void run(){
        //int[][] m = convertArray((getMatrixFromFile("ex5.txt")));
        setTop2Paths();
        System.out.println();
        paths.forEach(path-> System.out.println(path.toString()));
    }

    public void initPaths(int number){

        //List<Path> ps = new ArrayList<>();

        for(int i=0; i<number; i++){
            Path path = initialPath(matrix.length);
            path = randomPath(path);
            path.setDistance(matrix);
            paths.add(i,path);
        }
        //return ps;
    }

    public List<Path> setTop2Paths(){
        //int[] distances = new int[paths.size()];
        paths.forEach(path-> System.out.println(path.toString()));
       // Map<int[], Integer> map = new HashMap<>();
        List<Path> minPaths = new ArrayList<>();

        paths.sort(Comparator.comparing(Path::getDistance));
        minPaths.add(paths.get(0));
        minPaths.add(paths.get(1));
        paths = minPaths;
        /*for (Map.Entry<int[], Integer> entry : map.entrySet()) {
            System.out.println("key:"+ Arrays.toString(entry.getKey()) +" | Value: "+entry.getValue());
        }


        int min = Collections.sort(map.values());
        System.out.println(min);

        for (Map.Entry<int[], Integer> entry : map.entrySet()){
            if(entry.getValue()==min){
                maxPaths.add(entry.getKey());
                break;
                map.remove(entry.getKey());
                //map.remove(entry.getKey());
            }
        }
        //addRemMin(map, maxPaths);
        //addRemMin(map, maxPaths);
*/
        return minPaths;
    }

    /**
     * Auxiliary to getTop2Paths
     * @param map
     * @param maxPaths

    private void addRemMin(Map<int[], Integer> map, List<int[]> maxPaths) {
        int max = Collections.max(map.values());
        for (Map.Entry<int[], Integer> entry : map.entrySet()){
            if(entry.getValue()==max){
                maxPaths.add(entry.getKey());
                map.remove(entry.getKey());
            }
        }
    }*/

    public Path initialPath(int size){
        int[] path = new int[size];

        for(int i=0; i<size;i++){
            path[i]=i+1;
        }

        return new Path(path);
    }

    public Path randomPath(Path path){
        int size = path.size();
        for (int i = 0; i<size; i++){
            int a = rand.nextInt(size) % size;
            int tmp = path.getPath()[i];
            path.getPath()[i]=path.getPath()[a];
            path.getPath()[a]=tmp;

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


}

