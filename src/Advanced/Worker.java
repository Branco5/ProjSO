package Advanced;

import java.util.List;

public class Worker extends Thread {


    //private Base.Algorithm aj;
    private List<Path> paths;
    private Advanced advanced;
    private long time;
    private int popSize;

    Worker(Advanced advanced) {
        this.advanced = advanced;
        paths = advanced.getAlgorithm().initPaths();
        time = 0;
        this.popSize= paths.size();
    }

    public int getPopSize() {
        return paths.size();
    }

    public void updatePopSize() {
        this.popSize = paths.size();
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Path getBestPath() {
        return paths.get(0);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    /*public void putBestPath(){
        advanced.getAlgorithm().putWorkerPath(this);
    }*/
/*
    public synchronized void inter(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName()+" ENDING");
        Thread.currentThread().interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
*/


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while(Advanced.doWork){
                try {
                    advanced.getAlgorithm().runAlgorithm(paths, this, advanced);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
/*
            try {
                this.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }

    }
}
