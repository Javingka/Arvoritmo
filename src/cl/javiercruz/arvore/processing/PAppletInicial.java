package cl.javiercruz.arvore.processing;

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
 * E importante porque no onCreate pegamos dados que podem ser enviados desde o Main Activity
 * 
 * 3- Ao utilizar float tem que agregar ao numero uma 'f' no final. Por exemplo: float num = 3.14f;
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.JarException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.puredata.core.PdBase;

import cl.javiercruz.arvore.androidActivities.FileChooser;
import cl.javiercruz.arvore.multitouch.MTListenerCallBack;
import cl.javiercruz.arvore.multitouch.MultiTouchP;
import cl.javiercruz.arvore.pdstuff.PdListenerCallBack;
import cl.javiercruz.arvore.pdstuff.PureDataManager;
import cl.javiercruz.arvore.processing.UIbotoes.Codigo;
import cl.javiercruz.arvore.processing.arvoresystem.ArvoreSystem;
import cl.javiercruz.arvore.processing.arvoresystem.Galho;
import cl.javiercruz.arvore.processing.arvoresystem.PontoNota;
import cl.javiercruz.arvore.processing.barra.BarraControl;
import cl.javiercruz.arvore.processing.botoes.SampleSelector;

import cl.javiercruz.arvore.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import processing.core.PApplet;
import processing.core.PVector;

public class PAppletInicial extends PApplet  implements PdListenerCallBack,MTListenerCallBack {
	//esta variavel entrega um TAG para imprimir na consola mensagens que possam ser filtrados pelo TAG
	private static final String TAG = "PAppletInicial";
	String stringVal;
	float numeroFloat;

	PureDataManager pdManager;
	MultiTouchP multiTouch;
	ArrayList <ArvoreSystem> arvores;
	
	
	FileChooser janelaEscolhaSampleEmDisco;
	BarraControl barra; //gestionador de arvore para somar e restar, play geral e botão rec
	
	SampleSelector sampleSelector;
	private float scaleZoom; //nivel de zoom da imagem
	private PVector translateZoom; //o deslocamento da imagem dentro do zoom
	int cantidadInicialArvores;
	private int modificacionCantArvore;
	boolean emToqueTela  = false; //Para indicar que um toque na tela esta sendo gerado
	public int colorFundo;
	private boolean PAppletpronto = false; //para evita que se o usuario pega a tela antes de abrir o programa, ele de erro
	
	public void setup(){
		colorMode(HSB);
		imageMode(PApplet.CENTER);
		multiTouch = new MultiTouchP(this); //inicializa o multitouch de processing
		arvores = new ArrayList <ArvoreSystem>();
		cantidadInicialArvores = 1;
		colorFundo = color( 205,98, 124); //color(150, 200,180); //color(255*.111f, 255*.86f, 255f ) ; //
		colocaArvoresIniciais();
		
		barra = new BarraControl(this, cantidadInicialArvores, colorFundo); //Barra de control geral das árvores
		sampleSelector = new SampleSelector(this);
//		sampleSelector.setTituloText("escolher \num som");
		textSize(50);

		scaleZoom = 1;
		translateZoom = new PVector (width/2,height/2);
		modificacionCantArvore = 0;
		PAppletpronto = true;
		
	}
	
	public void colocaArvoresIniciais(){
		for (int a = 0 ; a < cantidadInicialArvores ;  a++) {
			float ang = PApplet.TWO_PI / (a + 1);
			int bpm = 600/ (a+1);
			PVector pos = new PVector ( 0 ,  height*.25f); //posição da entrada de uma 
			arvores.add (new ArvoreSystem(this, pos, height*.4f, 2, 4, bpm, colorFundo) );
		}
	}
	
