public class Booking {
    private Guest guest;
    private Room room;
    private int numberOfNights;
    private double totalPrice;

    public Booking(Guest guest, Room room, int numberOfNights) {
        this.guest = guest;
        this.room = room;
        this.numberOfNights = numberOfNights;
        this.totalPrice = room.getPricePerNight() * numberOfNights;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void displayBookingInfo() {
        System.out.println("=== ИНФОРМАЦИЯ О БРОНИРОВАНИИ ===");
        System.out.println("Гость: " + guest.getFirstName() + " " + guest.getLastName());
        System.out.println("Номер комнаты: " + room.getRoomNumber());
        System.out.println("Тип номера: " + room.getRoomType());
        System.out.println("Количество ночей: " + numberOfNights);
        System.out.println("Общая стоимость: " + totalPrice + " тенге");
        System.out.println("================================");
    }
}