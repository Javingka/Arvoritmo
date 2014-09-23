package cl.javiercruz.arvore.processing.arvoresystem;

import org.puredata.core.PdBase;

import android.util.Log;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Clase que desenha os pontos da árvore
 * @author javingka
 *
 */
public class PontoNota {
	private static int CONTADOR;
	private int id;
	public static float diametroOff;
	public static float diametroOnPlay;
	public float diametro;
	PApplet p5;
	PVector pos;
	boolean ativo;
	private int colorOff;
	private int colorOn;
	private int colorOffSound;
	private int pegaColor;
	private int numPosicaoNoGalho;
	private boolean filtro;
	public boolean conEfecto;
	
	PontoNota (PApplet _p5, PVector _pos, int posNoGalho) {
		p5 = _p5;
	    pos = _pos;
	    id = CONTADOR;
	    ativo = false;
//	    colorOff = p5.color((125*255)/360, 255, 255);
//	    colorOn = p5.color((300*255)/360, 255, 255);
	    colorOffSound = p5.color(150, 200,180); 
	    pegaColor = colorOffSound;
	    numPosicaoNoGalho = posNoGalho;
	    diametro = diametroOff;
	    CONTADOR++;
	    filtro = false;
//	    Log.i("PontoNota", "colorOffSound: "+colorOffSound);
	    conEfecto = false;
	  }
	
	/**
	 * 
	 * @param evaluar PVector da posiçāo to toque na tela pra evaluar se toca algum ponto
	 * @param translatePai PVector com a pcolorOffSoundosiçāo base da arbore ao qual pertece o ponto
	 * @param scalaZoom nivel de zoom na tela
	 * @param translateZoom PVector da posiçāo deslocada da tela
	 * @return retorna true se o ponto for clicado
	 */
	public boolean pontoOnClick (PVector evaluar, PVector translatePai, float scalaZoom, PVector translateZoom) {
		boolean resp = false;
//		PVector temp = new PVctor ( (pos.x + translatePai.x + translateZoom.x) * scalaZoom , 
//										(pos.y + translatePai.y + translateZoom.y) * scalaZoom);
		PVector temp = new PVector ( ((pos.x + translatePai.x )* scalaZoom )+ translateZoom.x , 
				((pos.y + translatePai.y) * scalaZoom ) + translateZoom.y);
		
		float dist = PVector.dist(temp, evaluar);
		
		if (dist < (diametro) * scalaZoom  ) {
			resp = true;
		}
		return resp;
	}
	
	private void atualiza(){
		diametro = diametroOff;
	}
	public void desenhar() {
		atualiza();
		float diam = diametro;
	   p5.pushMatrix();
	   p5.translate(pos.x, pos.y);
	   p5.strokeWeight(diametro * .1f);
/*	   if (!ativo){
		   p5.strokeWeight(diametro * .2f);
		   diam *= .8f;
	   } */
	   p5.fill (pegaColor);
	   p5.ellipse(0,0, diam, diam);
	   if (conEfecto) {
		   p5.fill (255);
		   p5.ellipse(0,0, diam*.4f, diam*.4f);  
	   }
		   
	   p5.popMatrix();
	  } 
	  
	  public int switchToAtivo(){
//		float var =  PApplet.cos( p5.frameCount * .005f);
//		var = PApplet.map(var, -1, 1, 0, 255);
//		colorOn = p5.color(var, 255, 255);
		colorOn = p5.color(255);//255/6, 255, 255);  
		pegaColor = colorOn;
		diametro =diametroOnPlay;
	    ativo = true;
	    filtro = false;
	    return 		pegaColor;
	  }
	  public void switchToColorOffSound() {
		  int pc = (int) p5.hue(colorOffSound);
		 //Log.i("PontoNota", "INSIDE: " + pc + " filtro: " + filtro);
		if (filtro) {
			  pegaColor = p5.color(pc,255, 255, 150);
		} else
			pegaColor = p5.color(pc, 255, 255);
	  }
	  public void botaFiltroNaCor() {
		  filtro = true;
	  }
	  public void tiraFiltroNaCor() {
		  filtro = false;
	  }
	  public void switchToOff(int indexPonto){
		  filtro = false;
		float var =  PApplet.cos( p5.frameCount * .005f);
		var = 150 * (1 + var*.1f );
		if (indexPonto % 2 == 1)
			colorOff = p5.color(150, 200,180, var); //(125*255)/360, 50, 120, var);
		else 
			colorOff = p5.color(150, 200,180, var-50); //(125*255)/360, 50, 120, var-50);
		
		pegaColor = colorOff;
		  
		ativo = false;  
		
	  }
	  
	  public void setNewPos(PVector pv){
		  pos = pv;
	  }
	  public PVector getPos(){
		  return pos;
	  }
	  public int getActualColor(){
		  return pegaColor;
	  }
	  public void setColor( int _novaCor ) {
		  int h = (int) p5.hue(_novaCor);
				  
		  colorOffSound = p5.color(h,255,255);
	  }
	  public void setColorHSB( int _novaCor ) {
		  int h = (int) p5.hue(_novaCor);
		  int s = (int) p5.saturation(_novaCor);
		  int b = (int) p5.brightness(_novaCor);
		  colorOffSound = p5.color(h,s,b);
	  }
	  public int getColorOn(){
		  return colorOn;
	  }
}
