package cl.javiercruz.arvore.processing.barra;

import android.util.Log;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class BotaoNomeRect {
	private PApplet p5;
	private String nomeBotao;
	private PFont roboto;
	private PVector posicaoBotao;
	private PVector posicaoBotaoOff;
	private float widthB, heightB;
	private float widthBOff;
	private boolean ligado;
	private int colorOff;
	private int colorOn;
	private PVector posTran = new PVector(); //PVector para transicao de posicao entre off e on do botao
	private float widthTran; //largura variabel do retangulo botao, 
	private float anguloDirec;
	private boolean direita = false;
	private int colorMuda;
	private int frameCountBase; //variavel para ter um contador do tempo desde que o botão tem sido ativado
	
	public BotaoNomeRect(PApplet _p5, String nomeBotao) {
		p5 = _p5;
		this.nomeBotao = nomeBotao; 
//		String[] fontList = PFont.list();
//		PApplet.println("ListaFontes: ");
//		PApplet.println(fontList);
		roboto = p5.createFont("Monospaced-Bold", 45, true);
		p5.textFont(roboto);
		widthB = p5.width*.3f;
		heightB = p5.height*.1f;
		posicaoBotao = new PVector(widthB*.5f, heightB*.5f);
		posicaoBotaoOff = new PVector(widthB*.25f, heightB*.5f);
		widthBOff = widthB*.25f;
		ligado = false;
		colorOn = p5.color(203,108,66) ; //p5.color(255 * 34/360f, 255 * .99f, 255,60);
		colorOff = p5.color(11, 191,  231) ; //p5.color(255 * 40/360f, 255 * .86f, 255 * 1f, 120);
		anguloDirec = 0;
		colorMuda = p5.color(0);
	}
	BotaoNomeRect(PApplet _p5, String nomeBotao, String direitaSup) {
		this( _p5, nomeBotao) ;
		direita = true;
		anguloDirec = p5.PI;
		posicaoBotao = new PVector(p5.width - widthB*.5f, heightB*.5f);
		posicaoBotaoOff = new PVector(p5.width - widthB*.25f, heightB*.5f);
	}
	
	public void desenhaNomeB(){
		p5.pushStyle();
		p5.pushMatrix();
		if (!direita)
			p5.textAlign(PApplet.LEFT, PApplet.CENTER);
		else
			p5.textAlign(PApplet.RIGHT, PApplet.CENTER);
		
		p5.rectMode(PApplet.CENTER);
		p5.noStroke();
		
		if (ligado) {
			colorMuda = colorOn; //lerpColorEsp(colorMuda,colorOn);
			float velocidade = .1f * ( PApplet.constrain(PApplet.map ( (p5.frameCount - frameCountBase), 0, 60, 0f, 1f), 0f,1f) );
			posTran = PVector.lerp(posTran, posicaoBotao, velocidade);
			widthTran = PApplet.lerp(widthTran, widthB, velocidade);
			p5.fill(colorMuda);
		} else {
			frameCountBase = p5.frameCount;
			colorMuda = colorOff; //lerpColorEsp(colorMuda,colorOff);
			posTran = PVector.lerp(posTran, posicaoBotaoOff , .1f);
			widthTran = PApplet.lerp(widthTran , widthBOff, .1f);
			p5.fill(colorMuda);
		}
		
		
		
		p5.translate(posTran.x, posTran.y);
		p5.rotate(anguloDirec);
		p5.rect(0,0, widthTran, heightB);
		p5.fill(255);
		
		p5.translate(widthB*.2f, 0);
		p5.rotate(-anguloDirec);
		p5.textSize(heightB/3);
		p5.text(nomeBotao, 0,0);
		p5.popStyle();
		p5.popMatrix();
	}
	private int lerpColorEsp(int c1, int c2){
		float h = p5.hue(c1);
		float b = p5.brightness(c1);
		float s = p5.saturation(c1);
		
		h = PApplet.lerp (h, p5.hue(c2), .1f );
		b = PApplet.lerp (b, p5.brightness(c2), .1f );
		s = PApplet.lerp (s, p5.saturation(c2), .1f );
		
		int c = p5.color(h,s,b);
		return c;
	}
	/**
	 * 
	 * @param evaluar
	 * @return boolean
	 */
	public boolean botaoOnClick (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		
		if (evaluar.x > (posTran.x - widthTran*.5f) && 
			evaluar.x < (posTran.x + widthTran*.5f) &&
			evaluar.y > (posicaoBotao.y - heightB*.5f) && 
			evaluar.y < (posicaoBotao.y + heightB*.5f) ) {
			resp = true;
			ligado = !ligado;
		}
				
		return resp;
	}
	public boolean botaoOnClick (PVector evaluar, int colorSampleAtivo) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		
		if (evaluar.x > (posTran.x - widthTran*.5f) && 
			evaluar.x < (posTran.x + widthTran*.5f) &&
			evaluar.y > (posicaoBotao.y - heightB*.5f) && 
			evaluar.y < (posicaoBotao.y + heightB*.5f) ) {
			resp = true;
			ligado = !ligado;
			colorOn = colorSampleAtivo;
		}
				
		return resp;
	}
	public boolean estaLigado(){
		return ligado;
	}
	public float getBotaoWith(){
		return widthB;
	}
	public float getBotaoHeight(){
		return heightB;
	}
	public void turnBotaoOff() {
		ligado = false;
	}
	public void turnBotaoOn() {
		ligado = true;
	}
}
