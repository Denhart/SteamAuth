package dk.denhart.steamauther.steamauth;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by denhart on 12/26/15.
 */
public class AddUserDialog extends Activity{
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    Context workingContext;
    Activity workingActivity;
    DatabaseHandler db;
    final CustomListAdapter adapter;

    public AddUserDialog(CustomListAdapter adapter, Activity workingActivity, DatabaseHandler db){
        this.workingActivity = workingActivity;
        this.db = db;
        this.adapter = adapter;
    }

    public void handleQRscan(){

        // check Android 6 permission
        if (ContextCompat.checkSelfPermission(this.workingActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            IntentIntegrator integrator = new IntentIntegrator(this.workingActivity);
            integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
            integrator.setOrientationLocked(true);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        } else {
            ActivityCompat.requestPermissions(this.workingActivity,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

    }

    public void showInputDialog() {
        final String[] items = {"Manually", "Scan QR-code", "Other method"};
        String Test = "Hej";
        AlertDialog.Builder builder = new AlertDialog.Builder(this.workingActivity);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Log.d("Scan intent", items[item]);

                if (items[item].equals("Manually")){
                    addManuallyDialog();
                } else if (items[item].equals("Scan QR-code")){
                    handleQRscan();
                } else if (items[item].equals("Other method")){

                }


            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showDeleteDialog(final DatabaseHandler db, final User curUser, final CustomListAdapter adapter, final int pos) {
        //TODO implement this shit.

        AlertDialog.Builder builder = new AlertDialog.Builder(this.workingActivity);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to remove " + curUser.getaccountName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.deleteUser(curUser);
                        adapter.deleteItem(pos);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void addManuallyDialog() {
        //TODO implement this shit.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.workingActivity);
        // Get the layout inflater
        LayoutInflater inflater = this.workingActivity.getLayoutInflater();
        final View addManDiaView = inflater.inflate(R.layout.dialog_add_man, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(addManDiaView);
        final EditText username = (EditText) addManDiaView.findViewById(R.id.username);
        final EditText shared_secret = (EditText) addManDiaView.findViewById(R.id.shared_secret);
        final EditText idn_secret = (EditText) addManDiaView.findViewById(R.id.idn_secret);
        final CustomListAdapter newTest = this.adapter;
        // Add action buttons
        builder.setPositiveButton("Add account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

//                        final EditText shared_secret = (EditText) findViewById(R.id.shared_secret);
                //                      final EditText idn_secret = (EditText) findViewById(R.id.idn_secret);
                Log.d("Test man add", username.getText().toString());
                   /*     User user = new User();
                        user.setaccountName(username.getText().toString());
                        user.setsharedSecret(shared_secret.getText().toString());
                        user.setidentitySecret(idn_secret.getText().toString());
                        db.addUser(user); */
                         ArrayList<User> userData = db.getAllUsers();
                        newTest.updateData(userData);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
