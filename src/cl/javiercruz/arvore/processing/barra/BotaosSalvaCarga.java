package cl.javiercruz.arvore.processing.barra;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class BotaosSalvaCarga extends BotaoBase {

	PImage botaoGuardar;
	
	public BotaosSalvaCarga(PApplet _p5, PVector _pos, float _diam, int _color, String arquivoIcone) {
		super(_p5, _pos, _diam, _color);
		// TODO Auto-generated constructor stub
		
		botaoGuardar = p5.loadImage(arquivoIcone);
	}

	public void desenhaComLogo() {
		if (textoBotao != null) {
			atualizaDataTexto();
			desenhaTexto();
		}
		p5.image(botaoGuardar, pos.x,pos.y, diam*2, diam*2);
	}
	
}

