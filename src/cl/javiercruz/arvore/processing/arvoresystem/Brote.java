package cl.javiercruz.arvore.processing.arvoresystem;

import processing.core.PApplet;
import processing.core.PVector;

public class Brote {

	PApplet p5;
	PVector posicao;
	int colorF;
	int colorFbase;
	private float diam;
	private float ang;
	private float alphaCont;
	private float comprimento;
	private float comprimentoMax;
	
	Brote (PApplet _p5, PVector pos, int colorIn) {
		p5 = _p5;
		posicao = pos;
		colorF = colorIn;
		colorFbase = colorF;
		diam = p5.width*.006f;
		ang = p5.random(- PApplet.PI);
		alphaCont = 0;
		comprimento = 1;
		comprimentoMax = diam * p5.random(p5.width*.002f, p5.width*.005f); 
	}
	public void setColor(int colorIn){
		int h = (int) p5.hue(colorIn);
		int b = (int) p5.brightness(colorIn);
		int s = (int) p5.saturation(colorIn);
		colorF = p5.color(h,s,b);
		//colorF = colorIn; 
		alphaCont = 255;
	}
	private void atualiza(){
	//negocio do color
		int h = (int) p5.hue(colorF);
		int b = (int) p5.brightness(colorF);
		int s = (int) p5.saturation(colorF);
		colorF = p5.color(h,s,b, alphaCont);
		alphaCont -= 5;
		ang -= PApplet.map(alphaCont, 255, 0, .5f , 0);
	    
		if (alphaCont <= 0)
			alphaCont = 0;
		
	//comprimento do brote
		comprimento++;
		if (comprimento > comprimentoMax)
			comprimento = comprimentoMax;
	}
	public void desenhar(){
		atualiza();
		p5.pushMatrix();
		p5.pushStyle();
		
		p5.translate (posicao.x, posicao.y);
		p5.fill(209,141,184);//colorF);
		p5.rotate( ang );
		p5.line(0,0,comprimento,0);
		p5.translate(comprimento,0);
		p5.beginShape();
		p5.vertex(diam * PApplet.cos (PApplet.TWO_PI * 1/5), diam * PApplet.sin (PApplet.TWO_PI * 1/5));
		p5.vertex(diam * PApplet.cos (PApplet.TWO_PI * 2/5), diam * PApplet.sin (PApplet.TWO_PI * 2/5));
		p5.vertex(diam * PApplet.cos (PApplet.TWO_PI * 3/5), diam * PApplet.sin (PApplet.TWO_PI * 3/5));
		p5.vertex(diam * PApplet.cos (PApplet.TWO_PI * 4/5), diam * PApplet.sin (PApplet.TWO_PI * 4/5));
		p5.vertex(diam * PApplet.cos (PApplet.TWO_PI * 5/5), diam * PApplet.sin (PApplet.TWO_PI * 5/5));
		p5.endShape(PApplet.CLOSE);
		
		p5.popStyle();
		p5.popMatrix();
	}
	
	public void setNewPos(PVector _newPos) {
		posicao = _newPos;
	}
	
	
}
