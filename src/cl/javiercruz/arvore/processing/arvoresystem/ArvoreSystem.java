package cl.javiercruz.arvore.processing.arvoresystem;

import java.util.ArrayList;

import cl.javiercruz.arvore.processing.UIbotoes.BotaoCodigo;
import cl.javiercruz.arvore.processing.UIbotoes.Codigo;
import cl.javiercruz.arvore.processing.barra.BotaoBase;
import cl.javiercruz.arvore.processing.barra.BotaoPlay;
import cl.javiercruz.arvore.processing.barra.BotaoRec;
import cl.javiercruz.arvore.processing.barra.SliderZoom;
import cl.javiercruz.arvore.processing.botoes.BpmSelector;

import android.util.Log;
import processing.core.PApplet;
import processing.core.PVector;


public class ArvoreSystem {
	
	PApplet p5;
	private float py, px;
	CreadorArvoreRecursivo nuevoArvore; //funçāo que vai criar e atualizar a forma da arvore
	public BotaoBase ArbPlayPause;
	public Codigo codigo; //Código da arvore, define os galhos ativos
	public BpmSelector bpmSelector; //O selector de bpm
	private float refTamanho; //referencia para o tamanho da árvore
	private float anguloAbertura; 
	public ArrayList <Galho> galhos; //lista que tem os objetos galhos que formam a arvore
	private int cantGalhos;
	ArrayList <Integer> arrayOrdemGalhos; //lista que tem ordenados os IDs de posiçāo dos galhos na árvore, contando de baixo para cima é de dereita a esquerda
	ArrayList <Integer> arrayCodigoBinario; //lista com o código que determina o caminho que som vāo seguir na árvore
	private int PontosPorGalho; //cantidade de pontos de som que vāo ter os galhos
	private int galhoOnPlay;

	private float tempoCont; //contador do bpm
	private long estabilizadorTempo; //variavel para ajudar a estabilizar o tempo
	private float inverseCount; //variavel que permite mudar a direção de leitura do ritmo
	private int indiceGalhoTempoAtivo; //indica qual Galho esta sendo reproducido
	private int indicePontoGalhoTempoAtivo; //indica qual ponto dentro do Galho esta sendo ativo
	private long tempoBase;
	private int pulsos; //quantidade de pulsos nos galhos
	private float offset_x_PontoMov, offset_y_PontoMov; //variaveis com a quantidade de pixels que a árvore tem se deslocado
	private float offset_y_play, offset_y_bpm, offset_y_invierte, offset_y_verControl; //variaveis de movimentação do play e bpm
	boolean arvoreCresce, arvoreDisminui;
	int mudancaArvore, mudancaGalho; //quantidade de galhos (mudancaArvore) e pontos nos galhos (mudancaGalho)
	
	private int bpm;
	private boolean direcaoTempo; //establece a direção da letura
	private boolean pauseInterno; //pause que atinge só a árvore	
	private boolean muestraControles;
	//Botões para a mudança da arvore
	private BotaoBase [] botoesModificaArvore; //somaGalho, restaGalho, somaPonto, restaPonto;
	private boolean direccion;
	public boolean estadoSliderBPM; //Para indicar se o slider BPM esta sendo utilizado
	public BotaoBase invierte; //botao que invierte o ordem de reprodução da árvore
	public BotaoBase verControlesArvore; //botao para ver e ocultar os controles da arvore
	public boolean arvoreEmMovimento; //true quando a árvore estever se deslocando
	public int colorFundo;
	
