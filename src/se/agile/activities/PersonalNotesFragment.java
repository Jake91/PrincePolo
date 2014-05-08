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
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
	private Button saveNoteButton;
    private ListView list,list_head;
    private ArrayList<HashMap<String, String>> mylist, mylist_title;
    private ListAdapter adapter_title, adapter;
    private HashMap<String, String> map1, map2;
    private ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<String> personalNotes = new ArrayList<String>();
	//private ArrayList<PersonalNote> noteList = new ArrayList<PersonalNote>();
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
        saveNoteButton.setOnClickListener(this);
        list.setOnItemClickListener(this);
		try {
			updateList();
		} catch (FileNotFoundException e1) {
			fileNotFoundToast();
			e1.printStackTrace();
		} catch (IOException e) {
			ioToast();
			e.printStackTrace();
		}
		
		//noteFile = getActivity().getFilesDir();
        
        return rootView;
    }
	
	private String getDate(){
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
	private void writeToFile(ArrayList<String> noteList) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile, false));
		int iter = 0;
		while (iter < noteList.size()){
			//Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), noteList.get(iter), Toast.LENGTH_LONG);
    	    //toast2.setGravity(Gravity.CENTER, 0, 0);
    	    //toast2.show();
			writer.write(noteList.get(iter));
			writer.newLine();
			//Log.d(logTag, noteList.get(iter));
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
		    	Log.d(logTag, line);
	    }
	    br.close();
	    populateLists();
	}

	@Override 
	public void onClick(View v) {
		
		if (v == saveNoteButton) {
			String date = getDate();
			EditText et = (EditText)rootView.findViewById(R.id.note_textField);
			String msg = et.getText().toString();
			et.setText("");
			if((msg != null) && (msg.trim().length() > 0)){
				notes.add(date);
				notes.add(msg);
				//notes.add("#¤#");
				try {
					writeToFile(notes);
					updateList();
					//Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), notes.get(0).toString(), Toast.LENGTH_LONG);
					//toast1.show();
				} catch (FileNotFoundException e) {
					fileNotFoundToast();
					e.printStackTrace();
				} catch (IOException e) {
					ioToast();
					e.printStackTrace();
				}
			}
		}
		//else if (v == list) {
			
		//}

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
        map1.put("nothing", "");
        mylist_title.add(map1);

        try {
            adapter_title = new SimpleAdapter(getActivity(), mylist_title, R.layout.headrow,
                    new String[] {"date", "note", "nothing"}, new int[] {
                            R.id.dateHead, R.id.noteHead, R.id.nothingHead});
            list_head.setAdapter(adapter_title);
        } catch (Exception e) {
           
        }
      
        /**********Display the contents************/
        mylist.clear();
        for (int i = personalNotes.size() - 1; i >= 0; i--) {
        	map2 = new HashMap<String, String>();
            map2.put("date", dates.get(i));
            map2.put("note", personalNotes.get(i));
            map2.put("deleteButton", "X");
            mylist.add(map2);
        }
        personalNotes.clear();
        dates.clear();

       
        try {
            adapter = new SimpleAdapter(getActivity(), mylist, R.layout.row,
                    new String[] {"date", "note", "deleteButton" }, new int[] {
                            R.id.date, R.id.note, R.id.deleteButton });
            list.setAdapter(adapter);
        } catch (Exception e) {
           
        }

    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		System.out.println("HEY MAN CLICK FOUDN");
		
		notes.remove((id*2)+1);
        notes.remove(id*2);
        try {
			updateList();
        } catch (FileNotFoundException e1) {
       	 fileNotFoundToast();
			e1.printStackTrace();
		} catch (IOException e) {
			ioToast();
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
}
