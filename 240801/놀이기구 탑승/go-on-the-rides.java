import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static int[][] map;
    static Map<Integer, ArrayList<Integer>> likes;
    static Queue<Integer> order;
    static int[] dx = {-1, 0, 0, 1};
    static int[] dy = {0, -1, 1, 0};
    static int[] score = {0,1,10,100,1000};
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        order = new LinkedList<>();
        likes = new HashMap<>();

        for(int i = 0; i < N * N; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int next = Integer.parseInt(st.nextToken());
            order.offer(next);
            ArrayList<Integer> temp = new ArrayList<>();
            for(int k = 0; k < 4; k++){
                int p = Integer.parseInt(st.nextToken());
                temp.add(p);
            }
            likes.put(next, temp);
        }

        ride();
        int ans = calculateScore();
        System.out.println(ans);
    }

    static void ride(){
        while(!order.isEmpty()){
            int cur = order.poll();
            ArrayList<Integer> like = likes.get(cur);
            PriorityQueue<Pos> pq = new PriorityQueue<>();
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(map[i][j] != 0) continue;
                    pq.offer(new Pos(cur, i, j, countEmpty(i, j), countLikes(i,j,like)));
                }
            }
            Pos p = pq.poll();
            map[p.x][p.y] = p.num;
        }
    }

    static int calculateScore(){
        int sum = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                int student = map[i][j];
                ArrayList<Integer> like = likes.get(student);
                int count = countLikes(i, j, like);
                sum += score[count];
            }
        }
        return sum;
    }

    static boolean checkRange(int x, int y){
        return x >= 0 && y >= 0 && x < N && y < N;
    }

    static int countLikes(int i, int j, ArrayList<Integer> like){
        int count = 0;
        for(int k = 0; k < dx.length; k++){
            int mx = i + dx[k];
            int my = j + dy[k];
            if(checkRange(mx, my) && like.contains(map[mx][my])){
                count++;
            }
        }
        return count;
    }

    static int countEmpty(int i, int j){
        int count = 0;
        for(int k = 0; k < dx.length; k++){
            int mx = i + dx[k];
            int my = j + dy[k];
            if(checkRange(mx, my) && map[mx][my] == 0){
                count++;
            }
        }
        return count;
    }

    static class Pos implements Comparable<Pos>{
        int num, x, y, empty, count;

        Pos(int num, int x, int y, int empty, int count){
            this.num = num;
            this.x = x;
            this.y = y;
            this.empty = empty;
            this.count = count;
        }

        @Override
        public int compareTo(Pos o){
            int c = Integer.compare(o.count, count);
            if(c==0){
                int cc = Integer.compare(o.empty, empty);
                if(cc == 0){
                    int ccc = Integer.compare(x, o.x);
                    if(ccc == 0){
                        return Integer.compare(y, o.y);
                    }else{
                        return ccc;
                    }
                }else{
                    return cc;
                }
            }else{
                return c;
            }
        }
    }
}