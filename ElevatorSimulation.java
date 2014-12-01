public class ElevatorSimulation {
    public static void main(String [] args) {
        int floors [] = {3,5,11,7,8};
        ElevatorBank b = new ElevatorBank(3,floors);
        b.runSimulation();
    }
}
