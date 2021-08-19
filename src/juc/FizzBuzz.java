package juc;


import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

class FizzBuzz {

    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static int a = 1;

    public static void main(String[] args) {
        int[][] a = new int[][]{{1,1}};
        System.out.println(new ArrayList(Arrays.asList(a[0])).contains(1));
//        System.out.println(isCovered(a, 1, 50));
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }


    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA == null || headB == null) return null;
        int headANumber = 0;
        int headBNumber = 0;
        ListNode n1 = headA;
        ListNode n2 = headB;
        while(headA != null || headB != null){
            if(headA != null){
                headANumber++;
                headA = headA.next;
            }
            if(headB != null){
                headBNumber++;
                headB = headB.next;
            }
        }
        if(headANumber > headBNumber){
            while((headANumber - headBNumber) > 0){
                headBNumber++;
                n1 =  n1.next;
            }
        }else{
            while((headBNumber - headANumber) > 0){
                headANumber++;
                n2 = n2.next;
            }
        }
        while((n1 != n2) && (n1 != null || n2 != null)){
            n1 = n1.next == null ? null : n1.next;
            n2 = n2.next == null ? null : n2.next;
        }
        return n1;
    }


    public static int findRepeatNumber(int[] nums) {
        if(nums != null && nums.length == 1){
            return nums[0];
        }
        Set set = new HashSet();
        int j = nums.length -1;
        for (int i = 0; i != j ; i++,j--) {
            if(!set.add(nums[i])){
                return nums[i];
            }
            if(!set.add(nums[j])){
                return nums[j];
            }
        }
        return -1;
    }

    public static String replaceSpace(String s) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            buffer.append(32 == s.charAt(i) ? "%20": s.charAt(i));
        }
        return buffer.toString();
    }

    public static int[] reversePrint(ListNode head) {
        int length = 0;
        int i = 1;
        ListNode newHead = head;
        while(head != null){
            length++;
            head = head.next;
        }
        int[] array = new int[length];
        while(newHead != null){
            array[--length] = newHead.val;
            newHead = newHead.next;
        }
        return array;
    }


    public static boolean isStraight(int[] nums) {
        Arrays.sort(nums);
        int zeroNumber = 0;
        for (int i = 1; i < nums.length; i++) {
            int current = nums[i];
            int pre = nums[i-1];
            if(current == 0 || pre == 0){
                zeroNumber++;
                continue;
            }
            if(current - pre == 1){
                continue;
            }else if(current - pre <= 0 || zeroNumber==0){
                return false;
            }
            if(zeroNumber >0 && (current - pre)-1 <= zeroNumber) {
                nums[i - 1] = current - 1;
                zeroNumber = zeroNumber - (current - pre)-1;
            }else {
                return false;
            }
        }
        return true;
    }

    public static int sumNums(int n) {
        int sum = 0;
        boolean flag = n > 0 && (sum +=sumNums(n-1)+n) > 0;
        return sum;
    }

    public static int[] singleNumbers(int[] nums) {
        Arrays.sort(nums);
        int[] result = new int[2];
        for (int i = 1; i < nums.length; i++) {

        }
        return result;
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        Map<String,List<String>> map = new WeakHashMap();
        List resultList = new ArrayList();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            List<String> listStr = map.get(key);
            if (listStr != null) {
                listStr.add(str);
                continue;
            }
            List list = new ArrayList();
            list.add(str);
            resultList.add(list);
            map.put(key, list);
        }
        return resultList;
    }

    public static boolean isCovered(int[][] ranges, int left, int right) {
        HashMap hashMap = new HashMap();
        for (int i = left; i <= right ; i++) {
            hashMap.put(i,i);
        }
        for (int[] range : ranges) {
            for (int i : range) {
                if(hashMap.containsKey(i)){
                   hashMap.remove(i);
                }
            }
        }
        return hashMap.values() == null || hashMap.values().size() <= 0;
    }


    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int[] ints : matrix) {
            if(Arrays.asList(ints).contains(target)){
                return true;
            }
        }
        return false;
    }
}