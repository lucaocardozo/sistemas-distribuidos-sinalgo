//Lucas Martins Cardozo 1996770

package projects.wsn1.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import projects.wsn1.nodes.messages.WsnMsg;
import projects.wsn1.nodes.timers.WsnMessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.Node.NodePopupMethod;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;


public class Sensor extends Node {

    //declaring the variables
    private Node proximoNoAteEstacaoBase;   
    private Node origem;
    private Integer sequenceNumber = 0;   
    private Random random = new Random();   
    private int sequencia = 0;
    private int tempoEnvio = 0;
    private int tempoRound = 0;   
    private int contador = 0; 
    private int verificador = 0;
    private int countsink = 0;
    private int saltos = 0;
    private boolean sink = false;


    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof WsnMsg) {
                Boolean encaminhar = Boolean.TRUE; //If it does not pass any of these conditions, the message will be forwarded.
                WsnMsg wsnMessage = (WsnMsg) message;   
                                
                if (wsnMessage.forwardingHop.equals(this)) { // The message came back. The node should discard it
                    encaminhar = Boolean.FALSE;
                } else if (wsnMessage.tipoMsg == 0) { // The message is a flood. We must update the route
                    if ((proximoNoAteEstacaoBase == null || wsnMessage.saltosAteDestino<this.saltos) && sink == false) { //It means that it is the first message received by the node or that it jumps is smaller and that it is not a sink                 	                  	
                    	System.out.println("Node " +this.ID + " recebe pacote de Sink " + wsnMessage.origem.ID + " com numero de saltos: " + wsnMessage.saltosAteDestino);
                    	proximoNoAteEstacaoBase = inbox.getSender(); //It is necessary to keep your references
                    	this.saltos = wsnMessage.saltosAteDestino;
                    	sequenceNumber = wsnMessage.sequenceID; //Updates the sequence number of the last message received
                    	this.origem = wsnMessage.origem;
                    	this.setColor(wsnMessage.origem.getColor());

                    } else if ((sequenceNumber < wsnMessage.sequenceID)&& sink == false) { //It means that the message received is new since the sequence number is smaller and it is not a sink.
                        sequenceNumber = wsnMessage.sequenceID; //Updates the sequence number of the last message received
                        
                        
                    } else { //If it is neither a new message nor the first one, it does not send the message
                        encaminhar = Boolean.FALSE;  
                        
                    }
                }                 
                if (encaminhar) {
                    //We must change the forwardingHop(field of the message) to store the node that will forward the message.
                    wsnMessage.forwardingHop = this;
                    wsnMessage.saltosAteDestino++;
                    this.broadcast(wsnMessage);
                }
            }
        }
    }

    @Override
    public void preStep() {	
	    
    	if (this.proximoNoAteEstacaoBase != null && sink == false) {
    		if(tempoEnvio < tempoRound) {		
	    		WsnMsg msgMessage = new WsnMsg(sequencia, this, this.origem, this, 1); 
	    		this.sequencia++;
	    		this.tempoRound = 0;	    			    		
    		}
    	}

	    //Reset nodes to update references
	    if (this.proximoNoAteEstacaoBase != null && sink == false) {
	    	if(verificador <= this.contador) { 
	    		System.out.println("Node resetando: " +this.ID);
	        	this.setColor(Color.GREEN);
	    		this.proximoNoAteEstacaoBase = null;
	    		this.contador = 0;	    			    		
	    	}
	    }

	    //If it is a sink, every 50 rounds it will build routing
	    if (countsink >=50 && sink == true) {
	    	construirRoteamento();
	    	countsink = 0;
	    }

	    //increments the counters
	    this.countsink++;
		this.contador++; 
		this.tempoRound++;
    }

    
	@NodePopupMethod(menuText = "Construir Arvore de Roteamento") //If you click on this button, it will turn into a sink node.
	public void construirRoteamento() {
		sink = true;
		System.out.println("Mensagem de roteamento agendada pelo Sink " +this.ID);
	    WsnMsg wsnMessage = new WsnMsg(1, this, null, this, 0); 
	    WsnMessageTimer timer = new WsnMessageTimer(wsnMessage);
	    timer.startRelative(1, this); //Send a message in the next round	
    	this.setColor(Color.BLUE);

	}
    
    @Override
    public void init() {
    	tempoEnvio = 50 + random.nextInt(150);
    	verificador = 200;
    	countsink = 0;
    	this.setColor(Color.GREEN);
    }

    @Override
    public void neighborhoodChange() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStep() { 	

    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
       
    public void draw(Graphics g, PositionTransformation pt, boolean highlight, int sizeInPixels) {
		super.drawAsDisk(g, pt, highlight ,10);
	}
}