	public ArvoreSystem( PApplet _p5, PVector _pos, float _refTamanho, int _nivelRecursivo, int pulsosPorGalho, int bpmIni) {
		this ( _p5,  _pos,  _refTamanho,  _nivelRecursivo,  pulsosPorGalho,  bpmIni, _p5.color (255,120) );
	}
/**
 * 
 * @param _p5 usa 'this' para accessar a metodos de Processing nesta classe
 * @param _pos //posição da árvore 
 * @param _refTamanho //um tamanho de referencia para desenhar a arvore (Não esta sendo utilizado)
 * @param _nivelRecursivo //nivel inicial da arvore
 * @param pulsosPorGalho //quantos pulsos de som por cada galho de arvore
 * @param bpmIni // Bits Per Minute inicial
 */
	public ArvoreSystem( PApplet _p5, PVector _pos, float _refTamanho, int _nivelRecursivo, int pulsosPorGalho, int bpmIni, int _colorFondo) {
		p5 = _p5; //PApplet para acessar aos metodos de Processing
		px = _pos.x; //posiçāo da arvore
		py = _pos.y;// p5.height*.2f; //posiçāo da arvore
		colorFundo = _colorFondo;
		
		cantGalhos = _nivelRecursivo;
		PontosPorGalho = pulsosPorGalho ;
		pulsos = cantGalhos * PontosPorGalho; //pulsos totais em cada loop
		indiceGalhoTempoAtivo = 0; // o indice que indica o galho ativo
		indicePontoGalhoTempoAtivo=0; //indice que indica o ponto de som ativa dentro de cada galho
		estabilizadorTempo = p5.millis();
		tempoCont = 0; // o contador que vāo percorrer a arvore fazendo soar os pontos ativos
		inverseCount = pulsos; 
		//obter os galhos ativos segundo o codigo
		arrayOrdemGalhos = new ArrayList <Integer>(); //arracy com os id dos Galhos ativos
		arrayCodigoBinario = new ArrayList <Integer>(); //array com o codigo que vāo gerar o caminho que segue o som na arvore
		
		refTamanho = p5.height*.2f * cantGalhos;//_refTamanho; //a referencia que vao dar o tamanho da arbore
		anguloAbertura =  PApplet.PI * .1f; //Angulo inicial de separaçāo de galhos.
		galhos = new ArrayList<Galho>(); //lista com todos os galhos da arvore
		fazNovaArvore(cantGalhos, refTamanho);
		PontoNota.diametroOff = p5.height * ( .105f / PontosPorGalho )  ;// * .06f; //.03f;
		PontoNota.diametroOnPlay = p5.height * ( .115f / PontosPorGalho ) ; // * .07f; //.04f;
		offset_y_PontoMov = -p5.height*.175f;
		offset_x_PontoMov = -p5.width*.11f;
		offset_y_play = p5.height*.03f;
		offset_y_bpm = p5.height*.2f;
		offset_y_invierte = p5.height*.12f;
		offset_y_verControl = p5.height*.2f;
		
		
//		ArbPlayPause = new BotaoPlay ( p5, new PVector (px, py + offset_y_play), p5.height*.04f, p5.color(30,255,255) );
		ArbPlayPause = new BotaoPlay ( p5,new PVector(px + p5.height*.1f, py + offset_y_play), p5.height*.06f, colorFundo );//p5.color(30,255,255) );
		ArbPlayPause.setDiamMen(p5.height*.03f);
		ArbPlayPause.setPosicaoTexto("abaixo", "centro");
		ArbPlayPause.setNomeBotao(" ");
		ArbPlayPause.setOffsetToPosII(new PVector(-p5.height*.1f , 0 ) );
		
		bpmSelector = new BpmSelector ( p5, new PVector (px, py + offset_y_bpm), p5.height*.05f, p5.color(150, 200,180), p5.width*.2f, bpmIni );
		bpmSelector.setPosicaoTexto("acima", "centro");
		bpmSelector.setNomeBotao("bits por minuto");
		bpmSelector.setVerticalFalse();
		
		invierte = new BotaoBase ( p5, new PVector (px-p5.height*.1f, py + offset_y_play ), p5.height*.08f, p5.color(80,255,255) ); //usa o offset do play 
		invierte.setDiamMen(p5.height*.05f);
		invierte.setPosicaoTexto("centro", "esquerda");
		invierte.setNomeBotao("invierte\npulso");
		invierte.setOffsetToPosII(new PVector(p5.height*.1f, offset_y_invierte - offset_y_play) );
		
		verControlesArvore = new BotaoBase ( p5, new PVector (px, py + offset_y_invierte ), p5.height*.05f, colorFundo ); //p5.color(40,255,255) );
		verControlesArvore.setPosicaoTexto("abaixo", "centro");
		verControlesArvore.setNomeBotao("controles");
		verControlesArvore.setOffsetToPosII(new PVector (0, offset_y_verControl) ); //defina a segunda posição, tambem mudar 0.5f no setListenerArvoreMove
		verControlesArvore.setColorON(p5.color(255));
		verControlesArvore.setImageBotao("ic_action_expand.png");
		
		criaBotoesDeMudanca();
		
		pauseInterno = false;
		muestraControles = false;
		direccion = true;
		//define o estado inicial do código que define o caminho de som da arvore
		codigo = new Codigo(p5, new PVector(px, py), cantGalhos ); //inicializaçāo do UI pra setear o codigo da arbore
		setCodigo();

		arvoreCresce = false;
		arvoreDisminui = false;
		
		estadoSliderBPM = false;
		direcaoTempo = true;
		bpm = 240;
		tempoBase = p5.millis(); //uma referencia de tempo para o ritmo
		
	}
	public void setDireccion (boolean d) {
		direccion = d;
	}
	public boolean setTempoArv() {
		boolean b = true;
		if (ArbPlayPause.ligado) {
			setTempoCont(bpmSelector.getBpm(), direccion);
		} else {
			b = false;
		}
		return b;
	}
	public void setTempoTo(int novotempo, boolean stop) {
		tempoCont = novotempo;
		tempoBase = p5.millis();
		estabilizadorTempo = 0;
		setTempoNovaPos();
		ativaPontoSom();
		if (stop) {
			ArbPlayPause.turnBotaoOff();
			ArbPlayPause.setNomeBotao(" "); //Play"); 
		}
	}
	public void setTempoCont(int bpm, boolean direcao) {
		this.bpm = bpm;
		boolean direcao0 = direcaoTempo; //direcao0 é a direção anterior
		direcaoTempo = direcao;
		boolean mudancaDirecao = (direcao0 != direcaoTempo) ? true : false ;
		atualizaTempoCont(mudancaDirecao);
	}
	private void atualizaTempoCont (boolean teveMudancaDir) {
		long unidadeTempo = 60000 / bpm;
		
		if (p5.millis() > tempoBase + unidadeTempo ) {
			estabilizadorTempo = p5.millis() - (tempoBase + unidadeTempo);
//			tempoBase = tempoBase + unidadeTempo;// - estabilizadorTempo;
			tempoBase = p5.millis() - estabilizadorTempo;
			
			tempoCont++;
		}

		tempoCont = tempoCont%pulsos;
		
		if (teveMudancaDir){
			tempoCont = (pulsos - 1) - tempoCont;
		}
		if (direcaoTempo) {
			indiceGalhoTempoAtivo = (int)  (cantGalhos *  (tempoCont / pulsos));
			indicePontoGalhoTempoAtivo = (int) (tempoCont % PontosPorGalho);				
		} else {
			inverseCount = (pulsos - tempoCont) -1;
			indiceGalhoTempoAtivo = (int)  (cantGalhos *  (inverseCount / pulsos));
			indicePontoGalhoTempoAtivo = (int) (inverseCount % PontosPorGalho);
		}
	}
	private void setTempoNovaPos () { //usado para atualizar a posiçāo quando da um Stop 

		tempoCont = tempoCont%pulsos;
		
		if (direcaoTempo) {
			indiceGalhoTempoAtivo = (int)  (cantGalhos *  (tempoCont / pulsos));
			indicePontoGalhoTempoAtivo = (int) (tempoCont % PontosPorGalho);				
		} else {
			inverseCount = (pulsos - tempoCont) -1;
			indiceGalhoTempoAtivo = (int)  (cantGalhos *  (inverseCount / pulsos));
			indicePontoGalhoTempoAtivo = (int) (inverseCount % PontosPorGalho);
		}
	}
	
