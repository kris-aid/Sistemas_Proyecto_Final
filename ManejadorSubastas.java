
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public enum ManejadorSubastas {

    INSTANCE;

    private HashMap<Integer, Subasta> subastas;
    private HashMap<String, User> suscribers;
    private HashMap<Integer, SubastaSession> subastaSessions;

    private ManejadorSubastas() {
        this.subastas = new HashMap<Integer, Subasta>();
        this.suscribers = new HashMap<String, User>();
    }

    public ManejadorSubastas getInstance() {
        return INSTANCE;
    }
    
    public HashMap<String, User> getSubscribers(){
        return suscribers;
    }
    
    public HashMap<Integer, Subasta> getSubastas(){
        return subastas;
    }
            
    public synchronized void addSubasta(Subasta subasta) {
        subastas.put(subasta.getId(), subasta);
    }

    public synchronized void removeSubasta(Subasta subasta) {
        subastas.remove(subasta.getId());
    }

    public synchronized void addSuscriber(User client) {
        suscribers.put(client.getName(), client);
    }

    public synchronized void removeSuscriber(User client) {
        suscribers.remove(client.getName());
    }

    public synchronized void update() {
        System.out.println("update");
    }

    public synchronized void notifySuscribers(String message) {
        for (User users : suscribers.values()) {
            users.sendMessage(message);
        }
    }

    public synchronized void addSubastaSession(SubastaSession subastaSession) {
        subastaSessions.put(subastaSession.getId(), subastaSession);
    }

    public synchronized void removeSubastaSession(SubastaSession subastaSession) {
        subastaSessions.remove(subastaSession);
    }

    public synchronized void addSubastaToSession(Subasta subasta, SubastaSession subastaSession) {
        subastaSession.addSubasta(subasta);
    }

    public synchronized void initializeSubastaSession(int id_Subasta_Session, int id_Subasta) {
        subastaSessions.get(id_Subasta_Session).initilizeSubasta(id_Subasta);
    }

    public synchronized int subscribeToSubastaSession(String id_Client, Integer id_Subasta_Session) {
        if (subastaSessions.get(id_Subasta_Session) == null) {
            return 0;
        }

        //if a client isnt subscribed to a subasta, he cant subscribe to a subasta session
        for (Subasta subasta : subastaSessions.get(id_Subasta_Session).getSubastas()) {
            if (!(subasta.getClients().contains(suscribers.get(id_Client)))) {
                return 1;
            }
        }

        if (subastaSessions.get(id_Subasta_Session).getIsFinished() == true) {
            return 2;
        } else {
            subastaSessions.get(id_Subasta_Session).addClient(suscribers.get(id_Client));
        }
        suscribers.get(id_Client).subscribeToSubastaSession(id_Subasta_Session);
        return 3;

    }

    // to do, control how to unsuscribe with the the number of subastas interesadas 
    public synchronized int unsubscribeToSubastaSession(Integer id_Client, Integer id_Subasta_Session) {
        subastaSessions.get(id_Subasta_Session).removeClient(suscribers.get(id_Client));
        return 1;
    }

    public synchronized int findSubastaSessionOfaSubasta(int id_Subasta) {
        int id_SubastaSession = 0;

        return id_SubastaSession;
    }

    public synchronized int subcribeToSubasta(String id_Client, Integer id_Subasta) {
        if (subastas.get(id_Subasta) == null) {
            return 0;
        }
        if (subastas.get(id_Subasta).getIsStarted() == true) {
            return 1;
        }
        if (subastas.get(id_Subasta).getIsOver() == true) {
            return 2;
        } else {
            subastas.get(id_Subasta).addClient(suscribers.get(id_Client));
        }
        suscribers.get(id_Client).subscribeToSubasta(id_Subasta);
        return 3;
    }

    public synchronized int unsubscribeToSubasta(Integer id_Client, Integer id_Subasta) {
        if (subastas.get(id_Subasta) == null) {
            return 0;
        }
        if (subastas.get(id_Subasta).getIsStarted() == true) {
            return 1;
        }
        if (subastas.get(id_Subasta).getIsOver() == true) {
            return 2;
        } else {
            subastas.get(id_Subasta).removeClient(suscribers.get(id_Client));
        }
        suscribers.get(id_Client).unsubscribeToSubasta(id_Subasta);
        return 3;
    }

}