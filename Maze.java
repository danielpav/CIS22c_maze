import java.util.Scanner;
import java.util.Random;
import java.io.*;

public class Maze {

    public int size;
    public Room[][] rooms; //array representing rooms
    public String[] graphicPath; //Represetns the shortes path found graphically

    public Random rand = new Random();

    public Maze() {
        int size = 0;
    }

    public Maze(int size) {
        this.size = size;
        rooms = new Room[size][size];
        int seq = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rooms[i][j] = new Room();
                rooms[i][j].rmNum = seq;
                seq++;
            }
        }
        rooms[0][0].buildWall(0, new Room());
        rooms[size - 1][size - 1].buildWall(1, new Room());
    }

    public Maze(String file) {
        try {
            Scanner scanner = new Scanner(new File(file));

            String input;
            size = scanner.nextInt();
            System.out.println(size);
            rooms = new Room[size][size];
            int seq = 0;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    rooms[x][y] = new Room();
                    rooms[x][y].rmNum = seq;
                    seq++;
                }
            }
            rooms[0][0].buildWall(0, new Room());
            rooms[size - 1][size - 1].buildWall(1, new Room());

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int a = 0; a < 4; a++) {
                        input = scanner.next();
                        int adj = chooseAdjacentRoom(rooms[i][j].rmNum, a);
                        int adjWall = getAdjacentWall(a);
                        if (input.equals("1") && isBorder(rooms[i][j].rmNum, a) == false) {
                            rooms[i][j].sides[a].wall = true;
                        } else if (input.equals("0") && isBorder(rooms[i][j].rmNum, a) == false) {
                            breakWall(rooms[i][j], a, rooms[adj / size][(adj + size) % size], adjWall);
                        }

                    }
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print(printMaze());
    }

    public void createMaze() {

        // type int array disjoint set representation of maze
        DisjointSet set = new DisjointSet(size * size);

        while (set.find(0) != set.find((size * size) - 1)) {
            // choose a random room and wall to be removed
            int randomRoom = rand.nextInt(size * size);
            int randomWall = chooseWall(randomRoom);

            // choose the adjacent room that is on the other side of randomWall
            // and chooses its corresponding wall to be removed.
            int adjRoom = chooseAdjacentRoom(randomRoom, randomWall);
            int adjWall = getAdjacentWall(randomWall);

            //if randomly chosen rooms are not in the same set then make a path between them
            if (set.find(randomRoom) != set.find(adjRoom)) {
                breakWall(rooms[randomRoom / size][(randomRoom + size) % size], randomWall,
                        rooms[adjRoom / size][(adjRoom + size) % size], adjWall);

                set.union(set.find(randomRoom), set.find(adjRoom));
            }
        }
        System.out.print(printMaze());
    }


    public void bfs() {
        String path = ""; //path traveresed by BFS
        String solPath = ""; //shortest path from goal to starting point
        int distance = 0;
        Queue q = new Queue(size * size);
        Room ptr;
        q.enqueue(rooms[0][0]);
        rooms[0][0].visited = true;
        rooms[0][0].distance = distance;

        while (!q.isEmpty()) {
            ptr = q.dequeue();
            path += ptr.rmNum + " ";
            if (ptr == rooms[size - 1][size - 1]) {
                System.out.println("Rooms visited by BFS: " + path);
                System.out.println(getShortestPath(rooms[size - 1][size - 1]));
                printShortestPath(graphicPath);
            }
            for (int i = 0; i < 4; i++) {//iterates through all 4 walls
                Room r = ptr.getRoom(i);
                if (r.wall == false && r.visited == false) {
                    q.enqueue(r);
                    r.visited = true;
                    r.distance = distance + 1;
                }
            }
            distance++;
        }
        if (rooms[size - 1][size - 1].visited == false) {
            System.out.println("No BFS solution was found for this maze");
        }

    }

    public void dfs() {
        String path = ""; //starting point will always be at room "0"
        String solPath = "";
        int distance = 0;
        Stack s = new Stack(size * size);
        Room temp;
        s.push(rooms[0][0]);
        rooms[0][0].visited = true;
        rooms[0][0].distance = distance;

        while (!s.isEmpty()) {
            temp = s.pop();
            path += temp.rmNum + " ";
            if (temp == rooms[size - 1][size - 1]) {
                System.out.println("Rooms visited by DFS: " + path);
                System.out.println(getShortestPath(rooms[size - 1][size - 1]));
                printShortestPath(graphicPath);
            }
            for (int i = 0; i < 4; i++) { //iterates through all 4 walls
                Room r = temp.getRoom(i);
                if (r.wall == false && r.visited == false) {
                    s.push(r);
                    r.visited = true;
                    r.distance = distance + 1;
                }
            }
            distance++;
        }
        if (rooms[size - 1][size - 1].visited == false) {
            System.out.println("No DFS solution was found for this maze");
        }
    }

    public String getShortestPath(Room room) {
        graphicPath = new String[size * size];
        graphicPath[size * size - 1] = "X ";
        int step = 0;
        int temp = room.distance;
        String sol = "This is the path (in reverse): " + room.rmNum + " ";
        while (room.distance != 0) {
            for (int i = 0; i < 4; i++) {
                Room r = room.getRoom(i);
                if (r.distance < temp && r.wall == false) {
                    temp = r.distance;
                    step = i;
                }
            }
            room = room.getRoom(step);
            graphicPath[room.rmNum] = "X ";
            sol += room.rmNum + " ";
        }

        return sol;
    }


    public int chooseAdjacentRoom(int room, int side) {
        int a = room;
        if (side == 0) {
            a = a - size;
        } else if (side == 1) {
            a = a + size;
        } else if (side == 2) {
            a = a + 1;
        } else if (side == 3) {
            a = a - 1;
        }
        return a;
    }

    private int chooseWall(int room) {
        boolean[] isBorder = new boolean[4];
        if (room < size) {
            isBorder[0] = true;
        }
        if ((room + size) >= (size * size)) {
            isBorder[1] = true;
        }
        if ((room + 1) % size == 0) {
            isBorder[2] = true;
        }
        if (room % size == 0) {
            isBorder[3] = true;
        }
        int wall;
        do {
            wall = rand.nextInt(4);
        } while (isBorder[wall] == true);
        return wall;
    }

    private int getAdjacentWall(int wall) {
        wall++;
        int adjWall;
        if (wall % 2 == 0) {
            adjWall = wall - 1;
        } else {
            adjWall = wall + 1;
        }
        return adjWall - 1;
    }

    public void breakWall(Room r1, int w1, Room r2, int w2) {
        r1.buildWall(w1, r2);
        r2.buildWall(w2, r1);
    }

    public void resetMaze() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rooms[i][j].visited = false;
            }
        }
    }

    public boolean isBorder(int room, int side) {
        boolean border = false;

        if ((room + 1) % size == 0 && side == 2) {
            border = true;
        }
        if (room % size == 0 && side == 3) {
            border = true;
        }
        if (room < size && side == 0) {
            border = true;
        }
        if ((room + size) >= (size * size) && side == 1) {
            border = true;
        }
        return border;
    }

    public void printShortestPath(String[] path) {
        for (int x = 0; x < size * size; x++) {
            if (x % size == 0 && x != 0) {
                System.out.print("\n");
            }
            if (path[x] != null) {
                System.out.print(path[x]);
            } else {
                System.out.print(" ");
            }
        }
        System.out.println("\n");
    }

    public String printMaze() {
        String s = "    ";
        for (int x = 1; x < size; x++) {
            s += "__ ";
        }
        s += "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j == 0) {
                    s += "|";
                }
                s += rooms[i][j].printRoom();
                if ((j + 1) % size == 0) {
                    s += "\n";
                }
            }
        }
        return s;
    }




    public static void main (String[] args) {
        Scanner input = new Scanner(System.in);

        Maze maze = new Maze();

        if(args.length != 0) {
            maze = new Maze(args[0]);
        } else {
            int size = 0;
            while (size <= 1) {
                System.out.println("Enter size of maze: ");
                size = input.nextInt();
                if (size <= 1) {
                    System.out.println("Error: size of maze must be bigger than 1\n");
                }
            }
            maze = new Maze(size);
            maze.createMaze();
        }

        maze.bfs();
        maze.resetMaze();
        maze.dfs();

    }
}

