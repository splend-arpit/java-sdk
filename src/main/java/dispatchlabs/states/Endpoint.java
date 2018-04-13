package dispatchlabs.states;

import dispatchlabs.utils.AJson;

/**
 *
 */
public class Endpoint extends AJson {

    /**
     * Class level-declarations.
     */
    private String host;
    private int port;

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }
}
