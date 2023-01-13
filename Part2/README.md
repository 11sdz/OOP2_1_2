# OOP2_Part2
## Brief Overview
In this assigment we were requested to create two new types that extend the functionality of Java's concurrency framework
1. is a generic task with a type that returns a result and may throw an exception each task has a priority used for scheduling 
2. a custom threadpool class that defines a method for submitting a generic task as described in the section 1 to a priority queueand a method for submitting a generic task created by a callable and a type passed as arguments

## Class structure 
|Name|Description|
|----|-----------|
|TaskType|Enum to descript a Task's type (priority)|
|Task|The class represents a task with a priority that may return a value of some type|
|MyFutureTask|The class extends FutureTask we used adapter design pattern to make it comparable|
|CustomExecutor|The class for our Executor|
|MyThreadPoolExecutor|The class extends ThreadPoolExecutor we used adapter design pattern|

#### MyThreadPoolExecutor 
The class extends ThreadPoolExecutor with its default constructor , and overriding 2 methods newTaskFor , beforeExecute.
newTaskFor the task been wrapped with our FutureTask class MyFutureTask.
beforeExecute in order to get the highest priority in the queue without getting it directly from the queue,
at the time the executor getting the runnable from the queue before executing we take casting the runnable and getting it's priority. (we also getting the epoch time its has been triggered mainly for Testing our code)

#### MyFutureTask 
been used wrapping in newTaskFor , we added to the "basic" FutureTask a field to store Tasks and also made comparable by comparing tasks priorityValue.

#### CustomExecutor
the Executor , has a main method to submit Tasks , and a method to gracefully shutdown (shutting down after executing tasks and the remaining tasks in the queue).

## Testing
the project was test using the UniTest library. 

## UML
