package cl.javiercruz.arvore.processing.barra;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.puredata.core.PdBase;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import processing.core.PApplet;
import processing.core.PVector;

public class BotaoRec extends BotaoBase {
	String saveFilePath;
	String saveFileName;
	
	public BotaoRec(PApplet _p5, PVector _pos, float _diam, int _color) {
		super(_p5, _pos, _diam, _color);
		// TODO Auto-generated constructor stub
	}
	
	public void comecaGrabacao() {
		prepareRecord();
		PdBase.sendFloat("pd_record", 1);
	}
	public void comecaGrabacao(String nomeArq) {
		prepareRecord(nomeArq);
		PdBase.sendFloat("pd_record", 1);
	}
	public void terminaGrabacao () {
		PdBase.sendFloat("pd_record", 0);
	}
	private void prepareRecord(String nomeArq) {
	//		p5.println ("Recording");
			 
		String root = Environment.getExternalStorageDirectory().toString();
	//	String state = Environment.getExternalStorageState();
	//	p5.println ("State: "+state + " root: " +root);
		File myDir = new File(root + "/arvoritmo");
		myDir.mkdirs();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
		Date now = new Date();
		String fileName = formatter.format(now);
		String fname = "recording_" + fileName;
		saveFilePath = myDir.getAbsolutePath();
	//	p5.println ("IN:  " + myDir + "/" + fname);
		PdBase.sendSymbol("pd_path", myDir + "/" + nomeArq);
		
		Context context = p5.getApplicationContext();
		CharSequence text = "Grabando em: " + myDir + "/" + nomeArq ;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}	
	// prepare the file system to store the recordings
	private void prepareRecord() {
//			p5.println ("Recording");
			 
		String root = Environment.getExternalStorageDirectory().toString();
//		String state = Environment.getExternalStorageState();
//		p5.println ("State: "+state + " root: " +root);
		File myDir = new File(root + "/arvoritmo");
		myDir.mkdirs();
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
		Date now = new Date();
		String fileName = formatter.format(now);
		String fname = "recording_" + fileName;
		saveFilePath = myDir.getAbsolutePath();
		saveFileName = fname + ".wav";
//		p5.println ("IN:  " + myDir + "/" + fname);
		PdBase.sendSymbol("pd_path", myDir + "/" + fname);
		
		Context context = p5.getApplicationContext();
		CharSequence text = "Grabando em: " + myDir + "/" + fname ;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}	
}