	private void criaBotoesDeMudanca(){
		float diamBotao = p5.height*.06f;
		botoesModificaArvore = new BotaoBase[4];
		botoesModificaArvore[0] = new BotaoBase ( p5, new PVector (px - diamBotao*5f, py + diamBotao) ,  diamBotao, colorFundo );
		botoesModificaArvore[0].setTamanhoEtiqueta(1);
		botoesModificaArvore[0].setEtiqueta("+");
		botoesModificaArvore[1] = new BotaoBase ( p5, new PVector (px - diamBotao*2f, py + diamBotao) ,  diamBotao, colorFundo );
		botoesModificaArvore[1].setTamanhoEtiqueta(1);
		botoesModificaArvore[1].setEtiqueta("-");
		botoesModificaArvore[2] = new BotaoBase ( p5, new PVector (px + diamBotao*2f, py + diamBotao) ,  diamBotao, colorFundo );
		botoesModificaArvore[2].setTamanhoEtiqueta(1);
		botoesModificaArvore[2].setEtiqueta("+");
		botoesModificaArvore[3] = new BotaoBase ( p5, new PVector (px + diamBotao*5f, py + diamBotao) ,  diamBotao, colorFundo);
		botoesModificaArvore[3].setTamanhoEtiqueta(1);
		botoesModificaArvore[3].setEtiqueta("-");
		
		botoesModificaArvore[0].setPosicaoTexto("centro", "direita");
		botoesModificaArvore[0].setNomeBotao(""+cantGalhos); //""+ permite comverter rapido de int a string
		botoesModificaArvore[0].setTamanhoTexto(diamBotao*1.5f);
		botoesModificaArvore[1].setPosicaoTexto("centro", "esquerda");
		botoesModificaArvore[1].setNomeBotao(" ");
		botoesModificaArvore[2].setPosicaoTexto("centro", "direita");
		botoesModificaArvore[2].setNomeBotao(""+PontosPorGalho);
		botoesModificaArvore[2].setTamanhoTexto(diamBotao*1.5f);
		botoesModificaArvore[3].setPosicaoTexto("centro", "direita");
		botoesModificaArvore[3].setNomeBotao(" ");
	}
	public boolean getEstadoMudanca(int index) {
		return botoesModificaArvore[index].mudandoOn;
	}
	public void setEstadoBotaoDeMudanca(int index, boolean novoE) {
		botoesModificaArvore[index].setEstadoBotao(novoE);
	}
	public void addGalhoEPontos(int _nivelRecursivo, int pulsosPorGalho) {

		int nivelRecAnt = cantGalhos;
		cantGalhos = _nivelRecursivo;
		PontosPorGalho = pulsosPorGalho ;
		pulsos = cantGalhos * PontosPorGalho; //pulsos totais em cada loop
		
		refTamanho = p5.height*.2f * cantGalhos;
		nuevoArvore = new CreadorArvoreRecursivo(p5, cantGalhos, refTamanho, anguloAbertura);
		nuevoArvore.criaArvore();
		
		int indexIn = 0;
		ArrayList <PVector> temPos = nuevoArvore.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		
		//al ser un arbol dividido en 2 este es la expresión que nos ayudara a ir escogiendo los pares
		int var = (temPos.size() -1) / 2;
		
		//atualizacao galhos existentes
		galhos.get(indexIn).atualizaPosGalho(new PVector(0,0), temPos.get(0), PontosPorGalho );
		indexIn++;
		int i;
		for (i = 0 ; i < nivelRecAnt-1 ; i++ ) { //un loop por cada nivel recursivo
			for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
				PVector pv = temPos.get(j);
				if (pv.z == i) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
				  //se dibujan las ramas respectivas
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1), PontosPorGalho );
					indexIn++;
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1+var), PontosPorGalho );
					indexIn++;
				}
			}
		var = (var - 1) / 2; 
		}
		
		//Agregamos os galhos novos
		int contadorDeGalhos = indexIn;
		for (int k = i ; k < cantGalhos-1 ; k++ ) { //un loop por cada nivel recursivo
		  for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
		    PVector pv = temPos.get(j);
		    if (pv.z == k) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
		        //se dibujan las ramas respectivas
		        galhos.add(new Galho (p5, temPos.get(j), temPos.get(j+1), PontosPorGalho, k, contadorDeGalhos) );
		        contadorDeGalhos++;
		        galhos.add(new Galho (p5, temPos.get(j), temPos.get(j+1+var), PontosPorGalho, k, contadorDeGalhos) );
		        contadorDeGalhos++;
		     }
		  }
		  var = (var - 1) / 2; 
		}
		
		codigo = new Codigo(p5, new PVector(px, py), cantGalhos ); //inicializaçāo do UI pra setear o codigo da arbore
		setCodigo();
	}
	public void subGalhoEPontos(int _nivelRecursivo, int pulsosPorGalho) {

		int nivelRecAnt = cantGalhos;
		cantGalhos = _nivelRecursivo;
		PontosPorGalho = pulsosPorGalho ;
		pulsos = cantGalhos * PontosPorGalho; //pulsos totais em cada loop
		refTamanho = p5.height*.2f * cantGalhos;
		nuevoArvore = new CreadorArvoreRecursivo(p5, cantGalhos, refTamanho, anguloAbertura);
		nuevoArvore.criaArvore();
		int indexIn = 0;
		
		ArrayList <PVector> temPos = nuevoArvore.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		
		//al ser un arbol dividido en 2 este es la expresión que nos ayudara a ir escogiendo los pares
		int var = (temPos.size() -1) / 2;
		galhos.get(indexIn).atualizaPosGalho(new PVector(0,0), temPos.get(0), PontosPorGalho );
		indexIn++;
		for (int i = 0 ; i < cantGalhos-1 ; i++ ) { //un loop por cada nivel recursivo
			for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
				PVector pv = temPos.get(j);
				if (pv.z == i) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
				  //se dibujan las ramas respectivas
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1), PontosPorGalho );
					indexIn++;
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1+var), PontosPorGalho );
					indexIn++;
				}
			}
		var = (var - 1) / 2; 
		}	
		
