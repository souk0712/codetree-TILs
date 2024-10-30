import java.io.*;
import java.util.*;

public class Main {

    static int Q, N, M;
    static ArrayList<Land>[] travelList;
    static Map<Integer, Product> products;
    static PriorityQueue<Product> pq;
    static int[] dist;
    static final int INF = 2_000_001;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        Q = Integer.parseInt(br.readLine());
        products = new HashMap<>();
        pq = new PriorityQueue<>();
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
                    Product p = new Product(id, revenue, dest, dist[dest]);
                    products.put(id, p);
                    pq.offer(p);
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
                    if(pq.isEmpty()){
                        sb.append(-1).append("\n");
                        break;
                    }
                    while(!pq.isEmpty()){
                        Product top = pq.poll();
                        if(products.containsKey(top.id)){
                            if(top.cost >= 0){
                                products.remove(top.id);
                                sb.append(top.id).append("\n");
                            }else{
                                sb.append(-1).append("\n");
                            }
                            break;
                        }
                    }
                    break;
                // (5) 여행 상품의 출발지 변경
                case 500:
                    id = Integer.parseInt(st.nextToken());
                    dijkstra(id);
                    for(Product pp : products.values()){
                        pp.setDistance(dist[pp.dest]);
                    }
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

    static class Product implements Comparable<Product> {
        int id, revenue, dest, distance, cost;

        Product(int id, int revenue, int dest, int distance){
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;
            this.distance = distance;
            this.cost = revenue - distance;
        }

        public void setDistance(int distance){
            this.distance = distance;
            this.cost = revenue - distance;
        }
        @Override
        public int compareTo(Product o){
            int c = Integer.compare(o.cost, cost);
            if(c == 0){
                return Integer.compare(id, o.id);
            }
            return c;
        }

        @Override
        public String toString(){
            return "id: " + id + ", revenue: " + revenue + ", dest: " + dest +", distance: "+ distance+", cost:" + cost;
        }
    }
}