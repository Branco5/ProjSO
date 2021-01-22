package Advanced;

import java.util.Scanner;

public class Global {
    private Worker[] workers;
    private Algorithm aj;
    private long startTime;
    private double mergeRate;
    private long duration;
    private Path bestPath;
    private long bestTime;
    private Worker bestWorker;
    private int bestDistance;
    static boolean doWork = true; //When false merge populations

    public Global(String file, int nrWorkers, int duration, int population, int swapChance, double mergeRate){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population, swapChance);
        this.mergeRate = mergeRate;
        this.duration = duration*1000;
        bestPath=null;
        bestDistance=999999999;
        bestTime=999999999;
        bestWorker = null;

        for (int i = 0; i<workers.length; i++) {
            String name = "Thread-"+i;
            Worker thread = new Worker(this);
            thread.setName(name);
            workers[i] = thread;
        }
    }

    public Worker[] getWorkers(){
        return workers;
    }

    public double getPeriod(){
        return mergeRate * duration;
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

    public void stopWorkers() throws InterruptedException {

        System.out.println("\nSTOPPING WORKERS\n");
        for(int i = 0; i<workers.length; i++){
            workers[i].interrupt();
        }
        print();
    }

    public void print(){
        for(Worker w : workers){
            Path best = w.getBestPath();
            System.out.println("Best path found by "+w.getName()+": "+
                    best + " in " + w.getTime() + " milliseconds and "+w.getIterationBest()+" iterations");
        }
        System.out.println();
        System.out.println("Best path found: " + getBestDistance() +
                " by "+bestWorker.getName()+" in "+bestTime + " milliseconds and "+ bestWorker.getIterationBest()+" iterations");

    }

    public synchronized int getBestDistance(){
        return this.bestDistance;
    }

    public synchronized void setBestPath(Worker thread){
        if(thread.getBestDistance() <= getBestDistance() && thread.getTime()<bestTime){
            bestWorker = thread;
            bestPath = thread.getBestPath();
            bestTime = thread.getTime();
            bestDistance= thread.getBestDistance();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Insert algorithm parameters in this order: " +
                "file - threads - duration(seconds) - population - swap chance(%) - merge rate(decimal)");
        System.out.println("Example: att48.txt 5 10 80 5 0.3");
        System.out.print("Write 'exit' to quit program\n> ");


        while(running) {
            String input = scanner.nextLine();
            if (input.trim().toLowerCase().equalsIgnoreCase("exit")) {
                running = false;
                break;
            }
            String arr[] = input.split("\\s");
            int nrWorkers = Integer.parseInt(arr[1]);
            int duration = Integer.parseInt(arr[2]);
            int population = Integer.parseInt(arr[3]);
            int swap = Integer.parseInt(arr[4]);
            double merge = Double.parseDouble(arr[5]);

            Global adv = new Global(arr[0], nrWorkers, duration, population, swap, merge);

            ThreadMerge tm = new ThreadMerge(adv);
            ThreadWait tw = new ThreadWait(adv);

            adv.startWorkers();
            tw.start();
            tm.start();
        }

      //  }

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

