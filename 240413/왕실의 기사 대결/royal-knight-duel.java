import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

// 6:45
public class Main {

	static int L, N, Q;
	static int[][] map, visit;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	static Knight[] arr;
	static Set<Integer> damages, knights;
	static int[] k;

	// 0이라면 빈칸
	// 1이라면 함정
	// 2라면 벽
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		map = new int[L][L];
		visit = new int[L][L];
		arr = new Knight[N + 1];
		k = new int[N + 1];

		// L×L 크기의 체스판에 대한 정보
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		// N 개의 줄에 걸쳐서 초기 기사들의 정보
		// (r,c,h,w,k) 순
		// 기사의 처음 위치는 (r,c)를 좌측 상단 꼭지점
		// 세로 길이가 h, 가로 길이가 w인 직사각형 형태
		// 초기 체력이 k
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken()) - 1;
			int c = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int kk = Integer.parseInt(st.nextToken());
			arr[i] = new Knight(i, r, c, h, w, kk);
			k[i] = kk;

		}
		// Q 개의 줄에 걸쳐 왕의 명령에 대한 정보
		// (i,d) 형태
		// i번 기사에게 방향 d로 한 칸 이동하라는 명령
		// 이미 사라진 기사 번호가 주어질 수도 있음에 유의
		for (int i = 0; i < Q; i++) {
			damages = new HashSet<>();
			knights = new HashSet<>();
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			if (arr[num].dead)
				continue;
			setVisit();
			moveKnight(num, num, d);
			for (int s : knights) {
				Knight knight = arr[s];
				knight.x += dx[d];
				knight.y += dy[d];
			}
			for (int s : damages) {
				fightDamage(arr[s]);
			}
		}
		setVisit();
		printAnswer();
	}

	static void setVisit() {
		visit = new int[L][L];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i].dead)
				continue;
			setKnightVisit(arr[i]);
		}
	}

	static void setKnightVisit(Knight num) {
		for (int j = num.x; j < num.x + num.h; j++) {
			for (int l = num.y; l < num.y + num.w; l++) {
				visit[j][l] = num.index;
			}
		}
	}

	// Q 개의 명령이 진행된 이후, 생존한 기사들이 총 받은 대미지의 합을 출력
	private static void printAnswer() {
		int sum = 0;
		for (int i = 1; i <= N; i++) {
			if (arr[i].dead)
				continue;
			sum += k[i] - arr[i].k;
		}
		System.out.println(sum);
	}

	// (2) 대결 대미지
	// 명령을 받은 기사가 다른 기사를 밀치게 되면, 밀려난 기사들은 피해를 입게 됩니다.
	// 각 기사들은 해당 기사가 이동한 곳에서 w×h 직사각형 내에 놓여 있는 함정의 수만큼만 피해를 입게 됩니다.
	// 명령을 받은 기사는 피해를 입지 않으며, 기사들은 모두 밀린 이후에 대미지를 입게 됩
	//
	private static void fightDamage(Knight knight) {
		int count = 0;
		for (int j = knight.x; j < knight.x + knight.h; j++) {
			for (int l = knight.y; l < knight.y + knight.w; l++) {
				if (map[j][l] == 1) {
					count++;
				}
			}
		}
		knight.k -= count;
		if (knight.k <= 0) {
			knight.dead = true;
		}
	}

	// (1) 기사 이동
	// 상하좌우 중 하나로 한 칸 이동
	// 만약 이동하려는 위치에 다른 기사가 있다면 그 기사도 함께 연쇄적으로 한 칸 밀려나게 됨
	// 그 옆에 또 기사가 있다면 연쇄적으로 한 칸씩 밀리게 됩
	// 만약 기사가 이동하려는 방향의 끝에 벽이 있다면 모든 기사는 이동할 수 없게 됩니다.

	private static boolean moveKnight(int origin, int num, int d) {
		Knight knight = arr[num];
		Queue<int[]> q = new LinkedList<>();
		// 명형이 위 또는 아래인 경우
		if (d == 0 || d == 2) {
			for (int l = knight.y; l < knight.y + knight.w; l++) {
				q.offer(new int[] { knight.x, l, knight.index });
			}
		} else {
			for (int j = knight.x; j < knight.x + knight.h; j++) {
				q.offer(new int[] { j, knight.y, knight.index });
			}
		}

		boolean check = true;
		while (!q.isEmpty()) {
			int[] cur = q.poll();
			int mx = cur[0] + dx[d];
			int my = cur[1] + dy[d];
			if (isRangeOut(mx, my)) {
				check = false;
				break;
			}
			// 벽일 경우
			if (map[mx][my] == 2) {
				check = false;
				break;
			}
			// 자신의 영역일 경우
			if (visit[mx][my] == cur[2]) {
				q.offer(new int[] { mx, my, cur[2] });
			}
			// 빈공간일 경우
			else if (visit[mx][my] == 0) {
				continue;
			}
			// 다른 기사 영역일 경우
			else {
				if (!moveKnight(origin, visit[mx][my], d)) {
					check = false;
					break;
				}
			}
		}

		if (check) {
			knights.add(knight.index);
			if (knight.index != origin) {
				damages.add(knight.index);
			}
		}
		return check;
	}

	static boolean isRangeOut(int x, int y) {
		return x < 0 || y < 0 || x >= L || y >= L;
	}

	static class Knight {
		int index, x, y, h, w, k;
		boolean dead;

		public Knight(int index, int x, int y, int h, int w, int k) {
			this.index = index;
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.k = k;
			this.dead = false;
		}

		@Override
		public String toString() {
			return "Knight [index=" + index + ", x=" + x + ", y=" + y + ", h=" + h + ", w=" + w + ", k=" + k + ", dead="
					+ dead + "]";
		}
	}

}