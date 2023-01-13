import org.junit.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.*;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class Ex2_1_Test {
    long start,elapsed;
    float elapsedTimeSec;
    public static final Logger logger = LoggerFactory.getLogger(Ex2_1_Test.class);
    public int result = 0;
    @Test
    public void createTextFiles(){
        int n = 10,seed = 14,bound =200;
        String []fileNames = Ex2_1.createTextFiles(n,seed,bound);
        assertEquals(n,fileNames.length);
        assertEquals(n, Objects.requireNonNull(new File("..\\OOP2\\").listFiles(
                file -> !file.isDirectory() && file.getName().startsWith("file_") && file.getName().endsWith(".txt"))).length);
    }
    @Test
    public void getNumOfLines(){
        Ex2_1 ex2_1 = new Ex2_1();
        String []fileName=ex2_1.createTextFiles(3,14,100);
        start = System.currentTimeMillis();
        result=ex2_1.getNumOfLines(fileName);
        elapsed = System.currentTimeMillis()-start;
        elapsedTimeSec = elapsed/1000F;
        logger.info(()->"Time(sec) = "+elapsedTimeSec + "\nlines = "+result);
        assertEquals(166,result);
    }
    @Test
    public void getNumOfLinesThreads(){
        Ex2_1 ex2_1 = new Ex2_1();
        String []fileName=ex2_1.createTextFiles(3,14,100);

        start = System.currentTimeMillis();
        result = ex2_1.getNumOfLinesThreads(fileName);
        elapsed = System.currentTimeMillis()-start;
        elapsedTimeSec = elapsed/1000F;
        logger.info(()->"Time(sec) = "+elapsedTimeSec + "\nlines = "+ result);
        assertEquals(166,result);

        fileName=Ex2_1.createTextFiles(3,14,300);
        start = System.currentTimeMillis();
        result = ex2_1.getNumOfLinesThreads(fileName);
        elapsed = System.currentTimeMillis()-start;
        elapsedTimeSec = elapsed/1000F;
        logger.info(()->"Time(sec) = "+elapsedTimeSec + "\nlines = "+result);
        assertEquals(466,result);
    }
    @Test
    public void getNumOfLinesThreadPool(){
        Ex2_1 ex2_1 = new Ex2_1();
        String []fileName=ex2_1.createTextFiles(3,14,100);
        start = System.currentTimeMillis();
        result = ex2_1.getNumOfLinesThreadPool(fileName);
        elapsed = System.currentTimeMillis()-start;
        elapsedTimeSec = elapsed/1000F;
        logger.info(()->"Time(sec) = "+elapsedTimeSec + "\nlines = "+result);
        assertEquals(166,result);

        fileName=Ex2_1.createTextFiles(3,14,300);
        start = System.currentTimeMillis();
        result = ex2_1.getNumOfLinesThreadPool(fileName);
        elapsed = System.currentTimeMillis()-start;
        elapsedTimeSec = elapsed/1000F;
        logger.info(()->"Time(sec) = "+elapsedTimeSec + "\nlines = "+result);
        assertEquals(466,result);
    }
    @AfterEach
    public void deleteFiles(){
        File file = new File("file_1.txt");
        file.delete();
        file = new File("file_2.txt");
        file.delete();
        file = new File("file_3.txt");
        file.delete();
    }
}
