package lettieri.masstexter;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Group> groups = new ArrayList<>();
    private ListView lstGroups;
    private ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
//        SmsManager.getDefault().sendTextMessage("5555555555", null, "Hello world", null, null);

        Cursor groupCursor = getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI,
                new String[]{
                        ContactsContract.Groups._ID,
                        ContactsContract.Groups.TITLE
                }, null, null, null
        );

        if(groupCursor!=null){
            while(groupCursor.moveToNext()){
                groups.add(new Group(groupCursor.getString(0), groupCursor.getString(1)));
            }
            groupCursor.close();
        }

        adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, groups);
        lstGroups.setAdapter(adapter);
        lstGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group selected = (Group)lstGroups.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, selected.toString(), Toast.LENGTH_LONG).show();
            }
        });

//        Cursor groupCursor = getContentResolver().query(
//                ContactsContract.Data.CONTENT_URI,
//                new String[]{ ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID },
//                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
//                        + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
//                        + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
//                new String[] { String.valueOf(24) }, null);
//
//        if(groupCursor!=null){
//            while(groupCursor.moveToNext()){
//                String group_title = groupCursor.getString(0);
//                String id = groupCursor.getString(1);
//                // groups.put(id, group_title);
//            Log.v("TOMTOM", group_title + " - " + id);
//
//            }
//        }

    }

    private void findViews() {
        lstGroups = (ListView)findViewById(R.id.lstGroups);
    }
}
