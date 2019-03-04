package uk.gov.homeoffice.borders.cscachecker.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;

@Configuration
public class CscaConfig {

    @Bean
    public KeyStore masterlist(@Value("${masterlist.location}") final Resource masterlistResource,
                               @Value("${masterlist.password}") final String password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        final InputStream is = masterlistResource.getInputStream();

        final KeyStore countrySigningCerts = KeyStore.getInstance("BKS", "BC");
        countrySigningCerts.load(is, password.toCharArray());
        return countrySigningCerts;
    }


}
