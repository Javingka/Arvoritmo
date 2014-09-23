package cl.javiercruz.arvore.processing.UIbotoes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Codigo {
	PApplet p5;
	private PVector pos;
	private int cantBotoes;
	private int cantGalhos;
	private ArrayList <BotaoCodigo> botoesCodigo; //lista com os botoes que vāo gerar o código
	public ArrayList <Integer> estadoBotoes; //lista com os estados (0 ou 1) dos botoes
	private int estadoInicialTronco;
	
	public Codigo(PApplet _p5, PVector _pos, int cantGalhos){
		p5 = _p5;
		pos = _pos;
	
		this.cantGalhos = cantGalhos;
		cantBotoes = cantGalhos-1;
		
		estadoInicialTronco = 1;
		botoesCodigo = new ArrayList<BotaoCodigo>();
		estadoBotoes = new ArrayList <Integer>();
		setlistaBotoes(cantBotoes);
		setCodigoInicial(this.cantGalhos);
	}
	private void setCodigoInicial(int cBotoes) {
		for (int i = 0 ; i < cantGalhos ; i++) {
			if (i == 0 )
				estadoBotoes.add( estadoInicialTronco );
			else
				estadoBotoes.add( botoesCodigo.get(i-1).getEstadoBotao() );
		}
	}
	private void setlistaBotoes(int cBotoes){
		float ang = PApplet.PI / (cBotoes+1) ;
		for (int i = 0 ; i < cBotoes ; i++) {
			float angulo = ang * (i+1);
			PVector posB = new PVector (pos.x + (p5.width*.2f * PApplet.cos(angulo)),
								          pos.y + (p5.width*.2f * PApplet.sin(angulo)));
			botoesCodigo.add( new BotaoCodigo (p5, posB, estadoInicialTronco) );
		}
	}
	public void setPos(int ind, PVector newPos) {
		botoesCodigo.get(ind).setNewPos(newPos);
		
	}
	public void desenhar () {
		for (BotaoCodigo bc : botoesCodigo) {
			bc.desenhar();
		}
	}
	
	public void evaluaBotoes ( PVector evaluar, float scaleZoom, PVector translateZoom) {
		for (int i = 0 ; i < cantBotoes ; i++) { 
			//i+1, por que o primeiro elemento de estadoBotoes é sempre 1, é o tronco da arvore
			estadoBotoes.set(i+1, botoesCodigo.get(i).botaoClickListener(evaluar, scaleZoom, translateZoom) );
		}
	}
}
