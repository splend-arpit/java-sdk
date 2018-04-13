package dispatchlabs.crypto;

import org.bitcoin.NativeSecp256k1;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import java.util.Arrays;

/**
 *
 */
public class Crypto {

    /**
     *
     * @param privateKey
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] privateKey, byte[] bytes) throws Exception {
        /*
        byte[] signature = NativeSecp256k1.Sign(bytes, privateKey);
        signature = Arrays.copyOfRange(signature, 5, signature.length);
        return signature;
        */
        return null;
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
}
