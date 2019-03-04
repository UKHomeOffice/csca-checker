package uk.gov.homeoffice.borders.cscachecker.certificates;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CertificateControllerTest {

    @Autowired
    private X509Certificate trusted;
    @Autowired
    private X509Certificate untrusted;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldTrustCertificate() throws CertificateEncodingException {
        final ResponseEntity<String> response = restTemplate.postForEntity("/isCertificateTrusted", trusted.getEncoded(), String.class);

        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void shouldNotTrustCertificate() throws CertificateEncodingException {
        final ResponseEntity<String> response = restTemplate.postForEntity("/isCertificateTrusted", untrusted.getEncoded(), String.class);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }
}
