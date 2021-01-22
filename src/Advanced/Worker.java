package Advanced;

import java.util.List;

public class Worker extends Thread {

    private List<Path> paths;
    private Path bestPath;
    private Global global;
    private long time;
    private int popSize;
    private int iterationBest;
    private int iterations;

    Worker(Global global) {
        this.global = global;
        paths = global.getAlgorithm().initPaths();
        bestPath = paths.get(0);
        time = 0;
        this.popSize= paths.size();
    }

    public int getIterationBest() {
        return iterationBest;
    }

    public void setIterationBest(int iterationBest) {
        this.iterationBest = iterationBest;
    }

    public int getIterations() {
        return iterations;
    }

    public void incrementIterations() {
        this.iterations++;
    }

    public int getPopSize() {
        return paths.size();
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Path getBestPath() {
        return bestPath;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public int getBestDistance() {
        return bestPath.getDistance();
    }

    public void setBestPath(Path bestPath) {
        this.bestPath = bestPath;
    }


    @Override
    public void run() {
        while (!Thread.interrupted()) {
            while(Global.doWork){
                try {
                    global.getAlgorithm().runAlgorithm(this, global);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        global.setBestPath(this);
    }
}
