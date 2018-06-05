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

	private static final long serialVersionUID = -6784018212068358301L;

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
    private byte type;
    private String from;
    private String fromName;
    private String to;
    private String toName;
    private long value;
    private long time;
    private String signature;
    private String method;
    private byte[] code;
    private long hertz;

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
    public byte getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(byte type) {
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
    
    public byte[] getCode() {
		return code;
	}

	public void setCode(byte[] code) {
		this.code = code;
	}

	public long getHertz() {
		return hertz;
	}

	public void setHertz(long hertz) {
		this.hertz = hertz;
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
    public static Transaction create(String privateKey, String from, String to, byte type, long value, long time) throws Exception {
    		return create(privateKey, from, to, type, value, time, null);
    }

    public static Transaction create(String privateKey, String from, String to, byte type, long value, long time, byte[] code) throws Exception {
		return create(privateKey, from, to, type, value, time, null);
    }
    
    public static Transaction create(String privateKey, String from, String to, byte type, long value, long time, byte[] code, String method) throws Exception {
        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKey);
        byte[] typeBytes = {type};
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
        byte[] signatureBytes = Crypto.sign(privateKeyBytes, hashBytes);

        // Create transaction.
        Transaction transaction = new Transaction();
        transaction.setHash(Utils.toHexString(hashBytes));
        transaction.setType(type);
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setValue(value);
        transaction.setTime(time);
        transaction.setSignature(Utils.toHexString(signatureBytes));
        if(code != null) {
        		transaction.setCode(code);
        		transaction.setHertz(0);
        		transaction.setMethod(method);
        }

        return transaction;
    }

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
