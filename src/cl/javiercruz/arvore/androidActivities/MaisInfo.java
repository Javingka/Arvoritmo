package cl.javiercruz.arvore.androidActivities;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cl.javiercruz.arvore.R;

public class MaisInfo extends Activity {
	private static final String TAG = "MaisInfo";

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
//	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.maisinfo);
	        
	        Button botaoWeb = (Button)this.findViewById(R.id.buttonWeb);
	        botaoWeb.setOnClickListener(mGlobal_OnClickListener);
	    }
	    
	    final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
	        public void onClick(final View v) {
	            switch(v.getId()) {
	                case R.id.buttonWeb:
	                	Log.i(TAG, "buttonWeb clicado"); 
	                	Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
	                			Uri.parse("http://musicamovelbahia.wordpress.com/about/"));
						startActivity(browserIntent);
	                break;
	            }
	        }
	    };	
}
