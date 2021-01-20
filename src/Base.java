import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Base {
    public Worker[] workers;
    private Algorithm aj;

    public Base(int nrWorkers, String file, int population){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population);

        for (int i = 0; i<workers.length; i++) {
            String name = "thread-"+i;
            Worker thread = new Worker(aj);
            thread.setName(name);
            workers[i] = thread;
        }
    }

    public void startWorkers(){
        for(int i = 0; i< workers.length; i++){
            workers[i].start();
        }
    }

    public void stopWorkers() throws InterruptedException {
        for(int i = 0; i<workers.length; i++){
            workers[i].interrupt();
            workers[i].join();
        }
    }

    public static class Worker extends Thread {
        private Algorithm aj;
        private List<Path> paths;

        Worker(Algorithm aj) {
            this.aj = aj;
            paths = aj.initPaths();
        }

        public Path getBestPath(){
            return paths.get(0);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    aj.runAl(paths, this.getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Base base = new Base(3, "burma14.txt", 4);
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
