package dispatchlabs.utils;

import dispatchlabs.crypto.Crypto;
import dispatchlabs.states.Transaction;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.google.common.base.Preconditions.checkArgument;

public class Utils {

    /**
     * <p>
     * The regular {@link BigInteger#toByteArray()} includes the sign bit of the number and
     * might result in an extra byte addition. This method removes this extra byte.
     * </p>
     * <p>
     * Assuming only positive numbers, it's possible to discriminate if an extra byte
     * is added by checking if the first element of the array is 0 (0000_0000).
     * Due to the minimal representation provided by BigInteger, it means that the bit sign
     * is the least significant bit 0000_000<b>0</b> .
     * Otherwise the representation is not minimal.
     * For example, if the sign bit is 0000_00<b>0</b>0, then the representation is not minimal due to the rightmost zero.
     * </p>
     *
     * @param b        the integer to format into a byte array
     * @param numBytes the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        checkArgument(b.signum() >= 0, "b must be positive or zero");
        checkArgument(numBytes > 0, "numBytes must be positive");
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        boolean isFirstByteOnlyForSign = src[0] == 0;
        int length = isFirstByteOnlyForSign ? src.length - 1 : src.length;
        checkArgument(length <= numBytes, "The given number does not fit in " + numBytes);
        int srcPos = isFirstByteOnlyForSign ? 1 : 0;
        int destPos = numBytes - length;
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    /**
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }

    /**
     * @param s
     * @return
     */
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    /**
     * @param value
     * @return
     */
    public static byte[] longToBytes(long value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putLong(0, value);
        return byteBuffer.array();
    }

    /**
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String toAddressFromPublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = toByteArray(publicKey);
        byte[] hashablePublicKey = new byte[32];
        for (int i = 1; i < publicKeyBytes.length; i++) {
            hashablePublicKey[i - 1] = publicKeyBytes[i];
        }
        byte[] hash = Crypto.hash(hashablePublicKey);
        byte[] address = new byte[20];
        for (int i = 0; i < address.length; i++) {
            address[i] = hash[i + 12];
        }
        return toHexString(address);
    }

    /**
     * Create transfer tokens transaction
     *
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param time
     * @param createSignature
     * @return
     * @throws Exception
     */
    public static Transaction createTransferTokensTransaction(String privateKey, String from, String to, byte type, long value, long time, boolean createSignature) throws Exception {
        return createTransaction(privateKey, from, to, type, value, time, null, null, null, null, createSignature);
    }

    /**
     * Create deploy contract transaction
     *
     * @param privateKey
     * @param from
     * @param to
     * @param type
     * @param value
     * @param time
     * @param code
     * @param createSignature
     * @return
     * @throws Exception
     */
    public static Transaction createDeployContractTransaction(String privateKey, String from, String to, byte type, long value, long time, String code, boolean createSignature) throws Exception {
        return createTransaction(privateKey, from, to, type, value, time, code, null, null, null, createSignature);
    }

    /**
     * Create execute contract transaction
     *
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
     * @param createSignature
     * @return
     * @throws Exception
     */
    public static Transaction createExecuteContractTransaction(String privateKey, String from, String to, byte type, long value, long time, String code, String abi, String method, Object[] params, boolean createSignature) throws Exception {
        return createTransaction(privateKey, from, to, type, value, time, code, abi, method, params, createSignature);
    }

    /**
     * Create transaction
     *
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
     * @param createSignature
     * @return
     * @throws Exception
     */
    private static Transaction createTransaction(String privateKey, String from, String to, byte type, long value, long time, String code, String abi, String method, Object[] params, boolean createSignature) throws Exception {

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
        if (code != null) {
            byte[] codeBytes = DatatypeConverter.parseHexBinary(code);
            byteArrayOutputStream.write(codeBytes);
        }
        if (abi != null) {
            byteArrayOutputStream.write(abi.getBytes("UTF-8"));
        }
        if (method != null) {
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
        if (createSignature) {
            transaction.setSignature(Utils.toHexString(signatureBytes));
        }
        transaction.setHertz(0);

        return transaction;
    }
}
