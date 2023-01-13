import java.util.concurrent.*;

public class MyThreadPoolExecutor extends ThreadPoolExecutor {
    //Field to store the currentTaskType
    private TaskType currentTaskType = null;
    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workQueue) {
        super(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new MyFutureTask<>(callable);
    }

    /*
     * setting task triggered time used for testing
     * and pulling TaskType prior to executing
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        long triggered =System.currentTimeMillis();
        ((MyFutureTask<?>) r).getTask().setTriggered(triggered);
        this.currentTaskType = ((MyFutureTask<?>) r).getTask().getTaskType();

    }

    /**
     * O(1)
     * getting TaskType
     * @return int representive of taskType
     */
    public int getTaskType() {
        return currentTaskType.getType().getPriorityValue();
    }
}
