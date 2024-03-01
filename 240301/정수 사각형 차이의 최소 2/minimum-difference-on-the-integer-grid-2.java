import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {1, 0};
    static int[] dy = {0, 1};
    static int n;
    static int[][] map;
    static int[][][] dp;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        map = new int[100][100];
        dp = new int[100][100][101];
        for(int i = 0; i < n; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0; j < n; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int k = 1; k <= 100; k++){
                    dp[i][j][k] = Integer.MAX_VALUE;
                }
            }
        }

        dp[0][0][map[0][0]] = map[0][0];
        
        for(int i = 1; i < n; i++){
            for(int k = 1; k <= 100; k++){
                dp[i][0][Math.min(k, map[i][0])] = Math.min(
                    dp[i][0][Math.min(k, map[i][0])], 
                    Math.max(dp[i - 1][0][k], map[i][0]));
            }
        }

        for(int i = 1; i < n; i++){
            for(int k = 1; k <= 100; k++){
                dp[0][i][Math.min(k, map[0][i])] = Math.min(
                    dp[0][i][Math.min(k, map[0][i])], 
                    Math.max(dp[0][i - 1][k], map[0][i]));
            }
        }

        for(int i = 1; i < n; i++){
            for(int j = 1; j < n; j++){
                for(int k = 1; k <= 100; k++){
                    dp[i][j][Math.min(k, map[i][j])] = Math.min(
                        dp[i][j][Math.min(k, map[i][j])], 
                        Math.max(Math.min(dp[i - 1][j][k], dp[i][j - 1][k]), map[i][j]));
                }
            }
        }

        int ans = Integer.MAX_VALUE;
        for(int k = 1;k<=100;k++){
            if(dp[n-1][n-1][k] != Integer.MAX_VALUE){
                ans = Math.min(ans, dp[n-1][n-1][k]-k);
            }
        }
        System.out.println(ans);
    }
}