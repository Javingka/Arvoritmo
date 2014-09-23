package cl.javiercruz.arvore.processing.arvoresystem;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class CreadorArvoreRecursivo {

	PApplet p5;
	int nivelRecursivo;
	float comprimentoGalho;
	GetGlobalPositionsFromRecursiveTree GlobalPos;
	int nivelesRecursivos;
	float anguloEntreGalhos;
	private boolean N2_dereita;
	private boolean N3_dereita;
	private float temNoise, temNoise2;
	
	/**
	 * 
	 * @param _p5
	 * @param _nivelesRecursivos O quantidade de niveis recursivos que a arvore vai ter
	 * @param altura comprimento de cada galho 
	 */
	CreadorArvoreRecursivo (PApplet _p5, int _nivelesRecursivos, float _refTamanho, float anguloEnt) {
		p5 = _p5; //PApplet para usar metodos de Processing
		GlobalPos = new GetGlobalPositionsFromRecursiveTree(p5); //inicializaçāo da clase que obtem as coordenadas globais da arvore
		nivelesRecursivos = _nivelesRecursivos; //cantidade de galhos
		comprimentoGalho = _refTamanho / nivelesRecursivos; //o comprimento do galho segundo o tamanho da arbore e os niveis recursivos
		anguloEntreGalhos =  anguloEnt; //angulo inicial de separaçāo dos galhos
		N2_dereita = false; //nos ajuda a intercalar os galhos que sāo criados para botar um angulo diferente de inclinaçāo
		N3_dereita = false;
		temNoise = 0; //noise para a animaçāo dos galhos
		temNoise2 = p5.random (50,150); //mas uma outra variavel para a animaçāo
	}
	
	public ArrayList <PVector> getArrayListPosicoes () {
		ArrayList <PVector> temPos = GlobalPos.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		return temPos; 
	}
	
	public void desenharLinhasArvore() {
		p5.stroke(0,0,255);
		p5.strokeWeight(8);
		ArrayList <PVector> temPos = GlobalPos.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		p5.line (0,0, 0,-comprimentoGalho);
		//al ser un arbol dividido en 2 este es la expresión que nos ayudara a ir escogiendo los pares
		int var = (temPos.size() -1) / 2;
		for (int i = 0 ; i < nivelesRecursivos-1 ; i++ ) { //un loop por cada nivel recursivo
			for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
				PVector pv = temPos.get(j);
				if (pv.z == i) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
					//se dibujan las ramas respectivas
					p5.line(temPos.get(j).x, temPos.get(j).y , temPos.get(j+1).x, temPos.get(j+1).y ); 
					p5.line(temPos.get(j).x, temPos.get(j).y , temPos.get(j+1+var).x, temPos.get(j+1+var).y );
				}
		    }
		    var = (var - 1) / 2; 
		}
	}

	
	public void criaArvore(){
		nivelRecursivo=0;
		GlobalPos.update(); //clear os ArrayList da classe GGP
		GlobalPos.newPos (0, -comprimentoGalho, 0, nivelRecursivo, 0);  
		masUmGalho();
	}
	
	public void masUmGalho () {
		
	//  fill(0); text ("N: "+nivelRecursivo, 0,0); noFill();
	  nivelRecursivo++; //aumenta o contador de recursividade
	  
	  if (nivelRecursivo < nivelesRecursivos) { //desenha 4 niveis recursivos
	    float aa= anguloEntreGalhos; //angulo de abertura dos galhos
	    float angDesloca = 0;
	    
	    //noise para o movimento dos galhos
	    float tv = p5.noise(temNoise + nivelRecursivo);
		temNoise+=.001;
		float tv2 = p5.noise(temNoise2 + nivelRecursivo);
		temNoise2+=.001;
		
	    switch (nivelRecursivo) {
	    case 1: 
	    	aa = PApplet.PI * .28f * (nivelesRecursivos *.15f); //.5f; //.28f; //abertura primeiros galhos
	    	//calcuo da variaçāo do angulo
			aa *= PApplet.map(tv, 0, 1, 1f, 1.3f);
	    	break;
	    case 2:
	    	aa = PApplet.PI * .16f; //.5f ;//*.125f;// //abertura segundo nivel de galhos
	    	if (N2_dereita)
	    		angDesloca = - PApplet.PI * .1f; //.36f;//.625f;//
	    	else
	    		angDesloca = PApplet.PI * .1f; //.36f;;//.625f; //
	    	
	    	N2_dereita = !N2_dereita;
	    	
	    	//calcuo da variaçāo do angulo
			aa *= PApplet.map(tv, 0, 1, .9f, 1.1f);
			angDesloca *= PApplet.map(tv, 0, 1, .9f, 1.1f);
			
	    	break;
	    case 3:
	    	aa = PApplet.PI * .1f;//.1f; //.28f; //abertura terceiros galhos
	    	if (N2_dereita) {
	    		if (!N3_dereita)
	    			angDesloca =  PApplet.PI * .1f; //.5f;//
	    		else
	    			angDesloca = - PApplet.PI * .1f; //.34f;//.34f; //.5f;//
	    	} else {
	    		if (!N3_dereita)
	    			angDesloca =  PApplet.PI * .1f; //.34f;//.34f; //.5f;//
	    		else
	    			angDesloca = - PApplet.PI * .1f; //.5f;//
	    	}
	    	N3_dereita = !N3_dereita;
	    	
	    	//calcuo da variaçāo do angulo
			aa *= PApplet.map(tv2, 0, 1, .8f, 1.2f);
			angDesloca *= PApplet.map(tv2, 0, 1, .8f, 1.2f);
			
	    	break;
	    }
	    
	    
		
	    GlobalPos.newPos (0, -comprimentoGalho, aa, nivelRecursivo, -angDesloca);
	    masUmGalho();
	    GlobalPos.newPos (0, -comprimentoGalho, -aa, nivelRecursivo, -angDesloca);
	    masUmGalho ();
	  }
	  nivelRecursivo--;
	}	  
}
