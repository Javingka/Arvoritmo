package cl.javiercruz.arvore.processing.arvoresystem;

import java.util.ArrayList;

import org.puredata.core.PdBase;

import android.util.Log;

import processing.core.PApplet;
import processing.core.PVector;

public class Galho {
	public static int GALHO_ON_PLAY = 0;
	private int id;
//	public static int CONTADOR;
	PApplet p5;
	private PVector pos1, pos2;
	private PVector colunaDoGalho;
	private float comprimento;
	private float angulo;
	public ArrayList <PontoNota> pontosNota; 
//	public ArrayList <String> bangsPorPontos;
	public ArrayList <ArrayList> listaBangs;
//	public ArrayList <Brote> brotes;
	public ArrayList <ArrayList> listaBrotes;
	private int tempoPorGalho;
	private int colorOff;
	private int colorOn;
	private int pegaColor;
	private int colorOnSound;
	private int indexEmSom;
	private int nivelRecursivo;
	private float largura;
	private boolean enLinhaSom;
	private String sinEfecto;
	
	Galho (PApplet _p5, PVector _pos1, PVector _pos2, int tempos, int _nivelRecursivo, int manualId) {
		p5 = _p5;
		id = manualId; //CONTADOR;
//		CONTADOR++;
		nivelRecursivo = _nivelRecursivo;
		pos1 = _pos1;
		pos2 = _pos2;
		tempoPorGalho = tempos;
		colorOff = p5.color(24, 145.35f, 63.75f); // p5.color(123*255/360, 180, 180);
		colorOn = p5.color(11, 191,  231); //p5.color(0); //p5.color(255 * 15/360f, 255 * .85f, 255 * .95f); //p5.color(255 * 15/360f, 255 * .85f, 255 * .95f); //p5.color(123*16/360, 70, 70); 
		colorOnSound = p5.color (211, 186, 229); //p5.color(255 * 34/360f, 255 * .57f, 255 * .5f); // p5.color(123*255/360, 20, 20);
		pegaColor = colorOff;
		pontosNota = new ArrayList <PontoNota>(tempoPorGalho);
//		brotes = new ArrayList<Brote>();
		listaBrotes = new ArrayList<ArrayList>();
		listaBangs = new ArrayList<ArrayList>();//Lista da mensagems para reproducir os sons
		sinEfecto = "sin_efecto";
		for (int lb = 0 ; lb < tempoPorGalho ; lb++) {
			listaBrotes.add(new ArrayList <Brote>());
			listaBangs.add(new ArrayList <String>());
			listaBangs.get( listaBangs.size() -1).add("silencio");
			listaBangs.get( listaBangs.size() -1).add(sinEfecto);
		}
		
		setColunaDoGalho(pos1, pos2);
		largura  = comprimento * (.08f / (nivelRecursivo+1) ); 
		indexEmSom = -1;
		
		enLinhaSom = false;
	}
	
