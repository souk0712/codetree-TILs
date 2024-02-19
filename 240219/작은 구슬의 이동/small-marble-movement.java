import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {-1,1,0,0};
    static int[] dy = {0,0,1,-1};

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int d = getDir(st.nextToken());

        for(int i = 0;i < t;i++){
            int mx = r+dx[d];
            int my = c+dy[d];
            if(mx<=0||my<=0||mx>n||my>n){
                if(d==0){
                    d=1;
                }else if(d==1){
                    d=0;
                }else if(d==2){
                    d=3;
                }else{
                    d=2;
                }
                continue;
            }
            r = mx;
            c = my;
        }
        System.out.println(r+" "+c);
    }

    static int getDir(String d){
        if(d.equals("U")){
            return 0;
        }else if(d.equals("D")){
            return 1;
        }else if(d.equals("R")){
            return 2;
        }else{
            return 3;
        }
    }
}