public class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void bookRoom() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Номер " + roomNumber + " забронирован");
        } else {
            System.out.println("Номер " + roomNumber + " уже занят");
        }
    }

    public void releaseRoom() {
        isAvailable = true;
        System.out.println("Номер " + roomNumber + " освобождён");
    }

    public void displayInfo() {
        System.out.println("Номер: " + roomNumber);
        System.out.println("Тип: " + roomType);
        System.out.println("Цена за ночь: " + pricePerNight + " тенге");
        System.out.println("Статус: " + (isAvailable ? "Свободен" : "Занят"));
    }
}