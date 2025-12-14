public class Room {
    // Атрибуты
    private int roomNumber;      // номер комнаты
    private String roomType;     // тип (одноместный, двухместный, люкс)
    private double pricePerNight; // цена за ночь
    private boolean isAvailable; // свободен ли номер

    // Конструктор
    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true; // по умолчанию номер свободен
    }

    // Геттеры
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

    // Сеттеры
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // Методы для бронирования и освобождения номера
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

    // Метод для вывода информации о номере
    public void displayInfo() {
        System.out.println("Номер: " + roomNumber);
        System.out.println("Тип: " + roomType);
        System.out.println("Цена за ночь: " + pricePerNight + " тенге");
        System.out.println("Статус: " + (isAvailable ? "Свободен" : "Занят"));
    }
}