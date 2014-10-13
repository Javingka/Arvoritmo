package cl.javiercruz.arvore.processing.UIbotoes;

import processing.core.PApplet;
import processing.core.PVector;

public class BotaoCodigo {
	private PVector pos;
	private float diametro;
	PApplet p5;
	private float angulo;
	private int estadoSwitch; //varia entre 0 e 1
	
	public BotaoCodigo(PApplet _p5, PVector _pos, int estadoIni) {
		p5 = _p5;
		pos = _pos;
		diametro = p5.height*.05f;
		angulo = PApplet.PI * .25f;
		estadoSwitch = estadoIni;
	}
	
	public void desenhar() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(pos.x, pos.y);
		p5.fill(255);
		p5.textSize(12);
		p5.textAlign(PApplet.LEFT, PApplet.CENTER);
		p5.text("Chave", diametro, 0);
		p5.fill(206, 141, 184); //(255 * 40/360f, 255 * .86f, 255 * 1f, 120);
		p5.rotate(-PApplet.PI*.5f);
		if (estadoSwitch == 0)
			p5.rotate(angulo);
		else
			p5.rotate(-angulo);
		p5.strokeWeight(2);
		p5.stroke(255);
	//	p5.line(0, 0, diametro*.55f, 0);
		p5.stroke(17, 145, 64); //255 * 33/360f, 255 * .44f, 255 * .99f);
		p5.ellipse(0, 0, diametro, diametro);
		p5.fill(211, 191,  231);
		p5.ellipse(diametro*.525f, 0, diametro*.2f, diametro*.2f);
		p5.popStyle();
		p5.popMatrix();
	}
/**
 * 
 * @param posEvaluar
 * @param scaleZoom
 * @param translateZoom
 * @return switch entre 0 e 1 
 */
	public int botaoClickListener (PVector posEvaluar, float scaleZoom, PVector translateZoom) {
//		PVector posGlobal = new PVector ( (pos.x + translateZoom.x ) * scaleZoom  , (pos.y + translateZoom.y) * scaleZoom );
		PVector posGlobal = new PVector ( (pos.x * scaleZoom ) + translateZoom.x , (pos.y  * scaleZoom  ) + translateZoom.y);
		float distance = PVector.dist(posEvaluar, posGlobal);
		if (distance < diametro * scaleZoom ) {
			if (estadoSwitch == 1)
				estadoSwitch = 0;
			else
				estadoSwitch = 1;
		}
		return estadoSwitch;
	}
	
	public int getEstadoBotao() {
		return estadoSwitch;
	}
	public void setNewPos(PVector _pos){
		this.pos = _pos;
	}
}
