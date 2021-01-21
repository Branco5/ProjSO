package Advanced;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ThreadMerge extends Thread {
    private Advanced advanced;
    private long period;

    public ThreadMerge(Advanced advanced){
        this.period = (long)advanced.getPeriod();
        this.advanced = advanced;
    }

    @Override
    public void run() {
        long time = advanced.getStartTime()+advanced.getDuration()-period*2;
        while (System.currentTimeMillis() <= time) {
            try {
                sleep(period);
                Advanced.doWork=false;
                sleep(200);
                System.out.println("\nMERGING POPULATIONS\n");
                List<Path> merged = new ArrayList<>();
                for(Worker w : advanced.getWorkers()){
                    merged.addAll(w.getPaths());
                }
                merged.sort(Comparator.comparing(Path::getDistance));

                for(Worker w : advanced.getWorkers()){
                    int size = w.getPopSize();
                    List<Path> newList = new ArrayList<>(merged.subList(0,size));
                    w.setPaths(newList);
                }

                Advanced.doWork=true;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
