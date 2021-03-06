package se.agile.activities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import se.agile.princepolo.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PersonalNotesFragment extends Fragment implements OnClickListener, OnItemClickListener{
	private String logTag;
	private View rootView;
	private static File noteFile;
	private Button saveNoteButton, createButton, finishButton, 
			addButton, deleteButton, mergeButton, branchButton,
			classButton, packageButton, interfaceButton,
			featureButton, taskButton, testButton;
    private ListView list,list_head;
    private ArrayList<HashMap<String, String>> mylist, mylist_title;
    private ListAdapter adapter_title, adapter;
    private HashMap<String, String> map1, map2;
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> personalNotes = new ArrayList<String>();
	private ArrayList<String> notes = new ArrayList<String>();
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d HH:mm");
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            										Bundle savedInstanceState) {
		noteFile = new File(getActivity().getFilesDir(), "notes.txt");
		rootView = inflater.inflate(R.layout.fragment_personal_notes, container, false);
		logTag = getResources().getString(R.string.logtag_main);
        list = (ListView) rootView.findViewById(R.id.listView2);
        list_head = (ListView) rootView.findViewById(R.id.listView1);
        saveNoteButton = (Button)rootView.findViewById(R.id.saveNote_Button);
        createButton = (Button)rootView.findViewById(R.id.create_Button);
        finishButton = (Button)rootView.findViewById(R.id.finish_Button);
        addButton = (Button)rootView.findViewById(R.id.add_Button);
        deleteButton = (Button)rootView.findViewById(R.id.delete_Button);
        mergeButton = (Button)rootView.findViewById(R.id.merge_Button);
        branchButton = (Button)rootView.findViewById(R.id.branch_Button);
        classButton = (Button)rootView.findViewById(R.id.class_Button);
        packageButton = (Button)rootView.findViewById(R.id.package_Button);
        interfaceButton = (Button)rootView.findViewById(R.id.interface_Button);
        featureButton = (Button)rootView.findViewById(R.id.feature_Button);
        taskButton = (Button)rootView.findViewById(R.id.task_Button);
        testButton = (Button)rootView.findViewById(R.id.test_Button);
        createButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        mergeButton.setOnClickListener(this);
        branchButton.setOnClickListener(this);
        classButton.setOnClickListener(this);
        packageButton.setOnClickListener(this);
        interfaceButton.setOnClickListener(this);
        featureButton.setOnClickListener(this);
        taskButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
        saveNoteButton.setOnClickListener(this);
        list.setOnItemClickListener(this);
		try {
			updateList();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}       
        return rootView;
    }
	
	private String getDate(){
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
	
	private void writeToFile(ArrayList<String> noteList) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile, false));
		int iter = 0;
		while (iter < noteList.size()){
			writer.write(noteList.get(iter));
			writer.newLine();
			iter++;
		}
		writer.flush();
		writer.close();
	}
	
	private void updateList() throws IOException{
		notes.clear();
	    BufferedReader br = new BufferedReader(new FileReader(noteFile));
	    String line;
	    Boolean date = true;
	    while ((line = br.readLine())!= null) {
	    	if (date){
	    		notes.add(line);
	    		dates.add(line);
	    		date = false;
	    	}
	    	else {
	    		notes.add(line);
	    		personalNotes.add(line);
	    		date = true;
	    	}
	    }
	    br.close();
	    populateLists();
	}

	@Override 
	public void onClick(View v) {
		EditText et = (EditText)rootView.findViewById(R.id.note_textField);
		String msg = et.getText().toString();
		if (v == saveNoteButton) {
			String date = getDate();
			et.setText("");
			if((msg != null) && (msg.trim().length() > 0)){
				notes.add(date);
				notes.add(msg);
				try {
					writeToFile(notes);
					updateList();
				} catch (FileNotFoundException e) {
					fileNotFoundToast();
					e.printStackTrace();
				} catch (IOException e) {
					ioToast();
					e.printStackTrace();
				}
			}
		}
		else if ( v == createButton){
			if (msg.length() == 0){
				et.setText(msg + "Create");
			}
			else {
				et.setText(msg + " create");
			}
			et.setSelection(et.length());
		}
		else if ( v == finishButton){
			if (msg.length() == 0){
				et.setText(msg + "Finish");
			}
			else {
				et.setText(msg + " finish");
			}
			et.setSelection(et.length());
		}
		else if ( v == addButton){
			if (msg.length() == 0){
				et.setText(msg + "Add");
			}
			else {
				et.setText(msg + " add");
			}
			et.setSelection(et.length());
		}
		else if ( v == deleteButton){
			if (msg.length() == 0){
				et.setText(msg + "Delete");
			}
			else {
				et.setText(msg + " delete");
			}
			et.setSelection(et.length());
		}
		else if ( v == mergeButton){
			if (msg.length() == 0){
				et.setText(msg + "Merge");
			}
			else {
				et.setText(msg + " merge");
			}
			et.setSelection(et.length());
		}
		else if ( v == branchButton){
			if (msg.length() == 0){
				et.setText(msg + "Branch");
			}
			else {
				et.setText(msg + " branch");
			}
			et.setSelection(et.length());
		}
		else if ( v == classButton){
			if (msg.length() == 0){
				et.setText(msg + "Class");
			}
			else {
				et.setText(msg + " class");
			}
			et.setSelection(et.length());
		}
		else if ( v == packageButton){
			if (msg.length() == 0){
				et.setText(msg + "Package");
			}
			else {
				et.setText(msg + " package");
			}
			et.setSelection(et.length());
		}
		else if ( v == interfaceButton){
			if (msg.length() == 0){
				et.setText(msg + "Interface");
			}
			else {
				et.setText(msg + " interface");
			}
			et.setSelection(et.length());
		}
		else if ( v == featureButton){
			if (msg.length() == 0){
				et.setText(msg + "Feature");
			}
			else {
				et.setText(msg + " feature");
			}
			et.setSelection(et.length());
		}
		else if ( v == taskButton){
			if (msg.length() == 0){
				et.setText(msg + "Task");
			}
			else {
				et.setText(msg + " task");
			}
			et.setSelection(et.length());
		}
		else if ( v == testButton){
			if (msg.length() == 0){
				et.setText(msg + "Test");
			}
			else {
				et.setText(msg + " test");
			}
			et.setSelection(et.length());
		}
	}
	
	private void ioToast() {
		Toast IOExceptionToast = Toast.makeText(getActivity().getApplicationContext(), "Damn! IOException!", Toast.LENGTH_LONG);
		IOExceptionToast.setGravity(Gravity.CENTER, 0, 0);
		IOExceptionToast.show();
	}
	
	private void fileNotFoundToast() {
		Toast fileNotFoundToast = Toast.makeText(getActivity().getApplicationContext(), "Damn! 404 etc! File not found", Toast.LENGTH_LONG);
		fileNotFoundToast.setGravity(Gravity.CENTER, 0, 0);
		fileNotFoundToast.show();
	}
	
	public void populateLists() {
        mylist = new ArrayList<HashMap<String, String>>();
        mylist_title = new ArrayList<HashMap<String, String>>();
        map1 = new HashMap<String, String>();
      
        /**********Display the headings************/
        map1.put("date", " Date");
        map1.put("note", "Note");
        mylist_title.add(map1);

        try {
            adapter_title = new SimpleAdapter(getActivity(), mylist_title, R.layout.headrow,
                    new String[] {"date", "note"}, new int[] {
                            R.id.dateHead, R.id.noteHead});
            list_head.setAdapter(adapter_title);
        } catch (Exception e) {
           //this is bad practice but.. yeah
        }
      
        /**********Display the contents************/
        mylist.clear();
        for (int i = personalNotes.size() - 1; i >= 0; i--) {
        	map2 = new HashMap<String, String>();
            map2.put("date", dates.get(i));
            map2.put("note", personalNotes.get(i));
            mylist.add(map2);
        }
        personalNotes.clear();
        dates.clear();
       
        try {
            adapter = new SimpleAdapter(getActivity(), mylist, R.layout.row,
                    new String[] {"date", "note" }, new int[] {
                            R.id.date, R.id.note});
            list.setAdapter(adapter);
        } catch (Exception e) {
        	//this is bad practice but.. yeah
        }

    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Integer pip = (int) id;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:

		    		if (notes.size() > 1){
		    			notes.remove((notes.size()-1) - (pip*2+1));
		    			notes.remove((notes.size()-1) - (pip*2));
		    		}
		        	try {
		    			writeToFile(notes);
		    			updateList();
		            } catch (FileNotFoundException e1) {
		           	 fileNotFoundToast();
		    			e1.printStackTrace();
		    		} catch (IOException e) {
		    			ioToast();
		    			e.printStackTrace();
		    		}
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //Nay sir keep me contents
		            break;
		        }
		    }
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete this note?").setNegativeButton("No", dialogClickListener)
		.setPositiveButton("Yes", dialogClickListener).show();	
	}
}
