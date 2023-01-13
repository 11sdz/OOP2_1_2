import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
/**
 *
 */

public class Ex2_1 {
    /**
     * Creating Text Files - each file contains random number of lines between 0 to bound
     * generated using java.Random using seed
     * each line contains 10 characters "0123456789"
     * @param n - number of files
     * @param seed - Random Seed
     * @param bound - Random number between 0-bound
     * @return String[] contains file names "file_1.txt , file_2.txt ,...., file_n.txt"
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] names = new String[n];
        Random random = new Random(seed);

        for (int i = 0; i < n; i++) {
            names[i] = "file_" + String.valueOf(i + 1) + ".txt";
            File file = new File(names[i]);
            int limit = random.nextInt(bound);
            try {
                FileWriter fileWriter = new FileWriter(file);
                for (int j = 0; j < limit; j++) {
                    fileWriter.write("0123456789\n");
                }
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return names;
    }

    /**
     * counting the number of lines in each file
     * and calculating sum of lines in all files together
     * @param fileNames - text files names
     * @return Integer number representing the sum of lines\rows
     */
    public static int getNumOfLines(String[] fileNames) {
        int counter = 0;
        Scanner scanner;
        for (int i = 0; i < fileNames.length; i++) {
            try {
                scanner = new Scanner(new File(fileNames[i]));
                while (scanner.hasNext()) {
                    counter++;
                    scanner.next();
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return counter;
    }

    /**
     * counting the number of lines in each file using Threads
     * @param fileNames - text files names
     * @return Integer number representing sum of lines\rows
     */
    public int getNumOfLinesThreads(String[] fileNames) {
        int numberOfLines = 0;
        CountNumOfLinesThreads cnot;
        for (int i = 0; i < fileNames.length; i++) {
            cnot = new CountNumOfLinesThreads(fileNames[i]);
            cnot.run();
            if (i==fileNames.length-1) {
                numberOfLines = cnot.getNumberOfLines();
                cnot.resetNumberOfLines();
            }
        }
        return numberOfLines;
    }

    /**
     *  creating fixed thread pool
     *  each thread assigned to one file and count its lines
     *  we sum all of threads results
     * @param fileNames
     * @return sum of lines
     */
    public int getNumOfLinesThreadPool(String[] fileNames) {
        ExecutorService executorService = Executors.newFixedThreadPool(fileNames.length);
        List <Future<Integer>> futureList= new ArrayList<Future<Integer>>();
        int num=0;
        for (int i = 0; i < fileNames.length; i++) {
            Callable callable = new CountNumOfLinesThreadPool(fileNames[i]);
            Future<Integer> integerFuture = executorService.submit(callable);
            futureList.add(integerFuture);
        }

        //each file has its own result of the amount of lines , and then we sum all results
        try {
            for (int i = 0; i < futureList.size(); i++) {
                num = num + futureList.get(i).get().intValue();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return num;
    }

    /**
     * Class inherit from Thread implements Runnable
     * this CountNumOfLinesThreads Class calculating the sum of Lines
     * Using Static counter that is shared by each thread
     */
    static class CountNumOfLinesThreads extends Thread {
        String filename;
        static int numberOfLines = 0;

        /**
         * Allocates a new {@code Thread} object.
         * {@code (null, null, gname)}, where {@code gname} is a newly generated
         * name. Automatically generated names are of the form
         * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
         */
        public CountNumOfLinesThreads(String fileName) {
            super();
            this.filename = fileName;
        }

        /**
         * Setters - that Set the numberOfLines to 0
         * to be Called when last thread is finished counting all lines
         */
        public static void resetNumberOfLines() {
            CountNumOfLinesThreads.numberOfLines = 0;
        }

        /**
         * run() counting lines
         */
        public void run() {
            try {
                Scanner scanner = new Scanner(new File(this.filename));
                while (scanner.hasNext()) {
                    numberOfLines++;
                    scanner.next();
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * Getter return numberOfLines sum of lines
         * @return Integer number representing the sum of lines counted in all files
         */
        public static int getNumberOfLines() {
            return numberOfLines;
        }
    }

    /**
     * each thread assigned to one file and count its own lines
     * CountNumOfLinesThreadPool - implements Callable
     * Thread Pool
     */
    static class CountNumOfLinesThreadPool implements Callable {
        String fileName;
        int counter=0;
        public CountNumOfLinesThreadPool(String fileName) {
            this.fileName=fileName;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public Integer call() throws Exception {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()){
                counter++;
                scanner.next();
            }
            scanner.close();
            return counter;
        }
    }
}
