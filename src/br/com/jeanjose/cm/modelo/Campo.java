package br.com.jeanjose.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo{
	
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;

	private List<Campo> vizinhos = new ArrayList<>();
	//Criando uma lista de observadores
	private List<CampoObservador> observadores = new ArrayList<>();
	
	Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}
	
	//Criando um método para registrar os observadores
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	//Método criado para quando algum evento acontecer, ele notificar os observadores
	//ele está privado, pois só precisará notificar o observador dentro do campo
	private void notificarObservadores(CampoEvento evento) {
		/*Executando um Stream, passando pelos observadores
		e dizendo que ele vai ocorrer nesse campo e vai receber o evento que foi 
		passado por parametro */
		observadores.stream()
				.forEach(obs -> obs.eventoOcorreu(this, evento));
	}

	//Metodo para abrir os vizinhos juntos caso seja seguro
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		//Distância
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	//metodo para colocar a marcação
	public void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}
	
	//Metodo para abrir as "casas", caso tenha bomba irar explodir, caso o vizinho ao lado seja seguro vai abrir junto
	public boolean abrir() {
	
		if (!aberto && !marcado) {
			if(minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
		
			return true;
		} else {
			return false;
		}
	}
	
	//Metodo para verificar se a vizinhaça tem bomba ou não para abrir os campos onde não tiver mina
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	//Metodo para minar?
	void minar() {
		minado = true;
	}
	
	//Campo está minado?
	public boolean isMinado() {
		return minado;
	}

	//Campo está marcado?
	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	
	//Campo está aberto?
	public boolean isAberto() {
		return aberto;
	}
	
	//Campo está fechado?
	public boolean isFechado() {
		return !isAberto();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protedigo = minado && marcado;
		return desvendado || protedigo;
	}
	
	public int minasNaVizinhanca() {
		//metodo para retornar a quantidade de minas na vizinhança
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
			

		
	}
	
}
