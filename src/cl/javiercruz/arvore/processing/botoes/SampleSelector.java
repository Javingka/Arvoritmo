package cl.javiercruz.arvore.processing.botoes;

import java.util.ArrayList;

import org.puredata.core.PdBase;

import cl.javiercruz.arvore.processing.barra.BotaoBase;
import cl.javiercruz.arvore.processing.barra.BotaoNomeRect;


import processing.core.PApplet;
import processing.core.PVector;
/**
 * Classe para a seleção do sample ativo. /
 * Implementa: ArrayList dos Samples / BotaoBase botaoMuestraControles
 * @author javingka
 *
 */
public class SampleSelector {
	private float altura;
	float posX, posY;
	PApplet p5;
	private int cantBotoes;
	private float bloqueBotaoWidth, bloqueBotaoHeight;
	private ArrayList <SampleBotao> samples;
	private int indexLigado;
	private String tituloSelector;
//	private BotaoBase botaoMuestraControles;
	public BotaoBase botaoTrocaSample;
	public BotaoNomeRect menuSons;
	public boolean abreJanelaSample;
	String [] samplesIniciais = {
			"ADD1_CongaPerc_03",
			"ADD1_Duck_1",
			"ADD1_TinPerc_03",
			"ADD1_LoTom_01_p01",
			"ADD1_StomperKick",
			"ADD1_HiHat_39",
			"bassslap02"
	};
	/**
	 * Criação da lista de samples e os botões que os representam
	 * @param _p5
	 */
	public SampleSelector(PApplet _p5){
		p5 = _p5;
		altura = p5.height*.105f;
		posX = p5.width *.05f;
		posY = p5.height*.5f;
		
		menuSons = new BotaoNomeRect(p5, "SONS");
		
		indexLigado = 0;
		cantBotoes = 7;
		bloqueBotaoWidth = menuSons.getBotaoWith() / 8;
		bloqueBotaoHeight = p5.height / (cantBotoes + 2);
		samples = new ArrayList<SampleBotao>();
		int cont = 0;
		float px = bloqueBotaoWidth * .75f;
		float py = bloqueBotaoHeight;
		for (int i = 0 ; i < cantBotoes ; i++) {
//			float px = bloqueBotaoWidth + (bloqueBotaoWidth * 4 * cont);
			py = py+bloqueBotaoHeight;
			if ( i % 2 == 0 ) {
			}
//			float py = (bloqueBotaoHeight*.75f) + (bloqueBotaoHeight*1.25f) * i;
//			samples.add(new SampleBotao ( p5, new PVector (px, posY), altura*.35f, p5.color((255/cantBotoes)*i, 255, 255), "silencio", i ) );
			samples.add(new SampleBotao ( p5, new PVector (px, py), altura*.35f, p5.color((255/cantBotoes)*i, 255, 255), "silencio", i ) );
			((SampleBotao)samples.get(i)).setBotaoComoToggle();
			((SampleBotao)samples.get(i)).setPosicaoTexto("centro", "direita");
			((SampleBotao)samples.get(i)).setNomeSampleBotao(samplesIniciais[i]);		//agregamos os nomes dos samples segundo os samples que estão colocanos no patch de PureData

			cont++;
			cont = cont % 2;
		}
		
		
		
		samples.get(indexLigado).setEstadoBotao(true);
		tituloSelector = " ";
		
/*		PVector posicaoMuestraC = new PVector (bloqueBotaoWidth, menuSons.getBotaoHeight()/2 ); //new PVector (p5.width*.95f, p5.height*.05f)
		botaoMuestraControles = new BotaoBase(p5, posicaoMuestraC, altura*.6f, p5.color(255*100/34, 255*100/99, 255*100/100) );
		botaoMuestraControles.setPosicaoTexto("centro", "direita");
		botaoMuestraControles.setNomeBotao(" ");
		botaoMuestraControles.setTamanhoEtiqueta(.4f);
		botaoMuestraControles.setEtiqueta("ver\n+");*/
		
//		botaoMuestraControles.setColorOff(p5.color(120,120));
		
		PVector posicaoMuestraC2 = new PVector (bloqueBotaoWidth, menuSons.getBotaoHeight()/2 );
		botaoTrocaSample= new BotaoBase(p5, posicaoMuestraC2, altura*.8f, p5.color(255*100/16, 255*100/85, 255*100/95));
		botaoTrocaSample.setPosicaoTexto("centro", "direita");
		botaoTrocaSample.setNomeBotao("troca\nsample");
		botaoTrocaSample.setTamanhoTexto(altura*.3f);
		botaoTrocaSample.setColorOff(p5.color(120,120));
		botaoTrocaSample.setImageBotao("ic_action_import_export.png");
		
		SetSamplesName();
	}
	public void setBotaoSampleNome(String nom) {
		samples.get(getIndexLigado()).setNomeSampleBotao(nom);
	}
	public void setTituloText(String s) {
		tituloSelector = s;
	}
	public void desenhar(){
		p5.pushMatrix();
		p5.pushStyle();
		menuSons.desenhaNomeB();
		if ( menuSons.estaLigado() ) {
			p5.rectMode(PApplet.CENTER);
//			linhasFondo();
			
			p5.translate(posX, posY);
//			p5.rect(0, 0, p5.width, altura);
			p5.popStyle();
			p5.popMatrix();
			
			for (SampleBotao bs : samples) {
				bs.desenharBotaoCircular();
			}
//			botaoMuestraControles.desenharBotaoCircular();
			botaoTrocaSample.desenharBotaoCircular();
			desenhaTexto();
		}
		
	}
	private void linhasFondo() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.strokeWeight(2);
		p5.stroke(0);
		p5.noFill();
//		p5.line(p5.width*.01f, altura/2, p5.width*.15f ,  altura/2);
		
