package cl.javiercruz.arvore.processing.barra;

import processing.core.PApplet;
import processing.core.PVector;

public class SliderZoom extends BotaoBase{
	public PVector posRef;
	public float indicePos;
	public float xMin, xMax;
	public float yMin, yMax;
	public float comprimento;
	private boolean vertical;
	public boolean botaoAtivo; //vira true quando o botão esta sendo utilizado
	
	public SliderZoom(PApplet _p5, PVector _pos, float _diam, int _color, float comp) {
		super(_p5, _pos, _diam, _color);
		posRef = new PVector (_pos.x, _pos.y);;
		indicePos = 0;
		comprimento = comp;
		xMin = _pos.x - (comprimento / 2);
		xMax = _pos.x + (comprimento / 2);
		yMax = _pos.y + (comprimento / 2);
		yMin = _pos.y - (comprimento / 2);
		
		vertical= true;
	}
	public void setVerticalFalse(){
		vertical = false;
	}
    public float getZoomFromPos() {
    	if (vertical) {
    		float v = posRef.y - yMin;
        	v = PApplet.map(v, 0, comprimento, .5f, 1.5f);
        	return v;
    	} else {
    		float v = posRef.x - xMin;
        	v = PApplet.map(v, 0, comprimento, .5f, 1.5f);
        	return v;
    	}
    	
    }
	public void desenharSlider() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.strokeWeight(2);
		p5.stroke(255);
		
		if (vertical) {
			p5.line(pos.x, yMin, pos.x, yMax);
			p5.line(pos.x-diam/2, yMin + (comprimento / 2), pos.x+diam/2, yMin + (comprimento / 2) );
		} else {
			p5.line(xMin, pos.y, xMax, pos.y);
			p5.line(xMin + (comprimento / 2), pos.y-diam/2, xMin + (comprimento / 2) , pos.y+diam/2);
		}
			
		
		
		p5.translate(posRef.x, posRef.y);
		p5.fill(cargaColor);
		p5.ellipse(0,0, diam, diam);
		p5.popMatrix();
		p5.popStyle();
		
		
		//p5.textAlign(PApplet.RIGHT, PApplet.CENTER);
		atualizaDataTexto();
		p5.textSize(15);
		p5.fill(255);
		if (vertical)
			p5.text(textoBotao, pos.x-p5.width*.03f, pos.y);
		else
			p5.text(textoBotao, xMin-5, pos.y);
	}
	
	@Override
	public boolean botaoOnClick (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist=diam*2;
		
		dist = PVector.dist(posRef, evaluar);
		
		if (dist < diam ) {
			resp = true;
			click = true;
			ligado = !ligado;
			if (vertical) 
				setNovaRefPosY(evaluar.y);
			else
				setNovaRefPosX(evaluar.x);
		}
		
		if (ligado && !ligadoPrev)
			mudandoOn = true;
		else
			mudandoOn = false;
		
		if (!ligado && ligadoPrev)
			mudandoOff = true;
		else
			mudandoOff = false;
				
		return resp;
	}
	
	public boolean botaoSliceOnClick (PVector evaluar, float scalaZoom, PVector translateZoom) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist=diam*2;
//		PVector temp = new PVector ( (posRef.x + translateZoom.x ) * scalaZoom  , (posRef.y + translateZoom.y) * scalaZoom );
		PVector temp = new PVector ( (posRef.x * scalaZoom ) + translateZoom.x  , (posRef.y * scalaZoom ) + translateZoom.y );
		
		dist = PVector.dist(temp, evaluar);
		
		if (dist < diam || botaoAtivo) {
			botaoAtivo = true;
			resp = true;
			click = true;
			ligado = !ligado;
//			float ppx = (evaluar.x / scalaZoom )  - translateZoom.x;
			
			if (vertical) {
				float ppy = (evaluar.y - translateZoom.y ) / scalaZoom;
				setNovaRefPosY(ppy);
			} else {
				float ppx = (evaluar.x - translateZoom.x ) / scalaZoom;
				setNovaRefPosX(ppx);
			}
				
		}
		
		if (ligado && !ligadoPrev)
			mudandoOn = true;
		else
			mudandoOn = false;
		
		if (!ligado && ligadoPrev)
			mudandoOff = true;
		else
			mudandoOff = false;
				
		return resp;
	}
	public void setNovaRefPosX( float  xPos) {
		posRef.x = PApplet.constrain(xPos, xMin, xMax);
	}
	public void setNovaRefPosY( float  yPos) {
		posRef.y = PApplet.constrain(yPos, yMin, yMax);
	}
}
