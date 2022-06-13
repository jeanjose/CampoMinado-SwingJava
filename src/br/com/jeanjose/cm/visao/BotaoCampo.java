package br.com.jeanjose.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import br.com.jeanjose.cm.modelo.Campo;
import br.com.jeanjose.cm.modelo.CampoEvento;
import br.com.jeanjose.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	//Criando cores para depois setar nos elementos
	private final Color BG_PADRAO = new Color(184, 184, 184); // Cinza
	private final Color BG_MARCAR = new Color(8, 179, 247); //
	private final Color BG_EXPLODIR = new Color(189, 66, 68); //Vermelho
	private final Color BG_LOSER = new Color(255, 10, 10); //Vermelho claro
	private final Color TEXTO_VERDE = new Color(0, 100, 0); //Verde
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		//Setando o background para cor que eu criei ali em cima
		setBackground(BG_PADRAO);
		//Criando bordas para as linhas, para ficar um efeito mais bonito
		setBorder(BorderFactory.createBevelBorder(0));
		//Vai aplicar o background de uma maneira diferente
		setOpaque(true);
		//Registrando o evento
		addMouseListener(this);
		//Colocando o campo para ser observado
		campo.registrarObservador(this);
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch(evento) {
		case ABRIR: 
			aplicarEstiloAbrir();
			break;
		case MARCAR: 
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
			setIcon(new ImageIcon(""));
		}
	

	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_LOSER);
		//Setando um icone que está dentro da minha pasta do projeto
		setIcon(new ImageIcon("bomba.png"));
		
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		//Setando um icone que está dentro da minha pasta do projeto
		setIcon(new ImageIcon("bandeira.png"));
	}

	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			//Setando um icone que está dentro da minha pasta do projeto
			setIcon(new ImageIcon("bomba.png"));
			return;
		}
		
		//Assim que eu colocar para abrir meu campo, vai setar a cor padrão e criar um line border
		setBackground(BG_PADRAO);
		
		/*Criando um Switch para definir a cor que o número irá aparecer,
		 * de acordo com a quantidade de bombas*/
		switch(campo.minasNaVizinhanca()) {
		case 1:
			//Método para setar a cor de frente 
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}
		
		/*Se a vizinhaça não for segura, me retorne o número de bombas ao redor, 
		 * caso ela seja segura, não retorne nada*/
		String valor =!campo.vizinhancaSegura() ?
				campo.minasNaVizinhanca() + "" : "";
		//Como estamos dentro da classe botão, temos esse método
		setText(valor);
	}
	
	//Interface dos eventos do mouse
	//Pegando os click do mouse
	@Override
	public void mousePressed(MouseEvent e) {
		//"e.getButton == 1" Já tem esse atributo/método no java para pegar a funcionalidade
		//do botão direito do mouse
			if(e.getButton() == 1) {
				campo.abrir();
			} else {
				campo.alternarMarcacao();
				//Se o campo não estiver marcado, set icone "vazio"
				if(!campo.isMarcado()) {
					setIcon(new ImageIcon(""));
				}
			}
	}
	

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
}
