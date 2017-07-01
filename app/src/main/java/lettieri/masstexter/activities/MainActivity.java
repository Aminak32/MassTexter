package lettieri.masstexter.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import lettieri.masstexter.Group;
import lettieri.masstexter.R;

public class MainActivity extends AppCompatActivity {
    // arbitrary number to determine which permission was granted
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 465;
    private ArrayList<Group> groups = new ArrayList<>();
    private ArrayAdapter<Group> adapter;

    private ListView lstGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            addGroups();
        }
        setUp();
    }

    /***
     * Get the views for the associated layouts
     */
    private void findViews() {
        lstGroups = (ListView)findViewById(R.id.lstGroups);
    }

    /***
     * Sets up adapters and on click events
     */
    private void setUp() {
        adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, groups);
        lstGroups.setAdapter(adapter);
        lstGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group selected = (Group)lstGroups.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, SendMessage.class);
                intent.putExtra(SendMessage.EXTRA_GROUP_ID, selected.getId());
                intent.putExtra(SendMessage.EXTRA_GROUP_NAME, selected.getName());
                startActivity(intent);
            }
        });
    }

    /***
     * Add all groups in contacts to the list
     */
    private void addGroups() {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addGroups();
                    adapter.notifyDataSetInvalidated();
                } else {
                    Toast.makeText(this, "This app requires contact permission, close the app and reopen to allow", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }
}