	public void draw() {
		background(colorFundo);
		if (modificacionCantArvore != 0) { // quando for distinto de 0. Atualização na cantidade de árvores
			colocaEtiraArvores();
		}
		
		for (ArvoreSystem as : arvores) 
			as.atualizaTamanhoArvore();
		
		pushMatrix();
		translate(translateZoom.x, translateZoom.y); 
		scale(scaleZoom);
		marcaAgua();
			
		for (ArvoreSystem as : arvores) {
			as.setTempoArv();	
			as.ativaPontoSom();
			as.desenhar();
			as.codigo.desenhar();
			as.trocaPosicaoDaArbore();  
		}
		popMatrix();
		
		sampleSelector.desenhar();
		barra.desenhar();
	}
	public void colocaEtiraArvores(){
		if (modificacionCantArvore == 1) { //se é 1 soma, mais um 
			float ang = PApplet.TWO_PI * (arvores.size() *.1f);
			float diffx = width * .35f;
			int bpm = 600/ (arvores.size()+1);
			PVector pos = new PVector ( diffx * PApplet.cos(ang) ,   diffx * PApplet.sin(ang) );
			arvores.add (new ArvoreSystem(this,pos , height*.4f, 2, 4, bpm, colorFundo) ); 
		} else {
			if (arvores.size() > 1)
				arvores.remove(arvores.size()-1);
		}
		modificacionCantArvore = 0;
	}
	public void marcaAgua() {
		textSize(height*.04f);
		textAlign(CENTER, CENTER);
		fill(255,20);
		text("ARVORITMO", 0, height*3f);
//		textSize(height*.03f);
//		text("beta", 0, height*.2f);
	}
/*============================================================================================================
 * METODOS PROPRIOS DE UM ACTIVITY, ligados direitamente ao ciclo de vida	
 * http://www.devmedia.com.br/entendendo-o-ciclo-de-vida-de-uma-aplicacao-android/22922
 * ===========================================================================================================
 * */
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
		pdManager.openPatch("ArvoreApp3b.pd", cl.javiercruz.arvore.R.raw.patch);
		pdManager.setTicksPerBuffer(1);
		pdManager.setChanelIn(0);
	//Agrega todos os Strings chaves que vāo receber dados
//		pdManager.addSendMessagesFromPD(<Nome da mensagem>);
		
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/arvoritmo/sessoes");
		myDir.mkdirs();
	}
	@Override 
	public void onRestart(){
		super.onRestart();
		for (ArvoreSystem as : arvores) 
			as.setTempoTo(0, true);
		
		barra.bPlayPause.turnBotaoOff();
		barra.bPlayPause.setNomeBotao("PLAY"); 
	}
	@Override
	public void onResume() {
		super.onResume();
		pdManager.onResume();
		
	}
	@Override
	public void onPause() {
		barra.bPlayPause.turnBotaoOff();
		barra.bPlayPause.setNomeBotao("PLAY"); 
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

/*============================================================================================================
 * METODOS PARA USO DE libPD, comunicação com puredata via mensagens
 * ===========================================================================================================
 * 
 * MESSAGES TO SEND DATA TO PD. Para enviar mensagens pra PD usar os seguentes metodos
 * 	pdBase.sendBang(String receiver);
 * 	pdBase.sendFloat(String receiver, float value);
 *  pdBase.sendSymbol(String receiver, String symbol);
 *	pdBase.sendList(String receiver, Object... list);
 *	pdBase.sendMessage(String receiver, String message, Oject... list);
	
 * TO GET AN ARRAY FROM PD	
 *	float [] newArray = pdManager.getArrayFromPd("nome do array em PD"); este metodo retorna o array.
 * TO SEND AN ARRAY TO PD	
 * 	pdManager.sendArrayToPD( float [] arrayToSend , "nome do array em PD"); este metodo envia um array de float para um outro array em PD
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
	public void callWhenReceiveMessage(String key, String symbol, 	Object... args) {
		// TODO Auto-generated method stub
	}

/*============================================================================================================
 *Métodos callback, chamados cada vez que tem um novo evento na tela
 *============================================================================================================*/
	@Override
	public void screenTouched(int id, float x, float y) {
		// TODO Auto-generated method stub

		PVector posT = new PVector (x,y);
		
		/** Deteção de interação para salvar ou carregar sessões | return */
		if ( barra.escutaSalva(posT) ) {
			salvaSessao();
			return;
		} else if ( barra.escutaCarga(posT) ) {
			escolheSessao();
			return;
		}
		/** Deteção de botão rec, grabação de audio | return */	
		if (barra.escutaBRec(posT)) {
			salvaAudio();
			return;
		}
		/** Seleção do sample Ativo | return */
		if (sampleSelector.escutaClicks( posT ) ) {//define o sample ativo seleccionado
			return;
		}
		/** Seleção de sample desde archivo | NO return */
		if(sampleSelector.abreJanelaSample()) {
			escolheSampleEmDisco(0);
			sampleSelector.abreJanelaSample = false;
		}
		/** Deteção se soma ou resta árvores */
		modificacionCantArvore = barra.escutaGestonadorArvore( posT ); //escuta se soma ou resta árvores
		/** Se não tem modificações na árvore evalua os click no menu expandile Arvore | NO return */	
		if (modificacionCantArvore == 0 ) {
			if (barra.escutaClicks( posT )); //evalua o click na barra só se não tiver modificação na árvore
			
			if (barra.bPlayPause.click && id == 0 && !barra.bPlayPause.ligado) {
				for (ArvoreSystem as : arvores) 
					as.setTempoTo(0, true);
				
			} else if  (barra.bPlayPause.click && id == 0 && barra.bPlayPause.ligado) {
				for (ArvoreSystem as : arvores) 
					as.DaPlay();
			}
		} else {//Se a árvore teve modificações fecha o botão depois de presionnar o botão
			barra.menuArvore.turnBotaoOff(); 
		}
		
		boolean testPlayPause = false; //Se alguma árvore tem ligado o play 'testPlayPause' vira true
		for (ArvoreSystem as : arvores) { //evalua se algum ponto da árvore for pegado 
			as.arvoreListener( posT, scaleZoom, translateZoom, sampleSelector.getSampleAtivo(), 
					sampleSelector.getSampleRandom(), sampleSelector.getCorAtiva(), sampleSelector.getEfectoLigado() );
			modificaArvore(as); //e modifica galhos, e/ou pontos x árvore. Se for o caso
			
			if (as.ArbPlayPause.ligado) { //Se alguma árvore tem ligado o play 'testPlayPause' vira true
				testPlayPause = true;
			}
		}
		
		if (testPlayPause) { //Se alguma árvore tem ligado o play o PLAY geral vira ligado também e mostra a palabra STOP para parar
			barra.bPlayPause.turnBotaoOn();
			barra.bPlayPause.setNomeBotao("STOP"); 
		} else {
			barra.bPlayPause.turnBotaoOff();
			barra.bPlayPause.setNomeBotao("PLAY"); 
		}
			
	}
	private void modificaArvore (ArvoreSystem a) {
		if (a.getEstadoMudanca(0)) { //verifica se o botāo de mudança da arvore foi clicado. O numero é o index do botao
			a.mudancaTamanhoArvore(1); //mudança na árvore, suma um galho
			a.setEstadoBotaoDeMudanca(0, false);
		} else if (a.getEstadoMudanca(1)) {
			a.mudancaTamanhoArvore(-1); //mudança na árvore, resta um galho
			a.setEstadoBotaoDeMudanca(1, false);
		} else if (a.getEstadoMudanca(2)) {
			a.mudancaPontosSom(1);  //mudança no galho, suma um ponto
			a.setEstadoBotaoDeMudanca(2, false);
		} else if (a.getEstadoMudanca(3)) {
			a.mudancaPontosSom(-1);  //mudança no galho, resta um ponto
			a.setEstadoBotaoDeMudanca(3, false);
		} 
	}
	@Override
	public void screenTouchedReleased(int id) {
		emToqueTela = false;
		for (ArvoreSystem as : arvores) {
			as.bpmSelector.botaoAtivo = false; //todos os sliders das árvores viram false
			as.arvoreEmMovimento = false;
		}
		
		// TODO Auto-generated method stub
	//	Log.i(TAG, "mutiTouch, released no id: " + id );
	}
	@Override
	public void screenTouchedDragged(int id, float x, float y, float dist, float ang) {
		// TODO Auto-generated method stub
		
		for (ArvoreSystem as : arvores) {
			//Os if permiten evitar pressionar um botao e no mesmo tempo mover a tela (fazer paneo)
			if ( as.setListenerArvoreMove(new PVector (x, y), scaleZoom, translateZoom ) ) {
				emToqueTela = true;
			//	return;
			} else if ( as.escutaSlider(new PVector (x, y), scaleZoom, translateZoom )) {
				emToqueTela = true;
			//	return;
			}
		}
		Log.i("PApplet Inicial" , " emToqueTela: " + emToqueTela  + " id: " + id );	
		//negocio para atualizar o "pan" no zoom
		if ( !emToqueTela && id == 0) { 
			PVector addToTrans = new PVector (dist * cos(ang), dist * sin (ang));
			translateZoom.add(addToTrans);
		}
		
	}

	@Override
	public void screenTouchedPinch(float pinchMag) {
		// TODO Auto-generated method stub
		emToqueTela = true;
		scaleZoom = scaleZoom + pinchMag*.001f;
		scaleZoom = PApplet.constrain(scaleZoom, .03f, 3);
	}
	public boolean surfaceTouchEvent(MotionEvent me) {
		if (PAppletpronto)  {
			multiTouch.surfaceTouchEvent(me);
			return super.surfaceTouchEvent(me);
		}
	    return false;
	}
	
/*============================================================================================================
* Activities de android
* =================================================================================================================*/
	public void salvaAudio() {
		startActivityForResult(new Intent("cl.javiercruz.arvore.androidActivities.FileChooserRec"), 3);
	}
	public void salvaSessao() {
		startActivityForResult(new Intent("cl.javiercruz.arvore.androidActivities.FileChooserSalvar"), 2);
	}
	public void escolheSampleEmDisco(int seletor){
		
		startActivityForResult(new Intent("cl.javiercruz.arvore.androidActivities.FileChooserMaior"), 0);
		
	}
	public void escolheSessao() {
		startActivityForResult(new Intent("cl.javiercruz.arvore.androidActivities.FileChooserSession"), 1);
	//	degeneraJSON();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		onRestart();
		
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String pathSample = data.getData().toString();
				
				Log.i("onActivityResult", "pathSample: " + pathSample );
				
				String nome = getNameFromPath(pathSample);
				
				Log.i("onActivityResult", "nome Sample: " + nome );
				sampleSelector.setBotaoSampleNome(nome);
				sampleSelector.setBotaoSampleNewPath(pathSample);
				
				PdBase.sendFloat("sample_array", sampleSelector.getIndexLigado()+1 ); //+1 para coincidir com o nome dos arrays em pd, que começam em 1 nçao em 0
				PdBase.sendSymbol("pathSample", pathSample);
			}
		}
		
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String pathSample = data.getData().toString();
				JSONParser parser = new JSONParser();
				
				Log.i(TAG, "pathSample: " + pathSample);
				
				try {
					Object obj = parser.parse(new FileReader(pathSample));
					JSONObject jsonObject = (JSONObject) obj;
					Log.i(TAG, "jsonObject: " + jsonObject);
					degeneraJSON(jsonObject);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				String nomeSessao = data.getData().toString();
				if (nomeSessao != null) {
					generaJSON(arvores, nomeSessao);
				}
			}
			
		}
		if (requestCode == 3) {
			if (resultCode == RESULT_OK) {
				String nomeAudio = data.getData().toString();
				if (nomeAudio != null) {
					barra.comecaGrabacao(nomeAudio);
				}
			} else {
				//Se a grabação for cancelada evalua o click com uma posição imposivel, para deixar o botáo de rec apagado
				barra.escutaBRec(new PVector (0,0)); 
			}
			
		}
	}
	/**
	 * Função para pegar o nome do arquino no path
	 * @param path
	 * @return
	 */
	private String getNameFromPath(String path) {
		String n;
		String[] parts = split(path, '/');
		String nome = parts[ (parts.length - 1 )];
		String[] nomeSplit = split(nome,'.');
		n = nomeSplit[0];
		return n;
	}
