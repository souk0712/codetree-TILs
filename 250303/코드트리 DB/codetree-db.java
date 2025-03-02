import java.io.*;
import java.util.*;

public class Main {

    static int Q;
    static HashMap<String, Integer> table;
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
    }

    public static void insert(String name, int value){
        if(table.containsKey(name) || table.containsValue(value)){
            sb.append(0).append("\n");
        }else{
            table.put(name, value);
            sb.append(1).append("\n");
        }
    }

    public static void delete(String name){
        if(!table.containsKey(name)){
            sb.append(0).append("\n");
        }else{
            sb.append(table.remove(name)).append("\n");
        }
    }


    public static void rank(int k){
        if(table.size() < k){
            sb.append("None").append("\n");
            return;
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(table.entrySet());
        entries.sort((o1, o2)-> Integer.compare(o1.getValue(), o2.getValue()));
        sb.append(entries.get(k - 1).getKey()).append("\n");
    }

    public static void sum(int k){
        int res = 0;
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((o1, o2)-> Integer.compare(o1.getValue(), o2.getValue()));
        pq.addAll(table.entrySet());
        while(!pq.isEmpty()){
            int value = pq.poll().getValue();
            if(value <= k){
                res += value;
                continue;
            }
            break;
        }
        sb.append(res).append("\n");
    }
}