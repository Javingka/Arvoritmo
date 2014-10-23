package cl.javiercruz.arvore.processing.barra;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.puredata.core.PdBase;

import cl.javiercruz.arvore.R;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Classe de control general do Aplicativo que implementa as seguintes clases:
 * GestionadorArvores 	gestionadorArvore /
 * BotaoRec				bRec /
 * BotaoPlay			bPlayPause 
 * @author javingka
 *
 */

public class BarraControl{
	PApplet p5;
	SeekBar tempoIn;
	private float altura;
	float posX, posY;
	
	private BotaoBase bRec;
	public BotaoBase bPlayPause;
	public BotaoBase gestionadorArvore;
	public BotaoBase bSincro;
	public BotaoBase zoom;
	public BotaoNomeRect menuArvore;
	boolean BotoesVisiveis;
	public int colorDeFondo;
	
	BotaoBase botaoSalva;
	BotaoBase botaoCarga;
	
	public BarraControl(PApplet _p5, int _cantidadInicialArvores){
		this(_p5, _cantidadInicialArvores, _p5.color(255) );
				
	}
	public BarraControl(PApplet _p5, int _cantidadInicialArvores, int color){
		colorDeFondo = color;
		p5 = _p5;
		posX = p5.width*.75f;
		altura = p5.height*.08f;
		posY =  (altura / 2 );
		
		gestionadorArvore = new GestionadorArvores ( p5, new PVector (p5.width*.9f, p5.height*.05f), altura*.6f,
				            colorDeFondo, _cantidadInicialArvores );
		gestionadorArvore.setPosicaoTexto("centro", "esquerda");
		gestionadorArvore.setNomeBotao(" ");
		gestionadorArvore.setColorOff(colorDeFondo);
		gestionadorArvore.setColorON(colorDeFondo);
		
		bRec = new BotaoRec ( p5, new PVector (p5.width*.95f, p5.height*.85f), altura*.8f, p5.color(0,255,255) );
		bRec.setPosicaoTexto("centro", "esquerda");
		bRec.setNomeBotao("REC");
		
		bPlayPause = new BotaoPlay ( p5, new PVector (p5.width*.95f, p5.height*.95f), altura*.8f, colorDeFondo); //p5.color(30,255,255) );
		bPlayPause.setPosicaoTexto("centro", "esquerda");
		bPlayPause.setNomeBotao("Play");

		menuArvore = new BotaoNomeRect(p5, "ÁRVORE", "direitaSup");
		
		botaoSalva = new BotaosSalvaCarga (p5, new PVector (p5.width*.95f, p5.height*.425f), altura*.75f, colorDeFondo,"ic_action_save_gris.png" );
		botaoSalva.setPosicaoTexto("centro", "esquerda");
		botaoSalva.setNomeBotao("Salva");
		botaoCarga = new BotaosSalvaCarga (p5, new PVector (p5.width*.95f, p5.height*.575f), altura*.75f, colorDeFondo,"ic_action_storage_gris.png" );
		botaoCarga.setPosicaoTexto("centro", "esquerda");
		botaoCarga.setNomeBotao("Abre");
	}
	
	public void desenhar(){
		p5.pushMatrix();
		p5.pushStyle();
		p5.rectMode(PApplet.CENTER);
		p5.translate(posX, posY);
//		p5.noStroke();
//		p5.fill(0);
//		p5.rect(0, 0, p5.width, altura);
		p5.popStyle();
		p5.popMatrix();
		
		menuArvore.desenhaNomeB();
		if ( menuArvore.estaLigado() ) {
			gestionadorArvore.desenharBotaoCircular();
		}
		((BotaosSalvaCarga) botaoSalva).desenhaComLogo();
		((BotaosSalvaCarga) botaoCarga).desenhaComLogo();
		bRec.desenharBotaoCircular();
		((BotaoPlay) bPlayPause).desenharBotaoCuadrado();
//		bSincro.desenharBotaoCircular();
//		((SliderZoom) zoom).desenharSlider();
	}
	
/**
 * Metodo para executar alguma açāo se o botāo for clicado
 * @param evalua
 */
	public boolean escutaClicks(PVector evalua) {
		boolean resp = false;
		//primero evalua os métodos dos botoes que estão sempre visiveis
		
		bPlayPause.botaoOnClick(evalua);
		if (bPlayPause.mudandoOn) {
			bPlayPause.setNomeBotao("STOP"); 
			resp = true;
		} else if (bPlayPause.mudandoOff) {
			bPlayPause.setNomeBotao("PLAY"); //evalua se o botao foi pressoado
			resp = true;
		}
			
		//depois evalua os botoes que estan visiveis quando o menu estiver ligado
/*		if (menuArvore.estaLigado()) {
			if (verControlesArvore.botaoOnClick(evalua)) { //se um control deles for pressionado, no final da um return, para evitar que leia o seguinte metodo
				if (verControlesArvore.mudandoOff)
					verControlesArvore.setNomeBotao("ver\ncontroles árvore");
				else if (verControlesArvore.mudandoOn)
					verControlesArvore.setNomeBotao("ocultar\ncontroles árvore");
			}
		}*/
		
//		bSincro.botaoOnClick(evalua); //evalua se o botao foi pressoado	
		menuArvore.botaoOnClick(evalua);
		
		return resp;
	}
	public int escutaGestonadorArvore(PVector evalua){
		int r = 0;
		if (menuArvore.estaLigado()) {
			r = ((GestionadorArvores) gestionadorArvore).escutaBotoesInternos(evalua); //botões internos de soma ou resta de árvores
		}
		return r;
	}
	public boolean escutaSlicer(PVector evalua) {
		boolean resp = false; 
		if (zoom.botaoOnClick(evalua))
			resp = true;
		return resp;
	}
	public float getSliderZoom() {
		return ((SliderZoom) zoom).getZoomFromPos();
	}
	public boolean  escutaBRec(PVector evalua) {
		boolean resp = false;
		boolean estadoAnterior = bRec.getEstadoBotao();
		boolean recB = bRec.botaoOnClick(evalua); //evalua se o botao foi pressoado
		
		if (recB && !estadoAnterior) {
			((BotaoRec)bRec).turnBotaoOff();
	//		p5.println ("ON");
			resp = true;
		} else if (recB && estadoAnterior) {
	//		p5.println ("OFF");
			((BotaoRec) bRec).terminaGrabacao();
		}		
		return resp;
	}
	public void comecaGrabacao (String nomeArq) {
		((BotaoRec) bRec).comecaGrabacao(nomeArq);
		((BotaoRec)bRec).turnBotaoOn();
	}
	public boolean escutaSalva(PVector evalua) {
		boolean es = botaoSalva.botaoOnClick(evalua); //evalua se o botao foi pressoado
		return es;
	}
	public boolean escutaCarga(PVector evalua) {
		boolean ec = botaoCarga.botaoOnClick(evalua); //evalua se o botao foi pressoado
		return ec;
	}
}
