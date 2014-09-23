package cl.javiercruz.arvore.processing.botoes;

import cl.javiercruz.arvore.processing.barra.BotaoBase;
import processing.core.PApplet;
import processing.core.PVector;

public class EfectoBotao extends BotaoBase{
	BotaoBase flanger;
	BotaoBase rever;
	BotaoBase delay;
	
	public EfectoBotao(PApplet _p5, PVector _pos, float _diam, int _color) {
		super(_p5, _pos, _diam, _color);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void desenharBotaoCircularTextoGira( float ang) {
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(pos.x, pos.y);
		p5.fill(colorOn);
		p5.strokeWeight(3);
		p5.stroke(colorOn, 100);
		p5.ellipse(0,0, diam, diam);
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTextoGira(ang);
		}
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(diam*.75f);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}
	
	@Override
	public void desenharBotaoCircularSemTexto() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(pos.x, pos.y);
		p5.fill(colorOn);
		p5.strokeWeight(3);
		p5.stroke(colorOn, 100);
		p5.ellipse(0,0, diam, diam);
		if (etiqueta != null) {
			p5.textAlign(PApplet.CENTER, PApplet.CENTER);
			p5.textSize(diam*.75f);
			p5.fill(255);
			p5.text(etiqueta, 0,0);
		}
		
		p5.popStyle();
		p5.popMatrix();
	}

}
