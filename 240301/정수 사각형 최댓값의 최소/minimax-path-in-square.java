import java.io.*;
import java.util.*;

public class Main {
    static int[] dx= {1,0};
    static int[] dy= {0,1};
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[][] map = new int[n][n];
        int[][] dp = new int[n][n];
        for(int i = 0; i < n; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                dp[i][j] = Integer.MAX_VALUE;
            }
        }

        dp[0][0] = map[0][0];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 0;k<dx.length;k++){
                    int mx = i+dx[k];
                    int my = j+dy[k];
                    if(mx < 0 || my < 0 || mx >= n || my >= n)continue;
                    dp[mx][my] = Math.min(Math.max(dp[i][j], map[mx][my]), dp[mx][my]);
                }
            }
        }
        
        System.out.println(dp[n-1][n-1]);
    }
}