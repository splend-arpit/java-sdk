package dispatchlabs.crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

import dispatchlabs.utils.Utils;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;

/**
 *
 */
public class Key {

    /**
     * Class level-declarations.
     */
    private static final ECDomainParameters ecParams;
    private static final SecureRandom secureRandom;
    private final BigInteger privateKey;
    private final byte[] publicKey;

    static {
        // All clients must agree on the curve to use by agreement. Bitcoin and Bitmessage use curve secp256k1.
        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
        ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        secureRandom = new SecureRandom();
    }

    /**
     * Generates an entirely new keypair.
     */
    public Key() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(ecParams, secureRandom);
        generator.init(keygenParams);
        AsymmetricCipherKeyPair keypair = generator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
        privateKey = privParams.getD();
        publicKey = pubParams.getQ().getEncoded(false);// The public key is an encoded point on the elliptic curve. It has no meaning independent of the curve.
    }

    /**
     * Creates an ECKey given only the private key. This works because EC public keys are derivable from their
     * private keys by doing a multiply with the generator value.
     */
    public Key(BigInteger privKey) {
        this.privateKey = privKey;
        this.publicKey = publicKeyFromPrivate(privKey);
    }

    /**
     * @return
     */
    public byte[] getPublicKeyBytes() {
        return publicKey;
    }

    /**
     * @return
     */
    public String getPublicKey() {
        return Utils.toHexString(getPublicKeyBytes());
    }

    /**
     * @return
     */
    public byte[] getPrivateKeyBytes() {
        return Utils.bigIntegerToBytes(privateKey, 32);
    }

    /**
     * @return
     */
    public String getPrivateKey() {
        return Utils.toHexString(getPrivateKeyBytes());
    }

    /**
     * Derive the public key by doing a point multiply of G * privateKey.
     * @param privKey
     * @return
     */
    public static byte[] publicKeyFromPrivate(BigInteger privKey) {
        return ecParams.getG().multiply(privKey).getEncoded(false);
    }
}