package com.xu.artifactorydemo;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
//    ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
//    LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<Runnable>();
    SynchronousQueue<Runnable> queue = new SynchronousQueue<Runnable>();

    ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE, 60, TimeUnit.SECONDS, queue);

    @Test
    public void useAppContext() {
        poolExecutor.execute(() -> {
            System.out.println("任务1：" + Thread.currentThread());
            while (true) {

            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        poolExecutor.execute(() -> {
            System.out.println("任务2：" + Thread.currentThread());
        });

        poolExecutor.execute(() -> {
            System.out.println("任务3：" + Thread.currentThread());
        });
    }


//        // Context of the app under test.
//      /*  Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.xu.artifactorydemo", appContext.getPackageName());*/
//        String text = "aab";
//        Log.d("xtf->", text);
////        for (int i = 0; i < text.length(); i++) {
////            for (int j = i + 1; j < text.length(); j++) {
////
////            }
////
////        }
////        println("xtf->" + text);
//        partition(text);
//        Log.d("xtf->", results.toString());
////        println("xtf->" + results);
//    }


    List<List<String>> results;
    boolean[][] isPalindrome;

    /**
     * @param s: A string
     * @return: A list of lists of string
     */
    public List<List<String>> partition(String s) {
        results = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return results;
        }

        getIsPalindrome(s);

        helper(s, 0, new ArrayList<Integer>());

        return results;
    }

    private void getIsPalindrome(String s) {
        int n = s.length();
        isPalindrome = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            isPalindrome[i][i] = true;
        }
        for (int i = 0; i < n - 1; i++) {
            isPalindrome[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
        }

        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                isPalindrome[i][j] = isPalindrome[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
            }
        }
    }

    private void helper(String s,
                        int startIndex,
                        List<Integer> partition) {
        if (startIndex == s.length()) {
            addResult(s, partition);
            return;
        }

        for (int i = startIndex; i < s.length(); i++) {
            if (!isPalindrome[startIndex][i]) {
                continue;
            }
            partition.add(i);
            helper(s, i + 1, partition);
            partition.remove(partition.size() - 1);
        }
    }

    private void addResult(String s, List<Integer> partition) {
        List<String> result = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < partition.size(); i++) {
            result.add(s.substring(startIndex, partition.get(i) + 1));
            startIndex = partition.get(i) + 1;
        }
        results.add(result);
    }
}