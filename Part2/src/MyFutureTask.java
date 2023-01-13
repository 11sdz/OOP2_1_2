import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyFutureTask<T> extends FutureTask<T> implements Comparable<MyFutureTask<T>>{
    //Adapter design pattern
    //to be able to compare between different FutureTasks
    private final Task task;

    /**
     * Constructor
     * @param callable - Task class implementing Callable
     */
    public MyFutureTask(Callable<T> callable) {
        super(callable);
        this.task = (Task) callable; //casting callable to Task
    }

    /**
     * Comparable method to compare between this to other MyFutureTask
     * @param o - MyFutureTask
     * @return negative number if this.task.TaskType is less then , 0 if equals , positive number if greater than other MyFutureTask o.task.TaskType
     */
    @Override
    public int compareTo(MyFutureTask<T> o) {
        return Integer.compare(this.task.getTaskType().getPriorityValue(),o.task.getTaskType().getPriorityValue());
    }

    /**
     * Getter returning Task task
     * @return task
     */
    public Task getTask() {
        return task;
    }

    @Override
    public String toString() {
        return "MyFutureTask{" +
                "task={" + task +" ,TaskType= "+task.getTaskType()+
                "}}";
    }
}
