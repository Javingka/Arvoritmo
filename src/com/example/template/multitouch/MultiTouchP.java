package com.example.template.multitouch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.view.MotionEvent;

import processing.core.PApplet;
import processing.core.PVector;
/*
 * Classe baseada no codigo feito por Alex Sanchez (kniren)
 * https://github.com/kniren/AndroidMT
 * A principal modificaçāo é a incorporaçāo de um CallBack para
 * pegar desde uma outra clase os eventos acontecidos, onde destaca
 * informaçāo da distança e o angulo ao arrastrar os toques na tela
 */
public class MultiTouchP {
	//variavel para colocar como ID invalido 
	final int INVALID_POINTER_ID = -1;
	
	//Uma lista que almacena os pontos com um ID.  Name/value pairs
	private Map<Integer,Point> hashList;
	ClassForMTCallBack mtCallBack; //objeto que vai enviar os dados para os metodos callbacks
	PApplet p5;
	
	public MultiTouchP(PApplet _p5) {
		//HashMap Let’s you store and access elements as name/value pairs
		p5 = _p5;
		hashList = new HashMap<Integer,Point>();
		mtCallBack = new ClassForMTCallBack ((MTListenerCallBack) p5);
	}

/** Quando o metodo surfaceTouchEvent é chamado no Activity implementado no PApplet, este método
 *  vai ser entāo também chamado
 */
	public void surfaceTouchEvent(MotionEvent me) {
		int action = me.getAction(); 
		float x    = me.getX();
		float y    = me.getY();
		int index  = action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;	
		int id     = me.getPointerId(index);
			
		switch (action & MotionEvent.ACTION_MASK) {
		    case MotionEvent.ACTION_DOWN: {
		    	insert(id, x, y);	        
			    break;
		    }  
	        case MotionEvent.ACTION_UP: {
	        	delete(id);
	            break;
	        }
		    case MotionEvent.ACTION_MOVE: {
		        int numPointers = me.getPointerCount();
		        for (int i=0; i < numPointers; i++) {
		          id = me.getPointerId(i);
		          x  = me.getX(i);
		          y  = me.getY(i);
		          update(id, x, y);
		        }
			    break;
		    } 
		    case MotionEvent.ACTION_POINTER_DOWN: {
		    	insert(id, x, y);
		    	break;
		    }	   
	        case MotionEvent.ACTION_POINTER_UP: {
	        	delete(id);
	            break;
	        }
	        case MotionEvent.ACTION_CANCEL: {
	        	clearMe();
	            id = INVALID_POINTER_ID;
	        break;
	        }
		}
	}
		
	public synchronized void gerenciadorDeToques(){}
		
	public synchronized void drawInfo() {
	//HashSet Prevents duplicates in the collections, and given an element, can find that element in the collection quickly.
	        Set<Integer> keyList = hashList.keySet();
	//Iterator enables you to cycle through a collection, obtaining or removing elements
	        Iterator<Integer> iter = keyList.iterator();
	        int cnt = 0;
	        Point anchor = null;
	//linkedList: Designed to give better performance when you  insert or delete elements from the middle of the collection 
	        ArrayList<Point> lista = new ArrayList<Point>();
	        while(iter.hasNext()){
	        	anchor = hashList.get(iter.next());
	        	lista.add(anchor);
	        	anchor.drawIt();
	        	cnt++;
	        }
	        /*
	         * We draw now all the lines between nodes
	         */
	        if (lista.size() > 1) {
	        	Object[] arList = lista.toArray();

	        	for (int i = 0; i < arList.length; i++ ) {
	        		for (int j = i+1; j < arList.length; j++) {
	        			drawLine((Point) arList[i], (Point) arList[j]);
	        		}
	        	}
	        }
	        p5.textSize(25);
	        p5.text("Active elements: " + cnt,10,25);

	        return;
		}
	synchronized void drawLine(Point a, Point b) {
		p5.line(a.posX,a.posY,b.posX,b.posY);
	}

	/**
	 * Remove item with the given id from the hashed list.
	 * @param id 
	 */
	synchronized void delete(int id) {
		if ( hashList.get(id) != null ) {
			hashList.remove(id);
			mtCallBack.sendToCallBackMethod(id);
		}
	}
		
	/**
	 * Remove all items from the list. This happens when an ACTION_CANCEL event
	 * occurs.
	 */
	synchronized void clearMe() {
		if (hashList.size() > 0) {
			for (int id=0 ; id < hashList.size() ; id++)
				mtCallBack.sendToCallBackMethod(id);
		}
		hashList.clear();
	}

	/**
	 * Check if the given ID is in the list, and if not, inserts it.
	 * @param id
	 * @param x
	 * @param y
	 */
	synchronized void insert(int id, float x, float y) {
		if ( hashList.get(id) == null ){
			hashList.put(id, new Point(p5, id,x,y));
			mtCallBack.sendToCallBackMethod(id, x, y);
		}
	}
		
	/**
	 * Updates the current position of the given ID
	 * @param id
	 * @param x
	 * @param y
	 */
	synchronized void update(int id, float x, float y) {
		Point p1 = hashList.get(id); // pega o valor do ponto antes de atualizar
		hashList.get(id).update(x, y); //atualizaçāo da nova posiçāo do ponto 
		Point p2 = hashList.get(id); // pega o valor do ponto depois da atualizaçāo
		float dist = PVector.dist(p1.getPVectorPos(), p2.getPVectorPos()); //pegamos distancia
		float ang = PVector.angleBetween(p1.getPVectorPos(), p2.getPVectorPos()); //pegamos angulo
		if (p2.posY < p1.posY) //adaptamos o angulo para cubrir os 360°
			ang = 2*p5.PI-ang;
		
		mtCallBack.sendToCallBackMethod(id, x, y, dist, ang); //chamamos a funçāo que f	
		
	}
	
}

