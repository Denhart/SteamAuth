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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
/**
 * Created by denhart on 12/26/15.
 */
public class AddUserDialog extends Activity{
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    Context workingContext;
    Activity workingActivity;

    public AddUserDialog(Activity workingActivity){
        this.workingActivity = workingActivity;
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

                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}
