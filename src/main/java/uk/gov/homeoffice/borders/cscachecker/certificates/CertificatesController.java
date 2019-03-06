package uk.gov.homeoffice.borders.cscachecker.certificates;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Controller
public class CertificatesController {

    private final CertificateChecker checker;

    public CertificatesController(final CertificateChecker checker) {
        this.checker = checker;
    }

    @PostMapping(path = "/isCertificateTrusted", consumes = "application/octet-stream")
    public ResponseEntity<Void> isCertificateTrusted(final InputStream derCertificate) {
        if (checker.certificateTrusted(parse(derCertificate))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private X509Certificate parse(final InputStream resource) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            return (X509Certificate) factory.generateCertificate(resource);
        } catch (CertificateException e) {
            throw new InvalidCertificateException(e);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Certificate must be an X509 Certificate in DER format")
    private static class InvalidCertificateException extends RuntimeException {

        InvalidCertificateException(CertificateException e) {
            super(e);
        }
    }
}
