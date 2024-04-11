import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {

	static int N, Q;
	static Node[] list;
	static BufferedReader br;
	static StringBuilder sb;

	public static void main(String[] args) throws Exception {
		init();
		System.out.println(sb);
	}

	// 알림을 받을 수 있는 채팅방 수 조회
	// 이때 c번 채팅방을 제외한 채팅방의 수를 세어야 함에 유의
	private static void printCount(int c) {
		Deque<Carry> dq = new ArrayDeque<>();
		dq.offer(new Carry(list[c], 0, false));
		int count = 0;
		while (!dq.isEmpty()) {
			Carry cur = dq.poll();
			if (cur.check) {
				count++;
			}

			for (int k = 0; k < cur.n.child.size(); k++) {
				int nextIndex = cur.n.child.get(k);
				Node next = list[nextIndex];
				if (!next.isActive)
					continue;
				if (next.power < cur.depth + 1) {
					dq.offer(new Carry(next, cur.depth + 1, false));
				} else {
					dq.offer(new Carry(next, cur.depth + 1, true));
				}
			}
		}
		sb.append(count).append("\n");
	}

	// 사내 메신저 준비
	private static void init() throws IOException {
		sb = new StringBuilder();
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		list = new Node[N + 1];
		for (int j = 0; j <= N; j++) {
			list[j] = new Node(j);
		}
		// input parents
		st = new StringTokenizer(br.readLine());
		int code = Integer.parseInt(st.nextToken());
		for (int j = 1; j <= N; j++) {
			int p = Integer.parseInt(st.nextToken());
			list[j].parent = p;
			list[p].child.add(j);
		}

		// input authority
		for (int j = 1; j <= N; j++) {
			list[j].power = Integer.parseInt(st.nextToken());
		}
		// 명령의 정보
		for (int i = 0; i < Q - 1; i++) {
			actCode();
		}
	}

	// 알림망 설정 ON / OFF
	private static void changeAlram(int c) {
		boolean before = list[c].isActive;
		list[c].isActive = !before;
	}

	// 부모 채팅방 교환
	// c1번 채팅방과 c2번 채팅방은 트리 내에서 같은 depth상에 있음을 가정
	private static void changeParent(int c1, int c2) {
		// c1과 c2의 부모의 child 바꿔주기
		int pc1 = list[c1].parent;
		int pc2 = list[c2].parent;
		list[pc1].child.remove(Integer.valueOf(c1));
		list[pc2].child.remove(Integer.valueOf(c2));
		list[pc1].child.add(c2);
		list[pc2].child.add(c1);

		// c1과 c2의 부모 바꿔주기
		int temp = list[c1].parent;
		list[c1].parent = list[c2].parent;
		list[c2].parent = temp;
	}

	// 코드 수행
	private static void actCode() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		int code = Integer.parseInt(st.nextToken());
		switch (code) {
		case 200:
			int c = Integer.parseInt(st.nextToken());
			changeAlram(c);
			break;
		case 300:
			c = Integer.parseInt(st.nextToken());
			int power = Integer.parseInt(st.nextToken());
			setPower(c, power);
			break;
		case 400:
			int c1 = Integer.parseInt(st.nextToken());
			int c2 = Integer.parseInt(st.nextToken());
			changeParent(c1, c2);
			break;
		case 500:
			c = Integer.parseInt(st.nextToken());
			printCount(c);
			break;
		}
	}

	// 권한 세기 변경
	private static void setPower(int c, int power) {
		list[c].power = power;
	}

	static class Node {
		int index, parent, power;
		boolean isActive;
		ArrayList<Integer> child;

		public Node(int index) {
			this.index = index;
			this.isActive = true;
			this.child = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "Node [index=" + index + ", parent=" + parent + ", power=" + power + ", isActive=" + isActive
					+ ", child=" + child + "]";
		}
	}

	static class Carry {
		int depth;
		Node n;
		boolean check;

		public Carry(Node n, int depth, boolean check) {
			this.depth = depth;
			this.n = n;
			this.check = check;
		}
	}
}