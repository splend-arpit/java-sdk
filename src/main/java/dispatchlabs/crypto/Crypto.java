package dispatchlabs.crypto;

import dispatchlabs.states.Account;
import dispatchlabs.states.Transaction;
import org.bitcoin.NativeSecp256k1;
import org.bouncycastle.jcajce.provider.digest.Keccak;

public class Crypto {

    /**
     *
     * @param bytes
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] bytes, byte[] privateKey) throws Exception {
        return NativeSecp256k1.sign(bytes, privateKey);
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
     * @param args
     */
    public static void main(String args[]) {

        try {
            Account fromAccount = Account.create();
            Account toAccount = Account.create();
            Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), toAccount.getAddress(), 0, 999);

        } catch (Throwable t) {
            int fook = 0;
        }

    }
}
