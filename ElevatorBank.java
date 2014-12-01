import java.util.*;
public class ElevatorBank {
    private int numElev;
    private Elevator [] m;
    private PQueue [] sharedQ;
    private int floors [];

    public ElevatorBank(int numElev, int [] requestedFloors) {
        this.numElev=numElev;
        floors=requestedFloors;
        m=new Elevator[numElev];
        sharedQ=new PQueue[numElev];

        for(int i=0; i<numElev; i++) {
            sharedQ[i]=new PQueue();
            m[i] = new Elevator(sharedQ[i]);
            //for now set start floor at zero
            m[i].setCFloor(0);
        }
    }
    public void runSimulation() {
        
        System.out.println("in elevator bank");
        for(int i=0; i<numElev; i++) {
            new Thread(m[i]).start();
        }
        
        //produce
        for(int i=0; i<floors.length; i++) {
            int f = floors[i];
            try{ Thread.sleep(1000);} catch (Exception e) {}
            int elevNum = pickElevator(f,m);
            System.out.println("Picked: "+elevNum+" for floor: "+f);
            synchronized(sharedQ[elevNum]) {
               System.out.println("producer adding floor: "+f+" to elevator: "+elevNum);
               sharedQ[elevNum].add(f);
               sharedQ[elevNum].notify();
            }
        }
        System.out.println("Done in main thread");
    
    }
    public int pickElevator(int dest, Elevator [] m) {
        if(m.length==1) {
            return 0;
        }
        int elev=0;
        for(int i=1; i<m.length; i++) {
            elev=compareElev(dest,elev,i,m);
        }
        return elev;
    }
    public int compareElev(int dest, int elevA, int elevB, Elevator [] m) {
        Elevator a = m[elevA];
        Elevator b = m[elevB];
        int aDist = Math.abs(a.getCFloor()-dest);
        int bDist = Math.abs(b.getCFloor()-dest);

        if(aDist>bDist) {
            if(isMovingTowards(dest,b) || b.getStatus()==0) {
                return elevB;
            } else if (isMovingTowards(dest, a) || a.getStatus()==0) {
                return elevA;
            } else {
                //they are both moving away so B is closer so return B
                return elevB;
            }
        } else {
            //has to be aDist <= bDist, logic is the same
            if(isMovingTowards(dest,a) || a.getStatus()==0) {
                return elevA;
            } else if(isMovingTowards(dest,b) || b.getStatus()==0) {
                return elevB;
            } else {
                //return shorter distance one
                return elevA;
            }
        }
    }
    public boolean isMovingTowards(int dest, Elevator a) {
        if(a.getCFloor() > dest && a.status==2) {
            return true;
        } else if (a.getCFloor() > dest && a.getStatus()==1) {
            return false;
        } else if (a.getCFloor() < dest && a.getStatus()==2) {
            return true;
        } else if (a.getCFloor() < dest && a.getStatus()==1) {
            return true;
        } else {
            return false;
        }
    } 
    
}
