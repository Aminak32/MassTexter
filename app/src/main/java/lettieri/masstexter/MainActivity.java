package lettieri.masstexter;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SmsManager.getDefault().sendTextMessage("5555555555", null, "Hello world", null, null);

//        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//        while (phones.moveToNext())
//        {
//            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Log.v("TOMTOM", name + " - " + phoneNumber);
//        }
//        phones.close();

//        Cursor groups_cursor= getContentResolver().query(
//                ContactsContract.Groups.CONTENT_URI,
//                new String[]{
//                        ContactsContract.Groups._ID,
//                        ContactsContract.Groups.TITLE
//                }, null, null, null
//        );
//
//        if(groups_cursor!=null){
//            while(groups_cursor.moveToNext()){
//                String group_title = groups_cursor.getString(1);
//                String id = groups_cursor.getString(0);
//                // groups.put(id, group_title);
//            Log.v("TOMTOM", group_title + " - " + id);
//
//            }
//        }

        Cursor groupCursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID },
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
                new String[] { String.valueOf(24) }, null);

        if(groupCursor!=null){
            while(groupCursor.moveToNext()){
                String group_title = groupCursor.getString(0);
                String id = groupCursor.getString(1);
                // groups.put(id, group_title);
            Log.v("TOMTOM", group_title + " - " + id);

            }
        }

    }
}
