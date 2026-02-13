public class Room {
    protected int id;
    protected int roomNumber;
    protected String roomType;
    protected double pricePerNight;
    protected boolean isAvailable;
    protected int capacity;
    protected Integer currentGuestId;
    protected String checkInDate;
    protected String checkOutDate;
    protected int numberOfGuests;

    public Room() {}

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
        this.capacity = 2;
        this.numberOfGuests = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public Integer getCurrentGuestId() { return currentGuestId; }
    public void setCurrentGuestId(Integer currentGuestId) { this.currentGuestId = currentGuestId; }

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

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
        currentGuestId = null;
        checkInDate = null;
        checkOutDate = null;
        numberOfGuests = 0;
        System.out.println("Номер " + roomNumber + " освобождён");
    }

    public void displayInfo() {
        System.out.println("Номер: " + roomNumber);
        System.out.println("Тип: " + roomType);
        System.out.println("Цена за ночь: " + pricePerNight + " тенге");
        System.out.println("Вместимость: " + capacity + " чел.");
        System.out.println("Статус: " + (isAvailable ? "Свободен" : "Занят"));
        if (!isAvailable && checkInDate != null) {
            System.out.println("Заезд: " + checkInDate);
            System.out.println("Выезд: " + checkOutDate);
            System.out.println("Гостей: " + numberOfGuests);
        }
    }
}