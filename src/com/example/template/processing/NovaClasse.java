package com.example.template.processing;

import processing.core.PApplet;

/* IMPORTANTE para ter em conta na hora de programar
 * 
 * 1- Quando cria uma nova clase o mínimo necessario é criar uma variavel de tipo PApplet
 * essa variavel vai permitir usar dentro da clase os metodos de processing.
 * 
 * 2- é preciso declara se as variaveis e os metodos sāo public ou private.
 * tem em conta que em Processing todos os metodos e variaveis sāo public por padrāo
 */

public class NovaClasse {
/* p5 vai ser inicializada com a variavel entregada pela clase que vai instanciar ela 
*  ou seja a clase PAppletInicial
*/
	PApplet p5;
	
	public NovaClasse( PApplet _p5) {
		p5 = _p5;
		//Implementación do resto do construtor desta clase
	}
	
/* Por ejemplo um metodo para desenhar uma elipse no centro da tela
 * É importante notar o p5. antes do chamado dos metodos de Processing
 */
	public void desenhar(){
		p5.pushMatrix();
		p5.stroke(255,0,0);
		int centroX = p5.width / 2;
		int centroY = p5.height / 2;
		p5.ellipse(centroX , centroY, 50, 50);
		
		p5.popMatrix();
		
	}
	
}
