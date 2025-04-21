package mg.steganographie.png;

import java.io.*;
import java.util.zip.Deflater;

public class PNGEncoder {

    public static String extractMessageFromDecompressedData(byte[] decompressed, int width, int height, int bytesPerPixel) {
        StringBuilder bits = new StringBuilder();
        int rowSize = width * bytesPerPixel;

        for (int y = 0; y < height; y++) {
            int rowStart = y * (rowSize + 1); // +1 pour l’octet de filtre

            for (int x = 0; x < rowSize; x++) {
                int index = rowStart + 1 + x;
                int lsb = decompressed[index] & 1;
                bits.append(lsb);
            }
        }

        StringBuilder message = new StringBuilder();
        for (int i = 0; i + 8 <= bits.length(); i += 8) {
            String byteStr = bits.substring(i, i + 8);
            int val = Integer.parseInt(byteStr, 2);
            if (val == 0) break; // fin du message
            message.append((char) val);
        }

        return message.toString();
    }

    public static void savePNG(String outputPath, byte[] decompressedData, int width, int height) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(fos));

        // Écrire la signature PNG
        dos.write(new byte[] {(byte)137, 80, 78, 71, 13, 10, 26, 10});

        // Écrire IHDR
        byte[] ihdrData = new byte[13];
        writeIntToBytes(width, ihdrData, 0);
        writeIntToBytes(height, ihdrData, 4);
        ihdrData[8] = 8;  // bit depth
        ihdrData[9] = 6;  // color type: 6 = RGBA
        ihdrData[10] = 0; // compression
        ihdrData[11] = 0; // filter
        ihdrData[12] = 0; // interlace
        writeChunk(dos, "IHDR", ihdrData);

        // Compresser les données
        byte[] compressedData = compressZlib(decompressedData);

        // Écrire IDAT
        writeChunk(dos, "IDAT", compressedData);

        // Écrire IEND
        writeChunk(dos, "IEND", new byte[0]);

        dos.flush();
        dos.close();
    }

    private static void writeChunk(DataOutputStream dos, String type, byte[] data) throws IOException {
        dos.writeInt(data.length);
        dos.writeBytes(type);
        dos.write(data);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream crcStream = new DataOutputStream(baos);
        crcStream.writeBytes(type);
        crcStream.write(data);
        byte[] crcBytes = baos.toByteArray();
        int crc = crc32(crcBytes);
        dos.writeInt(crc);
    }

    private static byte[] compressZlib(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            baos.write(buffer, 0, count);
        }
        deflater.end();
        return baos.toByteArray();
    }

    private static void writeIntToBytes(int value, byte[] array, int offset) {
        array[offset] = (byte)(value >> 24);
        array[offset + 1] = (byte)(value >> 16);
        array[offset + 2] = (byte)(value >> 8);
        array[offset + 3] = (byte)(value);
    }

    private static int crc32(byte[] data) {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(data);
        return (int)crc.getValue();
    }


}

