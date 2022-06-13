package br.com.jeanjose.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.jeanjose.cm.modelo.Tabuleiro;

/*JPanel � um componente que agrupa outros componentes dentro dele
 * Vamos criar os bot�es e agrupar dentro desse JPanel*/
@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel{

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		/*Metodo para mostrar como ser�o exibidos os meus bot�es
		 * O m�todo GridLayout � para definir que ele ser� mostrado
		 * atrav�s de linhas e colunas */
		setLayout(new GridLayout(
				tabuleiro.getQtdLinhas(), tabuleiro.getQtdColunas()));
		
		/*Uma fun��o para cada campo dentro do tabuleiro, criar um bot�o*/
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		//Vamos resgistrar o resultado do tabuleiro e mostrar para o usu�rio, se ganhou ou perdeu
		tabuleiro.RegistrarObsersadorResultado(e -> {
			
			//Esse m�todo significa que s� ir� executar as coisas depois
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
