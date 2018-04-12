package dispatchlabs.crypto;

import dispatchlabs.utils.Utils;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.FixedPointUtil;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 */
public class Key {

    /**
     *
     */
    private BigInteger privateKey;
    protected LazyECPoint publicKey;
    private static SecureRandom secureRandom;
    private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static ECDomainParameters CURVE;

    static {
        // Tell Bouncy Castle to precompute data that's needed during secp256k1 calculations. Increasing the width
        // number makes calculations faster, but at a cost of extra memory usage and with decreasing returns. 12 was
        // picked after consulting with the BC team.
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
        secureRandom = new SecureRandom();
    }

    /**
     * @throws Exception
     */
    public Key() throws Exception {
        secureRandom = new SecureRandom();
        ECKeyPairGenerator eckeyPairGenerator = new ECKeyPairGenerator();
        ECKeyGenerationParameters eckeyGenerationParameters = new ECKeyGenerationParameters(CURVE, secureRandom);
        eckeyPairGenerator.init(eckeyGenerationParameters);
        AsymmetricCipherKeyPair keypair = eckeyPairGenerator.generateKeyPair();
        ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) keypair.getPrivate();
        ECPublicKeyParameters pubParams = (ECPublicKeyParameters) keypair.getPublic();
        privateKey = privParams.getD();
        publicKey = new LazyECPoint(CURVE.getCurve(), pubParams.getQ().getEncoded(true));
    }

    /**
     * @return
     */
    public byte[] getPublicKeyBytes() {
        return publicKey.getEncoded();
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
     *
     * @return
     */
    public String getPrivateKey() {
        return Utils.toHexString(getPrivateKeyBytes());
    }
}
