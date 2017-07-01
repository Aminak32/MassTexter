package lettieri.masstexter;

/**
 * Created by Luigi on 7/1/2017.
 */
public class Contact
{
    private String id;
    private String name;
    private String phoneNumber;

    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + phoneNumber;
    }
}
