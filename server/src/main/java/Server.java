public class Server {

    private final ServerListener eventListener;
    private final AuthorizeManager authorizeManager;

    public Server(ServerListener eventListener, AuthorizeManager authorizeManager) {
        this.eventListener = eventListener;
        this.authorizeManager = authorizeManager;
    }

    public void startListening(int port) {
        authorizeManager.connect();
        putLog("Server run");
    }

    public void stopListening() {
        putLog("Server don't run");
    }

    private synchronized void putLog(String msg) {
        authorizeManager.disconnect();
        eventListener.onServerLog(msg, this);
    }

}