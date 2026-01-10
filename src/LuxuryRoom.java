public class LuxuryRoom extends Room {
    private boolean hasJacuzzi;
    private boolean hasBalcony;
    private boolean hasBreakfast;

    public LuxuryRoom(int roomNumber, double pricePerNight, boolean hasJacuzzi, boolean hasBalcony, boolean hasBreakfast) {
        super(roomNumber, "Люкс", pricePerNight);
        this.hasJacuzzi = hasJacuzzi;
        this.hasBalcony = hasBalcony;
        this.hasBreakfast = hasBreakfast;
    }

    public boolean hasJacuzzi() {
        return hasJacuzzi;
    }

    public boolean hasBalcony() {
        return hasBalcony;
    }

    public boolean hasBreakfast() {
        return hasBreakfast;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Джакузи: " + (hasJacuzzi ? "Да" : "Нет"));
        System.out.println("Балкон: " + (hasBalcony ? "Да" : "Нет"));
        System.out.println("Завтрак включен: " + (hasBreakfast ? "Да" : "Нет"));
    }
}