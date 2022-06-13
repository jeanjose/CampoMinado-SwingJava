package br.com.jeanjose.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

//A classe tabuleiro irá precisar detectar os eventos, por isso ela irá implementar campo observador
public class Tabuleiro implements CampoObservador {

	/*Os atributos estão em Final, porque uma vez que criado o tabuleiro
	 * não vamos alterar as linhas, colunas ou a quantidade de minas*/
	private final int qtdLinhas;
	private final int qtdColunas;
	private final int qtdMinas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	//Chamada de construtor, junto com as chamadas de metodo para gerar o campo, associnar os vizinhos e gerar as bombas;
	public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;
		this.qtdMinas = qtdMinas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	//Essa função vai permitir que eu percorra os elementos recebendo o campo e realizando uma ação
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	//Criando umm método para registrar os observadores
	public void RegistrarObsersadorResultado (Consumer<Boolean> observador) {
		observadores.add(observador); 
	}
	
	private void notificarObservadores(boolean resultado) {
		/*Executando um Stream, passando pelos observadores
		e dizendo que ele vai ocorrer nesse campo e vai receber o evento que foi 
		passado por parametro */
		observadores.stream()
				.forEach(obs -> obs.accept(resultado));
	}

	//Metodo para abrir campo através dos indices
	public void abrir(int qtdLinha, int qtdColuna) {
			campos.parallelStream()
				.filter(c -> c.getLinha() == qtdLinha && c.getColuna() == qtdColuna)
				//findFirst = pegue o primeiro elemento
				.findFirst()
				//Está presente? Se tiver vai chamar o c.abrir;
				.ifPresent(c -> c.abrir());;
	}
	
	//Metodo para fazer marcacao do campo
	public void alterarMarcacao(int qtdLinha, int qtdColuna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == qtdLinha && c.getColuna() == qtdColuna)
			//findFirst = pegue o primeiro elemento;
			.findFirst()
			//Está presente? Se tiver chame o metodo marcar
			.ifPresent(c -> c.alternarMarcacao());;
	}
	
	//lógica para gerar o campo, sem usar matriz
	private void gerarCampos() {	
		for (int linha=0; linha<qtdLinhas; linha++) {
			for(int coluna=0; coluna<qtdColunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
	}
	
	//Vai verificar todos os requisitos de quem pode ser ou não vizinho
	private void associarVizinhos() {	
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	//logica para sortear as minas do tabuleiro
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas<qtdMinas);
	}
	
	//Se todos os campos der Match, você alcançou o objetivo do jogo e venceu
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	public int getQtdLinhas() {
		return qtdLinhas;
	}
	
	public int getQtdColunas() {
		return qtdColunas;
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
	private void mostrarMinas() {
		//Vou pegar campo, vou passar por todo ele e filtrar apenas os campos minados
		//e abrir para o usuário ver os campos que estão minados
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
		
	}

}
