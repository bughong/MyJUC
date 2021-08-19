package juc;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author HongTaiwei
 * @date 2021/7/20
 */
public class Test{
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[][] arrays = new int[][]{
                {}};
        System.out.println(findNumberIn2DArray(arrays,5));
    }

    static class MyHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("进入拒绝策略");
        }
    }

    public void threads(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                System.out.println(System.currentTimeMillis() / 1000 + " Thread ID：" + Thread.currentThread().getId());
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            });
        }
    }

    public static int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int max = 0;
        for(int i=0;i<nums.length-1/2;i++){
            max = nums[i]+nums[nums.length-1-i] > max ? nums[i]+nums[nums.length-1-i] : max;
        }
        return max;
    }

    public static int titleToNumber(String columnTitle) {
        int resultNumber = 0;
        for (int i = 0; i < columnTitle.length(); i++) {
            resultNumber =  i == columnTitle.length()-1 ?
                    (resultNumber + columnTitle.charAt(i)-64):
                    (resultNumber + columnTitle.charAt(i)-64)*26;
        }
        return resultNumber;
    }

    public static String maximumTime(String time) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < time.length(); i++) {
            char at = time.charAt(i);
            if(at == ':'){
                buffer.append(at);
                continue;
            }
            switch (i){
                case 0: at = (at == '?' && (time.charAt(i+1) == '?' || Integer.valueOf(time.charAt(i+1)+"") < 4))  ? '2' : (at == '?' && (time.charAt(i+1) != '?' || Integer.valueOf(time.charAt(i+1)+"") >= 4) ? '1' :at); break;
                case 1: at = (at == '?' && buffer.charAt(i-1) == '2') ? '3' : ((at == '?' && buffer.charAt(i-1) != '2') ? '9' : at); break;
                case 3: at = at == '?' ? '5' : at; break;
                case 4: at = at == '?' ? '9' : at; break;
            }
            buffer.append(at);
        }
        return buffer.toString();
    }

    public static int[] kWeakestRows(int[][] mat, int k) {
        int i = 0;
        int j = mat.length - 1;
        TreeMap<Integer, Integer> treeMap = new TreeMap();
        while ( j > i) {
            int[] mati = mat[i];
            int count = 0;
            int count1 = 0;
            for (int i1 = 0; i1 < mati.length; i1++) {
                int i2 = mati[i1];
                if (i2 != 1) {
                    break;
                }
                count++;
            }
            treeMap.put(count, i);
            int[] matj = mat[j];
            for (int i1 = 0; i1 < matj.length; i1++) {
                int i2 = mati[i1];
                if (i2 != 1) {
                    break;
                }
                count1++;
            }
            treeMap.put(count1, j);
            i++;
            j--;
        }
        int[] ints = new int[k];
        int y = 0;
        for (Integer integer : treeMap.keySet()) {
            if (y == k) {
                break;
            }
            ints[y++] = treeMap.get(integer);
        }
        return ints;
    }

    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int[] nums : matrix) {
            if(nums.length <= 0 || nums[0] > target || nums[nums.length-1] < target){
                continue;
            }
            int left = 0;
            int right = nums.length-1;
            while(left <= right){
                if(nums[0] > target || nums[nums.length-1] < target){
                    continue;
                }
                int mid = ((right-left)>>1)+left;
                if(nums[mid] == target){
                    return true;
                }else if(nums[mid] < target){
                    left = mid+1;
                }else{
                    right = mid-1;
                }
            }
        }
        return false;
    }
}
