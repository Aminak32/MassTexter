package lettieri.masstexter;

/**
 * Created by Luigi on 7/1/2017.
 */
public class Group
{

    private String id;
    private String name;

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
