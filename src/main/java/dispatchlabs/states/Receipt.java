package dispatchlabs.states;

import dispatchlabs.utils.AJson;

import java.sql.Timestamp;

/**
 *
 */
public class Receipt extends AJson {

    /**
     *
     */
    public static class Type {

        /**
         * Class level-declarations.
         */
        public final static String GET_ACTION = "GET_ACTION";
        public final static String GET_DELEGATES = "GET_DELEGATES";
        public final static String SET_NAME = "SET_NAME";
        public final static String NEW_TRANSACTION = "NEW_TRANSACTION";
        public final static String GET_TRANSACTIONS = "GET_TRANSACTIONS";
        public final static String GET_TRANSACTIONS_BY_ADDRESS = "GET_TRANSACTIONS_BY_ADDRESS";
    }

    /**
     *
     */
    public static class Status {

        /**
         * Class level-declarations.
         */
        public final static String PENDING = "Pending";
        public final static String OK = "OK";
        public final static String NOT_FOUND = "NOT_FOUND";
        public final static String INVALID_TRANSACTION = "INVALID_TRANSACTION";
        public final static String INSUFFICIENT_TOKENS = "INSUFFICIENT_TOKENS";
        public final static String DUPLICATE_TRANSACTION = "DUPLICATE_TRANSACTION";
        public final static String UNABLE_TO_CONNECT_TO_DELEGATE = "UNABLE_TO_CONNECT_TO_DELEGATE";
        public final static String INVALID_ACTION = "INVALID_ACTION";
        public final static String INVALID_ADDRESS = "INVALID_ADDRESS";
        public final static String INTERNAL_ERROR = "INTERNAL_ERROR";

    }

    /**
     * Class level-declarations.
     */
    private String id;
    private String type;
    private String status;
    private String humanReadableStatus;
    private String nodeIp;
    private Timestamp updated;
    private Timestamp created;

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public String getHumanReadableStatus() {
        return humanReadableStatus;
    }

    /**
     *
     * @param humanReadableStatus
     */
    public void setHumanReadableStatus(String humanReadableStatus) {
        this.humanReadableStatus = humanReadableStatus;
    }

    /**
     *
     * @return
     */
    public String getNodeIp() {
        return nodeIp;
    }

    /**
     *
     * @param nodeIp
     */
    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdated() {
        return updated;
    }

    /**
     *
     * @param updated
     */
    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     *
     * @param created
     */
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    /**
     *
     * @return
     */
    public boolean isOk() {
        if (status == null) {
            return false;
        }
        return status.equals(Status.OK);
    }
}
