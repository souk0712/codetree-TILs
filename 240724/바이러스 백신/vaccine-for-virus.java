import java.io.*;
import java.util.*;

public class Main {
    static int N, M, total, ans;
    static int[][] map;
    static int[] dx = {-1,1,0,0};
    static int[] dy = {0,0,-1,1};
    static ArrayList<int[]> hospital;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        total = 0;
        ans = Integer.MAX_VALUE;
        hospital = new ArrayList<>();
        for(int i = 0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                if(map[i][j] == 2){
                    hospital.add(new int[]{i, j});
                }else if(map[i][j] == 0){
                    total++;
                }
            }
        }

        boolean[] visit = new boolean[hospital.size()];
        int[] pick = new int[M];
        comb(pick, visit, 0, 0);
        System.out.println(ans==Integer.MAX_VALUE?-1:ans);
    }

    static void comb(int[] pick, boolean[] visit, int start, int count){
        if(count == M){
            ans = Math.min(ans, bfs(pick, total));
            return;
        }

        for(int k = start; k < hospital.size(); k++){
            if(visit[k])continue;
            visit[k] = true;
            pick[count] = k;
            comb(pick, visit, k+1, count+1);
            visit[k] = false;
        }
    }

    static int bfs(int[] pick, int tot){
        boolean[][] visit = new boolean[N][N];
        Deque<int[]> dq = new ArrayDeque<>();
        for(int i = 0; i < pick.length; i++){
            int[] pos = hospital.get(pick[i]);
            visit[pos[0]][pos[1]] = true;
            dq.offer(new int[]{pos[0],pos[1], 0});
        }

        while(!dq.isEmpty()){
            int[] cur = dq.poll();
            if(map[cur[0]][cur[1]] == 0) tot--;
            if(tot == 0){ 
                return cur[2];
            }
            for(int k = 0; k < dx.length; k++){
                int mx = cur[0] + dx[k];
                int my = cur[1] + dy[k];
                if(mx < 0 || my < 0 || mx >= N || my >= N) continue;
                if(visit[mx][my] || map[mx][my] == 1) continue;
                visit[mx][my] = true;
                dq.offer(new int[]{mx, my, cur[2] + 1});
            }
        }
        return Integer.MAX_VALUE;
    }
}