		p5.beginShape();
		  p5.vertex(p5.width*.15f , altura/2); // first point
		  p5.bezierVertex(p5.width*.2f, altura/2,  
				  			samples.get(0).getPosicao().x - altura/2,samples.get(0).getPosicao().y * ( 1f + ( PApplet.cos(p5.frameCount*.005f) )),
				  			samples.get(0).getPosicao().x, samples.get(0).getPosicao().y);
		p5.endShape();
		int sw = 1;
		for (int i = 0 ; i < cantBotoes - 1 ; i++) {
			PVector p1 = new PVector ( samples.get(i).getPosicao().x, samples.get(i).getPosicao().y );
			PVector p2 = new PVector ( samples.get(i+1).getPosicao().x, samples.get(i+1).getPosicao().y );
			PVector contro_p1 = new PVector (samples.get(i).getPosicao().x + altura, 
									samples.get(i).getPosicao().y * ( 1f - ( PApplet.cos(p5.frameCount*.005f) * sw )));
			PVector contro_p2 = new PVector ( samples.get(i+1).getPosicao().x - altura, 
									samples.get(i+1).getPosicao().y * ( 1f - ( PApplet.sin(p5.frameCount*.005f) * sw )));
			p5.beginShape();
			  p5.vertex(p1.x, p1.y); // first point
			  p5.bezierVertex(contro_p1.x, contro_p1.y,  contro_p2.x, contro_p2.y, p2.x, p2.y);
			p5.endShape();
			
			sw *= -1;
		}
		p5.beginShape();
		  p5.vertex(p5.width*.01f, altura/2); // first point
		  p5.bezierVertex(p5.width*.0008f, altura*.75f,  
				  			bloqueBotaoWidth*.4f, altura, 
				  			bloqueBotaoWidth/2, altura*.9f);
		p5.endShape();
		
		p5.popStyle();
		p5.popMatrix();
	}
	private void desenhaTexto() {
		p5.pushMatrix();
		p5.pushStyle();
		p5.fill(255);
		p5.textAlign(PApplet.RIGHT, PApplet.CENTER);
		p5.textSize(p5.height*.02f);
		p5.translate(p5.width*.08f, p5.height*.05f);
		p5.text(tituloSelector, 0,0);
		p5.popMatrix();
		p5.popStyle();
	}
	/**
	 * Evaluação do ponto presionado, se for o caso, troca o sample ativo de som, e evalua também os botoes de reprodução e efeito de cada sample
	 * @param evalua
	 * @return boolean
	 */
	public boolean escutaClicks(PVector evalua) {
		boolean resp = false;
		if (menuSons.estaLigado()) {
	//		boolean cambiaAbertura = botaoMuestraControles.botaoOnClick(evalua); //se for presionado vai cambiar o conteudo visivel dos botoes
			for (SampleBotao bs : samples) {
				if ( bs.botaoToogleOnClick(evalua) ) { //evalua se o botao foi pressoado	
					samples.get(indexLigado).setEstadoBotao(false);
					indexLigado = ((SampleBotao) bs).getId(); //novo index para o som ativo
					botaoTrocaSample.setColorFixo(bs.getColorOn());
					PdBase.sendBang( ((SampleBotao) bs).getSampleNome() );  //Emite o son do sample ao pressoar o botao
				}
				bs.escutaBotoesInternos( evalua );
		/*		if (cambiaAbertura) {
					bs.cambiaAbertura();
				}	
				
		*/
			}
		/*	if (cambiaAbertura){
				return false;
			}*/
		
			if  (botaoTrocaSample.botaoOnClick(evalua)){
				abreJanelaSample = true;
				return false;
			} else {
				abreJanelaSample = false;
			}
		}
		resp = menuSons.botaoOnClick(evalua);
		return resp;
	}
	public boolean abreJanelaSample(){
		return abreJanelaSample;
	}
	public int getIndexLigado(){
		return indexLigado;
	}
 	public String getSampleAtivo(){
		return ((SampleBotao) samples.get(indexLigado)).getSampleNome();
	}
	public int getCorAtiva(){
		return samples.get(indexLigado).getColorOn();
	}
	public String getSampleRandom() {
		int n = (int) p5.random ( 0, (cantBotoes-1) );
		
		return ((SampleBotao)samples.get(n)).getSampleNome();
	}
	public String getEfectoLigado(){
		return ((SampleBotao) samples.get(indexLigado)).getEfectoNome();
	}
	private void SetSamplesName(){
		((SampleBotao) samples.get(0)).setNomeSample("sample1");
		((SampleBotao) samples.get(1)).setNomeSample("sample2");
		((SampleBotao) samples.get(2)).setNomeSample("sample3");
		((SampleBotao) samples.get(3)).setNomeSample("sample4");
		((SampleBotao) samples.get(4)).setNomeSample("sample5");
		((SampleBotao) samples.get(5)).setNomeSample("sample6");
		((SampleBotao) samples.get(6)).setNomeSample("sample7");
	}
	
	
}
