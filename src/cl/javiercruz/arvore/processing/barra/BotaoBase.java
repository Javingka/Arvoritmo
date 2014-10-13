package cl.javiercruz.arvore.processing.barra;

import android.util.Log;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class BotaoBase {
	public PApplet p5;
	public PVector pos, posII; //pos é a posição por defeito da classe, posII permite botar uma segunda posição
	public PVector PosEfetiva; //posicao efetiva em cada frame, permite transição entre posições
	public float diamMen; //diametro do botáo quando é menor
	public float diam;
	public float diamEfeivo; //diametro  em cada frame, permite transição entre posições
	protected int colorOff;
	protected int colorOn;
	public int cargaColor;
	public boolean ligado;
	public boolean mudandoOn;
	public boolean mudandoOff;
	public boolean click; //só para saber quando foi clicado
	public String etiqueta;
	public String textoBotao;
	private String posVer, posHor;
	public float tx, ty; //posicao do texto do botao
	public boolean botaoToggle;
	private boolean colorOffPorDefecto;
	private float tamanhoEtiqueta;
	private boolean posicaoDefault;
	public float tamanhoTextoBotao;
	PImage logoBotao;
	private boolean temIcone = false;
	/**
	 * 
	 * @param _p5
	 * @param _pos
	 * @param _diam
	 * @param _color
	 */
	public BotaoBase (PApplet _p5, PVector _pos, float _diam, int _color) {
		p5 = _p5;
		pos = _pos;
		diam = _diam;
		colorOn = _color;
		int hc = (int) p5.hue(colorOn);
		colorOff = p5.color(hc, 120,120);
		cargaColor = colorOff;
		ligado = false;
		click = false;
		p5.textAlign(PApplet.CENTER, PApplet.CENTER);
		setPosicaoTexto("abaixo", "centro");
		textoBotao = "Nome do Botao";
		botaoToggle = false;
		colorOffPorDefecto = true;
		posicaoDefault = true;
		tamanhoEtiqueta = diam * .75f;
		PosEfetiva = pos;
		tamanhoTextoBotao = diam * .55f;
		diamEfeivo = diam;
		diamMen = diamEfeivo;
	}
	public void setImageBotao ( String nomeArq ) {
		logoBotao = p5.loadImage(nomeArq);
		temIcone = true;
	}
	public void setPosBotaoMenor(PVector p){
		posII = new PVector (p.x, p.y);
	}
	public void setDiamMen(float d){
		diamMen = d;
	}
	/**
	 * Aplicação de um offset para obter uma segunda posição no botão
	 * @param offset
	 */
	public void setOffsetToPosII(PVector offset) {
		posII = PVector.add(pos, offset);
	}
	public void setPosicaoDefault( boolean np){
		posicaoDefault = np;
	}
	public boolean getPosDefault() {
		return posicaoDefault;
	}
/**
 * 
 * @param ver String: centro, esquerda ou direita
 * @param hor String: centro, acima ou abaixo
 */
	public void setPosicaoTexto(String ver, String hor){

		posVer = ver;
		posHor = hor;
		atualizaDataTexto();
	}
	public void setTamanhoTexto(float tamIn){
		tamanhoTextoBotao = tamIn;
	}
	public void setColorOff(int colorn){
		colorOff = colorn;
		colorOffPorDefecto = false;
		atualizaCor();
	}
	public void setColorON (int cOn) {
		colorOn = cOn;
	}
	public void setColorFixo(int col){
		cargaColor = col;
	}
	public void setBotaoComoToggle(){
		botaoToggle = true;
	}
	public void atualizaDataTexto() {
		int pTx, pTy;
		if (posHor == "esquerda") {
			pTx = PApplet.RIGHT;
			tx = -diam;
		} else if (posHor == "direita") {
			pTx = PApplet.LEFT;
			tx = diam;
		} else {
			pTx = PApplet.CENTER;
			tx = 0;
		}
		if (posVer == "acima") {
			pTy = PApplet.TOP;
			ty = -diam;
		} else if (posVer == "abaixo") {
			pTy = PApplet.BOTTOM;
			ty = diam*1.25f;
		} else {
			pTy = PApplet.CENTER;
			ty = 0;		
		}
		p5.textAlign(pTx, pTy);
	}
	public void desenhaTexto() {
		
		p5.fill(255);
		p5.textSize(tamanhoTextoBotao);
		p5.text(textoBotao, tx, ty);
	}
	public void desenhaTextoYOffset( float YOff) {
		p5.fill(255);
		p5.textSize(tamanhoTextoBotao);
		p5.text(textoBotao, tx, ty + YOff);
	}
	public void desenhaTextoGira(float ang) {
		p5.pushMatrix();
		p5.rotate(ang);
		p5.fill(255, 120);
		p5.textSize(tamanhoTextoBotao);
		p5.text(textoBotao, tx, ty);
		
		p5.popMatrix();
	}
	public void setNomeBotao(String novoNome){
		textoBotao = novoNome;
	}
	public void setEtiqueta(String newLabel) {
		etiqueta = newLabel;
	}
	public void setNovaPos( PVector nPos) {
		pos = nPos;
	}
	public void desenharBotaoCircular() {
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = pos;
		} else  {
			PosEfetiva = posII;
		}

		p5.translate(PosEfetiva.x, PosEfetiva.y);
		p5.fill(cargaColor);
		p5.strokeWeight(2);
		p5.stroke(255);
		if (!temIcone)
			p5.ellipse(0,0, diam, diam);
		else
			p5.image(logoBotao, 0,0, diam*1.25f, diam*1.25f);
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTexto();
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenharBotaoCircular(boolean stroke) {
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = pos;
		} else  {
			PosEfetiva = posII;
		}

		p5.translate(PosEfetiva.x, PosEfetiva.y);
		p5.fill(cargaColor);
		if (stroke) {
			p5.strokeWeight(3);
			p5.stroke(255);
		} else {
			p5.noStroke();
		}
			
		if (!temIcone)
			p5.ellipse(0,0, diam, diam);
		else
			p5.image(logoBotao, 0,0, diam*2, diam*2);
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTexto();
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	/**
	 * Metodo para botoes que ficam se dislocando de posição
	 */
	public void desenharBotaoCircularMovil() {
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = PVector.lerp(PosEfetiva, pos, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diam, .1f);
		} else  {
			PosEfetiva = PVector.lerp(PosEfetiva, posII, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diamMen, .1f);
		}

		p5.translate(PosEfetiva.x, PosEfetiva.y);
		p5.fill(cargaColor);
		p5.strokeWeight(3);
		p5.stroke(255);
		
		if (!temIcone)
			p5.ellipse(0,0, diamEfeivo, diamEfeivo);
		else
			p5.image(logoBotao, 0,0, diamEfeivo, diamEfeivo);
		
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTexto();
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenharBotaoCircularMovil(boolean stroke) {
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = PVector.lerp(PosEfetiva, pos, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diam, .1f);
		} else  {
			PosEfetiva = PVector.lerp(PosEfetiva, posII, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diamMen, .1f);
		}
		
		if (stroke) {
			p5.strokeWeight(3);
			p5.stroke(255);
		} else {
			p5.noStroke();
		}
		p5.translate(PosEfetiva.x, PosEfetiva.y);
		p5.fill(cargaColor);
		
		if (!temIcone)
			p5.ellipse(0,0, diamEfeivo, diamEfeivo);
		else
			p5.image(logoBotao, 0,0, diamEfeivo*2, diamEfeivo*2);
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTexto();
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenharBotaoCircularTextoGira( float ang) {

		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(pos.x, pos.y);
		p5.fill(cargaColor);
		p5.strokeWeight(3);
		p5.stroke(255);
		
		if (!temIcone)
			p5.ellipse(0,0, diam, diam);
		else
			p5.image(logoBotao,0,0, diam, diam);
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTextoGira(ang);
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	public void setTamanhoEtiqueta(float newTam) {
		newTam = PApplet.map(newTam, 0, 1, diam*.1f, diam);
		tamanhoEtiqueta = newTam;
	}
	public void desenharBotaoCircularSemTexto() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(pos.x, pos.y);
		p5.fill(cargaColor);
		p5.strokeWeight(3);
		p5.stroke(255);
		
		if (!temIcone)
			p5.ellipse(0,0, diam, diam);
		else
			p5.image(logoBotao, pos.x,pos.y, diam, diam);
		
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(tamanhoEtiqueta);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	public boolean botaoToogleOnClick (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist = PVector.dist(PosEfetiva, evaluar);
		
		if (botaoToggle && !ligado) {
			if (dist < diam ) {
				resp = true;
				click = true;
				ligado = !ligado;
				atualizaCor();
			}
			
			testMudanca(ligadoPrev);
		}
		return resp;
	}
	/**
	 * 
	 * @param evaluar
	 * @return boolean
	 */
	public boolean botaoOnClick (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist = PVector.dist(PosEfetiva, evaluar);
		
		if (dist < diam ) {
			resp = true;
			click = true;
			ligado = !ligado;
			atualizaCor();
		}
		
		testMudanca(ligadoPrev);
		return resp;
	}
	public boolean botaoOnClickPos (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist = PVector.dist(pos, evaluar);
		
		if (dist < diam ) {
			resp = true;
			click = true;
			ligado = !ligado;
			atualizaCor();
		}
		
		testMudanca(ligadoPrev);
		return resp;
	}
	public void turnBotaoOn() {
		boolean ligadoPrev = ligado;
		ligado = true;
		atualizaCor();
		testMudanca(ligadoPrev);
	}
	public void turnBotaoOff() {
		boolean ligadoPrev = ligado;
		ligado = false;
		atualizaCor();
		testMudanca(ligadoPrev);
	}
	//escuta o click quanto o botão for criado dinamicamente, quer dizer quando a sua posição na tela muda
	public boolean botaoOnClick (PVector evaluar, float scalaZoom, PVector translateZoom) {
		click = false;
		boolean ligadoPrev = ligado;
		boolean resp = false;
		//PVector temp = new PVector ( (pos.x + translateZoom.x ) * scalaZoom  , (pos.y + translateZoom.y) * scalaZoom );
		if (posicaoDefault) {
			PosEfetiva = pos;
		} else  {
			PosEfetiva = posII;
		}
		PVector temp = new PVector ( (PosEfetiva.x * scalaZoom ) + translateZoom.x  , (PosEfetiva.y * scalaZoom )+ translateZoom.y  );
		
		float dist = PVector.dist(temp, evaluar);
		
		if (dist < (diam) * scalaZoom  ) {
			resp = true;
			click = true;
			ligado = !ligado;
			atualizaCor();
		}
		
		testMudanca(ligadoPrev);
		
		return resp;
	}
	public boolean getEstadoBotao(){
		return ligado;
	}
	public void setEstadoBotao(boolean novoEstado){
		boolean ligadoPrev = ligado;
		
		ligado = novoEstado;
		atualizaCor();
		testMudanca(ligadoPrev);
		
	}
	public void atualizaCor(){
		if (ligado)
			cargaColor = colorOn;
		else
			cargaColor = colorOff;
	}

	public int getColorOn() {
		return colorOn;
	}
	public void testMudanca(boolean ligadoPrev) {
		if (ligado && !ligadoPrev)
			mudandoOn = true;
		else
			mudandoOn = false;
		
		if (!ligado && ligadoPrev)
			mudandoOff = true;
		else
			mudandoOff = false;
	}
	public PVector getPosicao(){
		return pos;
	}
	public void desenhaLogoSound() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.rectMode(PApplet.CENTER);
		p5.translate(pos.x, pos.y);
		p5.rotate(- PApplet.PI*.1f);
		p5.fill(colorOn);//p5.color(0));
		p5.noStroke();
		p5.rect(-diam*.1f, 0, diam*.3f, diam*.3f);
		p5.arc(-diam*.1f, 0, diam*.85f, diam*.85f, -PApplet.PI*.25f, PApplet.PI*.25f);
		p5.strokeWeight(1);
		p5.noFill();
		p5.stroke(p5.color(0));
		p5.arc(-diam*.1f, 0, diam*1.1f, diam*1.1f, -PApplet.PI*.26f, PApplet.PI*.26f);
		p5.arc(-diam*.1f, 0, diam*1.4f, diam*1.4f, -PApplet.PI*.27f, PApplet.PI*.27f);
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenhaLogoSound( int colorIn) {
		p5.pushMatrix();
		p5.pushStyle();
		p5.rectMode(PApplet.CENTER);
		p5.translate(pos.x, pos.y);
		p5.rotate(- PApplet.PI*.1f);
		p5.fill(colorIn);//p5.color(0));
		p5.noStroke();
		p5.rect(-diam*.1f, 0, diam*.3f, diam*.3f);
		p5.arc(-diam*.1f, 0, diam*.85f, diam*.85f, -PApplet.PI*.25f, PApplet.PI*.25f);
		p5.strokeWeight(1);
		p5.noFill();
		p5.stroke(colorIn);
		p5.arc(-diam*.1f, 0, diam*1.1f, diam*1.1f, -PApplet.PI*.26f, PApplet.PI*.26f);
		p5.arc(-diam*.1f, 0, diam*1.4f, diam*1.4f, -PApplet.PI*.27f, PApplet.PI*.27f);
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenhaFlecha(){
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = pos;
		} else  {
			PosEfetiva = posII;
		}
		
		p5.translate(PosEfetiva.x, PosEfetiva.y);
		if (ligado) {
			p5.rotate(PApplet.PI);
		}
		p5.noFill();
		p5.stroke(p5.color(255));
		p5.noFill();//fill(255);
		p5.ellipse(0,0,diamEfeivo,diamEfeivo);
		p5.strokeWeight(p5.height*.005f);
		p5.line(0, diamEfeivo*.3f, 0, -diamEfeivo*.3f);
		p5.line(0, -diamEfeivo*.3f, (diamEfeivo*.3f) * PApplet.cos (p5.PI), (diamEfeivo*.3f) * PApplet.sin (p5.PI));
		p5.line(0, -diamEfeivo*.3f, (diamEfeivo*.3f) * PApplet.cos (0), (diamEfeivo*.3f) * PApplet.sin (0));
		p5.popStyle();
		p5.popMatrix();
	}
	public void desenhaFlechaMovil(){
		p5.pushMatrix();
		p5.pushStyle();
		if (posicaoDefault) {
			PosEfetiva = PVector.lerp(PosEfetiva, pos, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diam, .1f);
		} else  {
			PosEfetiva = PVector.lerp(PosEfetiva, posII, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diamMen, .1f);
		}
//		Log.i("BotaoBase desenhaFlecha", "posEfetiva: " + PosEfetiva);
		p5.translate(PosEfetiva.x, PosEfetiva.y);
		if (ligado) {
			p5.rotate(PApplet.PI);
		}
		p5.noFill();
		p5.stroke(p5.color(255));
		p5.noFill();//fill(255);
		p5.ellipse(0,0,diamEfeivo,diamEfeivo);
		p5.strokeWeight(p5.height*.005f);
		p5.line(0, diamEfeivo*.3f, 0, -diamEfeivo*.3f);
		p5.line(0, -diamEfeivo*.3f, (diamEfeivo*.3f) * PApplet.cos (p5.PI), (diamEfeivo*.3f) * PApplet.sin (PApplet.PI));
		p5.line(0, -diamEfeivo*.3f, (diamEfeivo*.3f) * PApplet.cos (0), (diamEfeivo*.3f) * PApplet.sin (0));
		p5.popStyle();
		p5.popMatrix();
	}
	public float getBotaoDiam() {
		return diam;
	}
}
