package cl.javiercruz.arvore.androidActivities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.puredata.core.PdBase;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cl.javiercruz.arvore.R;

public class FileChooserMaior extends ListActivity {
	private TextView textoLista;
	private String pastaBaseDeSamples;
	private List<String> listaSamplesApp = null;
	private List<String> path = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.file_chooser_maior);
	    textoLista = (TextView)findViewById(R.id.path_interno);
	    
	    PackageManager m = getPackageManager();
	    
	    pastaBaseDeSamples = getPackageName();
	    try {
	        PackageInfo p = m.getPackageInfo(pastaBaseDeSamples, 0);
	        pastaBaseDeSamples = p.applicationInfo.dataDir;//>    dataDir;
	    } catch (NameNotFoundException e) {
	        Log.w("yourtag", "Error Package name not found ", e);
	    }
//	    AssetManager am = getAssets();
//	    pastaBaseDeSamples += "/samples"; //"/files/audioSamples";
//	    File ps = getCacheDir();
//	    pastaBaseDeSamples = ps.getPath();
	    File f = getFilesDir();
	    pastaBaseDeSamples = f.getPath() ;
	    pastaBaseDeSamples += "/audioSamples";
	    Log.i("FileChooserMaior", "pastaBaseDeSamples: " + pastaBaseDeSamples );
//	    pastaBaseDeSamples = getApplicationFilePath(this); //App.getApp().getApplicationContext().getFilesDir().getAbsolutePath();//"audioSamples";
	        
	    getDir(pastaBaseDeSamples);
	        
	    Button botaoPegaSampleEmDisco = (Button)this.findViewById(R.id.button_c_1);
	   	//implementamos o Listener para dar as instruçōes quando o botāo for pressionado.
	    botaoPegaSampleEmDisco.setOnClickListener(mGlobal_OnClickListener);
	    
	    Typeface typface=Typeface.createFromAsset(getAssets(),"fonts/Roboto_v1.2/Roboto/Roboto-Black.ttf");
	    botaoPegaSampleEmDisco.setTypeface(typface);
	 }
	 private void getDir(String dirPath) {
		 textoLista.setText("Ou escolhe um sample da lista");
		 listaSamplesApp = new ArrayList<String>();
		 path = new ArrayList<String>();
		 File f = new File(dirPath);
	     File[] files = f.listFiles();

	     if(!dirPath.equals(pastaBaseDeSamples)) 	{
	    	listaSamplesApp.add(pastaBaseDeSamples);
	    	path.add(pastaBaseDeSamples);
	    	listaSamplesApp.add("../");
	   		path.add(f.getParent());	
	   	}
	     
	     for(int i=0; i < files.length; i++) { //loop por cada um dos arquivos
	    	File file = files[i];
	    	
	    	if(!file.isHidden() && file.canRead()){ //Se o arquivo náo esta oculto e se pode lêr
	    		path.add(file.getPath()); 
	        	if(file.isDirectory()){
	        		listaSamplesApp.add(file.getName() + "/");
	        	}else{
	        		listaSamplesApp.add(file.getName());
	        	}
	    	}	
	     }
//	     Log.i("FileChooserMaior", "listaSamplesApp: " + listaSamplesApp );
		    
	     ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, listaSamplesApp);
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

	/*============================================================================================================
	* Função para detectar os clicks na tela e asociar tarefas segundo o botão ativado.
	* =================================================================================================================*/ 
	 final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
		 public void onClick(final View v) {
			 switch(v.getId()) {
	         	case R.id.button_c_1:
	         		escolheSampleEmDisco(0);
	             break;
	       }
	    }
	 };	
	 
	 /*============================================================================================================
	 * Activities do selector de arquivos desde raiz do dispositivo
	 * =================================================================================================================*/ 
	 public void escolheSampleEmDisco(int seletor){
		Intent intentGetSample = new Intent("cl.javiercruz.arvore.androidActivities.FileChooser");
		startActivityForResult(intentGetSample, seletor);
	 }
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
			
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String data1 = data.getData().toString();
				Intent dataResp = new Intent();
				dataResp.setData(Uri.parse(data1));
    			setResult(RESULT_OK, dataResp);
    			finish();
    			
//				PdBase.sendSymbol("caminho_A", data1);
			}
		}
			
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
//				String data2 = data.getData().toString();
//				PdBase.sendSymbol("caminho_B", data2);			
				}
			}
			
	}
}