//		Log.i("ArvoreSystem","indexIn: " + indexIn);
		//quitamos os galhos que ficam fora
		for (int k = galhos.size()-1 ; k >= indexIn; k-- ) { 
			galhos.remove(k);
//			Galho.CONTADOR--; //mantemos o contador atualizado
		}
		//atualizaçāo do novo código com a cant de niveis de galhos.
		codigo = new Codigo(p5, new PVector(px, py), cantGalhos ); //inicializaçāo do UI pra setear o codigo da arbore
		setCodigo();
		setTempoTo(0, false); //botamos el pulso actual a cero para evitar problemas
	}
	public void setMuestraControles(boolean muestraC) {
		muestraControles = muestraC;
	}
	
//Metodos de escuta de clicks nos Pontos
	public void DaPlay(){
		ArbPlayPause.turnBotaoOn();
		ArbPlayPause.setNomeBotao(" ");//Pause");
		tempoBase = p5.millis();
	}
	/**
	 * 
	 * @param evaluacao PVector que será evaluado
	 * @param scaleZoom float Com a escada de zoom atual para considerar nos calculos
	 * @param translateZoom PVector Com o deslocamento da árvore para considerar nos calculos
	 * @param sampleIn String com o nome do sample que for carregado no caso que o toque for num punto da árvore
	 * @param SampleRandom String com um nome random para botar no ponto
	 * @param colorAtivo Int color ativo do ponto
	 * @param efectoLigado String que diz se tem ou nao efeito
	 */
	public void arvoreListener(PVector evaluacao, float scaleZoom, PVector translateZoom, String sampleIn,
								String SampleRandom, int colorAtivo, String efectoLigado){
		//controlDeArvoreAtivo
		codigo.evaluaBotoes( evaluacao, scaleZoom, translateZoom ); //evaluação se for pressionado os botoes do código para ativar galhos
		setCodigo(); //atualiza o código. se tiver mudanças na linha anterior aqui aplica elas
		
		//evalua se algum dos pontos que componen a árvore foi pegado
		setPontosListener(evaluacao , indicePontoGalhoTempoAtivo, scaleZoom, translateZoom, sampleIn, SampleRandom, colorAtivo, efectoLigado);
		
		//Evaluação do botao que visauliza os controles
		boolean controlArvorePressionado = verControlesArvore.botaoOnClick(evaluacao, scaleZoom, translateZoom);
		if (controlArvorePressionado) { //se um control deles for pressionado, no final da um return, para evitar que leia o seguinte metodo
			if (verControlesArvore.mudandoOff) {
				invierte.setPosicaoDefault(true);
				ArbPlayPause.setPosicaoDefault(true);
				
				verControlesArvore.setPosicaoDefault(true);
				verControlesArvore.setNomeBotao("controles");
				verControlesArvore.setImageBotao("ic_action_expand.png");
				setMuestraControles(false);
			} else if (verControlesArvore.mudandoOn) {
				invierte.setPosicaoDefault(false);
				ArbPlayPause.setPosicaoDefault(false);
				
				verControlesArvore.setPosicaoDefault(false);
				verControlesArvore.setNomeBotao("fecha controles");
				verControlesArvore.setImageBotao("ic_action_volta.png");
				setMuestraControles(true);
			}
		}
		
		//evaluação de botões, só se estiver visivel
		if (muestraControles) {
			for (int i  = 0 ; i < botoesModificaArvore.length ; i++) {
				if (botoesModificaArvore[i].botaoOnClick(evaluacao, scaleZoom, translateZoom));
			}
		}
		//Evaluação botão de invertir só se o botão que abre e fecha os controles não tinha sido pressionado
		if (!controlArvorePressionado && invierte.botaoOnClick(evaluacao, scaleZoom, translateZoom)) {
			if (invierte.mudandoOff) {
				invierte.setNomeBotao("invierte\npulso");
				setDireccion(true);
			} else if (invierte.mudandoOn) {
				invierte.setNomeBotao("pulso\ninvierte");
				setDireccion(false);
			}
		}
		
		//eluaçao botāo Play só se o botão que abre e fecha os controles não tinha sido pressionado
		if (!controlArvorePressionado && ArbPlayPause.botaoOnClick(evaluacao, scaleZoom, translateZoom)) {
			if (ArbPlayPause.mudandoOn) {
				DaPlay();
				//ArbPlayPause.setNomeBotao("Pause"); 
			} else if (ArbPlayPause.mudandoOff) {
				setTempoTo(0, true);
				//ArbPlayPause.setNomeBotao("Play"); //evalua se o botao foi pressoado
			}
		}
		bpmSelector.bpmBotaoOnClick(evaluacao, scaleZoom, translateZoom);
	}
	private boolean setPontosListener(PVector evaluacao, int indexEmSom, float scale, PVector translateZoom, 
									String sampleIn, String SampleRandom, int colorAtivo, String efectoLigado) {
		boolean resp = false;
		int ct = 0;
		for (Galho g : galhos) {
			for (int i = 0 ; i < g.pontosNota.size() ; i++){
				boolean contato;
				contato = g.pontosNota.get(i).pontoOnClick(evaluacao, new PVector (px, py), scale, translateZoom);
				if (contato) {
					resp = true;
					galhos.get(ct).pontosNota.get(i).setColor(colorAtivo);
					if (g.listaBangs.get(i).get(0) == "silencio") {
						g.listaBrotes.get(i).add(new Brote (p5, g.pontosNota.get(i).getPos(), g.pontosNota.get(i).getActualColor() ) );
						if (sampleIn == "random") {
							g.listaBangs.get(i).set(0,SampleRandom);
							g.listaBangs.get(i).set(1,efectoLigado);
							if (efectoLigado == "sin_efecto")
								g.pontosNota.get(i).conEfecto = false;
							else
								g.pontosNota.get(i).conEfecto = true;
						} else {
							g.listaBangs.get(i).set(0,sampleIn);
							g.listaBangs.get(i).set(1,efectoLigado);
							if (efectoLigado == "sin_efecto")
								g.pontosNota.get(i).conEfecto = false;
							else
								g.pontosNota.get(i).conEfecto = true;
						}
					} else {
						if ( g.listaBrotes.get(i).size() > 0) 
							g.listaBrotes.get(i).remove(  g.listaBrotes.get(i).size() - 1 );
						
						g.listaBangs.get(i).set(0,"silencio");
						g.listaBangs.get(i).set(1,"sin_efecto");
					}
					
				}
			}
			ct++;
		}
		return resp;
	}
	public boolean setListenerArvoreMove(PVector evaluacao, float scaleZoom, PVector translateZoom) {
		boolean r = false;
		float dist = PVector.dist(new PVector ( ( (px  + offset_x_PontoMov) * scaleZoom )+ translateZoom.x  , 
												  ( (py  +  offset_y_PontoMov) * scaleZoom ) + translateZoom.y), evaluacao);
		
		if (dist < refTamanho*.2f ) {
			arvoreEmMovimento = true;
			px = (evaluacao.x - translateZoom.x ) / scaleZoom - offset_x_PontoMov;
			py = (evaluacao.y - translateZoom.y ) / scaleZoom -  offset_y_PontoMov;
			criaBotoesDeMudanca();
			bpmSelector.setNovaPos(new PVector(px, py + offset_y_bpm) );
			
			ArbPlayPause.setNovaPos(new PVector(px + p5.height*.1f, py + offset_y_play) ); //offset_y_play) );
			ArbPlayPause.setOffsetToPosII(new PVector(-p5.height*.1f,0) );
			
			invierte.setNovaPos(new PVector(px - p5.height*.1f , py + offset_y_play) );
			invierte.setOffsetToPosII(new PVector(p5.height*.1f, offset_y_invierte - offset_y_play) );
			
			verControlesArvore.setNovaPos(new PVector(px, py + offset_y_invierte) );
			verControlesArvore.setOffsetToPosII(new PVector (0, offset_y_verControl) );
			r = true;
		} 
		return r;
	}
	public void setListenerArvoreMuestraControles(PVector evaluacao, float scaleZoom, PVector translateZoom) {
		float dist = PVector.dist(new PVector ( ( (px  + offset_x_PontoMov) * scaleZoom )+ translateZoom.x  , 
				  ( (py  +  offset_y_PontoMov) * scaleZoom ) + translateZoom.y), evaluacao);
		if (dist < refTamanho*.2f ) {
			muestraControles = !muestraControles;
		}
	}
	public boolean escutaSlider(PVector evaluacao, float scaleZoom, PVector translateZoom) {
		boolean resp = false; 
		if (bpmSelector.bpmSlicerOnClick(evaluacao, scaleZoom, translateZoom)) {
			resp = true;
			bpmSelector.setBpm();
		}
		return resp;
	}
