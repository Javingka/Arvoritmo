package cl.javiercruz.arvore.multitouch;

import processing.core.PApplet;
import processing.core.PVector;

/** Classe para identificar cada ponto de toque
 * @author javier
 */
class Point {

	float posX,posY;
	int pointID;
	int textSize = 12;
	PApplet p5;
	PVector pos;
	
	Point(PApplet _p5, int id, float x, float y) {
		p5 = _p5;
		pointID = id;
		posX    = x;
		posY    = y;
		pos = new PVector (posX, posY);
	}
	
	void update(float x, float y) {
		posX = x;
		posY = y;
		pos = new PVector (posX, posY);
	}
	
	void drawIt() {

    	p5.fill(120);
    	p5.textSize(textSize);
    	p5.ellipse(posX,posY,150,150);
    	p5.text("X: " + posX + " Y: " + posY, posX-100, posY-100);
    	p5.text("ID: " + pointID, posX-100, posY-100-textSize);
    	p5.fill(200);
    	p5.ellipse(posX,posY,20,20);
	}
	
	public PVector getPVectorPos(){
		return pos;
	}
}

