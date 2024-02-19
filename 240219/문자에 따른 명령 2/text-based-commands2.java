import java.io.*;
import java.util.*;

public class Main {

    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();
        int x = 0;
        int y = 0;
        int dir = 0;
        // 명령 L이 주어지면 왼쪽으로 90도 방향 전환
        // 명령 R이 주어지면 오른쪽으로 90도 방향전환
        // 명령 F가 주어지면 바라보고 있는 방향으로 한칸 이동
        for(int i = 0;i<input.length();i++){
            char cur = input.charAt(i);
            if(cur=='L'){
                dir = (3 - dir) % 4;
            }else if(cur=='R'){
                dir = (dir+1)%4;
            }else if(cur=='F'){
                x +=dx[dir];
                y +=dy[dir];
            }
        }
        System.out.println(y+" "+x);
    }
}