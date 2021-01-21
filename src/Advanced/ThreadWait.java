package Advanced;


public class ThreadWait extends Thread {
    private Advanced advanced;
   // private ThreadMerge merge;

    public ThreadWait(Advanced advanced, ThreadMerge merge) {
        this.advanced = advanced;
        //this.merge = merge;
    }

    @Override
    public void run(){
        try {
            sleep(advanced.getDuration());
            Advanced.doWork=false;
            advanced.stopWorkers();
            System.out.println("\nProgram ended\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



