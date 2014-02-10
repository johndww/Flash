package com.android.flash.sync;

/**
 * User: johnwright
 * Date: 2/1/14
 * Time: 5:39 PM
 */
public enum ResponseCode {
    SUCCESS,
    ERROR,
    ADDED_NEW_WORDS,
    PUT_NEW_WORDS,
    NO_WORDS;

    @Override
    public String toString() {
        String response = "";
        switch (this) {
            case SUCCESS:
                response = "Success";
                break;
            case ERROR:
                response = "Error";
                break;
            case ADDED_NEW_WORDS:
                response = "Received New Words";
                break;
            case PUT_NEW_WORDS:
                response = "Synced New Words";
                break;
            case NO_WORDS:
                response = "No Words";
                break;
            default:
                break;
        }
        return response;
    }
}
