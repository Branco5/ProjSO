package Advanced;


public class ThreadWait extends Thread {
    private Advanced advanced;
    private final ThreadMerge merge;

    public ThreadWait(Advanced advanced, ThreadMerge merge) {
        this.advanced = advanced;
        this.merge = merge;
    }

    @Override
    public void run(){
        try {
            sleep(advanced.getDuration());
            merge.interrupt();
            advanced.stopWorkers();
            System.out.println("\nProgram ended");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



