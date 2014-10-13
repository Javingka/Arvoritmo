package cl.javiercruz.arvore.processing.botoes;

import cl.javiercruz.arvore.processing.barra.BotaoBase;
import cl.javiercruz.arvore.processing.barra.SliderZoom;
import processing.core.PApplet;
import processing.core.PVector;

public class BpmSelector extends SliderZoom {
	PApplet p5;
	private int bpm;
	public BotaoBase mais;
	public BotaoBase menos;
	private float tamanhoTextoBotao;
	
	public BpmSelector(PApplet _p5, PVector _pos, float _diam, int _color, float comp, int bpmIni) {
		super(_p5, _pos, _diam, _color, comp);
		p5 = _p5;
		posRef = _pos;
		bpm = bpmIni;
		mais = new BotaoBase ( p5, new PVector (xMax + diam , pos.y), _diam, _color );
		mais.setPosicaoTexto("centro", "centro");
		mais.setNomeBotao("+1");
		mais.setColorOff( _color ); //p5.color(0) );
		menos = new BotaoBase ( p5, new PVector (xMin - diam , pos.y), _diam, _color );
		menos.setPosicaoTexto("centro", "centro");
		menos.setNomeBotao("-1");
		menos.setColorOff( _color ); //p5.color(0) );
		tamanhoTextoBotao = diam * .55f;
		setPosSegunBpm();
	}
	@Override
	public void setNovaPos( PVector nPos) {
		pos = nPos;
		xMin = nPos.x - (comprimento / 2);
		xMax = nPos.x + (comprimento / 2);
		posRef.y = nPos.y;
		setPosSegunBpm();
		mais.setNovaPos(  new PVector (xMax + diam , pos.y) );
		menos.setNovaPos(  new PVector (xMin - diam , pos.y) );
	}
	public void setBpm() {
    	float v = posRef.x - xMin;
    	bpm = (int) PApplet.constrain(PApplet.map(v, 0, comprimento, 30, 750), 30, 750);
    }
	private void setPosSegunBpm() {
		posRef.x = PApplet.constrain(PApplet.map(bpm, 30, 750, xMin, xMax), xMin, xMax );
	}
	@Override
	public void desenharSlider() {
		p5.pushStyle();
		p5.strokeWeight(2);
		p5.stroke(255);
		p5.line(xMin - diam, pos.y, xMax + diam, pos.y);
		//p5.line(xMin + (comprimento / 2), pos.y-diam/2, xMin + (comprimento / 2) , pos.y+diam/2);
		p5.fill(colorOn);
		dibujaDisplay(posRef.x , posRef.y);
		mais.desenharBotaoCircular();
		menos.desenharBotaoCircular();
		//	p5.textAlign(PApplet.DOWN, PApplet.CENTER);
		atualizaDataTexto();
		p5.textSize(tamanhoTextoBotao);
		p5.fill(255);
		p5.text(textoBotao, pos.x, pos.y + p5.height*.048f);
		p5.popStyle();
	}

	public void dibujaDisplay(float x, float y) {
		p5.pushMatrix();
		p5.rectMode(PApplet.CENTER);
		p5.translate(x,  y);
		p5.rect(0,0 , diam*3f, diam*1.15f, 7);
		p5.fill(255);
		p5.textAlign(PApplet.CENTER, PApplet.CENTER);
		p5.textSize(22);
		p5.text("<"+bpm+">", 0, 0);
		p5.popMatrix();
	}
	
	public int getBpm() {
		return bpm;
	}
	/**
	 * 
	 * @param evaluar
	 * @param scalaZoom
	 * @param translateZoom
	 * @return boolean 
	 */
	public boolean bpmBotaoOnClick(PVector evaluar, float scalaZoom, PVector translateZoom){
		boolean resp = false;
		if (mais.botaoOnClick(evaluar, scalaZoom, translateZoom) ) {
			bpm++;
			setPosSegunBpm();
			resp = true;
			mais.turnBotaoOff();
		} else if ( menos.botaoOnClick(evaluar, scalaZoom, translateZoom) ) {
			bpm--;
			setPosSegunBpm();
			resp = true;
			menos.turnBotaoOff();
		}
		return resp;
	}
	
	public boolean bpmSlicerOnClick (PVector evaluar, float scalaZoom, PVector translateZoom) {
		boolean resp = false;
		
		if ( botaoSliceOnClick(evaluar, scalaZoom, translateZoom) )
			resp = true;
		return resp;
	}
}
