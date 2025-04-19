package ${package}.licensing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TrialManager {

  private static final String URCAP_DATA_PATH =
    "/data/root/.urcaps/barcodeLoader";

  private static final String USB_MOUNT_PATH = "/media/";
  private static final String TRIAL_FILE_NAME = "trialLicense.lic";
  private static final int MAX_TRIAL_CLICKS = 15;

  private static final String secret = "eOlY+-;D2,;C<X96ci's_gXQYfGx5c;R";
  private static final String ALGORITHM = "AES";

  private static File getTrialDataFile() {
    File dir = new File(URCAP_DATA_PATH);
    if (!dir.exists()) {
      boolean success = dir.mkdirs(); // Create the directory if it doesn't exist
      if (!success) {
        System.err.println("Failed to create directory: " + URCAP_DATA_PATH);
      }
    }
    // create the file if it doesn't exist
    return new File(dir, TRIAL_FILE_NAME);
  }

  public static int loadTrialClicks() {
    File file = getTrialDataFile();
    if (!file.exists()) {
      saveTrialClicks(MAX_TRIAL_CLICKS);
      return MAX_TRIAL_CLICKS;
    }

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String encodedTrialString = reader.readLine();
      //   String decodedTrialString = decodeInt(encodedTrialString,secret);
      return decodeInt(encodedTrialString, secret);
      //   return Integer.parseInt(reader.readLine());
    } catch (Exception e) {
      e.printStackTrace();
      return -1; // Default if an error occurs
    }
  }

  private static void saveTrialClicks(int clicks) {
    File file = getTrialDataFile();
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file, false));
      writer.write(encodeInt(clicks, secret));
      writer.flush();
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String encodeInt(int value, String secret) {
    try {
      // Derive a 128-bit AES key from the secret string
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      byte[] keyBytesPadded = new byte[16]; // AES requires a 16-byte key
      System.arraycopy(
        keyBytes,
        0,
        keyBytesPadded,
        0,
        Math.min(keyBytes.length, 16)
      );
      SecretKeySpec secretKey = new SecretKeySpec(keyBytesPadded, ALGORITHM);

      // Initialize the cipher in encryption mode
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      // Convert the integer to a string and encrypt it
      String valueString = String.valueOf(value);
      byte[] encryptedBytes = cipher.doFinal(
        valueString.getBytes(StandardCharsets.UTF_8)
      );

      // Encode the encrypted bytes as Base64
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      throw new RuntimeException("Error encoding value", e);
    }
  }

  private static int decodeInt(String encodedValue, String secret) {
    try {
      // Derive a 128-bit AES key from the secret string
      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      byte[] keyBytesPadded = new byte[16]; // AES requires a 16-byte key
      System.arraycopy(
        keyBytes,
        0,
        keyBytesPadded,
        0,
        Math.min(keyBytes.length, 16)
      );
      SecretKeySpec secretKey = new SecretKeySpec(keyBytesPadded, ALGORITHM);

      // Initialize the cipher in decryption mode
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);

      // Decode the Base64 string and decrypt it
      byte[] encryptedBytes = Base64.getDecoder().decode(encodedValue);
      byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

      // Convert the decrypted string back to an integer
      return Integer.parseInt(
        new String(decryptedBytes, StandardCharsets.UTF_8)
      );
    } catch (Exception e) {
      throw new RuntimeException("Error decoding value", e);
    }
  }

  public static void decrementTrialClicks() {
    int remainingClicks = loadTrialClicks();
    if (remainingClicks > 0) {
      saveTrialClicks(remainingClicks - 1);
    }
  }
}
