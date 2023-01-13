import java.util.concurrent.Callable;

public class Task<T> implements Callable<T>{
    //TaskType = COMPUTATIONAL,IO,OTHER
    private TaskType taskType;
    private Callable callable;
    private long triggered; // this field is used mainly for TESTING !
    //Factory Constructor method
    public Task(Callable callable, TaskType taskType) {
        this.callable=callable;
        this.taskType=taskType;
    }

    /**
     * using factory method for creating new Task with given parameters
     * @param callable - representing task that returns result
     * @param taskType - taskType representing Prirority
     * @return Task
     */
    public static Task createTask(Callable callable, TaskType taskType){
        return new Task(callable,taskType);
    }

    /**
     * using factory method for creating new Task with default priority
     * @param callable - representing task that returns result
     * @return Task
     */
    public static Task createTask(Callable callable){
        return new Task(callable , TaskType.OTHER);
    }

    /**
     * Getter for gettig TaskType
     * @return TaskType
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * this method to get the epoch time that the Task has been triggered
     * @return long triggered
     */
    public long getTriggered() {
        return triggered;
    }

    /**
     * this method to set the epoch time that the task has been triggered
     * @param triggered
     */
    public void setTriggered(long triggered) {
        this.triggered = triggered;
    }

    @Override
    public T call() throws Exception {
        return (T) callable.call();
    }
}
