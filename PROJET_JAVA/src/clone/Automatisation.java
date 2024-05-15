package clone;

import java.util.Timer;

// Cette classe permet de:
// Rendre la synchronisation automatique.


public class Automatisation {

	public static void main(String[] args) {
		Timer timer = new Timer();
        timer.schedule(new Synchronisation(), 0, 60 * 1000);
        // (fait en sorte que les classes Synchronisation Source et Reference soient lancees toutes les minutes)
    }

}