	public int getId() {
		return id;
	}
	public void atualizar(){
		   // setColunaDoGalho(pos1, pos2);
		    atualizaColunaDoGalho(pos1, pos2);
	}
	public void styleOn() {
		pegaColor = colorOn;
		enLinhaSom = true;
	}
	public void styleOff(){
		pegaColor = colorOff;
		indexEmSom = -1;
		enLinhaSom = false;
	}
	public void styleOnSound() {
		pegaColor = colorOnSound;
		indexEmSom = -1;
		enLinhaSom = true;
	}
	public void desenhar(){
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate (pos1.x, pos1.y); 
		p5.strokeWeight( largura );
		p5.stroke(pegaColor);
		p5.line (	
				(PontoNota.diametroOff *.75f) * PApplet.cos(angulo),
				(PontoNota.diametroOff *.75f) * PApplet.sin(angulo), 
				comprimento  * PApplet.cos(angulo), 
				comprimento * PApplet.sin(angulo)  
				); 
		p5.fill(0,25,120);
		p5.fill(0,255,255);
	//	p5.text(id, (comprimento/2) * PApplet.cos(angulo), (comprimento/2) * PApplet.sin(angulo));
		p5.popMatrix();
		
		for (int i = 0 ; i < pontosNota.size() ; i++) {
//			Log.i("Galho","i: " + i +" indexEmSom: "+indexEmSom);
			if (i == indexEmSom) { //Evalua se o ponto esta ativo 
			//	brotes.setColor(pontosNota.get(i).getActualColor());
				pontosNota.get(i).switchToAtivo();
			} else if (listaBangs.get(i).get(0) != "silencio" ) { //se não evalua se tem um som nele
				if (!enLinhaSom) { //evalua se o punto é parte do percuso ativo do pulso
//					Log.i("Galho","enLinhaSom: " + enLinhaSom );
					pontosNota.get(i).botaFiltroNaCor();
					pontosNota.get(i).switchToColorOffSound();
				} else {
					pontosNota.get(i).tiraFiltroNaCor();
					pontosNota.get(i).switchToColorOffSound();
				}
		    } else { //Se o ponto náo é o atual nem tem um som asociado, desenha desligado
				pontosNota.get(i).switchToOff(i);
		    }
			pontosNota.get(i).desenhar();
		}
		
		//desenha os brotes
		for (int j = 0 ; j < listaBrotes.size() ; j++ ){
			for (int k = 0 ; k < listaBrotes.get(j).size() ; k ++ ) {
				if (j == indexEmSom) {
					((Brote) listaBrotes.get(j).get(k)).setColor(  pontosNota.get(j).getActualColor() ); //switchToAtivo
				}
				((Brote) listaBrotes.get(j).get(k)).desenhar();
			}
		}
		
		p5.popStyle();
		
	}
	public void desenharEmPause(){
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate (pos1.x, pos1.y); 
		p5.strokeWeight( largura );
		p5.stroke(pegaColor);
		p5.line (	
				(PontoNota.diametroOff *.75f) * PApplet.cos(angulo),
				(PontoNota.diametroOff *.75f) * PApplet.sin(angulo), 
				comprimento  * PApplet.cos(angulo), 
				comprimento * PApplet.sin(angulo)  
				); 
		p5.fill(0,25,120);
		p5.fill(0,255,255);
	//	p5.text(id, (comprimento/2) * PApplet.cos(angulo), (comprimento/2) * PApplet.sin(angulo));
		p5.popMatrix();
		
		for (int i = 0 ; i < pontosNota.size() ; i++) {
			
			if (listaBangs.get(i).get(0) != "silencio" ) { //se não evalua se tem um som nele
				if (!enLinhaSom) { //evalua se o punto é parte do percuso ativo do pulso
//					Log.i("Galho","enLinhaSom: " + enLinhaSom );
					pontosNota.get(i).botaFiltroNaCor();
					pontosNota.get(i).switchToColorOffSound();
				} else {
					pontosNota.get(i).tiraFiltroNaCor();
					pontosNota.get(i).switchToColorOffSound();
				}
		    } else { //Se o ponto náo é o atual nem tem um som asociado, desenha desligado
				pontosNota.get(i).switchToOff(i);
		    }
			pontosNota.get(i).desenhar();
		}
		
		//desenha os brotes
		for (int j = 0 ; j < listaBrotes.size() ; j++ ){
			for (int k = 0 ; k < listaBrotes.get(j).size() ; k ++ ) {
				if (j == indexEmSom) {
					((Brote) listaBrotes.get(j).get(k)).setColor(  pontosNota.get(j).getColorOn() ); //switchToAtivo
				}
				((Brote) listaBrotes.get(j).get(k)).desenhar();
			}
		}
		
		p5.popStyle();
		
	}
	public void setPontoParaAtivo(int index) {
		int antIndex = indexEmSom;
		indexEmSom = index;
		if (antIndex != indexEmSom) {
			if ( listaBangs.get(indexEmSom) != null ) {
				if ( (String) listaBangs.get(indexEmSom).get(1) == "sin_efecto") { 
					PdBase.sendFloat("efecto"+(String) listaBangs.get(indexEmSom).get(0), 0);
//					pontosNota.get(indexEmSom).conEfecto = false;
				} else {
					PdBase.sendFloat( (String) listaBangs.get(indexEmSom).get(1), 1);
//					pontosNota.get(indexEmSom).conEfecto = true;
				}
//				Log.i("Galho", "efecto: " + (String) listaBangs.get(indexEmSom).get(1) );
				
				PdBase.sendBang( (String) listaBangs.get(indexEmSom).get(0) );
				
			}
		}
			
	}
	
	private void setColunaDoGalho (PVector a, PVector b ){
		colunaDoGalho = PVector.sub(b, a);
		comprimento = colunaDoGalho.mag();
		PVector horRef = new PVector (1,0);
		angulo = PVector.angleBetween(horRef, colunaDoGalho);
		if (colunaDoGalho.y < horRef.y)
		  angulo = 2*PApplet.PI-angulo;  //angulo de inclinaçāo do galho
		  
		pontosNota.clear(); //limpa a lista de notas para desenhas as novas
		int segmentos = tempoPorGalho * 2; 
		float comprimentoSeg = comprimento / segmentos;
		    
		for (int i = 0 ; i < tempoPorGalho ; i++) {
		   float com = (comprimentoSeg * 2) + (comprimentoSeg * 2 * i );
		 //     println("com: " +com);
		   PVector posTem = new PVector (pos1.x + com * PApplet.cos (angulo), pos1.y + com * PApplet.sin (angulo) );
		   pontosNota.add( new PontoNota (p5, posTem, i) ); 
		}  
	}
	private void atualizaColunaDoGalho (PVector a, PVector b ){
		colunaDoGalho = PVector.sub(b, a);
		comprimento = colunaDoGalho.mag();
		PVector horRef = new PVector (1,0);
		angulo = PVector.angleBetween(horRef, colunaDoGalho);
		if (colunaDoGalho.y < horRef.y)
		  angulo = 2*PApplet.PI-angulo;  //angulo de inclinaçāo do galho
		  
//		pontosNota.clear(); //limpa a lista de notas para desenhas as novas
		int segmentos = tempoPorGalho * 2; 
		float comprimentoSeg = comprimento / segmentos;
		    
		for (int i = 0 ; i < tempoPorGalho ; i++) {
		   float com = (comprimentoSeg * 2) + (comprimentoSeg * 2 * i );
		 //     println("com: " +com);
		   PVector posTem = new PVector (pos1.x + com * PApplet.cos (angulo), pos1.y + com * PApplet.sin (angulo) );
		   pontosNota.get(i).setNewPos(posTem); 
		}  
	}
	
