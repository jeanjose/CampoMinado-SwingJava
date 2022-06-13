package br.com.jeanjose.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.jeanjose.cm.modelo.Tabuleiro;

/*JPanel é um componente que agrupa outros componentes dentro dele
 * Vamos criar os botões e agrupar dentro desse JPanel*/
@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel{

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		/*Metodo para mostrar como serão exibidos os meus botões
		 * O método GridLayout é para definir que ele será mostrado
		 * através de linhas e colunas */
		setLayout(new GridLayout(
				tabuleiro.getQtdLinhas(), tabuleiro.getQtdColunas()));
		
		/*Uma função para cada campo dentro do tabuleiro, criar um botão*/
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		//Vamos resgistrar o resultado do tabuleiro e mostrar para o usuário, se ganhou ou perdeu
		tabuleiro.RegistrarObsersadorResultado(e -> {
			
			//Esse método significa que só irá executar as coisas depois
			SwingUtilities.invokeLater(() -> {
				if(tabuleiro.objetivoAlcancado()) {
					JOptionPane.showMessageDialog(this, "UII... Ganhou! Bora de novo?");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu TROUXA! kkkkkk");
				}
				
				tabuleiro.reiniciarJogo();
			});
			
		});
}
}
