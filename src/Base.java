import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Base {
    public Worker[] workers;
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

    public void stopWorkers() throws InterruptedException {
        System.out.println();
        for(int i = 0; i<workers.length; i++){
            workers[i].interrupt();
            workers[i].join();
            System.out.println("Best path found by "+workers[i].getName()+": "+workers[i].getBestPath() + " in " + workers[i].getTime() + " milliseconds");
        }
        System.out.println();
        System.out.println("Best path found: " + aj.getBestPath());

    }

    public static class Worker extends Thread {
        //private Algorithm aj;
        private List<Path> paths;
        private Base base;
        private long time;

        Worker(Base base) {
            this.base = base;
            paths = base.getAlgorithm().initPaths();
            time = -1;
        }

        public Path getBestPath(){
            return paths.get(0);
        }

        public void setTime(long time){
            this.time = time;
        }

        public long getTime(){
            return time;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    base.getAlgorithm().runAlgorithm(paths, this, base);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Base base = new Base(3, "burma14.txt", 4);
        ThreadWait tw = new ThreadWait(3000, base);
        tw.start();
        base.startWorkers();
/*
        long t= System.currentTimeMillis();
        long end = t+3000;
        while(System.currentTimeMillis() < end) {

        }*/


    }
}
