package cl.javiercruz.arvore.androidActivities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.javiercruz.arvore.R;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileChooserSession extends ListActivity {
	
	private String pastaBaseDeSessoes;
	private List<String> item = null; 
	private List<String> path = null;
	private TextView myPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_chooser);
        myPath = (TextView)findViewById(R.id.path);
        
        pastaBaseDeSessoes = Environment.getExternalStorageDirectory().getPath();
        pastaBaseDeSessoes = pastaBaseDeSessoes + "/arvoritmo/sessoes";
        
        
//	    Log.i("FileChooserSession", "pastaBaseDeSessoes: " + pastaBaseDeSessoes );
	        
	    getDir(pastaBaseDeSessoes);
    }
    
    private void getDir(String dirPath)
    {
    	myPath.setText("Location: " + dirPath);
    	item = new ArrayList<String>();
    	path = new ArrayList<String>();
    	File f = new File(dirPath);
    	File[] files = f.listFiles();
    	
    	if(!dirPath.equals(pastaBaseDeSessoes))
    	{
    		item.add(pastaBaseDeSessoes);
    		path.add(pastaBaseDeSessoes);
    		item.add("../");
    		path.add(f.getParent());	
    	}
    	
    	for(int i=0; i < files.length; i++)
    	{
    		File file = files[i];
    		
    		if(!file.isHidden() && file.canRead()){
    			path.add(file.getPath());
        		if(file.isDirectory()){
        			item.add(file.getName() + "/");
        		}else{
        			item.add(file.getName());
        		}
    		}	
    	}

    	ArrayAdapter<String> fileList =
    			new ArrayAdapter<String>(this, R.layout.row, item);
    	setListAdapter(fileList);	
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		final File file = new File(path.get(position));
		
		if (file.isDirectory())
		{
			if(file.canRead()){
				getDir(path.get(position));
			}else{
				new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("[" + file.getName() + "] folder can't be read!")
					.setPositiveButton("OK", null).show();	
			}	
		}else {
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("[" + file.getName() + "]")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			            @Override
			            public void onClick(DialogInterface dialog, int which) {
			            	Intent data = new Intent();
			    			data.setData(Uri.parse(file.getAbsolutePath().toString()));
			    			setResult(RESULT_OK, data);
			    			finish();
			            }})
			        .show();


		  }
	}

}
