package cl.javiercruz.arvore.processing.botoes;

import org.puredata.core.PdBase;

import cl.javiercruz.arvore.processing.barra.BotaoBase;

import processing.core.PApplet;
import processing.core.PVector;
/**
 * Classe para desenhar um botão especial para a seleção de samples, e implementada na classe SampleSelector
 * @author javingka
 *
 */
public class SampleBotao extends BotaoBase{
	String nomeSample;
	String nomeSampleBotao;
	private int id;
	private boolean aberto;
	private int estado;
	BotaoBase reproductor;
	public BotaoBase efectoSwitch;
	int colorOn;
	/**
	 * 
	 * @param _p5 / PApplet - Objeto do  PAppletInicial
	 * @param _pos / PVector - posição do botão
	 * @param _diam / float - diametro do botão
	 * @param _color / int - color
	 * @param _nomeSample / String - nome do sample "silencio" quando não tiver sample asociado
	 * @param _id
	 */
	SampleBotao(PApplet _p5, PVector _pos, float _diam, int _color, String _nomeSample, int _id) {
		super(_p5, _pos, _diam, _color);
		nomeSample = _nomeSample;
		id = _id;
		aberto = false;
		estado = 0;
		setPosicoesBotoes(_pos);
		colorOn = 255;
	}
	/**
	 * Posicionamento dos botoes de reprodução do som e de incrporação de efeito
	 * @param _p
	 */
	private void setPosicoesBotoes( PVector _p ) {
		PVector posRep = new PVector (_p.x  ,  _p.y);
		reproductor = new BotaoBase(p5, posRep, diam * 1.25f, p5.color(255));
		reproductor.setPosicaoTexto("acima", "direita");
		reproductor.setNomeBotao("escuta");
		reproductor.setColorOff(p5.color(120,120));
		PVector posEfec = new PVector (_p.x + diam * 1.75f ,  _p.y - diam * .75f);
		efectoSwitch = new EfectoBotao(p5, posEfec, diam*1.25f, p5.color (getColorOn(), 200) );
		efectoSwitch.setPosicaoTexto("centro", "direita");
		efectoSwitch.setNomeBotao("efecto Off");
		efectoSwitch.setColorOff(p5.color(120,120));
		
	}
	@Override //una nova versión para disenhar este botao
	public void desenharBotaoCircular() {
		p5.pushMatrix();
		p5.pushStyle();
		
		if (estado > 0) {
			
			p5.stroke(getColorOn(), 120);
			p5.noFill();
			p5.strokeWeight(3);
			
/*			p5.beginShape();
			  p5.vertex(pos.x, pos.y); // first point
			  p5.bezierVertex(pos.x + diam, pos.y , 
					  reproductor.getPosicao().x - diam, reproductor.getPosicao().y + diam*1.6f, //* ( 1f - ( PApplet.cos(p5.frameCount*.005f) / 10)),
					  reproductor.getPosicao().x, reproductor.getPosicao().y  + diam*.3f);
			p5.endShape();*/
			
			p5.beginShape();
			  p5.vertex(pos.x, pos.y); // first point
			  p5.bezierVertex(pos.x + diam, pos.y, 
					  efectoSwitch.getPosicao().x - diam, efectoSwitch.getPosicao().y - diam*.6f, // * ( 1f - ( PApplet.cos(p5.frameCount*.005f) / 10)),
					  efectoSwitch.getPosicao().x, efectoSwitch.getPosicao().y);
			p5.endShape();
			if (estado > 1)
				efectoSwitch.desenharBotaoCircularSemTexto();
			else
				efectoSwitch.desenharBotaoCircularTextoGira(0);
			
			
		}
		if (ligado) {
			p5.stroke(getColorOn(), 120);
			p5.strokeWeight(p5.height*.03f);
			reproductor.desenhaLogoSound();
		} else {
			p5.strokeWeight(p5.height*.01f);
			p5.stroke(getColorOn(), 30);
			int colorN = p5.color(255, 120);
			reproductor.desenhaLogoSound(colorN);
		}
		p5.translate(pos.x, pos.y);
		p5.fill(cargaColor);
		p5.ellipse(0,0, diam, diam);
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
	public boolean escutaBotoesInternos(PVector evaluar) {
		boolean resp = false;
		if (reproductor.botaoOnClick(evaluar))  {
			resp = true;
			String nome = "efecto" + nomeSample;
			PdBase.sendFloat(nome, 0);
			if (efectoSwitch.ligado) {	
				PdBase.sendFloat(nome, 1);
			}
				
			PdBase.sendBang( getSampleNome() );  //Emite o son do sample ao pressoar o botao
			
			if (reproductor.getEstadoBotao())
				reproductor.setNomeBotao("escuta");
			
			reproductor.setEstadoBotao(false);
		} else if (efectoSwitch.botaoOnClick(evaluar)) {
			resp = true;
			if (efectoSwitch.mudandoOn) {
				String nome = "efecto" + nomeSample;
//				PdBase.sendFloat(nome, 1);
			} else if (efectoSwitch.mudandoOff) {
				String nome = "efecto" + nomeSample;
//				PdBase.sendFloat(nome, 0);
			}
			if (efectoSwitch.getEstadoBotao())
				efectoSwitch.setNomeBotao("efecto On");
			else
				efectoSwitch.setNomeBotao("efecto Off");
		}
		return resp;
	}
	@Override //reescribimos esta funciona para agregar un propio switch de cambio para visualizar el boton abierto e agrandar a parea de toque
	public boolean botaoToogleOnClick (PVector evaluar) {
		boolean ligadoPrev = ligado;
		boolean resp = false;
		click = false;
		float dist = PVector.dist(pos, evaluar);
		
		if (dist < diam * 1.5f) {
			if (botaoToggle && !ligado) {
				resp = true;
				click = true;
				ligado = !ligado;
				atualizaCor();
			}
			testMudanca(ligadoPrev);
		}
		return resp;
	}
	/**
	 * Recebe o nome do sample a reproducir
	 * @param nome
	 */
	public void setNomeSample (String nome){
		nomeSample = nome;
		setNomeBotao(nomeSample);
	}
	public String getEfectoNome(){
		if (efectoSwitch.ligado)
			return "efecto" + nomeSample;
		else
			return "sin_efecto";
	}
	public String getSampleNome(){
		return nomeSample;
	}
	public int getId() {
		return id;
	}
	public void cambiaAbertura() {
		aberto = !aberto;
		estado++;
		estado = estado % 3;
	}
	public void setNomeSampleBotao(String nome){
		nomeSampleBotao = nome;
	}
	@Override
	public void desenhaTextoYOffset( float YOff) {
		p5.fill(255,colorOn);
		p5.textSize(tamanhoTextoBotao);
		p5.text(nomeSampleBotao, tx, ty + YOff);
		
		colorOn--;
		if (colorOn < 0) colorOn = 0;
	}
	@Override
	public void testMudanca(boolean ligadoPrev) {
		if (ligado && !ligadoPrev) {
			mudandoOn = true;
			colorOn = 255;
		} else
			mudandoOn = false;
		
		if (!ligado && ligadoPrev)
			mudandoOff = true;
		else
			mudandoOff = false;
	}
	public void setColorOn() {
		colorOn = 255;
	}
}
