package com.company;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


class MyThread extends Thread {
    private int let;
    public int getLet() {
        return let;
    }
    public void setLet(int l) {
        let = l;
    }
    private int colNum;
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private static AtomicInteger count = new AtomicInteger(0);
    public synchronized int getCount() {
        return count.get();
    }
    public synchronized void addCount() {
        count.incrementAndGet();
    }
    public synchronized void resetCount() {
        count = new AtomicInteger((0));
    }
    public void setColNum(int c) {
        colNum = c;
    }
    public int getColNum() {
        return colNum;
    }
    public synchronized void run() {
        int dur = randInt(8, 25);

        for (int i = 1; i <= dur; i++) {                    //Add and pop values from threads into/out of queue
            int a = getLet() + 64;                          //Format timestamp and label for each thread and instance
            char letter = (char) a;
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
            String formatted = sdf.format(now);
            queue.add(formatted + "[" + letter + String.format("%02d", i) + "]\t");
            System.out.print(queue.poll());
            addCount();

            int curCount = getCount();                                  //Count number of entries in columns
            if (curCount >= getColNum()) {                              //and start new line at end
                System.out.print("\n");
                resetCount();
            }
            int wait = (randInt(1, 3) * 1000);
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static int randInt(int low, int high) {                                     //Random number generator
        Random rand = new Random();
        int number = rand.nextInt(high - low) + low;
        return number;
    }
}
public class Main {
    private static boolean checkChoice(int choice, int min, int max) {                 //Choose min and max input values
    if (choice < min || choice > max) {
        System.out.print("Error. ");
        return false;
    }
    return true;
}
    public static void main(String[] args) throws InterruptedException {

        Scanner scan = new Scanner(System.in);
        int threadCount;
        do {
            System.out.println("Please input number of threads between 1 and 5:");      //Input thread count
            while (!scan.hasNextInt()) {
                System.out.println("Error. Please enter a valid number: ");             //Check for valid input
                scan.next();
            }
            threadCount = scan.nextInt();
        } while (!checkChoice(threadCount, 1, 5));

        int columnCount;
        do {
            System.out.println("Please input number of columns between 5 and 10:");     //Input column count
            while (!scan.hasNextInt()) {
                System.out.println("Error. Please enter a valid number: ");             //Check for valid input
                scan.next();
            }
            columnCount = scan.nextInt();
        } while (!checkChoice(columnCount, 5, 10));
        for (int l = 1; l <= threadCount; l++) {                                         //Create threads and run
            MyThread thread = new MyThread();
            thread.setLet(l);
            thread.setColNum(columnCount);
            thread.start();
        }
    }


}
