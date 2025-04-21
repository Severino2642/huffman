package mg.steganographie.wav;

import mg.algorithme.Source;
import mg.util.Generateur;

import java.io.*;

public class WavEncoder {

    public static int getMaxMessageBytes(String wavPath) throws IOException {
        File file = new File(wavPath);
        FileInputStream in = new FileInputStream(file);
        byte[] header = new byte[44];
        in.read(header);
        in.close();

        // Bits par échantillon (par canal)
        int bitsPerSample = ((header[35] & 0xFF) << 8) | (header[34] & 0xFF);

        // Nombre de canaux (1 = mono, 2 = stéréo)
        int numChannels = ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);

        // Nombre total de données audio (en octets)
        int fileSize = (int) file.length();
        int dataSize = fileSize - 44;

        // Taille d’un échantillon complet (en octets)
        int bytesPerSample = (bitsPerSample / 8) * numChannels;

        // Nombre total d’échantillons (chaque échantillon = 1 bit cachable)
        int totalSamples = dataSize / bytesPerSample;

        // Nombre maximal de bytes de message (1 byte = 8 bits)
        return totalSamples / 8;
    }



    public static void hideMessage(String inputWav, String outputWav, String message,int [] position) throws IOException {
        FileInputStream in = new FileInputStream(inputWav);
        FileOutputStream out = new FileOutputStream(outputWav);

        byte[] header = new byte[44];
        in.read(header);
        out.write(header);

        // Lire bits par échantillon à l’offset 34-35
        int bitsPerSample = ((header[35] & 0xFF) << 8) | (header[34] & 0xFF);
        boolean is16bit = bitsPerSample == 16;

        // Convertir le message en bits + marqueur de fin

        int[] bits = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            bits[i] = message.charAt(i);
        }

        int bitIndex = 0;
        int count = 0;
        while (bitIndex < bits.length) {
            if (count == position[bitIndex]) {
                if (is16bit) {
                    int byte1 = in.read();
                    int byte2 = in.read();
                    if (byte2 == -1) break;

                    int sample = (byte2 << 8) | byte1;
                    sample = (sample & 0xFFFE) | bits[bitIndex++];
                    // Réécrire en little endian
                    out.write(sample & 0xFF);
                    out.write((sample >> 8) & 0xFF);
                    count+=2;
                } else {
                    int sample = in.read();
                    if (sample == -1) break;
                    sample = (sample & 0xFE) | bits[bitIndex++];
                    out.write(sample);
                    count++;
                }
            }
            else {
                out.write(in.read());
                count++;
            }
        }

//        for (int i :position){
////            System.out.println(i);
//            if (is16bit) {
//                raf.seek(i);
//                int byte1 = raf.read();
//                int byte2 = raf.read();
//                if (byte2 == -1) break;
//                int sample = (byte2 << 8) | byte1;
//                System.out.println("sample before :"+sample);
//                sample = (sample & 0xFFFE) | bits[bitIndex++];
//
//                // Réécrire en little endian
//                raf.seek(i);
//                raf.write(sample & 0xFF);
//                raf.seek(i+1);
//                raf.write((sample >> 8) & 0xFF);
//                System.out.println(" modif "+(sample & 0xFF)+" | "+((sample>>8) & 0xFF));
//                System.out.println("sample after :"+sample);
//
//            } else {
//                raf.seek(i);
//                int byte1 = raf.read();
//                if (byte1 == -1) break;
//                byte1 = (byte1 & 0xFE) | bits[bitIndex++];
//
//                raf.seek(i);
//                raf.write(byte1);
//            }
//        }

        // Créer un nouveau fichier WAV

//         Lire tout et copier
//        byte[] buffer = new byte[4096];
//        int bytesRead;
//        while ((bytesRead = raf.read(buffer)) != -1) {
//            out.write(buffer, 0, bytesRead);
//        }

        // Copier le reste sans modifier
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }

        in.close();
        out.close();
        System.out.println("Message caché dans : " + outputWav);
    }

    public static String extractMessage(String wavPath) throws IOException {
        FileInputStream in = new FileInputStream(wavPath);
        byte[] header = new byte[44];
        in.read(header);

        int bitsPerSample = ((header[35] & 0xFF) << 8) | (header[34] & 0xFF);
        boolean is16bit = bitsPerSample == 16;

        ByteArrayOutputStream bitStream = new ByteArrayOutputStream();
        int b1, b2;

        while (true) {
            if (is16bit) {
                b1 = in.read();
                b2 = in.read();
                if (b2 == -1) break;
                int sample = (b2 << 8) | b1;
                bitStream.write(sample & 1);
            } else {
                b1 = in.read();
                if (b1 == -1) break;
                bitStream.write(b1 & 1);
            }
        }

        in.close();

        byte[] bits = bitStream.toByteArray();
        ByteArrayOutputStream message = new ByteArrayOutputStream();

        for (int i = 0; i + 7 < bits.length; i += 8) {
            int ch = 0;
            for (int j = 0; j < 8; j++) {
                ch = (ch << 1) | bits[i + j];
            }
            if (ch == 0) break;
            message.write(ch);
        }

        return message.toString();
    }

    public static String extractBinaryMessage(String wavPath,int [] position) throws IOException {
        FileInputStream in = new FileInputStream(wavPath);
        byte[] header = new byte[44];
        in.read(header);

        int bitsPerSample = ((header[35] & 0xFF) << 8) | (header[34] & 0xFF);
        boolean is16bit = bitsPerSample == 16;

        StringBuilder bitStream = new StringBuilder();
        int b1, b2;

        int index = 0;
        int count = 0;
        while (index < position.length) {
            if (count == position[index]) {
                if (is16bit) {
                    b1 = in.read();
                    b2 = in.read();
                    if (b2 == -1) break;
                    int sample = (b2 << 8) | b1;
                    bitStream.append(sample & 1);
                    count+=2;
                } else {
                    b1 = in.read();
                    if (b1 == -1) break;
                    bitStream.append(b1 & 1);
                    count++;
                }
                index++;
            }
            else {
                in.read();
                count++;
            }
        }
//        for (int i : position){
//            if (is16bit) {
//                raf.seek(i);
//                b1 = raf.read();
//                b2 = raf.read();
//                if (b2 == -1) break;
//                int sample = (b2 << 8) | b1;
//                System.out.println(" 16bit: "+b1+" | "+b2);
//                bitStream.append(sample & 1);
//                index++;
//            } else {
//                raf.seek(i);
//                b1 = raf.read();
//                System.out.println(" 8bit "+b1);
//                if (b1 == -1) break;
//                bitStream.append(b1 & 1);
//
//                index++;
//            }
//        }

        in.close();
        return bitStream.toString();
    }

    public static void main(String[] args) throws IOException {
        String message = "010";
        String filePath = "D:\\ETUDE ITU\\Mr Tsinjo\\DS_Studio\\target\\DS_Studio-1.0-SNAPSHOT\\upload\\output.wav";
//        int [] position = new Generateur().generateRandomPositionForWav(getMaxMessageBytes(filePath),message.length());
//        for (int p : position){
//            System.out.println(p);
//        }
        int [] position = new int[message.length()];
        position[0] = 100;
        position[1] = 200;
        position[2] = 300;
//        System.out.println(position);
        hideMessage(filePath,"stegwavz.wav",message,position);
        System.out.println(extractBinaryMessage("stegwavz.wav",position));
    }


}
