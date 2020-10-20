package com.siamin.fivestart.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

    public  ValidationHelper(){

    }


    public boolean validationPhoneNumber(String PhoneNumber){
        if(PhoneNumber.length()==11){
            return android.util.Patterns.PHONE.matcher(PhoneNumber).matches();
        }else{
            return false;
        }

    }

    public boolean validationPassword(String Password){
        if(Password.length() > 5){
            return true;
        }else {
            return false;
        }
    }

    public boolean validationPinCode(String pincode){
        if(pincode.length() == 4){
            return true;
        }else {
            return false;
        }
    }

    public boolean validationModelSystemName(String Model) {
        if (!Model.equals("selected system model") && !Model.isEmpty() && !Model.equals("faSelected")) {
            return true;
        } else {
            return false;
        }
    }


    public boolean validateEnglish(String name) {
        Pattern RTL_CHARACTERS =
                Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = RTL_CHARACTERS.matcher(name);
        if (matcher.matches()) {
            return true;  // it's RTL
        } else
            return false;
    }


}
