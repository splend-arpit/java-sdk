package dispatchlabs.states;

import dispatchlabs.crypto.Crypto;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;

/**
 * Transaction
 */
public class Transaction extends AJson {

    /**
     *
     */
    public static class Type {

        /**
         * Class level-declarations.
         */
        public final static byte TRANSFER_TOKENS = 0;
        public final static byte SET_NAME = 1;
        public final static byte SMART_CONTRACT = 2;
    }

    /**
     * Class level-declarations.
     */
    private String hash;
    private long type;
    private String from;
    private String fromName;
    private String to;
    private String toName;
    private long value;
    private long time;
    private String signature;

    /**
     *
     * @return
     */
    public String getHash() {
        return hash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     *
     * @return
     */
    public long getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(long type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     */
    public String getFromName() {
        return fromName;
    }

    /**
     *
     * @param fromName
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     */
    public String getToName() {
        return toName;
    }

    /**
     *
     * @param toName
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     *
     * @return
     */
    public long getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     *
     * @return
     */
    public String getSignature() {
        return signature;
    }

    /**
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     *
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param time
     * @return
     * @throws Exception
     */
    public static Transaction create(String privateKey, String from, String to, long type, long value, long time) throws Exception {
        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKey);
        byte[] typeBytes = Utils.longToBytes(type);
        byte[] fromBytes = DatatypeConverter.parseHexBinary(from);
        byte[] toBytes = DatatypeConverter.parseHexBinary(to);
        byte[] timeBytes = Utils.longToBytes(time);
        byte[] valueBytes = Utils.longToBytes(value);

        // Hash bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
        byteArrayOutputStream.write(typeBytes);
        byteArrayOutputStream.write(fromBytes);
        byteArrayOutputStream.write(toBytes);
        byteArrayOutputStream.write(valueBytes);
        byteArrayOutputStream.write(timeBytes);
        byte[] hashBytes = Crypto.hash(byteArrayOutputStream.toByteArray());
        byte[] signatureBytes = Crypto.sign(hashBytes, privateKeyBytes);

        // Create transaction.
        Transaction transaction = new Transaction();
        transaction.setHash(Utils.toHexString(hashBytes));
        transaction.setType(type);
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setValue(value);
        transaction.setTime(time);
        transaction.setSignature(Utils.toHexString(signatureBytes));

        return transaction;
    }
}
