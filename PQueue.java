import java.util.*;
public class PQueue {
    private Queue<Integer> q;
    private int size;
    
    public PQueue() {
        size=0;
        q=new LinkedList<Integer>();
    }
    public boolean add(int a) {
        this.size+=1;
        return q.add(a);
    }
    public boolean offer(int a) {
        this.size+=1;
        return q.offer(a);
    }
    public Integer peek() {
        return q.peek();
    }
    public int poll() {
        this.size-=1;
        return q.poll();
    }
    public int size() {
        return size;
    }
    public boolean removeIfMember(int n) {
        Queue<Integer> temp = new LinkedList<Integer>();
        boolean isMember=false;
        while(q.peek() != null) {
            int t = q.poll();
            if(t==n) {
                this.size-=1;
                isMember=true;
            } else {
                temp.add(t);
            }
        }
        q=temp;
        return isMember;
    }
}
