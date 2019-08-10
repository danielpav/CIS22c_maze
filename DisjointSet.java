

public class DisjointSet {

    int set[];
    int size;

    public DisjointSet(int size) {
        this.size = size;
        set = new int[size];
        for (int i = 0; i < size; i++) {
            set[i] = -1;
        }
    }

    public void union(int root1, int root2) {
        if (set[root2] < set[root1]) {
            set[root2] += set[root1];
            set[root1] = root2;
        } else {
            set[root1] += set[root2];
            set[root2] = root1;
        }
    }

    public int find(int x) {
        if (set[x] < 0) {
            return x;
        } else {
            set[x] = find(set[x]);
            return set[x];
        }
    }
}
