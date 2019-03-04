package uk.gov.homeoffice.borders.cscachecker.certificates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


@Configuration
public class Certificates {

    @Bean
    public X509Certificate trusted(@Value("classpath:/certs/trusted.pem") final Resource cert) {
        return getCertificate(cert);
    }

    @Bean
    public X509Certificate untrusted(@Value("classpath:/certs/untrusted.pem") final Resource cert) {
        return getCertificate(cert);
    }

    private X509Certificate getCertificate(final Resource resource) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            return (X509Certificate) factory.generateCertificate(resource.getInputStream());
        } catch (CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
