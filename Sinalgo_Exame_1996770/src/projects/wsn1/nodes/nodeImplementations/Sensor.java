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
                Boolean encaminhar = Boolean.TRUE; //Caso ele nao passe por nenhuma dessas condicoes, a mensagem sera encaminhada
                WsnMsg wsnMessage = (WsnMsg) message;   
                                
                if (wsnMessage.forwardingHop.equals(this)) { // A mensagem voltou. O nó deve descarta-la
                    encaminhar = Boolean.FALSE;
                } else if (wsnMessage.tipoMsg == 0) { // A mensagem e um flood. Devemos atualizar a rota
                    if ((proximoNoAteEstacaoBase == null || wsnMessage.saltosAteDestino<this.saltos) && sink == false) { //Significa que é a primeira mensagem recebida pelo nó ou que saltos é menor                 	                  	
                    	System.out.println("Node " +this.ID + " recebe pacote de Sink " + wsnMessage.origem.ID + " com numero de saltos: " + wsnMessage.saltosAteDestino);
                    	proximoNoAteEstacaoBase = inbox.getSender(); //Logo, é preciso guardar as  suas referencias
                    	this.saltos = wsnMessage.saltosAteDestino;
                    	sequenceNumber = wsnMessage.sequenceID; //Atualiza o numero de sequencia da ultima mensagem recebida
                    	this.origem = wsnMessage.origem;
                    	this.setColor(wsnMessage.origem.getColor());

                    } else if ((sequenceNumber < wsnMessage.sequenceID)&& sink == false) { //Significa que a mensagem recebida é nova ja que o sequence number é menor
                        sequenceNumber = wsnMessage.sequenceID; //Atualiza o número de sequencia da ultima mensagem recebida
                        
                        
                    } else { //Se não for nem uma mensagem nova, nem a primeira, não envia a mensagem
                        encaminhar = Boolean.FALSE;  
                        
                    }
                }                 
                if (encaminhar) {
                    //Devemos alterar o campo forwardingHop(da mensagem) para armazenar o
                	//nó que vai encaminhar a mensagem.
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
  
	    if (this.proximoNoAteEstacaoBase != null && sink == false) {
	    	if(verificador <= this.contador) { 
	    		System.out.println("Node resetando: " +this.ID);
	        	this.setColor(Color.GREEN);
	    		this.proximoNoAteEstacaoBase = null;
	    		this.contador = 0;	    			    		
	    	}
	    }
	    if (countsink >=50 && sink == true) {
	    	construirRoteamento();
	    	countsink = 0;
	    }
	    
	    this.countsink++;
		this.contador++; 
		this.tempoRound++;
    }

    
	@NodePopupMethod(menuText = "Construir Arvore de Roteamento") //Caso clique nesse botao, se transformara em sink node
	public void construirRoteamento() {
		sink = true;
		System.out.println("Mensagem de roteamento agendada pelo Sink " +this.ID);
	    WsnMsg wsnMessage = new WsnMsg(1, this, null, this, 0); 
	    WsnMessageTimer timer = new WsnMessageTimer(wsnMessage);
	    timer.startRelative(1, this); //Envia uma mensagem no round seguinte	
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
