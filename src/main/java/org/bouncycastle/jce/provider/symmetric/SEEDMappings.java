package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;

import org.bouncycastle.asn1.kisa.KISAObjectIdentifiers;

public class SEEDMappings
    extends HashMap
{
    public SEEDMappings()
    {
        put("AlgorithmParameters.SEED", "org.bouncycastle.jce.provider.symmetric.SEED$AlgParams");
        put("Alg.Alias.AlgorithmParameters." + KISAObjectIdentifiers.id_seedCBC, "SEED");

        put("AlgorithmParameterGenerator.SEED", "org.bouncycastle.jce.provider.symmetric.SEED$AlgParamGen");
        put("Alg.Alias.AlgorithmParameterGenerator." + KISAObjectIdentifiers.id_seedCBC, "SEED");

        put("Cipher.SEED", "org.bouncycastle.jce.provider.symmetric.SEED$ECB");
        put("Cipher." + KISAObjectIdentifiers.id_seedCBC, "org.bouncycastle.jce.provider.symmetric.SEED$CBC");

        put("Cipher.SEEDWRAP", "org.bouncycastle.jce.provider.symmetric.SEED$Wrap");
        put("Alg.Alias.Cipher." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "SEEDWRAP");

        put("KeyGenerator.SEED", "org.bouncycastle.jce.provider.symmetric.SEED$KeyGen");
        put("KeyGenerator." + KISAObjectIdentifiers.id_seedCBC, "org.bouncycastle.jce.provider.symmetric.SEED$KeyGen");
        put("KeyGenerator." + KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap, "org.bouncycastle.jce.provider.symmetric.SEED$KeyGen");
    }
}
