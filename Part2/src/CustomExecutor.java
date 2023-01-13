import java.util.concurrent.*;

public class CustomExecutor{

    private MyThreadPoolExecutor poolExecutor;
    private int currentMax;
    private int cores=Runtime.getRuntime().availableProcessors();
    private PriorityBlockingQueue blockingQueue;

    /**
     * Empty Constructor
     */
    public CustomExecutor() {
        this.blockingQueue = new PriorityBlockingQueue<Task>();
        this.poolExecutor = new MyThreadPoolExecutor(cores/2,cores-1,300,TimeUnit.MILLISECONDS,this.blockingQueue);
    }

    /**
     * Constructor used for testing
     * @param core -core pool size
     * @param maxCore maximum range
     * @param keepAlive - keep alive time
     * @param timeUnit - enum TimeUnit
     *//*

    public CustomExecutor(int core,int maxCore,int keepAlive , TimeUnit timeUnit){
        this.blockingQueue = new PriorityBlockingQueue<Task>();
        this.poolExecutor = new MyThreadPoolExecutor(core,maxCore,keepAlive,timeUnit,this.blockingQueue);
    }

*/
    /**
     * this method to submit task into ThreadPoolExecutor
     * @param task - define a Task that return a result and its priority
     * @return future represents the result of the task
     */
    public Future submit(Task task){
        Future future = this.poolExecutor.submit(task);
        return future;
    }

    /**
     * method to create Task and submit into ThreadPoolExecutor
     * @param callable - define a task that return a result
     * @param taskType - priority of the task
     * @return future represents the result of the task
     */
    public Future submit(Callable callable,TaskType taskType){
        Task task = Task.createTask(callable,taskType);
        Future future = this.poolExecutor.submit(task);
        return future;
    }

    /**
     * Getter current maximum priority
     * @return Integer value representing priority
     */
    public int getCurrentMax() {
        this.currentMax = this.poolExecutor.getTaskType();
        return this.currentMax;
    }

    /**
     * returns True if threadPoolExecuter is running false if not
     * @return boolean
     */
    public boolean isActive(){
        return this.poolExecutor.getActiveCount()>0;
    }

    /**
     * shutdown function
     */
    public void gracefullyTerminate() {
        this.poolExecutor.shutdown();
        while(!this.poolExecutor.isTerminated()){};
    }
}

