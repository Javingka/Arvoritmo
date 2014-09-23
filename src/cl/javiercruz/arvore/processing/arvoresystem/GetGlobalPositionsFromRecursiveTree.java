package cl.javiercruz.arvore.processing.arvoresystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class GetGlobalPositionsFromRecursiveTree {
	PApplet p5;
	ArrayList <PVector> posiciones;
	ArrayList <Float> angulosIn;  
	ArrayList <PVector> posDinamicaNivel; //Posiciones dos pontos por quanto sao criados

	GetGlobalPositionsFromRecursiveTree (PApplet _p5) {
		p5 = _p5;
	    posiciones = new ArrayList <PVector>();
	    angulosIn = new ArrayList <Float>();
	    posDinamicaNivel = new ArrayList <PVector>();
	}

	public void update() {
		posiciones.clear();
	    angulosIn.clear();
	    posDinamicaNivel.clear();
	}

	void seePos(float px, float py) {
	    p5.pushMatrix();
	    p5.translate (px, py);
	    int t=0; 
	    p5.textAlign(PApplet.CENTER, PApplet.CENTER);
	    for (PVector p : posiciones) { 
	      p5.noFill();
	      p5.stroke(255, 0, 0);
	      p5.ellipse (p.x, p.y, 20, 20);
	      if (t == p5.frameCount%posiciones.size() ) { 
	        p5.fill (0, 0, 255); 
	        p5.text (t, p.x, p.y);
	      }
	      t++;
	    }
	    p5.popMatrix();
	  }

	  void newPos (float nuevoX, float nuevoY, float ang, int nivelRec, float _angDesloca) {
	    p5.pushStyle();
	    p5.fill(0); 
//	    p5.text ("N: "+nivelRec, 0, 0); 
	    p5.noFill();
	    float anguloUso = 0f;
	    ang += _angDesloca;
	    
	    if (angulosIn.size() == 0) {
	      angulosIn.add(nivelRec, ang);
	    }  
	    else {
	      float angP = angulosIn.get(nivelRec-1);
	      ang+=angP;
	      if (angulosIn.size()>nivelRec)
	        angulosIn.remove(nivelRec); 

	      angulosIn.add(nivelRec, ang);
	      anguloUso=angulosIn.get(nivelRec);
	    }

	    float angBase = PApplet.atan2(nuevoY, nuevoX);
	    float hipo = PApplet.sqrt(PApplet.pow(nuevoX, 2) + PApplet.pow(nuevoY, 2));
	    float nx = hipo * PApplet.cos(anguloUso+angBase);//angulo+angBase);
	    float ny = hipo * PApplet.sin(anguloUso+angBase);//angulo+angBase);
	    PVector nuevo = new PVector (nx, ny, nivelRec);

	    int i = nivelRec-1; 
	    if (posDinamicaNivel.size() == nivelRec) {
	      posDinamicaNivel.add(nuevo);
	      //     println("nive: "+nivelRec+" posDinamicaNivel: "+posDinamicaNivel);
	    } 
	    else {
	      //    println ("i: "+i+" nive: "+nivelRec+" posDinamicaNivel: "+posDinamicaNivel);
	      PVector t = posDinamicaNivel.get(i);
	      PVector n = new PVector (nuevo.x+t.x, nuevo.y+t.y);
	      posDinamicaNivel.remove(i+1);
	      posDinamicaNivel.add(i+1, n);
	    } 

	    if (posiciones.size()==0) {
	      posiciones.add(nuevo);
	    } 
	    else {
	      PVector t = posDinamicaNivel.get(i);
	      nuevo.add(t);
	      nuevo.z=nivelRec;
	      posiciones.add(nuevo);      
	    }
	  p5.popStyle();
	  }

	  void seePosNivel(float px, float py) {
	    p5.pushMatrix();
	    p5.translate (px, py);
	    int t=0; 
	    p5.textAlign(PApplet.CENTER, PApplet.CENTER);
	    float c = 255/posDinamicaNivel.size(); //divición para cambiar los colores
	    for (PVector p : posiciones) { 
	      p5.noFill(); 
//	      println ("p.z: "+p.z);
	      p5.stroke (c*p.z,0,0); 
	      p5.ellipse (p.x, p.y, 20, 20);
	      if (t == p5.frameCount%posiciones.size() ) { 
	        p5.fill (0, 0, 255); 
	        p5.text (t, p.x, p.y);
	      }
	      t++;
	    }
	    p5.popMatrix();
	  }
	  
	  public ArrayList <PVector> getArrayListPosicoes () {
	   return posiciones; 
	  }
	  public ArrayList <PVector> getArrayListPosDinamica(){
	   return posDinamicaNivel;
	  }
}
