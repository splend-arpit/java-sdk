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
    private String hash; // Hash = (Type + From + To + Value + Code + Abi + Method + Params + Time)
    private byte type;
    private String from;
    private String to;
    private long value;
    private String code;
    private String abi;
    private String method;
    private Object []params;
    private long time;
    private String signature;
    private long hertz;

    // Transients
    private String fromName;
    private String toName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getHertz() {
        return hertz;
    }

    public void setHertz(long hertz) {
        this.hertz = hertz;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Transfer tokens
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
    		return create(privateKey, from, to, type, value, time, null, null, null, null);
    }

    /**
     * Deploy contract
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param time
     * @param code
     * @return
     * @throws Exception
     */
    public static Transaction create(String privateKey, String from, String to, byte type, long value, long time, String code) throws Exception {
		return create(privateKey, from, to, type, value, time, code, null, null, null);
    }

    /**
     * Execute contract
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param time
     * @param code
     * @param abi
     * @param method
     * @param params
     * @return
     * @throws Exception
     */
    public static Transaction create(String privateKey, String from, String to, byte type, long value, long time, String code, String abi, String method, Object []params) throws Exception {

        byte[] privateKeyBytes = DatatypeConverter.parseHexBinary(privateKey);
        byte[] typeBytes = {type};
        byte[] fromBytes = DatatypeConverter.parseHexBinary(from);
        byte[] toBytes = DatatypeConverter.parseHexBinary(to);
        byte[] valueBytes = Utils.longToBytes(value);
        byte[] timeBytes = Utils.longToBytes(time);

        // Hash bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(typeBytes);
        byteArrayOutputStream.write(fromBytes);
        byteArrayOutputStream.write(toBytes);
        byteArrayOutputStream.write(valueBytes);
        if(code != null) {
            byte[] codeBytes = DatatypeConverter.parseHexBinary(code);
            byteArrayOutputStream.write(codeBytes);
        }
        if(abi != null) {
            byteArrayOutputStream.write(abi.getBytes("UTF-8"));
        }
        if(method !=null) {
            byteArrayOutputStream.write(method.getBytes("UTF-8"));
        }
        // TODO: Add Params ro hash.
        byteArrayOutputStream.write(timeBytes);
        byte[] hashBytes = Crypto.hash(byteArrayOutputStream.toByteArray());
        byte[] signatureBytes = Crypto.sign(privateKeyBytes, hashBytes);

        Transaction transaction = new Transaction();
        transaction.setHash(Utils.toHexString(hashBytes));
        transaction.setType(type);
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setValue(value);
        transaction.setCode(code);
        transaction.setAbi(abi);
        transaction.setMethod(method);
        transaction.setParams(params);
        transaction.setTime(time);
        transaction.setSignature(Utils.toHexString(signatureBytes));
        transaction.setHertz(0);
        
        return transaction;
    }
}