	public void atualizaPosGalho(PVector a, PVector b, int novoTempo ){
		pos1 = a;
		pos2 = b;
		
		int tempoAnt = tempoPorGalho;
		tempoPorGalho = novoTempo;
		
		colunaDoGalho = PVector.sub(b, a);
		comprimento = colunaDoGalho.mag();
		PVector horRef = new PVector (1,0);
		angulo = PVector.angleBetween(horRef, colunaDoGalho);
		if (colunaDoGalho.y < horRef.y)
		  angulo = 2*PApplet.PI-angulo;  //angulo de inclinaçāo do galho
		
//		Log.i("Galho","tempoAnt: "+tempoAnt+" tempoPorGalho: "+tempoPorGalho);
	//	pontosNota.clear(); //limpa a lista de notas para desenhas as novas
		int segmentos = tempoPorGalho * 2; 
		float comprimentoSeg = comprimento / segmentos;
		    
		for (int i = 0 ; i < tempoPorGalho ; i++) {
			
		   float com = (comprimentoSeg * 2) + (comprimentoSeg * 2 * i ); //calculo da distancia ente os pontos
		   //cria um vetor com a distança e a direçāo do novo ponto
		   PVector posTem = new PVector (pos1.x + com * PApplet.cos (angulo), pos1.y + com * PApplet.sin (angulo) );
		   
		   if ( i < tempoAnt ) {
			   //atualiza a posiçāo dos pontos de som
			   pontosNota.get(i).setNewPos(posTem); 
			   
			   //atualiza a posiçāo dos brotes
			   for (int j = 0 ; j < listaBrotes.get(i).size() ; j++) {
					  ((Brote) listaBrotes.get(i).get(j)).setNewPos( posTem );
			   }
			   
		   } else {
			   //agrega um novo ponto de som
			   pontosNota.add( new PontoNota (p5, posTem, i) );
			   
			   //agrega brotes para o novo ponto de som
			   listaBrotes.add(new ArrayList <Brote>());
			   listaBangs.add(new ArrayList <String>());
			   listaBangs.get( listaBangs.size() -1).add("silencio");
			   listaBangs.get( listaBangs.size() -1).add(sinEfecto);
		   }
		   
		   if ( tempoAnt - tempoPorGalho > 0) {
			   for (int k = pontosNota.size()-1 ; k >= tempoPorGalho; k-- ) { 
				    pontosNota.remove(k);
//					Log.i("ArvoreSystem","k: " + k);
					listaBrotes.remove(k);
					listaBangs.remove(k);
				}
		   }
		   
		   
		  
		}
	}
	
	public void atualizaPosGalho(PVector a, PVector b){
		pos1 = a;
		pos2 = b;
		
		colunaDoGalho = PVector.sub(b, a);
		comprimento = colunaDoGalho.mag();
		PVector horRef = new PVector (1,0);
		angulo = PVector.angleBetween(horRef, colunaDoGalho);
		if (colunaDoGalho.y < horRef.y)
		  angulo = 2*PApplet.PI-angulo;  //angulo de inclinaçāo do galho
		  
	//	pontosNota.clear(); //limpa a lista de notas para desenhas as novas
		int segmentos = tempoPorGalho * 2; 
		float comprimentoSeg = comprimento / segmentos;
		    
		for (int i = 0 ; i < tempoPorGalho ; i++) {
		   float com = (comprimentoSeg * 2) + (comprimentoSeg * 2 * i );
		 //     println("com: " +com);
		   PVector posTem = new PVector (pos1.x + com * PApplet.cos (angulo), pos1.y + com * PApplet.sin (angulo) );
			  pontosNota.get(i).setNewPos(posTem);
		 
			  for (int j = 0 ; j < listaBrotes.get(i).size() ; j++) {
				((Brote) listaBrotes.get(i).get(j)).setNewPos( posTem );
			  }
		}
	}
	public int getNivelRecursivo(){
		return nivelRecursivo;
	}
	public PVector getFinalGalho(){
		PVector fin = new PVector (pos1.x + ( comprimento  * PApplet.cos(angulo) ) ,  pos1.y + (comprimento * PApplet.sin(angulo)) );  
		return fin;
	}
}
