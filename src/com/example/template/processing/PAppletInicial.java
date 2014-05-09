package com.example.template.processing;

/* IMPORTANTE a ter presente:
 * 
 * 1- Para poder importar PApplet, temos primeiro que botar na pasta "libs" o arquivo:
 * processing-android-core.jar que pode ser encontrado nas pasta Processing/Core ou
 * http://www.java2s.com/Code/Jar/p/Downloadprocessingandroidcorejar.htm
 * 
 * 2- Cada sketch de processing que é criado pelo meio de um 'intent' tem que ser declarado
 * no AndroidManifest.xml
 * 
 * 4- No final da classe tem os metodos proprios do ciclo de vida de uma Activity em Android
 * E importante por que no onCreate pegamos dados que podem ser enviados desde o Main Activity
 * 
 * 3- Ao utilizar float tem que agregar ao numero uma 'f' no final. Por exemplo 3.14f
 */

import org.puredata.core.PdBase;

import com.example.template.multitouch.MTListenerCallBack;
import com.example.template.multitouch.MultiTouchP;
import com.example.template.pdstuff.PdListenerCallBack;
import com.example.template.pdstuff.PureDataManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import processing.core.PApplet;

public class PAppletInicial extends PApplet  implements PdListenerCallBack,MTListenerCallBack {
	//esta variavel entrega um TAG para imprimir na consola mensagens que possam ser filtrados pelo TAG
	private static final String TAG = "PAppletInicial";
		
	String stringVal;
	float numeroFloat;

// Criamos uma clase especialmente para botar ali todo o codigo de libPD.
	PureDataManager pdManager;
	MultiTouchP multiTouch;
/* Para mostrar o uso de clases aqui tem uma criada
 * Importante notar o "this" na hora de criar um objeto
 * todas as classes criadas terāo minimo um "this" como parámetro.
 */
	NovaClasse novaClasse;
	
	public void setup(){
/* Nāo é preciso botar size(); Android vai implementar o sketch com a maxima resoluçāo da tela
 * depois é so pedir os valores de width e height, como no exemplo dentro da clase "NovaClasse"
 */	
		multiTouch = new MultiTouchP(this);
		novaClasse = new NovaClasse(this);
		textSize(50);
	}
	
	public void draw() {
		
		
		PdBase.sendFloat("Rfloat_1", map(mouseY, 0, height, 60,70));
//Imprimimos na tela os dados que foram enviados desde o MainActivity para o processing.
		text(stringVal, 10, 10);
		text(numeroFloat, 10, 50);
		
//implementa o draw() com o metodo para desenhar da clase NovaClasse
		novaClasse.desenhar();
		
		if (mousePressed)
			PdBase.sendBang("Rbang_2");
	
		multiTouch.drawInfo();
	}
	
	
/* METODOS PROPRIOS DE UM ACTIVITY, ligados direitamente ao ciclo de vida	
 * http://www.devmedia.com.br/entendendo-o-ciclo-de-vida-de-uma-aplicacao-android/22922
 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		//pegamos os valores "extras" que enviamos desde o MainActivity
		Bundle extras=getIntent().getExtras(); //criamos um objeto que pega todos os extras existentes no intent
		stringVal = extras.getString("Um String"); // pegamos o valor segundo a etiqueta dada no MainActivity
		numeroFloat = extras.getFloat("Um float"); // pegamos o valor segundo a etiqueta dada no MainActivity
		
	//Inicializaçāo do objeto que vai gerenciar o Pd
		pdManager = new PureDataManager(this);
	//coloca o nome do Patch, nāo confundir com o nome do arquivo .zip, e logo coloca 'path' desde onde pega o zip
		pdManager.openPatch("entonador.pd", com.example.template.R.raw.patch); 
		pdManager.setTicksPerBuffer(1);
		pdManager.setChanelIn(0);
	//Agrega todos os Strings chaves que vāo receber dados
		pdManager.addSendMessagesFromPD("Sfloat_0");
		pdManager.addSendMessagesFromPD("Sfloat_1");
	}
	@Override
	public void onResume() {
		super.onResume();
		pdManager.onResume();
	}
	@Override
	public void onPause() {
		super.onPause();
		pdManager.onPause();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		pdManager.onDestroy();
	}
	@Override
	public void finish() {
		super.finish();
		pdManager.finish();
	}

/* MESSAGES TO SEND DATA TO PD. Para enviar mensagens pra PD usar os seguentes metodos
 * 	pdBase.sendBang(String receiver);
  	pdBase.sendFloat(String receiver, float value);
    pdBase.sendSymbol(String receiver, String symbol);
	pdBase.sendList(String receiver, Object... list);
	pdBase.sendMessage(String receiver, String message, Oject... list);
	
	TO GET AN ARRAY FROM PD	
	float [] newArray = pdManager.getArrayFromPd("nome do array em PD"); este metodo retorna o array.
	TO SEND AN ARRAY TO PD	
	pdManager.sendArrayToPD( float [] arrayToSend , "nome do array em PD"); este metodo envia um array de float para um outro array em PD
	
 */
	
//CALLBACKS FROM PD As seguentes funçōes sāo chamadas quando tiver alguma mensagem nova desde PD
	@Override
	public void callWhenReceiveFloat(String key, float val) {
		// TODO Auto-generated method stub
//		Log.i(TAG, "recebendo do PD = " + key + ": " + val );
	}
	@Override
	public void callWhenReceiveBang(String key) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void callWhenReceiveSymbol(String key, String symbol) {
		// TODO Auto-generated method stub
	}
	@Override
	public void callWhenReceiveList(String key, Object... args) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void callWhenReceiveMessage(String key, String symbol,
			Object... args) {
		// TODO Auto-generated method stub
	}
	
//Método callback, que é chamado cada vez que tem um novo evento na tela
	@Override
	public void screenTouched(int id, float x, float y) {
		// TODO Auto-generated method stub
//		Log.i(TAG, "mutiTouch, toque em x:" + x + " y: " + y + " e id: " + id );
	}
	@Override
	public void screenTouchedReleased(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void screenTouchedDragged(int id, float x, float y, float dist, float ang) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Touch dragged, toque em x:" + x + " y: " + y + " e id: " + id + " dist: "+dist+" angulo: "+ang);
	}

	public boolean surfaceTouchEvent(MotionEvent me) {
		multiTouch.surfaceTouchEvent(me);
	    return super.surfaceTouchEvent(me);
	}


	
}
