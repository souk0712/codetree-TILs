import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

	static int N, M, P, C, D, outCount;
	static int rudolX, rudolY, rudolDir;
	static Map<Integer, Santa> map;
	static int[][] visit;
	static int[] rx = { -1, 0, 1, 0, -1, 1, 1, -1 };
	static int[] ry = { 0, 1, 0, -1, 1, 1, -1, -1 };
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		rudolX = Integer.parseInt(st.nextToken());
		rudolY = Integer.parseInt(st.nextToken());
		rudolDir = 0;
		map = new HashMap<>();
		visit = new int[N + 1][N + 1];
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int p = Integer.parseInt(st.nextToken());
			int sr = Integer.parseInt(st.nextToken());
			int sc = Integer.parseInt(st.nextToken());
			map.put(p, new Santa(p, sr, sc));
			visit[sr][sc] = p;
		}
		outCount = 0;
		for (int i = 0; i < M; i++) {
			moveRudol();
			moveSanta();
			if (outCount == P) {
				break;
			}
			addScore();
		}
		for (Santa s : map.values()) {
			sb.append(s.score).append(" ");
		}
		System.out.println(sb);
	}

	// 매 턴 이후 아직 탈락하지 않은 산타들에게는 1점씩을 추가로 부여
	private static void addScore() {
		for (Santa s : map.values()) {
			if (s.dead)
				continue;
			s.score += 1;
			if (s.faintCnt > 0) {
				s.faintCnt -= 1;
			}
		}
	}

	// 상호작용
	private static void throwOut(int dir, int index) {
		Queue<Santa> q = new LinkedList<>();
		q.offer(map.get(index));
		while (!q.isEmpty()) {
			// 밀려나는 산타
			Santa cur = q.poll();
			visit[cur.x][cur.y] = 0;
			int mx = cur.x + rx[dir];
			int my = cur.y + ry[dir];

			// 게임판 밖이면 탈락
			if (isOutRange(mx, my)) {
				cur.dead = true;
				continue;
			}

			cur.x = mx;
			cur.y = my;
			visit[cur.x][cur.y] = cur.index;

			// 그 옆에 산타가 있다면 연쇄적으로 1칸씩 밀려나는 것을 반복
			for (int i = 0; i < dx.length; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				if (isOutRange(nx, ny)) {
					continue;
				}
				if (visit[nx][ny] > 0) {
					q.offer(map.get(visit[nx][ny]));
				}
			}
		}

	}

	// 충돌
	private static void crash(int score, int dir, Santa s) {
		visit[s.x][s.y] = 0;
		s.score += score;
		int mx = s.x + rx[dir] * score;
		int my = s.y + ry[dir] * score;
		if (isOutRange(mx, my)) {
			s.dead = true;
			outCount++;
			return;
		}
		s.x = mx;
		s.y = my;
		if (visit[mx][my] > 0) {
			throwOut(dir, visit[mx][my]);
		}
		visit[s.x][s.y] = s.index;
		s.faintCnt = 2;
	}

	// 산타의 움직임
	private static void moveSanta() {
		for (Santa s : map.values()) {
			if (s.faintCnt > 0 || s.dead)
				continue;
			int min = getDist(rudolX, rudolY, s.x, s.y);
			int resX = s.x, resY = s.y;
			for (int k = 0; k < dx.length; k++) {
				int mx = s.x + dx[k];
				int my = s.y + dy[k];
				if (isOutRange(mx, my))
					continue;
				if (visit[mx][my] > 0)
					continue;
				int dist = getDist(rudolX, rudolY, mx, my);
				if (min > dist) {
					min = dist;
					resX = mx;
					resY = my;
					s.dir = k;
				}
			}
			visit[s.x][s.y] = 0;
			s.x = resX;
			s.y = resY;
			s.dist = min;
			visit[resX][resY] = s.index;

			if (rudolX == s.x && rudolY == s.y) {
				crash(D, (s.dir + 2) % dx.length, s);
			}
		}
	}

	// 루돌프의 움직임
	private static void moveRudol() {
		PriorityQueue<Santa> pq = new PriorityQueue<>();
		for (Santa s : map.values()) {
			if (s.dead)
				continue;
			int dist = getDist(rudolX, rudolY, s.x, s.y);
			s.dist = dist;
			pq.offer(s);
		}
		Santa distSanta = pq.poll();
		int min = Integer.MAX_VALUE;
		int resX = rudolX, resY = rudolY;
		for (int i = 0; i < rx.length; i++) {
			int mx = rudolX + rx[i];
			int my = rudolY + ry[i];
			if (isOutRange(mx, my))
				continue;
			int dist = getDist(mx, my, distSanta.x, distSanta.y);
			if (min > dist) {
				min = dist;
				resX = mx;
				resY = my;
				rudolDir = i;
			}
		}
		rudolX = resX;
		rudolY = resY;
		if (rudolX == distSanta.x && rudolY == distSanta.y) {
			crash(C, rudolDir, distSanta);
		}
	}

	private static boolean isOutRange(int x, int y) {
		return x <= 0 || y <= 0 || x > N || y > N;
	}

	private static int getDist(int x, int y, int mx, int my) {
		int dist = (x - mx) * (x - mx) + (y - my) * (y - my);
		return dist;
	}

	static class Santa implements Comparable<Santa> {
		int index, x, y, score, faintCnt, dist, dir;
		boolean dead;

		Santa(int index, int x, int y) {
			this.x = x;
			this.y = y;
			this.index = index;
			this.faintCnt = 0;
			this.score = 0;
			this.dir = Integer.MAX_VALUE;
			this.dead = false;
		}

		@Override
		public int compareTo(Santa o) {
			int com = Integer.compare(dist, o.dist);
			if (com == 0) {
				int xx = Integer.compare(o.x, x);
				if (xx == 0)
					return Integer.compare(o.y, y);
				else
					return xx;
			} else {
				return com;
			}
		}

		@Override
		public String toString() {
			return "Santa [index=" + index + ", x=" + x + ", y=" + y + ", score=" + score + ", faintCnt=" + faintCnt
					+ ", dist=" + dist + ", dir=" + dir + ", dead=" + dead + "]";
		}
	}
}