public class StandardRoom extends Room {
    private int numberOfBeds;

    public StandardRoom(int roomNumber, double pricePerNight, int numberOfBeds) {
        super(roomNumber, "Стандартный", pricePerNight);
        this.numberOfBeds = numberOfBeds;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Количество кроватей: " + numberOfBeds);
    }
}