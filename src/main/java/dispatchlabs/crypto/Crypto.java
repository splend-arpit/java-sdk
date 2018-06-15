package dispatchlabs.crypto;

import dispatchlabs.states.Transaction;
import org.bitcoin.NativeSecp256k1;
import org.bouncycastle.jcajce.provider.digest.Keccak;

/**
 *
 */
public class Crypto {

    /**
     *
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
}
