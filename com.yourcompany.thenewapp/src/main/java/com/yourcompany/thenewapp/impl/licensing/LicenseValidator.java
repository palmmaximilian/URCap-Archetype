package com.yourcompany.thenewapp.impl.licensing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class LicenseValidator {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd_HH-mm-ss"
  );

  private static PublicKey PUBLIC_KEY;

  static {
    try {
      PUBLIC_KEY = loadPublicKey();
    } catch (Exception e) {
      throw new RuntimeException("Failed to load public key", e);
    }
  }

  private static PublicKey loadPublicKey() throws Exception {
    InputStream inputStream = null;
    PEMParser pemParser = null;
    try {
      // Load PEM file from resources
      inputStream =
        LicenseValidator.class.getResourceAsStream("/key/public_key.pem");
      if (inputStream == null) {
        throw new IOException("Public key file not found.");
      }

      pemParser = new PEMParser(new InputStreamReader(inputStream));
      Object pemObject = pemParser.readObject();

      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

      if (pemObject instanceof X509CertificateHolder) {
        // Extract public key from X.509 certificate (BC 1.68 compatible)
        X509CertificateHolder certHolder = (X509CertificateHolder) pemObject;
        return converter.getPublicKey(certHolder.getSubjectPublicKeyInfo());
      } else if (pemObject instanceof RSAPublicKey) {
        // Handle PKCS#1 RSA Public Key
        RSAPublicKey rsaPub = (RSAPublicKey) pemObject;
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(
          rsaPub.getModulus(),
          rsaPub.getPublicExponent()
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
      } else if (
        pemObject instanceof org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
      ) {
        // Handle PKCS#8 Public Key
        return converter.getPublicKey(
          (org.bouncycastle.asn1.x509.SubjectPublicKeyInfo) pemObject
        );
      } else {
        throw new IOException(
          "Unsupported PEM object: " + pemObject.getClass().getName()
        );
      }
    } finally {
      // Close resources (Java 6 style)
      if (pemParser != null) {
        try {
          pemParser.close();
        } catch (IOException ignored) {}
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException ignored) {}
      }
    }
  }

  public static boolean isLicenseValid(
    String licensePath,
    String serialNumber
  ) {
    try {
      // 1. Read and parse license file
      String licenseContent = new String(
        Files.readAllBytes(Paths.get(licensePath))
      );
      System.out.println("License: " + licenseContent);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode license = mapper.readTree(licenseContent);

      System.out.println("Checking Signature");
      // 2. Verify signature
      if (!verifySignature(license)) {
        System.out.println("Signature not valid");
        return false;
      }
      System.out.println("Checking date");
      // 3. Check expiration date
      if (isLicenseExpired(license)) {
        System.out.println("Expired");
        return false;
      }

      System.out.println("Checking Serial Number");
      // 4. Verify serial number matches current robot
      if (!verifySerialNumber(license, serialNumber)) {
        System.out.println("Wrong Serial");
        return false;
      }
      System.out.println("Signature is valid");
      return true;
    } catch (Exception e) {
      System.out.println("error while verifying");
      System.out.println(e);
      return false;
    }
  }

  private static boolean verifySignature(JsonNode license) throws Exception {
    try {
      // 1. Prepare data
      ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        .configure(SerializationFeature.INDENT_OUTPUT, false);

      String dataToVerify = mapper.writeValueAsString(license.path("data"));

      System.out.println(dataToVerify);

      // 2. Prepare signature
      String signature = license.path("signature").asText();
      System.out.println(signature);
      // byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);
      byte[] signatureBytes = Base64.getDecoder().decode(signature);

      // 3. Verify
      Signature sig = Signature.getInstance("SHA256withRSA");
      sig.initVerify(PUBLIC_KEY);
      sig.update(dataToVerify.getBytes(StandardCharsets.UTF_8)); // Explicit charset

      return sig.verify(signatureBytes);
    } catch (Exception e) {
      System.err.println("Verification error: " + e.getMessage());
      return false;
    }
  }

  private static boolean isLicenseExpired(JsonNode license) {
    String expirationDateStr = license
      .path("data")
      .path("expiration_date")
      .asText();
    LocalDateTime expirationDate = LocalDateTime.parse(
      expirationDateStr,
      DATE_FORMAT
    );
    return LocalDateTime.now().isAfter(expirationDate);
  }

  private static boolean verifySerialNumber(
    JsonNode license,
    String serialNumber
  ) {
    String licenseSerial = license
      .path("data")
      .path("robot_serial_number")
      .asText();
    return licenseSerial.equals(serialNumber);
  }
}
