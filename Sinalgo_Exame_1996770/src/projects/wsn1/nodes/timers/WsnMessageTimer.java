//Lucas Martins Cardozo 1996770

package projects.wsn1.nodes.timers;

import projects.wsn1.nodes.messages.WsnMsg;
import projects.wsn1.nodes.nodeImplementations.Sensor;
import sinalgo.nodes.timers.Timer;

/**
 *
 * @author pozza
 */
public class WsnMessageTimer extends Timer {

    private WsnMsg message = null;

    public WsnMessageTimer(WsnMsg message) {
        this.message = message;
    }

    @Override
    public void fire() {
    	((Sensor)node).broadcast(message);
    }
}
