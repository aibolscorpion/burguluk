package kz.shymkent.relaxhouse.models;

import java.util.Comparator;

public class Client {
    public String key="";
    public String checkInDate="";
    public String checkOutDate="";
    public String quantity="";
    public String checkInTime="";
    public String checkOutTime="";
    public String avans="";
    public String debt="";
    public String phoneNumber="";
    public String comment="";
    public Client(){};
    public Client(String checkInDate,String checkOutDate, String quantity, String checkInTime, String checkOutTime, String avans, String debt, String phoneNumber,String comment){
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.quantity = quantity;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.avans = avans;
        this.debt = debt;
        this.phoneNumber = phoneNumber;
        this.comment = comment;
    }
    public static Client copy(Client client){
        Client newClient = new Client();
        newClient.checkInDate = client.checkInDate;
        newClient.checkOutDate = client.checkOutDate;
        newClient.checkInTime = client.checkInTime;
        newClient.checkOutTime = client.checkOutTime;
        newClient.avans = client.avans;
        newClient.debt = client.debt;
        newClient.quantity = client.quantity;
        newClient.comment = client.comment;
        newClient.phoneNumber = client.phoneNumber;
        return  newClient;
    }

    public static Comparator<Client> compareCheckInTime = (s1, s2) -> {
        String[] timeArray1 = s1.checkInTime.split(":");
        String[] timeArray2 = s2.checkInTime.split(":");
        int Id1 = Integer.parseInt(timeArray1[0]);
        int Id2 = Integer.parseInt(timeArray2[0]);

        /*For ascending order*/
        return Id1-Id2;

    };
    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
