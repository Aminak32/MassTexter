package lettieri.masstexter.models;

/**
 * Created by Luigi on 7/1/2017.
 */
public class Group
{

    private String id;
    private String name;

    /***
     * Invokes setters
     * @param id
     * @param name
     */
    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /***
     *
     * @return the groups id
     */
    public String getId() {
        return id;
    }

    /***
     *
     * @return the groups name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
