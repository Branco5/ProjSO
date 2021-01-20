public class ThreadWait extends Thread {
    private Base base;
    private int time;

    public ThreadWait(int time, Base base){
        this.time = time;
        this.base = base;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                sleep(time);
                base.stopWorkers();
                System.out.println("Program ended");
                this.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
