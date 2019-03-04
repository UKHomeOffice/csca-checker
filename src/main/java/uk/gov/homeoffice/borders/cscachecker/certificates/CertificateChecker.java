package uk.gov.homeoffice.borders.cscachecker.certificates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.EnumSet;

@Slf4j
@Component
public class CertificateChecker {
    private final KeyStore countrySigningCerts;

    public CertificateChecker(final KeyStore countrySigningCerts) {
        this.countrySigningCerts = countrySigningCerts;
    }


    public boolean certificateTrusted(final X509Certificate signingCert) {
        try {
            final X509CertSelector certSelector = new X509CertSelector();
            certSelector.setCertificate(signingCert);
            final PKIXBuilderParameters params = new PKIXBuilderParameters(countrySigningCerts, certSelector);
            final CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
            final PKIXRevocationChecker crlChecker = (PKIXRevocationChecker) builder.getRevocationChecker();
            crlChecker.setOptions(EnumSet.of(
                    PKIXRevocationChecker.Option.PREFER_CRLS,
                    PKIXRevocationChecker.Option.ONLY_END_ENTITY,
                    PKIXRevocationChecker.Option.NO_FALLBACK
            ));
            params.addCertPathChecker(crlChecker);
            builder.build(params);
            return true;
        } catch (CertPathBuilderException e) {
            log.warn("Certificate with subject name {} issued by {} is not trusted", signingCert.getSubjectDN(), signingCert.getIssuerDN());
            return false;
        } catch (InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException e) {
            log.warn("Unable to check trust for certificate with subject name {}: {}", signingCert.getSubjectDN(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
