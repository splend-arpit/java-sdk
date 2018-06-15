package dispatchlabs.crypto;

import dispatchlabs.states.Transaction;
import dispatchlabs.utils.Utils;
import org.bitcoin.NativeSecp256k1;
import org.bouncycastle.jcajce.provider.digest.Keccak;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;

/**
 *
 */
public class Crypto {

    /**
     * @param privateKey
     * @param hash
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] privateKey, byte[] hash) throws Exception {
        return NativeSecp256k1.sign(privateKey, hash);
    }

    /**
     * @param bytes
     * @return
     */
    public static byte[] hash(byte[] bytes) {
        final Keccak.Digest256 keccak = new Keccak.Digest256();
        keccak.update(bytes);
        return keccak.digest();
    }

    /**
     * @param transaction
     * @return
     * @throws Exception
     */
    private static String createHash(Transaction transaction) throws Exception {

        byte[] typeBytes = {transaction.getType()};
        byte[] fromBytes = DatatypeConverter.parseHexBinary(transaction.getFrom());
        byte[] toBytes = DatatypeConverter.parseHexBinary(transaction.getTo());
        byte[] valueBytes = Utils.longToBytes(transaction.getValue());
        byte[] timeBytes = Utils.longToBytes(transaction.getTime());

        // Hash bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(typeBytes);
        byteArrayOutputStream.write(fromBytes);
        byteArrayOutputStream.write(toBytes);
        byteArrayOutputStream.write(valueBytes);
        if (transaction.getCode() != null) {
            byte[] codeBytes = DatatypeConverter.parseHexBinary(transaction.getCode());
            byteArrayOutputStream.write(codeBytes);
        }
        if (transaction.getAbi() != null) {
            byteArrayOutputStream.write(transaction.getAbi().getBytes("UTF-8"));
        }
        if (transaction.getMethod() != null) {
            byteArrayOutputStream.write(transaction.getMethod().getBytes("UTF-8"));
        }
        // TODO: Add Params ro hash.
        byteArrayOutputStream.write(timeBytes);

        byte[] hashBytes = Crypto.hash(byteArrayOutputStream.toByteArray());

        return Utils.toHexString(hashBytes);
    }
}
