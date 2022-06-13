package br.com.jeanjose.cm.visao;

import javax.swing.JFrame;

import br.com.jeanjose.cm.modelo.Tabuleiro;

/*Como a uma herança entre tela principal e JFrame, a tela principal é um JFrame*/
@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame{

	public TelaPrincipal() {
		
		
		//Criando o meu tabueliro
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		
		//Criando o painel Tabuleiro e adicionando na tela
		PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);
		add(painelTabuleiro);
		
		//Setando o título da tela
		setTitle("Campo Minado - Jean José");
		//Setando o tamanho da tela
		setSize(690,438);
		//Setando a tela para centralizar com o monitor
		setLocationRelativeTo(null);
		//Setando a tela para fechar a processo quando apertar o "X|fechar" da janela
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Setando a visibilidade da tela para true
		setVisible(true);
	}
	public static void main(String[] args) {
		new TelaPrincipal();
		
	}
}
