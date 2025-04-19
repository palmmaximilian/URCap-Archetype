package ${package}.licensing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LicenseManager {

  private static final String URCAP_DATA_PATH =
    "/data/root/.urcaps/barcodeLoader";

  private static final String USB_MOUNT_PATH = "/media/";
  private static final String TRIAL_FILE_NAME = "trialLicense.lic";
  private static final int MAX_TRIAL_CLICKS = 15;

  private static String license_name="";

  private static final String secret = "eOlY+-;D2,;C<X96ci's_gXQYfGx5c;R";
  private static final String ALGORITHM = "AES";
  private static String serialnumber;

  public LicenseManager(String serialnumer) {
    this.serialnumber = serialnumer;
  }

  public static boolean isLicenseValid() {
    return isLicenseValid(URCAP_DATA_PATH + "/" + license_name);
  }

  private static boolean isLicenseValid(String licensePath) {
    return LicenseValidator.isLicenseValid(licensePath, serialnumber);
  }


  public static boolean copyLicenseFile() {
    return copyLicenseFile(USB_MOUNT_PATH, URCAP_DATA_PATH);
  }

  private static boolean copyLicenseFile(
    String usbMountPath,
    String urcapDataPath
  ) {
    File usbMountDir = new File(usbMountPath);
    File[] usbDevices = usbMountDir.listFiles();

    if (usbDevices == null) {
      return false;
    }

    for (File usbDevice : usbDevices) {
      if (
        usbDevice.isDirectory() &&
        usbDevice.getName().startsWith("urmountpoint_")
      ) {
        // Search for all .lic files in this USB device
        File[] licenseFiles = usbDevice.listFiles(
          new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
              return name.toLowerCase().endsWith(".lic");
            }
          }
        );

        if (licenseFiles != null) {
          for (File licenseFile : licenseFiles) {
            if (isLicenseValid(licenseFile.getAbsolutePath())) {
              System.out.println("found valid license");
              try {
                Path targetPath = Paths.get(
                  urcapDataPath,
                  licenseFile.getName()
                );
                Files.createDirectories(targetPath.getParent());
                Files.copy(
                  licenseFile.toPath(),
                  targetPath,
                  StandardCopyOption.REPLACE_EXISTING
                );
                license_name=licenseFile.getName();
                return true;
              } catch (IOException e) {
                System.out.println("error while copying license");
                // Log error but continue checking other files
                continue;
              }
            }
          }
        }
      }
    }
    return false;
  }
}
