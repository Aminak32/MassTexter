package lettieri.masstexter.activities;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import lettieri.masstexter.Contact;
import lettieri.masstexter.Group;
import lettieri.masstexter.R;

public class SendMessage extends AppCompatActivity {
    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ListView lstContacts;
    private ArrayAdapter<Contact> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        findViews();
        addContactsByGroup(getIntent().getStringExtra(EXTRA_GROUP_ID));
        setUp();
    }

    /***
     * Find the views that are associated with this layout
     */
    private void findViews() {
        lstContacts = (ListView)findViewById(R.id.lstContacts);
    }

    /***
     * Setup the list adapter
     */
    private void setUp() {
        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts);
        lstContacts.setAdapter(adapter);
    }

    /***
     * Given a group id it will query for the contact ids in that group
     * From that result it will query for users by id and add them to the objects contacts variable
     * @param groupId is the id of the group to add the users by
     */
    private void addContactsByGroup(String groupId) {
        Cursor contactInGroupCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID},
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                new String[] {groupId}, null);

        if(contactInGroupCursor!=null){
            while(contactInGroupCursor.moveToNext()){
                String contactId = contactInGroupCursor.getString(0);
                addContactsById(contactId);
            }
            contactInGroupCursor.close();
        }
    }

    /***
     * Adds all the contacts associated with the given contact id to the objects contacts array
     * @param contactId is the id of thed contact
     */
    private void addContactsById(String contactId) {
        Cursor contactCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { contactId }, null);

        if(contactCursor != null) {
            while(contactCursor.moveToNext()) {
                contacts.add(new Contact(contactId, contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),  contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
            }
            contactCursor.close();
        }
    }
}
