public class Stack{

    private final int size;

    private int top;
        private int items;

    private Room[] stackList;

    /*public Stack() {
        size = 50;
        top = 0;
        items = 0;
        stList = new Room[size];
    }
*/
    public Stack(int size) {
        this.size = size;
        top = 0;
        items = 0;
        stackList = new Room[size];
    }

    public void push (Room room) {
        if (top == size - 1){
            System.out.println("Full");
        }
        stackList[top] = room;
        top++;
        items++;
    }

    public Room pop() {
        if (top == 0) {
            System.out.println("Empty");
        }
        Room room = stackList[top - 1];
        top--;
        items--;
        return room;
    }

    public boolean isEmpty() {
        if(top == 0) {
            return true;
        }
        else
        return false;
    }

}