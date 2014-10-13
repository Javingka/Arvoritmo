package cl.javiercruz.arvore.processing.barra;

import org.puredata.core.PdBase;

import cl.javiercruz.arvore.processing.botoes.EfectoBotao;

import processing.core.PApplet;
import processing.core.PVector;
/**
 * Classe para gestionar la soma e a resta de árvores no aplicativo.
 * @author javingka
 *
 */
public class GestionadorArvores extends BotaoBase {
	BotaoBase botaoSuma, botaoResta;
	private int cantidadAtual;
	private int variacion;
	/**
	 * 
	 * @param _p5 PApplet do PApplet inicial
	 * @param _pos PVector de posição da árvore
	 * @param _diam float do diametro dos botões 
	 * @param _color int color
	 * @param _ca int quantidade de árvores
	 */
	public GestionadorArvores(PApplet _p5, PVector _pos, float _diam, int _color, int _ca) {
		super(_p5, _pos, _diam, _color);
		cantidadAtual = _ca;
		setPosicoesBotoes(pos); 
		variacion = 0;
	}
	/**
	 * Criação dos botoes de suma e resta das árvores
	 * @param _p PVector Posição do botão central
	 */
	private void setPosicoesBotoes( PVector _p ) {
		PVector posRep = new PVector ( _p.x - diam * .75f,  _p.y ); //_p.x - diam * 1.75f ,  _p.y - diam * .75f);
		botaoSuma = new BotaoBase(p5, posRep, diam * 1.25f, p5.color(0) ); //colorOn);//);
		botaoSuma.setPosicaoTexto("centro", "esquerda");
		botaoSuma.setNomeBotao(" ");
		botaoSuma.setTamanhoEtiqueta(1);
		botaoSuma.setEtiqueta("+");
		botaoSuma.setColorOff(p5.color(0));
		PVector posEfec = new PVector (_p.x + diam * .75f, _p.y ); //(_p.x - diam * 1.75f ,  _p.y + diam * .75f);
		botaoResta = new BotaoBase(p5, posEfec, diam*1.25f, colorOn); //p5.color (getColorOn(), 200) );
		botaoResta.setPosicaoTexto("centro", "direita");
		botaoResta.setTamanhoEtiqueta(1);
		botaoResta.setEtiqueta("-");
		botaoResta.setNomeBotao(" ");
		botaoResta.setColorOff(p5.color(0));
		
	}
	@Override //una nova versión para disenhar este botao
	public void desenharBotaoCircular() {
		p5.pushMatrix();
		p5.pushStyle();
		
			
			p5.stroke(getColorOn());
			p5.noFill();
			p5.strokeWeight(3);
			
/*			p5.beginShape(); 
			  p5.vertex(pos.x, pos.y); // first point
			  p5.bezierVertex(pos.x - diam/2, pos.y , 
					  botaoSuma.getPosicao().x , botaoSuma.getPosicao().y + diam*.6f, //* ( 1f - ( PApplet.cos(p5.frameCount*.005f) / 10)),
					  botaoSuma.getPosicao().x, botaoSuma.getPosicao().y);
			p5.endShape();*/
			
//				reproductor.desenharBotaoCircularSemTexto();
			botaoSuma.desenharBotaoCircular();//desenharBotaoCircularTextoGira(0);
		
/*			p5.beginShape();
			  p5.vertex(pos.x, pos.y); // first point
			  p5.bezierVertex(pos.x - diam / 2, pos.y, 
					  botaoResta.getPosicao().x, botaoResta.getPosicao().y - diam*.6f, // * ( 1f - ( PApplet.cos(p5.frameCount*.005f) / 10)),
					  botaoResta.getPosicao().x, botaoResta.getPosicao().y);
			p5.endShape();*/
			
			botaoResta.desenharBotaoCircular();//desenharBotaoCircularTextoGira(0);
			
		
		
		p5.translate(pos.x, pos.y);
		p5.fill(cargaColor);
		
/*		Desenho de uma esfera entre os dois botões de soma e resta de árvores
 * 		if (ligado) {
			p5.stroke(getColorOn(), 120);
			p5.strokeWeight(p5.height*.03f);
		} else {
			p5.strokeWeight(p5.height*.01f);
			p5.stroke(0);
		}
		p5.ellipse(0,0, diam, diam);*/
		
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTextoYOffset( diam*.3f);
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
	public int getVariacao(){
		return variacion;
	}
	public int escutaBotoesInternos(PVector evaluar) {
		int variacion = 0;
		if (botaoSuma.botaoOnClick(evaluar))  {
			variacion = 1;
			botaoSuma.setEstadoBotao(false);
		} else if (botaoResta.botaoOnClick(evaluar)) {
			variacion = -1;
			botaoResta.setEstadoBotao(false);
		}
		return variacion;
	}
}
