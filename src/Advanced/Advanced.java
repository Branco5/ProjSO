package Advanced;

public class Advanced {
    private Worker[] workers;
    private Algorithm aj;
    private long startTime;
    private long start;
    private double percent;
    private long duration;
    static boolean doWork = true; //When false merge populations
    static boolean merge = false;

    public Advanced(int nrWorkers, double percent, String file, int population, int duration){
        this.workers = new Worker[nrWorkers];
        this.aj = new Algorithm(file, population);
        this.percent = percent;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();

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
        return percent*duration;
    }

    public Algorithm getAlgorithm(){
        return aj;
    }

    public void startWorkers(){
        //startTime = System.currentTimeMillis();
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

        Advanced adv = new Advanced(4, 0.3, "att48.txt", 50, 10000);
        ThreadMerge tm = new ThreadMerge(adv);
        ThreadWait tw = new ThreadWait(adv, tm);

        adv.startWorkers();
        tw.start();
        tm.start();

/*
        long t= System.currentTimeMillis();
        long end = t+3000;
        while(System.currentTimeMillis() < end) {

        }*/


    }
}

