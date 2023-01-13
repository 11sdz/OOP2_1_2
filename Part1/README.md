# Ex2_Part_1 OOP class - ariel CS

## Brief overview
In this part of the assigment we were requested to implement 4 methods.
The first method to create an amount of text files, each text file has a random number of lines and each line contains 10 chars.
The other methods we were requested to count the sum of the lines from all the files, each method is running in a different way (no threads,using threads, using thread pool).
## class structure
being an OOP course we had to make our classes to accomplish out desired design pattern. The classes we implemented are:

| Name                  | Description                                                                                                        |
|-----------------------|--------------------------------------------------------------------------------------------------------------------|
| Ex2_1 | The class contains all 4 methods (creating text files,getNumOfLines , getNumOfLinesThreads , getNumOfLinesThreadPool) and 2 nested classes (CountNumOfLinesThreads , CountNumOfLinesThreadPool) |

## Comparison Time in seconds
we used same seed number (14) in all runs , in each row all files limited to (10,100,1000,...1000000) lines
#### 10 files 
|Lines|getNumOfLines|getNumOfLinesThreads|getNumOfLinesThreadPool|
|---|---|---|---|
|54|0.042|0.006|0.005|
|474|0.02|0.006|0.006|
|6174|0.056|0.016|0.014|
|46174|0.126|0.038|0.012|
|436174|0.392|0.215|0.09|
|4636174|1.897|1.767|0.585|
#### 50 files
|Lines|getNumOfLines|getNumOfLinesThreads|getNumOfLinesThreadPool|
|---|---|---|---|
|220|0.106|0.023|0.034|
|2370|0.12|0.024|0.025|
|28370|0.212|0.025|0.014|
|232370|0.68|0.118|0.042|
|2152370|1.513|0.859|0.284|
|23652370|9.83|9.374|2.882|
#### 100 files
|Lines|getNumOfLines|getNumOfLinesThreads|getNumOfLinesThreadPool|
|---|---|---|---|
|483|0.176|0.025|0.064|
|4973|0.196|0.024|0.02|
|48573|0.392|0.037|0.044|
|482573|1.347|0.207|0.111|
|4242573|3.004|1.636|0.519|
|49142573|20.286|19.078|6.182|

## Conclusion
First we can see that in each row in the table (meaning for each number of files and total number of lines), the time it takes for getNumOfLines to sum the number of lines in all files is the longest. The reason for that is because this function does all the work on the main thread, without splitting the work, so the program works on one file at a time. Accordingly, as the number of files increases, the time taken for this function increases. And of course the same happens when the number of rows increases.
The running time of getNumOfLinesThreads is better than getNumOfLines's running time, because it uses Threads, that allows execution of tasks in parallel. But we can see that as the number of lines increase, the adventages of it reduced.
We can see that the running time of getNumOfLinesThreadPool is the lowest in most of the rows (except for those with fewer lines) and does not rise dramatically as the number of lines rise, in contrast to getNumOfLines. The reason for that is the fact that thread pool is more effective. The threads are already created and ready to take on new tasks, which reduces the time required to create new threads and makes the program more efficient.

## Testing
We created a test class using the UniTest library to test all the public functions of Ex2_1.
