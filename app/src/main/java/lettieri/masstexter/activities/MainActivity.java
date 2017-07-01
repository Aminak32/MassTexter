package lettieri.masstexter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
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
                Intent intent = new Intent(MainActivity.this, SendMessage.class);
                intent.putExtra(SendMessage.EXTRA_GROUP_ID, selected.getId());
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        lstGroups = (ListView)findViewById(R.id.lstGroups);
    }
}
