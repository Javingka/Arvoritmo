package cl.javiercruz.arvore.androidActivities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.javiercruz.arvore.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FileChooserRec extends ListActivity {
	
	private String pastaBaseDeSessoes;
	private List<String> item = null; 
	private List<String> path = null;
	private TextView myPath;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.file_chooser_salva);
		myPath = (TextView)findViewById(R.id.path);
	    
	    pastaBaseDeSessoes = Environment.getExternalStorageDirectory().getPath();
        pastaBaseDeSessoes = pastaBaseDeSessoes + "/arvoritmo/sessoes";
        
        
        final Button botaoSalvaSessao = (Button) findViewById(R.id.button_salva);
        final EditText editText = (EditText) findViewById(R.id.editText1);

        botaoSalvaSessao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String arquivoGuardar = editText.getText().toString();
            	Intent data = new Intent();
		    	data.setData(Uri.parse( arquivoGuardar ));
		    	setResult(RESULT_OK, data);
		    	finish();
            }
        });
        
	}


}
