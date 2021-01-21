package Base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class Base {
    private Worker[] workers;
    private Algorithm aj;
    private long startTime;

    public Base(int nrWorkers, String file, int population){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population);

        for (int i = 0; i<workers.length; i++) {
            String name = "thread-"+i;
            Worker thread = new Worker(this);
            thread.setName(name);
            workers[i] = thread;
        }
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

    public long getStartTime(){
        return startTime;
    }

    synchronized void stopWorkers(){

        System.out.println("STOPPING WORKERS");
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

        Base base = new Base(10, "att48.txt", 50);
        ThreadWait tw = new ThreadWait(5000, base);
        tw.start();
        base.startWorkers();
/*
        long t= System.currentTimeMillis();
        long end = t+3000;
        while(System.currentTimeMillis() < end) {

        }*/


    }
}
