import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);
    public CustomExecutor myTestCustomExecutor = new CustomExecutor();
    public List <Future> futureList = new LinkedList<>();
    public List <Task> taskList = new LinkedList<>();
    public int amountOfTasks =31; // Amount of tasks to be created

    /**
     * BeforeEach initializing tests
     * creating tasks 1 to amountOfTasks each task return her "ID" number 1 to 31
     * each task gets an appropriate TaskType COMPUTATIONAL,IO,OTHER (even number and not divided by 5,divided by 5 ,else)
     * COMPUTATIONAL Tasks takes the longest time to complete ,
     * IO takes average (between the longest and shortest) time to complete,and OTHER tasks takes the lowest time to complete
     */
    @BeforeEach
    public void beforeEach(){
        this.myTestCustomExecutor = new CustomExecutor();
        //this.myTestCustomExecutor= new CustomExecutor(1,1,300, TimeUnit.MILLISECONDS); //Used for testing
        taskList.add(null);//for comfort each task result is her id 1 to amountOfTasks
        TaskType taskType;
        int sleep=0;
        for (int i = 1; i <= amountOfTasks; i++) {
            if (i%2==0 && i%5!=0){
                taskType=TaskType.COMPUTATIONAL;
                sleep=1000;
            }else if(i%5==0){
                taskType=TaskType.OTHER;
                sleep=200;
            }else {
                taskType= TaskType.IO;
                sleep = 600;
            }
            int finalI = i;
            int finalSleep = sleep;
            Callable callable = () -> {
                sleep(finalSleep);
                return finalI;
            };
            Task task= Task.createTask(callable,taskType);
            taskList.add(task);
            futureList.add(myTestCustomExecutor.submit(task));
        }
    }

    /**
     * while iteration on all tasks
     * whenever task isDone Logger printing task info and removed from list (then we need to decrease mod)
     * assertion part we assert if tasks that has a higher Priority really triggered before tasks with lower Priority
     * (IMPORTANT NOTE the first tasks could be executed even before higher Priority Tasks because workerQueue size is
     * less than the coreSize
     */
    @Test
    public void myTest(){
        //this.myTestCustomExecutor = new CustomExecutor(1,1,300, TimeUnit.MILLISECONDS); //Used for testing
        int i=0,taskNum=0,modulo = amountOfTasks;
        while (!futureList.isEmpty() && modulo>1 && i>=0){
            if(futureList.get(i).isDone()){
                try {
                    taskNum = (int) futureList.get(i).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                int finalTaskNum = taskNum;
                int currentMax= myTestCustomExecutor.getCurrentMax();
                int finalI = i;
                logger.info(() -> "Task number = " + finalTaskNum+"  , " +futureList.get(finalI).toString()+"\nCurrent maximum = "+currentMax);
                futureList.remove(i);
                i--;
                modulo--;
            }else{
                i++;
            }
            i=Math.floorMod(i,modulo);
        }
        //testing if odd task been executed after even tasks (except the first ones because threadPool size is cores/2 to (cores-1))
        assertTrue(taskList.get(15).getTriggered() > taskList.get(28).getTriggered());
        assertTrue(taskList.get(15).getTriggered() > taskList.get(27).getTriggered());
        assertTrue(taskList.get(2).getTriggered() < taskList.get(16).getTriggered());
        assertTrue(taskList.get(12).getTriggered() < taskList.get(30).getTriggered());
        assertTrue(taskList.get(22).getTriggered() < taskList.get(17).getTriggered());
        myTestCustomExecutor.gracefullyTerminate();
    }

    /**
     * we test if Gracefully Terminated orderly shutdown after all tasks(tasks that currently running and from queue)
     * been executed
     */
    @Test
    public void myTestGracefullyTerminatedTest(){
        this.myTestCustomExecutor.gracefullyTerminate();
        boolean isGracefullyTerminated = true;
        for (int i = 0; i <amountOfTasks ; i++) {
            if(!futureList.get(i).isDone()) isGracefullyTerminated = false;
        }
        //testing if odd task been executed after even tasks (except the first ones because threadPool size is cores/2 to (cores-1))
        assertTrue(taskList.get(15).getTriggered() > taskList.get(28).getTriggered());
        assertTrue(taskList.get(15).getTriggered() > taskList.get(27).getTriggered());
        assertTrue(taskList.get(2).getTriggered() < taskList.get(16).getTriggered());
        assertTrue(taskList.get(12).getTriggered() < taskList.get(30).getTriggered());
        assertTrue(taskList.get(22).getTriggered() < taskList.get(17).getTriggered());
        assertTrue(isGracefullyTerminated);
    }

    /**
     * this Test using the other Constructor of CustomExecutor
     */
    @Test
    public void myTest2(){
        CustomExecutor customExecutor = new CustomExecutor();
        TaskType taskType;
        List <Future> futureList2 = new LinkedList<>();
        int sleep;
        for (int i = 1; i <= amountOfTasks; i++) {
            if (i%2==0 && i%5!=0){
                taskType=TaskType.COMPUTATIONAL;
                sleep=1000;
            }else if(i%5==0){
                taskType=TaskType.OTHER;
                sleep=200;
            }else {
                taskType= TaskType.IO;
                sleep = 600;
            }
            int finalI = i;
            int finalSleep = sleep;
            Callable callable = () -> {
                sleep(finalSleep);
                return finalI;
            };
            futureList2.add(customExecutor.submit(callable,taskType));
        }
        while(customExecutor.isActive()){};
        // A = task number 15 (OTHER) , B task number 28 (COMPUTATIONAL)
        MyFutureTask myFutureTaskA = (MyFutureTask) futureList2.get(14);
        MyFutureTask myFutureTaskB = (MyFutureTask) futureList2.get(27);
        assertTrue(myFutureTaskA.getTask().getTriggered() > myFutureTaskB.getTask().getTriggered());
        // A = task number 2 (COMPUTATIONAL) , B task number 16 (COMPUTATIONAL)
        myFutureTaskA = (MyFutureTask) futureList2.get(1);
        myFutureTaskB = (MyFutureTask) futureList2.get(15);
        assertTrue(myFutureTaskB.getTask().getTriggered() > myFutureTaskA.getTask().getTriggered());
        // A = task number 11 (IO) , B task number 30 (OTHER)
        myFutureTaskA = (MyFutureTask) futureList2.get(10);
        myFutureTaskB = (MyFutureTask) futureList2.get(29);
        assertTrue(myFutureTaskA.getTask().getTriggered() < myFutureTaskB.getTask().getTriggered());
        // A = task number 3 (IO) , B task number 29 (IO)
        myFutureTaskA = (MyFutureTask) futureList2.get(2);
        myFutureTaskB = (MyFutureTask) futureList2.get(28);
        assertTrue(myFutureTaskA.getTask().getTriggered() < myFutureTaskB.getTask().getTriggered());
    }

    @AfterAll
    public static void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = (int) sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = (Double) priceTask.get();
            reversed = (String) reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }
}
