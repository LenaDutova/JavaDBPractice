package org.example.dto.exceptions;

public class NullOrEmptyException
        extends IllegalArgumentException {

    public NullOrEmptyException(Object obj, String arg) {
        super(obj.getClass().getSimpleName() + ": Empty string as value of " + arg);
    }

    public static boolean checkString (String text){
        if (text != null && !text.isBlank()) return true;
        else return false;
    }
}
