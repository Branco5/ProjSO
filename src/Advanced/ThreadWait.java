package Advanced;


public class ThreadWait extends Thread {
    private Global global;

    public ThreadWait(Global global) {
        this.global = global;
    }

    @Override
    public void run(){
        try {
            sleep(global.getDuration());
            Global.doWork=false;
            global.stopWorkers();
            System.out.print("\nAlgorithm finished\n\n> ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



