public class Elevator implements Runnable {
    public int cFloor;
    //0-not moving
    //1-moving up
    //2-moving down
    //3-loading/unloading
    public int status;
    public final PQueue floorQ;

    public Elevator (PQueue q) {
        cFloor=0;
        status=0;
        floorQ = q;
    }
    public String toString() {
        String out = "Elevator["+Thread.currentThread().getName()+"]";
        out += " Floor: "+cFloor+" ";
        switch (status) {
            case 0:
                out+="Not moving";
                break;
            case 1:
                out+="Moving up";
                break;
            case 2:
                out+="Moving down";
                break;
            case 3:
                out +="Loading";
                break;
        }
        out += " Queue size: "+floorQ.size();
        return out;
    }
    public void run() {
        while (true) {
            int dest=-1;
            while(floorQ.size()==0) {
                this.status=0;
                synchronized (floorQ) {
                    System.out.println("waiting for request: "+Thread.currentThread().getName());
                    try {
                        floorQ.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            synchronized(floorQ) {
                dest = floorQ.poll();
            }
            if(dest>cFloor) {
                //move up
                this.status=1;
            } else if (dest<cFloor) {
                //move down
                this.status=2;
            } else {
                this.status=3;
                //TODO: process requests
                continue;
            }
            //TODO:order queue here[don't need to order since ordering won't help anyway...]
            
            while(dest!=cFloor) {
                try {Thread.sleep(1000);} catch (Exception e) {}
                if(status==1) {
                    moveUp();   
                } else if (status==2) {
                    moveDown();
                }
                System.out.println(this.toString());
                boolean inQueue = false;
                synchronized(floorQ) {
                    inQueue = floorQ.removeIfMember(cFloor);
                }
                if (inQueue) {
                    int prevStatus=status;
                    status=3;
                    System.out.println(this.toString());
                    //simulate loading
                    try {Thread.sleep(1000);} catch (Exception e) {}
                    //TODO: process requests
                    status=prevStatus;
                }
            }
            status=3;
            System.out.println("elevator "+Thread.currentThread().getName()+" ARRIVED on cFloor"+cFloor+" dest:"+dest);
        }
    }
    public void setStatus(int s) {
        status=s;
    }
    public int getStatus() {
        return status;
    }
    public int getCFloor() {
        return cFloor;
    }
    public void setCFloor(int cf) {
        cFloor=cf;
    }
    public void moveUp() {
        cFloor++;
    }
    public void moveDown() {
        cFloor--;
    }
    public int getNextFloor() {
        return floorQ.poll();
    }
    public void addFloor(int f) {
        floorQ.offer(f);
    }
}