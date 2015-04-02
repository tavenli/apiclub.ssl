package org.bouncycastle.x509;

import java.util.Collection;

import org.bouncycastle.util.Selector;

public abstract class X509StoreSpi
{
    public abstract void engineInit(X509StoreParameters parameters);

    public abstract Collection engineGetMatches(Selector selector);
}
