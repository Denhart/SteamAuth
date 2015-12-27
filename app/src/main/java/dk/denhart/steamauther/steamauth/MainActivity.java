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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    JSONArray pages;
    DatabaseHandler db = new DatabaseHandler(this);
    private Handler handler = new Handler(Looper.getMainLooper());
    ListView list;

    ArrayList<User> user_details;
    CustomListAdapter adapter;

    AddUserDialog UserDialog;

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
        UserDialog = new AddUserDialog(MainActivity.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialog.showInputDialog();
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
        list.setLongClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("short click, item: ", Integer.toString(position));
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long id) {
                UserDialog.showDeleteDialog(db, user_details.get(pos), adapter, pos);
                return true;
            }
        });

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


    // Handle Callback from QR reader...
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Log.d("Im here2", "Im here2");
        if (scanResult != null) {
            String contents = scanResult.getContents();
            if (contents != null)
                try {
                    JSONObject QRdata = new JSONObject(contents);
                    User newUser = new User();
                    newUser.setaccountName(QRdata.getString("account_name"));
                    newUser.setsharedSecret(QRdata.getString("shared_secret"));
                    newUser.setidentitySecret(QRdata.getString("identity_secret"));
                    db.addUser(newUser);

                } catch (JSONException e) {
                    Log.d("Scan intent", "Not valid json");
                    Toast.makeText(MainActivity.this,
                            "Not valid format, scan again!",
                            Toast.LENGTH_LONG).show();
                    UserDialog.handleQRscan();
                }
                Log.d("Scan intent", scanResult.getContents());
        }

    }


    protected void redrawList(){

    adapter.updateData(db.getAllUsers());
    }

}
