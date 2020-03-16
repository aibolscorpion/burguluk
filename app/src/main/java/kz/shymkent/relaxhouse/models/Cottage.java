package kz.shymkent.relaxhouse.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Cottage {
    String name;
    ArrayList<String> phoneNumbers;
//

    public String getName() {
        return name;

    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }
    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
