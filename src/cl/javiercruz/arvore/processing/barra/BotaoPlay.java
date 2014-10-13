package cl.javiercruz.arvore.processing.barra;

import processing.core.PApplet;
import processing.core.PVector;

public class BotaoPlay extends BotaoBase {

	public BotaoPlay(PApplet _p5, PVector _pos, float _diam, int _color) {
		super(_p5, _pos, _diam, _color);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void desenharBotaoCircularMovil() {
		p5.pushStyle();
		if (getPosDefault()) {
			PosEfetiva = PVector.lerp(PosEfetiva, pos, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diam, .1f);
		} else  {
			PosEfetiva = PVector.lerp(PosEfetiva, posII, .1f);
			diamEfeivo = PApplet.lerp(diamEfeivo, diamMen, .1f);
		}
		p5.strokeWeight(3);
		p5.stroke(255);
		if (!ligado) {
			p5.pushMatrix();
			p5.pushStyle();
			p5.translate(PosEfetiva.x, PosEfetiva.y);
			p5.fill(colorOn);
			p5.beginShape();
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 1/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 1/3));
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 2/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 2/3));
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 3/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 3/3));
			p5.endShape(PApplet.CLOSE);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popStyle();
			p5.popMatrix();
		} else {
			p5.pushMatrix();
			p5.rectMode(PApplet.CENTER);
			p5.translate(PosEfetiva.x, PosEfetiva.y);
			p5.fill(colorOff);
			p5.rect(-diamEfeivo*.25f, 0, diamEfeivo*.3f, diamEfeivo);
			p5.rect(diamEfeivo*.25f, 0, diamEfeivo*.3f, diamEfeivo);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popMatrix();
		}
		
		p5.popStyle();
	}
	
	@Override
	public void desenharBotaoCircular() {
		p5.pushStyle();
		if (getPosDefault()) {
			PosEfetiva = pos;
		} else  {
			PosEfetiva = posII;
		}
		p5.strokeWeight(1);
		p5.stroke(255);
		if (!ligado) {
			p5.pushMatrix();
			p5.pushStyle();
			p5.translate(PosEfetiva.x, PosEfetiva.y);
			p5.fill(colorOn);
			p5.beginShape();
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 1/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 1/3));
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 2/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 2/3));
			p5.vertex((diamEfeivo * .6f) * PApplet.cos (PApplet.TWO_PI * 3/3), (diamEfeivo * .6f) * PApplet.sin (PApplet.TWO_PI * 3/3));
			p5.endShape(PApplet.CLOSE);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popStyle();
			p5.popMatrix();
		} else {
			p5.pushMatrix();
			p5.rectMode(PApplet.CENTER);
			p5.translate(PosEfetiva.x, PosEfetiva.y);
			p5.fill(colorOff);
			p5.rect(-diamEfeivo*.25f, 0, diamEfeivo*.3f, diamEfeivo);
			p5.rect(diamEfeivo*.25f, 0, diamEfeivo*.3f, diamEfeivo);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popMatrix();
		}
		
		p5.popStyle();
	}
	public void desenharBotaoCuadrado() {
		p5.pushStyle();
		p5.strokeWeight(3);
		p5.stroke(255);
		if (!ligado) {
			p5.pushMatrix();
			p5.pushStyle();
			p5.translate(pos.x, pos.y);
			p5.fill(colorOn);
			p5.beginShape();
			p5.vertex((diam * .6f) * PApplet.cos (PApplet.TWO_PI * 1/3), (diam * .6f) * PApplet.sin (PApplet.TWO_PI * 1/3));
			p5.vertex((diam * .6f) * PApplet.cos (PApplet.TWO_PI * 2/3), (diam * .6f) * PApplet.sin (PApplet.TWO_PI * 2/3));
			p5.vertex((diam * .6f) * PApplet.cos (PApplet.TWO_PI * 3/3), (diam * .6f) * PApplet.sin (PApplet.TWO_PI * 3/3));
			p5.endShape(PApplet.CLOSE);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popStyle();
			p5.popMatrix();
		} else {
			p5.pushMatrix();
			p5.rectMode(PApplet.CENTER);
			p5.translate(pos.x, pos.y);
			p5.fill(colorOn);
			p5.rect(0, 0, diam, diam);
			if (textoBotao != null) {
				atualizaDataTexto();
				desenhaTexto();
			}
			p5.popMatrix();
		}
		
		p5.popStyle();
	}
}
