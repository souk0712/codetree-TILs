import java.io.*;
import java.util.*;

public class Main {

    static int Q, N, M;
    static ArrayList<Land>[] travelList;
    static Map<Integer, Product> products;
    static int[] dist;
    static final int INF = 2_000_001;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        Q = Integer.parseInt(br.readLine());
        products = new HashMap<>();
        for(int i = 0; i < Q; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int code = Integer.parseInt(st.nextToken());
            int id;
            switch(code){
                // (1) 코드트리 랜드 건설
                case 100:
                    N = Integer.parseInt(st.nextToken());
                    M = Integer.parseInt(st.nextToken());
                    travelList = new ArrayList[N];
                    for(int j = 0; j < N; j++)travelList[j] = new ArrayList<>();
                    for(int j = 0; j < M; j++){
                        int v = Integer.parseInt(st.nextToken());
                        int u = Integer.parseInt(st.nextToken());
                        int w = Integer.parseInt(st.nextToken());
                        if(v == u) continue;
                        travelList[v].add(new Land(u, w));
                        travelList[u].add(new Land(v, w));
                    }
                    dist = new int[N];
                    dijkstra(0);
                    break;
                // (2) 여행 상품 생성
                case 200:
                    id = Integer.parseInt(st.nextToken());
                    int revenue = Integer.parseInt(st.nextToken());
                    int dest = Integer.parseInt(st.nextToken());
                    products.put(id, new Product(id, revenue, dest));
                    break;
                // (3) 여행 상품 취소
                case 300:
                    id = Integer.parseInt(st.nextToken());
                    if(products.containsKey(id)){
                        products.remove(id);
                    }
                    break;
                // (4) 최적의 여행 상품 판매
                case 400:
                    PriorityQueue<Product> pq = new PriorityQueue<>((o1, o2) -> {
                        int c = Integer.compare(o2.revenue - dist[o2.dest], o1.revenue - dist[o1.dest]);
                        if(c == 0){
                            return Integer.compare(o1.id, o2.id);
                        }
                        return c;
                    });
                    for(Product p : products.values()){
                        pq.offer(p);
                    }
                    if(pq.isEmpty()){
                        sb.append(-1).append("\n");
                    }else{
                        Product top = pq.poll();
                        int cost = top.revenue - dist[top.dest];
                        if(cost >= 0){
                            products.remove(top.id);
                            sb.append(top.id).append("\n");
                        }else{
                            sb.append(-1).append("\n");
                        }
                    }
                    break;
                // (5) 여행 상품의 출발지 변경
                case 500:
                    id = Integer.parseInt(st.nextToken());
                    dijkstra(id);
                    break;
            }
        }
        System.out.println(sb);
    }

    static void dijkstra(int start){
        PriorityQueue<Land> pq = new PriorityQueue<>();
        Arrays.fill(dist, INF);
        dist[start] = 0;
        pq.offer(new Land(start, dist[start]));

        while(!pq.isEmpty()){
            Land cur = pq.poll();
            if(dist[cur.next] < cur.cost){
                continue;
            }
            
            for(int k = 0; k < travelList[cur.next].size(); k++){
                Land next = travelList[cur.next].get(k);
                if(dist[next.next] > dist[cur.next] + next.cost){
                    dist[next.next] = dist[cur.next] + next.cost;
                    pq.offer(new Land(next.next, dist[next.next]));
                }
            }
        }
    }

    static class Land implements Comparable<Land> {
        int next, cost;

        Land(int next, int cost){
            this.next = next;
            this.cost = cost;
        }
        @Override
        public int compareTo(Land o){
            return Integer.compare(cost, o.cost);
        }
    }

    static class Product {
        int id, revenue, dest;

        Product(int id, int revenue, int dest){
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;
        }
    }
}