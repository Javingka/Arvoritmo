package cl.javiercruz.arvore.processing.barra;

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
		colorOn = p5.color(200,150);
		colorOff = p5.color(255,50);
		anguloDirec = 0;
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
			p5.textAlign(p5.LEFT, p5.CENTER);
		else
			p5.textAlign(p5.RIGHT, p5.CENTER);
		
		p5.rectMode(p5.CENTER);
		p5.noStroke();
		
		if (ligado) {
			posTran = PVector.lerp(posTran, posicaoBotao, .1f);
			widthTran = PApplet.lerp(widthTran, widthB, .1f);
			p5.fill(colorOn);
		} else {
			posTran = PVector.lerp(posTran, posicaoBotaoOff , .1f);
			widthTran = PApplet.lerp(widthTran , widthBOff, .1f);
			p5.fill(colorOff);
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
