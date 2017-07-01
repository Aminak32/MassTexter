package lettieri.masstexter;

import android.telephony.SmsManager;

/**
 * Created by Luigi on 7/1/2017.
 */
public class Contact
{
    private String id;
    private String name;
    private String phoneNumber;

    /***
     * Invokes setters
     * @param id
     * @param name
     * @param phoneNumber
     */
    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    /***
     *
     * @return the contact id
     */
    public String getId() {
        return id;
    }

    /***
     *
     * @return the contact name
     */
    public String getName() {
        return name;
    }

    /***
     *
     * @return the contacts phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /***
     * Sends a text to this objects phone number with the given message
     * @param message
     */
    public boolean sendMessage(String message) {
        if(message != null && !message.isEmpty()) {
            SmsManager.getDefault().sendTextMessage(getPhoneNumber(), null, message, null, null);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumber + ")";
    }
}
