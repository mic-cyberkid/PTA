

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class UtilityMethods {
    
    //method to store image 
    public static void storeImage(String username, String imagePath){
        
        try{
            File file = new File(imagePath);
            byte[] imageBytes = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(imageBytes);
            
            
        } catch (Exception e) {
            System.out.println("Error: "+ e);
        }
    }
    
    // method to generate md5 hash to hash password
    public static String getMD5(String input) {
        try {
            // Create a MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Update the MessageDigest with the bytes of the input string
            md.update(input.getBytes());

            // Perform the hash calculation and get the resulting bytes
            byte[] digest = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                // Convert each byte to a two-character hexadecimal representation
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Return the final MD5 hash as a string
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // If MD5 is not available, throw an exception
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
    
    // Method to show alert dialog
    // Display Warnings and Messages
    public static void showAlert(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        
        //Set image
        alert.getDialogPane().setStyle("-fx-font-family: Century; -fx-font-size: 15px; -fx-background-radius: 10; -fx-border-radius: 10;");
        alert.show();
    }
    
    
    
    
}