/*============================================================================================================
* Funções de leitura e guardado de dados
* =================================================================================================================*/
	
	String saveFilePath;
	String saveFileName;
	
	@SuppressWarnings("unchecked")
	private String generaJSON (ArrayList <ArvoreSystem> arvoreS, String nomaSessao) {
		FileWriter writeFile = null;
		
		JSONObject sessao = new JSONObject();
		JSONArray arvoreArray = new JSONArray();
	
			sessao.put("scaleZoom", scaleZoom );
			sessao.put("translateZoom.x", translateZoom.x );
			sessao.put("translateZoom.y", translateZoom.y );
			sessao.put("pathSample0", sampleSelector.getStringPathSample(0) );
			sessao.put("pathSample1", sampleSelector.getStringPathSample(1) );
			sessao.put("pathSample2", sampleSelector.getStringPathSample(2) );
			sessao.put("pathSample3", sampleSelector.getStringPathSample(3) );
			sessao.put("pathSample4", sampleSelector.getStringPathSample(4) );
			sessao.put("pathSample5", sampleSelector.getStringPathSample(5) );
			sessao.put("pathSample6", sampleSelector.getStringPathSample(6) );
			
			
			for (ArvoreSystem as : arvoreS) { //pasamos por cada árvore
				JSONObject arvoreAux = new JSONObject(); //creamos o objeto que vai se preencher dos dados
				JSONArray galhosAux = new JSONArray(); //criamos um array para a lista de galhos da árvore
				arvoreAux.put("pos_x", as.getPx());  //e damos os valores basicos da árvore
				arvoreAux.put("pos_y", as.getPy());
				arvoreAux.put("ref_tam", as.getReferencaTamanho());
				arvoreAux.put("nivelRecursivo", as.getNivelRecursivo());
				arvoreAux.put("pulsosPorGalho", as.getPulsosPorGalho());
				arvoreAux.put("bpm", as.getBpm());
				
				for ( Galho g : as.galhos) { //Pasamos por cada galho da árvore
					JSONArray pontosAux = new JSONArray(); //e criamos um array para os pontos de som, pegaremos uma lista de nome de sons e efeitos
					
					int i= 0;
					for ( ArrayList <String> listaBangsG : g.listaBangs ) { //Pegamos o ArrayList que cada ponto tem, onde o index 0 é o nome do som
						JSONObject pontoDeSom = new JSONObject();
						pontoDeSom.put("nomeDoSom", listaBangsG.get(0) ); 
						pontoDeSom.put("nomeDoEfeito", listaBangsG.get(1) );//o index 1 é o nome do  efeito	
						pontoDeSom.put("colorPonto", g.pontosNota.get(i).getActualColor() );
						pontosAux.add(pontoDeSom); //Fica um objeto por ponto, com os nomes do som e do efeito
						i++;
					}
					galhosAux.add(pontosAux); //O array de Pontos se coloca no array de galhos
				}
				
				arvoreAux.put("galhos", galhosAux); //e o array de galhos se coloca no objeto auxiliar árvore
				arvoreArray.add(arvoreAux); 	//e esse se coloca no array de árvores
			}
			sessao.put("arvores", arvoreArray); //E o array de árvores se coloca no objeto que salva a sessão	
			
		try{
			String root = Environment.getExternalStorageDirectory().toString(); //raiz do dispositivo
			File myDir = new File(root + "/arvoritmo/sessoes");
			myDir.mkdirs();
			SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmm");
			Date now = new Date();
			String dataEmFormato = formatter.format(now);
			String fname = dataEmFormato + "sessao_arvore" ;
			saveFilePath = myDir.getAbsolutePath();
			saveFileName =  myDir + "/" + nomaSessao + ".json";
			
			
			writeFile = new FileWriter(saveFileName);
			//Escreve no arquivo conteudo do Objeto JSON
			writeFile.write(sessao.toString());
			writeFile.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return (sessao.toString());
	}
	
	private void degeneraJSON ( JSONObject data) {
		while (arvores.size() >= 1)
			arvores.remove(arvores.size()-1);
		
		
			JSONObject sessao = data;
			JSONArray arvoreArray;
			
			scaleZoom = ( (Double) sessao.get("scaleZoom") ).floatValue();
			float tanslateX = ( (Double) sessao.get("translateZoom.x") ).floatValue();//  (float) sessao.getDouble("translateZoom.x");
			float tanslateY = ( (Double) sessao.get("translateZoom.y") ).floatValue();//sessao.getDouble("translateZoom.y");
			translateZoom = new PVector (tanslateX, tanslateY);
			
			//Pegamos o array com os path dos samples utilizados, carregamos eles e atualizamos no sampleSelector
			String [] pathSamples = new String[7];
			for (int i=0 ; i < pathSamples.length ; i++ ) {
				String nomeKeyJSON = "pathSample" + i;
				pathSamples[i] =  (String) sessao.get(nomeKeyJSON);	
				Log.i(TAG, "pathSamples["+i+"]: " + pathSamples[i] );
				PdBase.sendFloat("sample_array",i+1 ); //+1 para coincidir com o nome dos arrays em pd, que começam em 1 nçao em 0
				PdBase.sendSymbol("pathSample", pathSamples[i]);
				
				String nome = getNameFromPath(pathSamples[i]);
				sampleSelector.setBotaoSampleNome(nome, i);
			}			
			sampleSelector.setPathSamples(pathSamples);
			
			
			arvoreArray = (JSONArray) sessao.get("arvores");//  .getJSONArray("arvores");
			
			for (int i=0 ; i < arvoreArray.size() ; i++) { //pasamos colocando cada árvore
				//JSONObject arvoreAux = arvoreArray.getJSONObject(i); //creamos o objeto que vai se preencher dos dados
				JSONObject arvoreAux = (JSONObject) arvoreArray.get(i); 
				JSONArray galhosAux; //criamos um array para a lista de galhos da árvore
				galhosAux = (JSONArray) arvoreAux.get("galhos");
				
				float pos_x = ( (Double) arvoreAux.get("pos_x") ).floatValue();
				float pos_y = ( (Double) arvoreAux.get("pos_y") ).floatValue();
				float ref_tam = ( (Double) arvoreAux.get("ref_tam") ).floatValue();
				int nivelRecursivo = ( (Long) arvoreAux.get("nivelRecursivo") ).intValue();
				int pulsosPorGalho = ( (Long) arvoreAux.get("pulsosPorGalho") ).intValue();
				int bpm = ( (Long) arvoreAux.get("bpm") ).intValue();
				Log.i(TAG, "bpm: " + bpm );
				arvores.add (new ArvoreSystem(this, new PVector(pos_x, pos_y), ref_tam, nivelRecursivo, pulsosPorGalho, bpm, colorFundo) ); //creamos el objeto

				
				for ( int j=0 ; j < galhosAux.size() ; j++ ) { //Pasamos por cada galho da árvore
					JSONArray pontosAux;// = new JSONArray(); //e criamos um array para os pontos de som, pegaremos uma lista de nome de sons e efeitos
					pontosAux = (JSONArray) galhosAux.get(j);
					
					for ( int k=0 ; k < pontosAux.size() ; k++) {  //ArrayList <String> listaBangsG : g.listaBangs ) { //Pegamos o ArrayList que cada ponto tem, onde o index 0 é o nome do som
						JSONObject pontoDeSom = (JSONObject) pontosAux.get(k);
						String nomeSom = (String) pontoDeSom.get("nomeDoSom");
						String nomeEfeito = (String) pontoDeSom.get("nomeDoEfeito");
						int colorP = color ( ( (Long) pontoDeSom.get("colorPonto") ).intValue() );
						
						
					//	Log.i(TAG, "arvores.size()-1: " + (arvores.size()-1) + " galho: " + j + " ponto: " + k );
						
					//	arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(0, nomeSom);
					//	arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(1, nomeEfeito);
						if (nomeSom.equals("silencio")  ) {
							arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(0,"silencio");
							arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(1,"sin_efecto");
						}
						else {
							arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(0, nomeSom);
							arvores.get(arvores.size()-1).galhos.get(j).listaBangs.get(k).set(1, nomeEfeito);
							arvores.get(arvores.size()-1).galhos.get(j).pontosNota.get(k).setColor(colorP);
							if (nomeEfeito == "sin_efecto")
								arvores.get(arvores.size()-1).galhos.get(j).pontosNota.get(k).conEfecto = true;
							else
								arvores.get(arvores.size()-1).galhos.get(j).pontosNota.get(k).conEfecto = false;
						}
						
				//		Log.i(TAG, "colorP: " + colorP );
						
					}
				}
			}
	}

}