//Metodos de atualizaçao em cada frame
	public void ativaPontoSom () {
		PontoNota.diametroOff = p5.height * ( .105f / PontosPorGalho )  ;// * .06f; //.03f;
		PontoNota.diametroOnPlay = p5.height * ( .115f / PontosPorGalho ) ; // * .07f; //.04f;
		
		galhoOnPlay = arrayOrdemGalhos.get(indiceGalhoTempoAtivo); //pegamos o numero do galho que esta ativo
		galhos.get( galhoOnPlay ).setPontoParaAtivo(indicePontoGalhoTempoAtivo); //ativaçāo de cada ponto
	//	Log.i("ArvoreSystem","Galho.GALHO_ON_PLAY : "+ Galho.GALHO_ON_PLAY +" indiceGalhoTempoAtivo: " + indiceGalhoTempoAtivo );
	}
	public void desenhar(){
		
		p5.pushMatrix();
		p5.pushStyle();
		p5.translate(px, py);
		desenhaPontoCentral();
		desenhaLinhasNiveisRecursivos();
		
		int cont = 0;
		for (Galho g : galhos) {
			if ( g.getId() == galhoOnPlay  ) {//Se o galho que esta em Play 
				g.styleOn(); //troca o estilo do galho
			} else if ( arrayOrdemGalhos.contains( g.getId() )  ) //se toca desenhar um galho que esta na sequança visivel
				g.styleOnSound();
			else {//todos os outros galhos
				g.styleOff();
			}
			g.atualizar();
			
			if (!ArbPlayPause.getEstadoBotao()) {
				g.desenharEmPause();
			} else {
				g.desenhar();
			}
			
			cont++;
		}
//		nuevoArvore.desenharLinhasArvore();
		p5.popMatrix();
		p5.popStyle();
		desenhaBotoesTam();
		
	}
	private void desenhaBotoesTam() {
		//Desenha o botão de control fixo se a árvore esta em movimento 
		if(arvoreEmMovimento) {
			verControlesArvore.desenharBotaoCircular(false);
			invierte.desenhaFlecha();
			ArbPlayPause.desenharBotaoCircular();
		} else {
			verControlesArvore.desenharBotaoCircularMovil(false);
			invierte.desenhaFlechaMovil();
			ArbPlayPause.desenharBotaoCircularMovil();
		}
		
		if (muestraControles) {
			bpmSelector.desenharSlider();
			for (BotaoBase b : botoesModificaArvore) {
				b.desenharBotaoCircular();
			}
		}
	}
	private void desenhaLinhasNiveisRecursivos() {
		//desenho das linhas que marcam os niveis de recursividade
		p5.strokeWeight(1);
		p5.stroke( 255 );
		int eval = 0;
		int evalA = 0;
		float anguloDesenho = PApplet.PI*.5f / (cantGalhos-1);
		for (int i = 0 ; i < cantGalhos -1 ; i++) {
			float angD = anguloDesenho * i;
			for (int ind = 0 ; ind < galhos.size()-1 ; ind ++) {
				if (galhos.get(ind).getId() == eval ) {
					PVector pp = new PVector(galhos.get(ind).getFinalGalho().x + p5.height*.1f * PApplet.cos(p5.PI*.25f) ,
											galhos.get(ind).getFinalGalho().y + p5.height*.1f * PApplet.sin(p5.PI*.25f) );
					
					//multiplica *.3f para que a linha nao cheque a pegar ao bot�o
					PVector pp2 = new PVector(galhos.get(ind).getFinalGalho().x + p5.height*.08f * PApplet.cos(p5.PI*.25f) ,
							galhos.get(ind).getFinalGalho().y + p5.height*.08f * PApplet.sin(p5.PI*.25f) );
					
					PVector pp3 = new PVector(pp2.x + p5.height*.08f * PApplet.cos(angD) ,
												pp2.y + p5.height*.08f * PApplet.sin(angD) );
					
					//linha desde  o ponto ate o botao
					p5.line (galhos.get(ind).getFinalGalho().x ,galhos.get(ind).getFinalGalho().y,
							pp2.x, pp2.y); 
					p5.line(pp2.x, pp2.y, pp3.x, pp3.y);
					
					//SET novas posi��es para os switchs segundo o ponto da arvore
				//	PVector p = new PVector (px + pp2.x +refTamanho/10 , py + pp2.y ); //vetor que define posi��o dos botoes
					PVector p = new PVector (px+ 	pp3.x,py+ pp3.y);
					codigo.setPos(i, p);
				}	
			}
			evalA = eval;
			eval = (eval*2) + 1;
			
			for (int j = evalA; j < eval-1 ; j++) {
				p5.line (galhos.get(j).getFinalGalho().x ,galhos.get(j).getFinalGalho().y,
						galhos.get(j+1).getFinalGalho().x , galhos.get(j+1).getFinalGalho().y );
			}
		}		
	}
	private void desenhaPontoCentral() {
		p5.pushMatrix();
		p5.pushStyle();
//		p5.stroke(p5.color(0));
//		p5.line(-refTamanho/2, 0, refTamanho/2, 0);
		
		p5.translate(offset_x_PontoMov, offset_y_PontoMov);
		p5.noStroke();
		p5.fill(255,120);
		p5.ellipse(0,0, p5.height * .01f, p5.height * .01f);
		p5.fill(255);
		p5.textAlign(PApplet.CENTER, PApplet.CENTER);
		p5.textSize(p5.height * .025f);
	//	p5.text("mover", 0, p5.height * .05f);
	//	p5.text("árvore", 0, - p5.height * .05f);
		
		//Função que disenhas as flechas do "mover arvore"
		p5.rotate(PApplet.PI*.25f);
		for (int i = 0 ; i < 3 ; i++) {
			p5.stroke(255);
			p5.strokeWeight(2);
			p5.rotate(PApplet.TWO_PI / 3);
			p5.line (p5.height * .01f,0, p5.height * .04f, 0);
			p5.line (p5.height * .04f, 0, p5.height * .03f, p5.height * .01f);
			p5.line (p5.height * .04f, 0, p5.height * .03f, -p5.height * .01f);
		}
		p5.popStyle();
		p5.popMatrix();
	}
