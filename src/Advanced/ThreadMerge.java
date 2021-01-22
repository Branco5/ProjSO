package Advanced;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ThreadMerge extends Thread {
    private Global global;
    private long period;

    public ThreadMerge(Global global){
        this.period = (long) global.getPeriod();
        this.global = global;
    }

    @Override
    public void run() {
        long time = global.getStartTime()+ global.getDuration()-period;
        //System.out.println(period);
        while (System.currentTimeMillis() <= time) {
            try {
                sleep(period);
                Global.doWork=false;
                sleep(200);
                System.out.println("\nMERGING POPULATIONS\n");
                List<Path> merged = new ArrayList<>();
                for(Worker w : global.getWorkers()){
                    merged.addAll(w.getPaths());
                }
                merged.sort(Comparator.comparing(Path::getDistance));

                for(Worker w : global.getWorkers()){
                    int size = w.getPopSize();
                    List<Path> newList = new ArrayList<>(merged.subList(0,size));
                    w.setPaths(newList);
                    //System.out.println(w.getName() + " - " + w.getPaths().size());
                }

                Global.doWork=true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.interrupt();

    }
}
