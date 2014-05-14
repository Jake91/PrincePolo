package se.agile.model;

import se.agile.princepolo.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class NotificationDialog extends DialogFragment 
{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Incoming commit!")
        		.setIcon(R.drawable.warning)
        		.setMessage("A commit has been submitted to your watched branch. Do you want to view it?")
        		.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                       // FIRE ZE MISSILES!
                   }
               })
               .setNegativeButton("No, not now", new DialogInterface.OnClickListener() 
               {
                   public void onClick(DialogInterface dialog, int id) 
                   {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}