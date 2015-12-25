package dk.denhart.steamauther.steamauth;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    private Handler handler = new Handler(Looper.getMainLooper());
    ListView list;

    ArrayList<User> user_details;
    CustomListAdapter adapter;

    static final private int DELAY_TIME = 30 * 1000;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            adapter.updateCodes();
      /* post new handler to re-trigger in 30 seconds */
             handler.postDelayed(this, DELAY_TIME);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<User> results =  db.getAllUsers();
                User user = db.getUser(1);
                Log.d("Test" ,user.getaccountName());
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_details = db.getAllUsers();
        startRunnableSynced();
        list = (ListView) findViewById(R.id.list_myContent);
        adapter = new CustomListAdapter(this, user_details);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.d("Unix time", "Now Pause");
        handler.removeCallbacks(runnable);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

            return super.onOptionsItemSelected(item);
    }

    private int startRunnableSynced(){
        SimpleDateFormat sdf = new SimpleDateFormat("ss");
        String currentDateandTime = sdf.format(new Date());
        int i=Integer.parseInt(currentDateandTime);
        Log.d("Sync with", String.valueOf(i));
        if(i == 0 || i == 30 ) {
            Log.d("Sync with", "nothing");
            handler.postDelayed(runnable, DELAY_TIME);
        } else if(i < 30 ){
            int sync = 30-i;
            Log.d("Sync with", String.valueOf(sync));
            handler.postDelayed(runnable, sync*1000);
        } else if(i > 30){
            int sync = 60-i;
            Log.d("Sync with", String.valueOf(sync));
            handler.postDelayed(runnable, sync*1000);
        }
      return 1;
    }

    private ArrayList<User> getListDataDB() {
        db.addUser(new User(0, "Test 1", "M+/zRGFUvlntWFBPXvZGrzLubhc=", "T033yy9A9QIOaOofW+br2MG/VY8="));
        ArrayList<User> results =  db.getAllUsers();
        return results;
    }


    private ArrayList<User> getListData() {
        ArrayList<User> results = new ArrayList<User>();
        results.add(new User(1, "Test 1", "M+/zRGFUvlntWFBPXvZGrzLubhc=", "T033yy9A9QIOaOofW+br2MG/VY8="));
        results.add(new User(2, "Test 2", "M+/zRGFUvlntWAAPXvZGrzLubhc=", "T033yy9A9QIOaOofW+br2MG/VY8="));
        results.add(new User(3, "Test 3", "M+/zRGFUvlntAFAPXvZGrzLubhc=", "T033yy9A9QIOaOofW+br2MG/VY8="));
        return results;
    }
}
