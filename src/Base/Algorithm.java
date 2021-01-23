package Base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.System;

public class Algorithm {
    private Random rand;
    private int[][] matrix;
    private int population;
    private int bestDistance;
    private Path bestPath;
    private HashMap<Worker, Path> bestPathByWorker;


    public Algorithm(String file, int population){
        rand = new Random();
        this.population = population;
        matrix = convertArray(getMatrixFromFile(file));
        bestDistance = 999999999;
        bestPathByWorker = new HashMap<>();
    }

    public synchronized void runAlgorithm(List<Path> paths, Worker thread, Base base) throws InterruptedException {
        int parent1[] = paths.get(0).getPath();
        int parent2[] = paths.get(1).getPath();

        int child1[] = new int[matrix.length];
        int child2[] = new int[matrix.length];

        pmxCrossover(parent1, parent2, child1, child2, matrix.length, rand);
        swap(child1);
        swap(child2);
        paths.add(new Path(child1, matrix));
        paths.add(new Path(child2, matrix));

        setTop2Paths(paths);
        if(paths.get(0).getDistance() < bestDistance) {
            thread.setTime(System.currentTimeMillis() - base.getStartTime());
            bestDistance = paths.get(0).getDistance();
            System.out.println("Best path: " + paths.get(0).getDistance() + " found by " + thread.getName() + " in " + thread.getTime() + " milliseconds");
        }

   /*     if(paths.get(0).getDistance() < bestDistance) {
            thread.setTime(System.currentTimeMillis() - base.getStartTime());
            setBestPathByWorker(paths, thread);
            System.out.println("Best path: " + paths.get(0) + " found by " + thread.getName() + " in " + thread.getTime() + " milliseconds");
        }*/
    }
    synchronized void setBestPath() {
        List<Path> bestPaths = new ArrayList<>(getBestPathByWorker().values());
        setTop2Paths(bestPaths);
        this.bestPath = bestPaths.get(0);
    }

    public Path getBestPath(){
        return bestPath;
    }

    public void putWorkerPath(Worker w){
        bestPathByWorker.put(w, w.getBestPath());
    }

    public HashMap<Worker, Path> getBestPathByWorker(){
        return bestPathByWorker;
    }


    public List<Path> initPaths(){

        List<Path> ps = new ArrayList<>();

        for(int i = 0; i< population; i++){
            Path path = initialPath();
            path = randomPath(path);
            path.setDistance(matrix);
            ps.add(i,path);
        }

        setTop2Paths(ps);

        return ps;
    }

    public void setTop2Paths(List<Path> paths){

        //paths.forEach(path-> System.out.println(path.toString()));

        List<Path> minPaths = new ArrayList<>();

        paths.sort(Comparator.comparing(Path::getDistance));
        minPaths.add(paths.get(0));
        minPaths.add(paths.get(1));
        paths = minPaths;

        //return paths;
    }

    public Path initialPath(){
        int[] path = new int[matrix.length];

        for(int i=0; i<matrix.length;i++){
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
            int a = rand.nextInt(size) % size;
            int b = rand.nextInt(size) % size;
            int tmp = path[a];
            path[a]=path[b];
            path[b]=tmp;
        }
        return path;
    }

    /**
     * Método auxiliar a swap com 5% de chance de retornar true
     * @return
     */
    private boolean rollDice(){
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
            while (count<size) {
                line = br.readLine();
                //line = line.replace(" ", "xxx");
                String[] values = line.split("\\s+");
                //String[] arr = arraycopy(values)
                List<String> list = new ArrayList<>(Arrays.asList(values));
                //System.out.println(list.toString());
                //int[] array = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                if(list.get(0).equals("")){
                    for(int i=0;i< values.length-1;i++){
                        list.set(i, list.get(i+1));
                    }
                    list.remove(size-1);
                }
               // System.out.println(list.toString());
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

    /*
    public void runTest(){
        //int[][] m = convertArray((getMatrixFromFile("ex5.txt")));
        System.out.println("INITIAL PATHS");
        paths.forEach(path-> System.out.println(path.toString()));
        System.out.println();

        setTop2Paths();
        System.out.println("TOP 2");
        paths.forEach(path-> System.out.println(path.toString()));
        System.out.println();

        int parent1[] = paths.get(0).getPath();
        int parent2[] = paths.get(1).getPath();

        int child1[] = new int[matrix.length];
        int child2[] = new int[matrix.length];

        pmxCrossover(parent1, parent2, child1, child2, matrix.length, rand);
        swap(child1);
        swap(child2);
        paths.add(new Base.Path(child1, matrix));
        paths.add(new Base.Path(child2, matrix));

        System.out.println("TOP 2 + CHILDREN");
        paths.forEach(path-> System.out.println(path.toString()));
        System.out.println();

        setTop2Paths();
        System.out.println("FINAL");
        paths.forEach(path-> System.out.println(path.toString()));
        System.out.println();

    }*/


}

