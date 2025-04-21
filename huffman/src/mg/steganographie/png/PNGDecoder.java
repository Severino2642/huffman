package mg.steganographie.png;

import mg.util.Generateur;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.zip.Inflater;

public class PNGDecoder {



    public static void hideMessageInDecompressedBytes(byte[] decompressedData, int width, int height, int bytesPerPixel, String message) {
        // Convertir le message en bits
        byte[] msgBytes = message.getBytes();
        StringBuilder binaryMessage = new StringBuilder();
        for (byte b : msgBytes) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
        }
        binaryMessage.append("00000000"); // fin du message

        int msgBitIndex = 0;
        int rowSize = width * bytesPerPixel;

        for (int y = 0; y < height; y++) {
            int rowStart = y * (rowSize + 1); // +1 pour le byte de filtre

            for (int x = 0; x < rowSize; x++) {
                if (msgBitIndex >= binaryMessage.length()) return;

                int pixelByteIndex = rowStart + 1 + x;
                byte original = decompressedData[pixelByteIndex];

                // Modifier le LSB avec le bit du message
                int bit = binaryMessage.charAt(msgBitIndex++) - '0';
                decompressedData[pixelByteIndex] = (byte)((original & 0xFE) | bit);
            }
        }
    }


    public static byte[] decodePNG(String filePath) throws IOException {
        FileInputStream input = new FileInputStream(filePath);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(input));

        // Lire signature PNG
        byte[] signature = new byte[8];
        dis.readFully(signature);
        if (!isPNG(signature)) throw new IOException("Fichier PNG invalide.");

        byte[] idatData = new byte[0];
        int width = 0, height = 0;

        while (true) {
            int length = dis.readInt();
            byte[] typeBytes = new byte[4];
            dis.readFully(typeBytes);
            String chunkType = new String(typeBytes, "UTF-8");

            byte[] data = new byte[length];
            dis.readFully(data);
            dis.readInt(); // skip CRC

            if (chunkType.equals("IHDR")) {
                width = toInt(data, 0);
                height = toInt(data, 4);
                System.out.println("Image: " + width + "x" + height);
            } else if (chunkType.equals("IDAT")) {
                idatData = concat(idatData, data);
            } else if (chunkType.equals("IEND")) {
                break;
            }
        }

        // Décompresser les données IDAT
        byte[] decompressed = decompressZlib(idatData);
        dis.close();
        return decompressed; // Données pixel brutes, avec filtre par ligne (non encore reconstruites)
    }

    private static boolean isPNG(byte[] sig) {
        byte[] pngSig = {(byte)137, 80, 78, 71, 13, 10, 26, 10};
        return Arrays.equals(sig, pngSig);
    }

    private static int toInt(byte[] b, int offset) {
        return ((b[offset] & 0xFF) << 24) |
                ((b[offset + 1] & 0xFF) << 16) |
                ((b[offset + 2] & 0xFF) << 8) |
                (b[offset + 3] & 0xFF);
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static byte[] decompressZlib(byte[] compressedData) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        while (!inflater.finished()) {
            try {
                int count = inflater.inflate(buffer);
                if (count == 0 && inflater.needsInput()) break;
                baos.write(buffer, 0, count);
            } catch (Exception e) {
                throw new IOException("Erreur lors de la décompression : " + e.getMessage());
            }
        }
        inflater.end();
        return baos.toByteArray();
    }
}

