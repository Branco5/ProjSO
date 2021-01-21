package Advanced;

import java.util.Scanner;

public class Advanced {
    private Worker[] workers;
    private Algorithm aj;
    private long startTime;
    private double mergeRate;
    private long duration;
    static boolean doWork = true; //When false merge populations

    public Advanced(String file, int nrWorkers, int duration, int population, int swapChance, double mergeRate){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population, swapChance);
        this.mergeRate = mergeRate;
        this.duration = duration*1000;
        //this.startTime = System.currentTimeMillis();

        for (int i = 0; i<workers.length; i++) {
            String name = "thread-"+i;
            Worker thread = new Worker(this);
            thread.setName(name);
            workers[i] = thread;
        }
    }

    public Worker[] getWorkers(){
        return workers;
    }

    public double getPeriod(){
        return mergeRate *duration;
    }

    public Algorithm getAlgorithm(){
        return aj;
    }

    public void startWorkers(){
        startTime = System.currentTimeMillis();
        for(int i = 0; i< workers.length; i++){
            workers[i].start();
        }
    }

    public long getDuration(){
        return duration;
    }

    public long getStartTime(){
        return startTime;
    }

    synchronized void stopWorkers(){

        System.out.println("\nSTOPPING WORKERS\n");
        for(int i = 0; i<workers.length; i++){
            workers[i].interrupt();
            // workers[i].join();
        }

        for(int i = 0; i<workers.length; i++){
            Path best = workers[i].getBestPath();
            getAlgorithm().getBestPathByWorker().put(workers[i], best);
            System.out.println("Best path found by "+workers[i].getName()+": "+ best + " in " + workers[i].getTime() + " milliseconds");
        }
        //List<Path> bestPaths = new ArrayList<>(aj.getBestPathByWorker().values());
        aj.setBestPath();
        Worker best = getWorkerBestPath();
        System.out.println();
        System.out.println("Best path found: " + aj.getBestPath() +" by "+best.getName()+" in "+best.getTime() + " milliseconds");
    }

    public Worker getWorkerBestPath(){
        Worker worker = null;
        int min = 999999999;
        for (Worker w: workers) {
            if(w.getBestPath().getDistance()<min){
                worker = w;
                min = w.getBestPath().getDistance();
            }
        }
        return worker;
    }



    public static void main(String[] args) throws InterruptedException {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert algorithm parameters in this order: " +
                "file - threads - duration(seconds) - population - swap chance(%) - merge rate(decimal)");
        System.out.println("Example: dantzig42.txt 5 10 80 5 0.3");

        while(running){
            System.out.print("> ");
            String input = scanner.nextLine();
            String arr[] = input.split("\\s");
            int nrWorkers = Integer.parseInt(arr[1]);
            int duration = Integer.parseInt(arr[2]);
            int population = Integer.parseInt(arr[3]);
            int swap = Integer.parseInt(arr[4]);
            double merge = Double.parseDouble(arr[5]);

            Advanced adv = new Advanced(arr[0], nrWorkers, duration, population, swap, merge);

            ThreadMerge tm = new ThreadMerge(adv);
            ThreadWait tw = new ThreadWait(adv, tm);

            adv.startWorkers();
            tw.start();
            tm.start();

        }

        /*Advanced adv = new Advanced(4, 0.3, "att48.txt", 50, 10000, 23);
        ThreadMerge tm = new ThreadMerge(adv);
        ThreadWait tw = new ThreadWait(adv, tm);

        adv.startWorkers();
        tw.start();
        tm.start();*/

/*
        long t= System.currentTimeMillis();
        long end = t+3000;
        while(System.currentTimeMillis() < end) {

        }*/


    }
}

