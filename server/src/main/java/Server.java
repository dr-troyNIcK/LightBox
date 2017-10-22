public class Server {

    private final ServerListener eventListener;

    public Server(ServerListener eventListener) {
        this.eventListener = eventListener;
    }

    public void startListening(int port) {
        putLog("Server already run");
    }

    public void stopListening() {
            putLog("Server don't run");
    }

    private synchronized void putLog(String msg) {
        eventListener.onServerLog(msg, this);
    }

}