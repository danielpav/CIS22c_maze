public class Queue {


    private Room[] qList;
    private int size, first, last;
    private final int max;


    public Queue() {
        size = 0;
        first = 0;
        last = 0;
        max = 50; //default
        qList = new Room[max];

    }

    public Queue(int maxSize) {
        size = 0;
        first = 0;
        last = 0;
        max = maxSize;
        qList = new Room[max];
    }
    public boolean isEmpty() {
        if (size ==0)
            return true;
        else
            return false;
    }
    public void enqueue(Room x) {
        qList[last] = x;
        last++;
        if(last == max) {
            last = 0;
        }
        size++;
    }

    public Room dequeue() {
        Room item = qList[first];
        first++;
        if (first == max) {
            first = 0;
        }
        size--;
        return item;
    }



   /* public void printQueue() {
        for (int i = first+1; i < last+1; i++) {
            System.out.print(qList[i] + " ");
        }
        System.out.println();
    }
*/
}