//Metodos de definiçāo do codigo e aplicaçāo dele nas nos galhos
	private void setCodigo() {
		if (cantGalhos > 0) {
			arrayCodigoBinario.clear();
			
			for (int ig = 0 ; ig < codigo.estadoBotoes.size() ; ig++ ) {
				arrayCodigoBinario.add( codigo.estadoBotoes.get(ig) );
//				Log.i("ArvoreSystem","arrayCodigoBinario " + ig + ": " + arrayCodigoBinario.get(ig));
			}
		}
		
		aplicaCodigoAGalhos();
	}
	private void aplicaCodigoAGalhos() {
		if (arrayOrdemGalhos.size() > 0)
			arrayOrdemGalhos.clear();
		
		
		for (int c = 0  ;  c < arrayCodigoBinario.size() ; c++ ) {
			int calculador = 0;
			int valorBinario = arrayCodigoBinario.get(c);
			
			int posInicialPorNivel = galhos.size()-1;
			int sumaPosicoesSegundoNivel = 0;
			
			for ( int n = 0 ; n < arrayCodigoBinario.size() - c ; n++) {
				posInicialPorNivel = (posInicialPorNivel - 2 ) / 2;
				if (posInicialPorNivel < 0)
					posInicialPorNivel = 0;
			}
			
			if ( c > 1 ){
				int contE = 1;
				for (int ct = c ; ct > 1 ; ct--) {
					sumaPosicoesSegundoNivel += arrayCodigoBinario.get(ct-1) * PApplet.pow(2, contE) ;
					contE++;
				}
			}
			
//			Log.i("ArvoreSystem","posInicialPorNivel: " + posInicialPorNivel);
//			Log.i("ArvoreSystem","sumaPosicoesSegundoNivel: " + sumaPosicoesSegundoNivel);
			
			if ( valorBinario == 1 ) {
				calculador = posInicialPorNivel + sumaPosicoesSegundoNivel + 2;
			} else {
				calculador = posInicialPorNivel + sumaPosicoesSegundoNivel + 1;
			}
			
			if (c == 0) //o primeiro elemento vai ter a posiçāo 0 
				calculador = 0;
			
			arrayOrdemGalhos.add(calculador);
//			Log.i("ArvoreSystem","posInicialPorNivel: " + posInicialPorNivel + " sumaPosicoesSegundoNivel: " + sumaPosicoesSegundoNivel + " calculador: " + calculador);
//			Log.i("ArvoreSystem","arrayOrdemGalhos " + c + ": " + arrayOrdemGalhos.get(c));
		}
		
//		int calculador = 0;
//		int a = arrayCodigoBinario.get(0);
//		int b = (int) arrayCodigoBinario.get(1);
//		int c = (int) arrayCodigoBinario.get(2);
//		int d = (int) arrayCodigoBinario.get(3);
//		
//		if (a == 1) {
//			calculador = 0 + (0 * a);
//		} else {
//			calculador = 0 + (0 * a);
//		}
//		arrayOrdemGalhos.add(calculador); //o primeiro galho é sempre o mesmo
//		
//		if (b == 1) {
//			calculador = 0 + (0 * a) + 2;
//			arrayOrdemGalhos.add(calculador); // b == 1 ; cal = 2
//		} else {
//			calculador = 0 + (0 * a) + 1;
//			arrayOrdemGalhos.add(calculador); // b == 0 ; cal = 1
//		}
//		
//		if (c == 1) {
//			calculador = 2 + (2 * b ) + 2; // c == 1 ; b == 1 ; cal = 6 //  c == 1 ; b == 0 ; cal = 4 
//			arrayOrdemGalhos.add(calculador); // 4 o 5
//		} else {
//			calculador = 2 + (2 * b ) + 1; // c == 0 ; b == 1 ; cal = 5 //  c == 0 ; b == 0 ; cal = 3 
//			arrayOrdemGalhos.add(calculador); // 3 o 4
//		}
//		
//		if (d == 1) {
//			calculador = 6 + (4 * b ) + (2 * c) + 2; // d == 1 ; c == 1 ; b == 0 ; cal = 12 //  c == 1 ; b == 0 ; cal = 4 
//			arrayOrdemGalhos.add(calculador); // 4 o 5
//		} else {
//			calculador = 6 + (4 * b ) + (2 * c) + 1; // d == 1 ; c == 1 ; b == 0 ; cal = 11
//			arrayOrdemGalhos.add(calculador); // 3 o 4
//		}
	}
