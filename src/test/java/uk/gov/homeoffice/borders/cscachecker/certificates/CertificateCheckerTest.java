package uk.gov.homeoffice.borders.cscachecker.certificates;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.cert.X509Certificate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CertificateCheckerTest {

    @Autowired
    private CertificateChecker checker;

    @Autowired
    private X509Certificate trusted;

    @Autowired
    private X509Certificate untrusted;

    @Test
    public void shouldTrustCertificate() {
        final boolean result = checker.certificateTrusted(trusted);

        assertTrue(result);
    }

    @Test
    public void shouldNotTrustCertificate() {
        final boolean result = checker.certificateTrusted(untrusted);

        assertFalse(result);
    }
}
