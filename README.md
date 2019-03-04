# CSCA Checker 

Service to check MRTD document signing certificates for trust against a master
list of Country-signing CAs.

## Building

Requires JDK 8+

```
./gradlew clean test
```

## Running

See the `bootrun` block in `build.gradle` for how to specify the location of
the masterlist keystore.

```
./gradlew bootRun
```

## Example Masterlist

The German government produces a masterlist which can be downloaded (along 
with the certificates used to sign the list) from [their website.](https://www.bsi.bund.de/EN/Topics/ElectrIDDocuments/securPKI/securCSCA/Root_Certificate/cscaGermany_node.html)

## Parsing the CSCA masterlist

The CSCA masterlists are received as a CMS message, signed by a particular
certificate. The list first needs to be extracted from the CMS wrapper with
openSSL, and then split up into its constituent certificates before importing
into a Java keystore. We also need to check the message signature, and that
we trust the certificate used to sign the message.

Splitting up the extracted message is relatively simple: it's a binary file,
which lists the certificates one after the other. `openssl asn1parse` can be
used to get the offsets and lengths for each, see the [example
script](etc/csca_to_ks.sh). This script also converts from DER to PEM format.

Some of the certificates use EC cryptography without specifying the curve name.
This is not supported by the Oracle JDK provider, so BouncyCastle must be used
instead. To use this with `keytool` add the jar to the `lib/ext` directory of
the JDK, and add the BouncyCastle provider as the first provider in `java.security`

A second verification step is needed to verify the certificate that is used to
sign the CMS message. This certificate should be checked against a
predetermined CA certificate from a trusted origin, using `openssl verify`.
Alternatively, we can check that we trust the signing certificate directly.
