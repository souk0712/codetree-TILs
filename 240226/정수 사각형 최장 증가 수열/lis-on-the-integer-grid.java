import java.io.*;
import java.util.*;

public class Main {
    
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int[][] map, dp;

    public static void main(String[] args) throws Exception{
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        dp = new int[N][N];
        for(int i = 0; i < N; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                dp[i][j] = 1;
            }
        }

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                for(int k = 0; k < dx.length; k++){
                    int mx = i + dx[k];
                    int my = j + dy[k];
                    if(mx < 0 || my < 0 || mx >= N || my >= N) continue;
                    if(map[mx][my] > map[i][j]){
                        dp[mx][my] = Math.max(dp[i][j] + 1, dp[mx][my]);
                    }
                }
                            
            }
        }
        
        int max = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                max = Math.max(dp[i][j], max);      
            }
        }
        
        System.out.println(max);
    }
}