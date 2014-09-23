package cl.javiercruz.arvore;

import cl.javiercruz.arvore.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/* NĀO ESQUEÇER
 * Cada sketch de processing que é criado pelo meio de um 'intent' tem que ser declarado
 * no AndroidManifest.xml ao igual que qualquer outra nova Activity
 */

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity_Template";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	//Inicializamos o objeto botāo linkando ele com o botāo desenhado no res/layout/activity_main.xml
		Button botaoInicioApp = (Button)this.findViewById(R.id.button1);
	//implementamos o Listener para dar as instruçōes quando o botāo for pressionado.
		botaoInicioApp.setOnClickListener(mGlobal_OnClickListener);
		

	}
	
/*	Uma variavel para escutar os click que todos os botōes
 *  Cada botāo existente e instanciado vai gerar um numero de id, que pode ser filtrado com o switch / case
 *  
 *  Aqui se cria uma variavel de referencia  cujo tipo é a Interface "OnClickListener". 
 *	a gente define uma classe anónima, que implementa a interface e instancia ela. Por isso o "new" 
 *  + info: http://www.coderanch.com/t/552277/Android/Mobile/interface-constructor
 */
	final OnClickListener mGlobal_OnClickListener = new OnClickListener() {
        public void onClick(final View v) {
            switch(v.getId()) {
                case R.id.button1:
                	Log.i(TAG, "Botāo1 clicado"); //funçāo que imprime na consola                	
//Criaçāo de um intent que vāo permitir abrir o sketch de Processing
                	Intent intent = new Intent();
/*  Colocamos como parametro: em setClassName
 * 1. o pacote de nosso aplicativo (onde tem o MainActivity)
 * 2. o pacote exato onde tiver a clase PAppletInicial */
					intent.setClassName("cl.javiercruz.arvore", 
									    "cl.javiercruz.arvore.processing.PAppletInicial" );
/* Pode enviar dados para ser utilizados no sketch pelo meio de "putExtra"
 * http://developer.android.com/training/basics/firstapp/starting-activity.html  */
					float valorX = 3.14f;
					String stringX = "qualquer string";
					intent.putExtra("Um float", valorX );
					intent.putExtra("Um String",stringX);
					startActivity(intent);
                break;
            }
        }
    };	

}