//creaçāo da arvore, pegamos as posiçōes globais de uma funçāo recursiva
	private void fazNovaArvore(int niveisRecursivos, float _refTamanho) {
		nuevoArvore = new CreadorArvoreRecursivo(p5, niveisRecursivos, _refTamanho, anguloAbertura);
		nuevoArvore.criaArvore();
		colocaGalhosNaArvore();
	}
	public void colocaGalhosNaArvore() {
		//dibuja usando las posiciones obtenidas desde el arbol recursivo
		galhos.clear(); //limpiamos la lista de objetos a dibujar
		ArrayList <PVector> temPos = nuevoArvore.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		//al ser un arbol dividido en 2 este es la expresión que nos ayudara a ir escogiendo los pares
		int var = (temPos.size() -1) / 2;
		int contadorDeGalhos = 0;
		galhos.add(new Galho (p5, new PVector(0,0), temPos.get(0), PontosPorGalho, 0, contadorDeGalhos) );
		contadorDeGalhos++;
		for (int i = 0 ; i < cantGalhos-1 ; i++ ) { //un loop por cada nivel recursivo
			
		  for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
			  
		    PVector pv = temPos.get(j);
		    if (pv.z == i) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
		        //se dibujan las ramas respectivas
		        galhos.add(new Galho (p5, temPos.get(j), temPos.get(j+1), PontosPorGalho, i+1, contadorDeGalhos) );
		        contadorDeGalhos++;
		        galhos.add(new Galho (p5, temPos.get(j), temPos.get(j+1+var), PontosPorGalho, i+1, contadorDeGalhos) );
		        contadorDeGalhos++;
		     }
		  }
		  var = (var - 1) / 2; 
		}
	}

	public void trocaPosicaoDaArbore(){
		int indexIn = 0;
		nuevoArvore.criaArvore();
		ArrayList <PVector> temPos = nuevoArvore.getArrayListPosicoes(); //adquirimos la lista de posiciones 
		
		//al ser un arbol dividido en 2 este es la expresión que nos ayudara a ir escogiendo los pares
		int var = (temPos.size() -1) / 2;
		galhos.get(indexIn).atualizaPosGalho(new PVector(0,0), temPos.get(0) );
		indexIn++;
		for (int i = 0 ; i < cantGalhos-1 ; i++ ) { //un loop por cada nivel recursivo
			for (int j = 0 ; j < temPos.size()-1 ; j++) { //por c/nivel revisamos el array de vectores
				PVector pv = temPos.get(j);
				if (pv.z == i) {  //cuando el vector corresponda al nivel recursivo actual (recordar que .z equivale a este valor)
				  //se dibujan las ramas respectivas
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1) );
					indexIn++;
					galhos.get(indexIn).atualizaPosGalho(temPos.get(j), temPos.get(j+1+var) );
					indexIn++;
				}
			}
		var = (var - 1) / 2; 
		}		
	}
	public int getCantGalhos() {
		return cantGalhos;
	}
	public int getCantPontosPorGalho(){
		return PontosPorGalho;
	}
	public void mudancaTamanhoArvore(int mudancaGalho){ //para establecer se soma ou resta um galho
		if (mudancaGalho > 0) { // mudancaGalho vai ser 1
			arvoreCresce = true;
		} else if (mudancaGalho < 0) { //mudancaGalho vai ser -1
			arvoreDisminui = true;
		}
		this.mudancaArvore = getCantGalhos() + mudancaGalho;
		this.mudancaGalho = getCantPontosPorGalho();
		
		if (mudancaArvore < 1) //Para asegurar de não ter menos que um galho por árvore
			mudancaArvore = 1;
		if (mudancaArvore > 8) //Para asegurar de não ter mais que 8 galhos
			mudancaArvore = 8;
	}
	
	public void mudancaPontosSom(int mudancaEmPontos) {
		if (mudancaEmPontos > 0) {
			arvoreCresce = true;
		} else if (mudancaEmPontos < 0) {
			arvoreDisminui = true;
		}
		
		this.mudancaGalho = getCantPontosPorGalho() + mudancaEmPontos;
		this.mudancaArvore = getCantGalhos();
		
		if (mudancaGalho < 2)
			mudancaGalho = 2;
		if (mudancaGalho > 8)
			mudancaGalho = 8;
	}
	public void atualizaTamanhoArvore(){
		if (arvoreCresce) {
			addGalhoEPontos(mudancaArvore, mudancaGalho );
			arvoreCresce = false;
		} else if (arvoreDisminui) {
			subGalhoEPontos(mudancaArvore, mudancaGalho );
			arvoreDisminui = false;
		}
		botoesModificaArvore[2].setNomeBotao(""+PontosPorGalho); //atualiza o botão
		botoesModificaArvore[0].setNomeBotao(""+cantGalhos); //atualiza o botão
		
	}

	public float getPx() {
		return px;
	}
	public float getPy() {
		return py;
	}
	public float getReferencaTamanho(){
		return refTamanho;
	}
	public int getNivelRecursivo() {
		return cantGalhos;
	}
	public int getPulsosPorGalho() {
		return PontosPorGalho;
	}
	public int getBpm() {
		return bpmSelector.getBpm();
	}
}
