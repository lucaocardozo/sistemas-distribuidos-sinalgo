//Lucas Martins Cardozo 1996770

package projects.wsn1.nodes.messages;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

/**
 *
 * @author pozza
 */
public class WsnMsg extends Message {
//Message identifier
    public Integer sequenceID;
//Lifetime of the Package
    public Integer ttl;
//Destination node
    public Node destino;
//Origin node
    public Node origem;
//Node that will forward the message - from whom the message was received
    public Node forwardingHop;
//Number of jumps to destination
    public Integer saltosAteDestino = 0;
//Packet Type. 0 for Route Establishment and 1 for data packets
    public Integer tipoMsg = 0;
    
//Class Constructor
    public WsnMsg(Integer seqID, Node origem, Node destino, Node forwardingHop, Integer tipo) {
        this.sequenceID = seqID;
        this.origem = origem;
        this.destino = destino;
        this.forwardingHop = forwardingHop;
        this.tipoMsg = tipo;
    }

    @Override
    public Message clone() { //Used to send the message
        WsnMsg msg = new WsnMsg(this.sequenceID, this.origem,
        this.destino, this.forwardingHop, this.tipoMsg);
        msg.ttl = this.ttl;
        msg.saltosAteDestino = saltosAteDestino;
        return msg;
    }
}
