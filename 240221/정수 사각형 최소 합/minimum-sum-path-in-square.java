import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {0, 1};
    static int[] dy = {-1, 0};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[][] map = new int[N][N];
        int[][] dp = new int[N][N];
        for(int i = 0;i < N; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
                dp[i][j] = 1_000_001;
            }
        }

        dp[0][N-1] = map[0][N-1];

        for(int i = 0; i < N; i++){
            for(int j = N-1; j >= 0; j--){
                for(int k = 0;k<dx.length;k++){
                    int mx = i + dx[k];
                    int my = j + dy[k];
                    if(mx<0||my<0||mx>=N||my>=N)continue;
                    dp[mx][my] = Math.min(dp[i][j] + map[mx][my], dp[mx][my]);
                }
            }
        }

        System.out.println(dp[N-1][0]);
    }
}