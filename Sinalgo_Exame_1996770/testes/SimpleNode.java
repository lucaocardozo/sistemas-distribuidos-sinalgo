//Victor Silvano Verri - RA: 1914871

package projects.wsn1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import projects.wsn1.nodes.messages.WsnMsg;
import projects.wsn1.nodes.timers.SimpleMsgTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class SimpleNode extends Node {

    //Armazenar o nó que sera usado para alcancar a Estacao-Base
    private Node proximoNoAteEstacaoBase;
    //Armazena o numero de sequencia da ultima mensagem recebida
    private Integer sequenceNumber = 0; 
    //Boolean que sera utilizado para saber se a mensagem ja foi encaminhada antes ou nao
    private boolean messageSent = false;
        
    private Integer contador = 0;
    
    private Random randomico = new Random();
    
    private Integer intervalo;
    
    private Node sinkNode;

    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof WsnMsg) {
                Boolean encaminhar = Boolean.TRUE; //Caso ele nao passe por nenhuma dessas condicoes, a mensagem sera encaminhada
                WsnMsg wsnMessage = (WsnMsg) message;                
                if (wsnMessage.forwardingHop.equals(this)) { // A mensagem voltou. O nó deve descarta-la
                    encaminhar = Boolean.FALSE;
                } else if (wsnMessage.tipoMsg == 0) { // A mensagem e um flood. Devemos atualizar a rota
                    if (proximoNoAteEstacaoBase == null) { //Significa que é a primeira mensagem recebida pelo nó
                        proximoNoAteEstacaoBase = inbox.getSender(); //Logo, é preciso guardar as  suas referencias
                        sequenceNumber = wsnMessage.sequenceID; //Atualiza o numero de sequencia da ultima mensagem recebida
                        sinkNode = wsnMessage.origem; //Armazena o n� Esta��o Base ou Sink

                    } else if (sequenceNumber < wsnMessage.sequenceID) { //Significa que a mensagem recebida é nova ja que o sequence number é menor
                    	//Recurso simples para evitar loop.
                        //Exemplo: Nó A transmite em brodcast. Nó B recebe a
                        //msg e retransmite em broadcast.
                        //Consequentemente, nó A ira receber a msg. Sem esse
                        //condicional, nó A iria retransmitir novamente, gerando um loop
                        sequenceNumber = wsnMessage.sequenceID; //Atualiza o número de sequencia da ultima mensagem recebida

                    } else { //Se não for nem uma mensagem nova, nem a primeira, não envia a mensagem
                        encaminhar = Boolean.FALSE;
                    }
                }
                if (encaminhar) {
                    //Devemos alterar o campo forwardingHop(da mensagem) para armazenar o
                	//nó que vai encaminhar a mensagem.
                    wsnMessage.forwardingHop = this;
                    if (wsnMessage.tipoMsg == 0) {
                    	contador++;
                    	System.out.println(this.ID + " recebeu pacote do sink " + wsnMessage.origem.ID +" com " +contador + " saltos.");
//                    	Tools.appendToOutput(this.ID + "recebeu pacote do sink" + wsnMessage.origem.ID +" com " +contador + " saltos.\n");
                    	System.out.println("Node " + this.ID + " recebe pacote de roteamento do sink " + wsnMessage.origem.ID);
//                    	Tools.appendToOutput("Node " + this.ID + " recebe pacote de roteamento do sink " + wsnMessage.origem.ID+"\n");
	                    broadcast(wsnMessage);
	                    
                	} else if (wsnMessage.tipoMsg == 1) {
                		System.out.println("\n O nó " + this.ID + " recebeu a mensagem direta " + wsnMessage.sequenceID + " do nó: " + wsnMessage.origem.ID +
                				" e está enviando para o nó: " + this.proximoNoAteEstacaoBase.ID);
                		Tools.appendToOutput("\n O nó " + this.ID + " recebeu a mensagem direta " + wsnMessage.sequenceID + " do nó: " + wsnMessage.origem.ID +
                				" e está enviando para o nó: " + this.proximoNoAteEstacaoBase.ID);
                		send(wsnMessage, proximoNoAteEstacaoBase);
                	}
                    contador++;
                }
            }
        }
    }

    @Override
    public void preStep() {
    	this.setColor(Color.ORANGE);
//    	
//    	
//    	if (proximoNoAteEstacaoBase != null && messageSent == false) {
//    		WsnMsg msg = new WsnMsg(new Random().nextInt(2000)+2, this, sink, this, 1); //Soma 2 ao numero da mensagem
//    		//Assim restringindo os n�meros novos da mensagem a >2, j� que 0 e 1 j� est�o sendo utilizados
//    		
//    		SimpleMsgTimer timer = new SimpleMsgTimer(msg);
//    		timer.startRelative(1, this); //Envia a mensagem desse n� em direa��o ao Esta��o Base no pr�ximo round
//    		
//    		this.setMessageSent(true);  
//    		
//    	}
    }

    @Override
    public void init() {
    	this.setColor(Color.BLACK);
    	intervalo = 50+randomico.nextInt(100);
    	System.out.println("Node " +this.ID +" tem valor de envio de pacotes marcado para " +intervalo);
    }

    @Override
    public void neighborhoodChange() {
    	this.setColor(Color.ORANGE);
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStep() {
    	this.setColor(Color.ORANGE);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean getMessageSent() {
    	return this.messageSent;
    }
    
    public void setMessageSent (Boolean bool) { 
    	this.messageSent = bool;
    }
    
    public Node getProximoNoAteEstacaoBase() {
    	return this.proximoNoAteEstacaoBase;
    }
    
    public void setProximoNoAteEstacaoBase(Node proximoNoAteEstacaoBase) {
    	this.proximoNoAteEstacaoBase = proximoNoAteEstacaoBase;
    }
    
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		super.drawAsDisk(g, pt, highlight, 10);
	}
}
