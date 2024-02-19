import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {-1,1,0,0};
    static int[] dy = {0,0,-1,1};
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[][] map = new int[N][N];
        for(int i = 0;i<N;i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j = 0;j<N;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int ans = 0;
        for(int i = 0;i<N;i++){
            for(int j = 0;j<N;j++){
                int m = map[i][j];
                int count = 0;
                for(int k = 0;k<dx.length;k++){
                    int mx = i+dx[k];
                    int my = j+dy[k];
                    if(mx<0||my<0||mx>=N||my>=N)continue;
                    if(map[mx][my] == 1){
                        count++;
                    }
                }
                if(count>=3){
                    ans++;
                }
            }
        }
        System.out.println(ans);
    }
}