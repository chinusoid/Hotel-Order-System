public class Guest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public Guest(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void displayInfo() {
        System.out.println("Гость: " + firstName + " " + lastName);
        System.out.println("Телефон: " + phoneNumber);
        System.out.println("Email: " + email);
    }
}