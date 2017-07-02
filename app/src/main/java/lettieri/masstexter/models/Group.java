package lettieri.masstexter.models;

import java.util.ArrayList;

/**
 * Created by Luigi on 7/1/2017.
 */
public class Group
{

    private ArrayList<String> ids = new ArrayList<>();
    private String name;

    /***
     * Invokes setters
     * @param name
     */
    public Group( String name) {
        this.name = name;
    }

    /***
     *
     * @return a deep copy of the list of ids
     */
    public ArrayList<String> getIds() {
        return new ArrayList<>(ids);
    }

    /***
     *
     * @return the groups name
     */
    public String getName() {
        return name;
    }

    /***
     *
     * @param id is the id to add to the list
     */
    public void addId(String id) {
        ids.add(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
