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
//        ContactsContract.CommonDataKinds.Phone.NUMBER
        Cursor groupCursor = getContentResolver().query(
        ContactsContract.Data.CONTENT_URI,
        new String[]{ ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID},
        ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
        new String[] { getIntent().getStringExtra(EXTRA_GROUP_ID) }, null);

        if(groupCursor!=null){
            while(groupCursor.moveToNext()){

                String contactId = groupCursor.getString(0);
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
            groupCursor.close();
        }

        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts);
        lstContacts.setAdapter(adapter);
    }

    private void findViews() {
        lstContacts = (ListView)findViewById(R.id.lstContacts);
    }
}
