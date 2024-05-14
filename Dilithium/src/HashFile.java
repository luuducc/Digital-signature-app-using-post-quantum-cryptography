import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFile {
    public static byte[] hashFile(String filePath) {
        try {
            byte[] fileBytes = readFileBytes(filePath);
            byte[] hash = calculateHash(fileBytes);
            System.out.println("SHA-512 hash of the PDF file: " + bytesToHex(hash));
            return hash;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] calculateHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        return messageDigest.digest(data);
    }

    public static byte[] readFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
