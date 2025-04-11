import java.io.*;
import java.util.*;

public class Main {

    static int Q;
    static HashMap<String, Integer> table;
    static HashMap<Integer, String> valueToName;
    static DynamicSegmentTree dynamicSegmentTree;
    static StringBuilder sb;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());
        sb = new StringBuilder();
        for(int i = 0; i < Q; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            String keyword = st.nextToken();
            switch(keyword){
                case "init":
                    init();
                    break;
                case "insert":
                    insert(st.nextToken(), Integer.parseInt(st.nextToken()));
                    break;
                case "delete":
                    delete(st.nextToken());
                    break;
                case "rank":
                    rank(Integer.parseInt(st.nextToken()));
                    break;
                case "sum":
                    sum(Integer.parseInt(st.nextToken()));
                    break;
            }
        }
        System.out.println(sb);
    }

    public static void init(){
        table = new HashMap<>();
        valueToName = new HashMap<>();
        dynamicSegmentTree = new DynamicSegmentTree();
    }

    public static void insert(String name, int value){
        if(table.containsKey(name) || valueToName.containsKey(value)){
            sb.append(0).append("\n");
        }else{
            table.put(name, value);
            valueToName.put(value, name);
            dynamicSegmentTree.update(value, value);
            sb.append(1).append("\n");
        }
    }

    public static void delete(String name){
        if(!table.containsKey(name)){
            sb.append(0).append("\n");
        }else{
            int value = table.remove(name);
            valueToName.remove(value);
            dynamicSegmentTree.update(value, 0);
            sb.append(value).append("\n");
        }
    }


    public static void rank(int k){
        if(table.size() < k){
            sb.append("None").append("\n");
            return;
        }
        int value = dynamicSegmentTree.queryRank(k);
        String name = valueToName.get(value);

        sb.append(name).append("\n");
    }

    public static void sum(int k){
        long res = dynamicSegmentTree.queryOfSum(k);
        sb.append(res).append("\n");
    }

    static class Data implements Comparable<Data> {
        String name;
        int value;

        Data(String name, int value){
            this.name = name;
            this.value = value;
        }

        public int compareTo(Data o){
            return Integer.compare(value, o.value);
        }
    }

    static class Node {
        long value;
        Node left, right;
        int count;

        Node(){
            this.value = 0;
            this.count = 0;
            this.left = null;
            this.right = null;
        }
    }

    static class DynamicSegmentTree{
        Node root = new Node();
        final int MIN = 1;
        final int MAX = 1_000_000_000;

        public void update(int idx, int val){
            update(root, MIN, MAX, idx, val);
        }

        private void update(Node node, int start, int end, int idx, int val){
            if(idx < start || idx > end) return;
            if(start == end){
                node.value = val;
                node.count = val > 0 ? 1 : 0;
                return;
            }

            int mid = start + (end - start) / 2;
            if(idx <= mid){
                if(node.left == null) node.left = new Node();
                update(node.left, start, mid, idx, val);
            }else{
                if(node.right == null) node.right = new Node();
                update(node.right, mid + 1, end, idx, val);
            }
            node.value = (node.left != null ? node.left.value : 0) + (node.right != null ? node.right.value : 0);
            node.count = (node.left != null ? node.left.count : 0) + (node.right != null ? node.right.count : 0);
        }

        private long queryOfSum(Node node, int start, int end, int left, int right){
            if(node == null || right < start || left > end) return 0;
            if(left <= start && right >= end) return node.value;
            int mid = start + (end - start) / 2;

            return queryOfSum(node.left, start, mid, left, right)
             + queryOfSum(node.right, mid + 1, end, left, right);
        }

        public long queryOfSum(int k){
            return queryOfSum(root, MIN, MAX, MIN, k);
        }

        public int queryRank(int k) {
        return queryRank(root, MIN, MAX, k);
        }
        
        private int queryRank(Node node, int start, int end, int k) {
            if (start == end) return start;
            int mid = start + (end - start) / 2;
            int leftCount = (node.left != null ? node.left.count : 0);
            if (leftCount >= k) {
                return queryRank(node.left, start, mid, k);
            } else {
                return queryRank(node.right, mid + 1, end, k - leftCount);
            }
        }
    }
}