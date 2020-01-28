package kz.shymkent.relaxhouse;

public class Client {
    public String date;
    public String quantity;
    public String checkInTime;
    public String checkOutTime;
    public String avans;
    public String debt;
    public String phoneNumber;
    public String comment;
    public Client(){};
    public Client(String date, String quantity, String checkInTime, String checkOutTime, String avans, String debt, String phoneNumber,String comment){
        this.date = date;
        this.quantity = quantity;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.avans = avans;
        this.debt = debt;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getAvans() {
        return avans;
    }

    public void setAvans(String avans) {
        this.avans = avans;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setComment(String comment){this.comment = comment;}

    public String getComment(){return comment; }
}
