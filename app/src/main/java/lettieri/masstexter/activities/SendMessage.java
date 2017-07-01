package lettieri.masstexter.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import lettieri.masstexter.Contact;
import lettieri.masstexter.R;

public class SendMessage extends AppCompatActivity {
    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";
    // arbitrary number to determine which permission was granted
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 466;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 467;


    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayAdapter<Contact> adapter;

    private ListView lstContacts;
    private Button btnSend;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        findViews();

        // this will only be called if the user is on this screen and then manually goes in and revokes permission to the read contacts (because they granted it on the previous screen
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            addContactsByGroup();
        }
        setUp();
    }

    /***
     * Find the views that are associated with this layout
     */
    private void findViews() {
        lstContacts = (ListView)findViewById(R.id.lstContacts);
        btnSend = (Button)findViewById(R.id.btnSend);
        etMessage = (EditText)findViewById(R.id.etMessage);
    }

    /***
     * Setup the list adapter and click listeners
     */
    private void setUp() {
        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts);
        lstContacts.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    /***
     * Grab the text from teh edit text and
     * send the message to all contacts in the group
     * it then clears the edit text
     */
    private void sendMessage() {
        // this will only be called if the user is on this screen and then manually goes in and revokes permission to the read contacts (because they granted it on the previous screen
        if(ContextCompat.checkSelfPermission(SendMessage.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SendMessage.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            String message = etMessage.getText().toString();
            sendMessage(message);
            etMessage.getText().clear();
        }
    }

    /***
     * send the message to all contacts in the group
     * @param message is the message to send to everyone
     */
    private void sendMessage(String message) {
        for(Contact c: contacts) {
            c.sendMessage(message);
        }
    }

    /***
     * Uses the group id of the intent
     */
    private void addContactsByGroup() {
        addContactsByGroup(getIntent().getStringExtra(EXTRA_GROUP_ID));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addContactsByGroup();
                    adapter.notifyDataSetInvalidated();
                } else {
                    Toast.makeText(this, "This app requires contact permission, close the app and reopen to allow", Toast.LENGTH_LONG).show();
                }
                return;
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    Toast.makeText(this, "This app requires permission to send the message, try sending the message again and grant permission", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }
}
