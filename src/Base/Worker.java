package Base;

import java.util.List;

public class Worker extends Thread {
    //private Base.Algorithm aj;
    private List<Path> paths;
    private Base base;
    private long time;


    Worker(Base base) {
        this.base = base;
        paths = base.getAlgorithm().initPaths();
        time = 0;
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

    public void putBestPath(){
        base.getAlgorithm().putWorkerPath(this);
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
