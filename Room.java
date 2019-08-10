public class Room {

    boolean visited, wall;
    Room sides[];
    int rmNum;
    int distance = Integer.MAX_VALUE;

    Room() {
        sides = new Room[4];
        for (int i = 0; i < 4; i++) {
            sides[i] = new Room(true);
        }
        visited = false;
    }
    public void buildWall(int a, Room b) {
        sides[a] = b;// set wall a as room b
    }

    public Room getRoom(int side) {
        return sides[side];
    }
    Room(boolean wall) {
        this.wall = wall;
        visited = false;
    }


    /*Room (int wall) {
        if (wall == 1) {
            this.wall = true;
        } else if (wall == 0) {
            this.wall = false;
        }
    }
*/





    //public boolean isWall() {
        //return wall;
   // }

    public String printRoom() {
        String s = "";
        if (sides[1].wall != false) {
            s += "__";
        } else {
            s += " ";
        }
        if (sides[2].wall != false) {
            s += "|";
        } else {
            s += " ";
        }
        return s;
    }
}


