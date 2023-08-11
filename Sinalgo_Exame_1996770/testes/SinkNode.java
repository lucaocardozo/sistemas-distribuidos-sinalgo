//Victor Silvano Verri - RA: 1914871

package projects.wsn1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.wsn1.nodes.messages.WsnMsg;
import projects.wsn1.nodes.timers.SinkMsgTimer;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;

public class SinkNode extends Node {
	
	private Node proximoNoAteEstacaoBase = null;
	
		

	@Override
	public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof WsnMsg) {
                WsnMsg wsnMessage = (WsnMsg) message;
                if (wsnMessage.tipoMsg == 1) {
                	System.out.println("O nó Sink " + this.ID + " recebeu a mensagem direta " + wsnMessage.sequenceID + " do nó: " + wsnMessage.origem.ID);
                	Tools.appendToOutput("O nó Sink " + this.ID + " recebeu a mensagem direta " + wsnMessage.sequenceID + " do nó: " + wsnMessage.origem.ID);
                	SimpleNode sensorNodeRecebido = (SimpleNode) wsnMessage.origem;
                	sensorNodeRecebido.setColor(Color.GREEN); //Altera a cor dos nós simples que a mensagem foi recebida pelo nó Sink mais próximo
                }
            }
        }
    }
	
	@NodePopupMethod(menuText = "Construir Arvore de Roteamento") //O n� sink inicia criando a �rvore de roteamento com broadcast
	public void construirRoteamento() {
	    this.setProximoNoAteEstacaoBase(this); //Seta ele mesmo como o Esta��o Base
	    WsnMsg wsnMessage = new WsnMsg(1, this, null, this, 0); //O n� destino � null porque o destino s�o todos simple atrav�s do broadcast
	    //Tipo 0 para estabelecer rota, se fosse 1 seria dados (ultima instancia)
	    SinkMsgTimer timer = new SinkMsgTimer(wsnMessage);
	    timer.startRelative(1, this); //Envia uma mensagem no roud seguinte
//	    System.out.println("Envio do Sink " + this.ID + " de mensagem de rota agendada. \n");
	    Tools.appendToOutput("Envio do Sink " + this.ID + " de mensagem de rota agendada. \n");
	}
	
	@Override
	

	
	
		
	

	
    public void init() {

		this.setColor(Color.RED);

		
    }


	@Override
	public void preStep() {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub

		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}

	public Node getProximoNoAteEstacaoBase() {
		return proximoNoAteEstacaoBase;
	}

	public void setProximoNoAteEstacaoBase(Node proximoNoAteEstacaoBase) {
		this.proximoNoAteEstacaoBase = proximoNoAteEstacaoBase;
	}
	
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		super.drawAsDisk(g, pt, highlight, 20);
	}
}
