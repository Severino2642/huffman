package mg.steganographie.png;

import mg.util.Generateur;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PNGDeEncoderWithLib {
    public static BufferedImage readImage(String filepath) throws IOException {
        BufferedImage image = null;
        image = ImageIO.read(new File(filepath));
        System.out.println(image.getWidth() + " " + image.getHeight());
        return image;
    }

    public static void hideMessage(BufferedImage image, String message,int [][] position) {
        // Convertir le message en binaire
        byte[] bytes = message.getBytes();

        StringBuilder binaryMessage = new StringBuilder();
//        for (byte b : bytes) {
//            binaryMessage.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
//        }
        for (char c:message.toCharArray()) {
            binaryMessage.append(c);
        }

        int msgIndex = 0;
        // Sans Random position
        if (position == null) {
            outerLoop:
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (msgIndex >= binaryMessage.length()) break outerLoop;

                    System.out.println("("+x+","+y+") : "+binaryMessage.charAt(msgIndex));
                    int rgb = image.getRGB(x, y);
                    int red = (rgb >> 16) & 0xFF;
                    // Modifier le LSB du rouge
                    red = (red & 0xFE) | (binaryMessage.charAt(msgIndex) - '0');
                    msgIndex++;

                    // Réassembler les composantes
                    int newRGB = (red << 16) | (rgb & 0xFF00FFFF);
                    image.setRGB(x, y, newRGB);
                }
            }
        }

        // Avec Random position
        else {
            for (int [] row : position) {
                System.out.println("("+row[0]+","+row[1]+") : "+binaryMessage.charAt(msgIndex));
                int rgb = image.getRGB(row[0], row[1]);
                int red = (rgb >> 16) & 0xFF;
                // Modifier le LSB du rouge
                red = (red & 0xFE) | (binaryMessage.charAt(msgIndex) - '0');
                msgIndex++;

                // Réassembler les composantes
                int newRGB = (red << 16) | (rgb & 0xFF00FFFF);
                image.setRGB(row[0], row[1], newRGB);
            }
        }

        try {
            ImageIO.write(image, "png", new File("steganographie.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String extractMessage(BufferedImage image, int length,int [][] position) {
        StringBuilder binaryMessage = new StringBuilder();
//        int bitsToRead = length * 8;
        int bitsToRead = length;
        if (position == null) {
            outerLoop:
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (binaryMessage.length() >= bitsToRead) break outerLoop;

                    int rgb = image.getRGB(x, y);
                    int red = (rgb >> 16) & 0xFF;
                    binaryMessage.append(red & 1);
                }
            }
        }
        else {
            for (int [] row : position) {
                int rgb = image.getRGB(row[0], row[1]);
                int red = (rgb >> 16) & 0xFF;
                binaryMessage.append(red & 1);
            }
        }
        System.out.println("m : "+binaryMessage.toString());

        // Convertir le message binaire en texte
//        StringBuilder message = new StringBuilder();
//        for (int i = 0; i < binaryMessage.length(); i += 8) {
//            System.out.println("extract :"+(binaryMessage.substring(i, i + 8)));
//
//            int charCode = Integer.parseInt(binaryMessage.substring(i, i + 8), 2);
//            message.append((char) charCode);
//        }

        return binaryMessage.toString();
    }


    public static void main(String[] args) throws IOException {
        String message = "0110001";
        Generateur generateur = new Generateur();
        BufferedImage image = readImage("C:\\\\Users\\\\diva\\\\Pictures\\\\avant1.png");
        int [][] position = generateur.generateRandomPosition(image.getWidth(), image.getHeight(),message.length());
        hideMessage(image,message,position);
        System.out.println(extractMessage(readImage("steganographie.png"),message.length(),position));
    }
}
