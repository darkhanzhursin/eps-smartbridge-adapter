package kz.ezdrav.eps_smartbridge_adapter.util;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;
import kz.gov.pki.kalkan.jce.provider.KalkanProvider;

public class FileSystemFunctions {
    public static PrivateKey loadKeyFromBase64(String fileBase64, String fileType, String pass) {
        try {
            KeyStore store = KeyStore.getInstance(fileType, KalkanProvider.PROVIDER_NAME);
            store.load(new ByteArrayInputStream(Base64.getDecoder().decode(fileBase64)), pass.toCharArray());

            Enumeration<String> en = store.aliases();
            String alias = null;
            while (en.hasMoreElements()) alias = en.nextElement();

            return (PrivateKey) store.getKey(alias, pass.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static X509Certificate loadCertFromBase64(String fileBase64, String fileType, String pass) {
        try {
            KeyStore store = KeyStore.getInstance(fileType, KalkanProvider.PROVIDER_NAME);
            store.load(new ByteArrayInputStream(Base64.getDecoder().decode(fileBase64)), pass.toCharArray());

            Enumeration<String> en = store.aliases();
            String alias = null;
            while (en.hasMoreElements()) alias = en.nextElement();

            return (X509Certificate) store.getCertificateChain(alias)[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
