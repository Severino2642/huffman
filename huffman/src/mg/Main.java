package mg;

import mg.algorithme.Code;
import mg.algorithme.HuffMan;
import mg.algorithme.Source;
import mg.steganographie.png.PNGDecoder;
import mg.steganographie.png.PNGEncoder;
import mg.util.StringAnalyser;
import mg.util.StringCodage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String text = "hoouuurrrraaaaa";
        HuffMan hm = new HuffMan();
        List<Source> sources = new StringAnalyser().getSources(text);
        Code[] codes = hm.algoHuffman(sources);

        for (Code code : codes) {
            System.out.println(code.getSource().getS()+" : "+code.getCodeString());
        }

        StringCodage cod = new StringCodage();
        String textCode = cod.codage(text,codes);
        System.out.println("Codage : "+textCode);
//        byte [] bytes = PNGDecoder.decodePNG("C:\\Users\\diva\\Pictures\\avant1.png");
//        PNGDecoder.hideMessageInDecompressedBytes(bytes,629,849,3,textCode);
//        PNGEncoder.savePNG("stegano.png",bytes,629,849);

//        byte [] bytes = PNGDecoder.decodePNG("stegano.png");
//        textCode = PNGEncoder.extractMessageFromDecompressedData(bytes,629,849,3);
//        int a = Integer.parseInt(textCode,2);
//        System.out.println(a+" : "+Integer.toBinaryString(a));
//        System.out.println("Decodage : "+cod.decode(textCode,codes));
    }
}





//import java.io.*;
//import java.util.*;
//
//class HuffmanNode {
//    char character;
//    int frequency;
//    HuffmanNode left, right;
//
//    public HuffmanNode(char character, int frequency) {
//        this.character = character;
//        this.frequency = frequency;
//    }
//}
//
//// Comparateur pour la file de priorité
//class HuffmanComparator implements Comparator<HuffmanNode> {
//    public int compare(HuffmanNode a, HuffmanNode b) {
//        return a.frequency - b.frequency;
//    }
//}
//
//public class Main {
//    // Générer les codes de Huffman
//    public static void generateCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
//        if (root == null) return;
//
//        if (root.left == null && root.right == null) {
//            huffmanCodes.put(root.character, code);
//        }
//
//        generateCodes(root.left, code + "0", huffmanCodes);
//        generateCodes(root.right, code + "1", huffmanCodes);
//    }
//
//    // Construire l'arbre de Huffman
//    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> freqMap) {
//        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(new HuffmanComparator());
//
//        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
//            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
//        }
//
//        while (queue.size() > 1) {
//            HuffmanNode left = queue.poll();
//            HuffmanNode right = queue.poll();
//
//            HuffmanNode merged = new HuffmanNode('\0', left.frequency + right.frequency);
//            merged.left = left;
//            merged.right = right;
//
//            queue.add(merged);
//        }
//
//        return queue.poll();
//    }
//
//    // Convertir une chaîne binaire en tableau de bytes
//    public static byte[] convertToBytes(String binaryString) {
//        int length = (binaryString.length() + 7) / 8;
//        byte[] byteArray = new byte[length];
//
//        for (int i = 0; i < binaryString.length(); i += 8) {
//            String byteStr = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
//
//            // Compléter avec des zéros si nécessaire
//            while (byteStr.length() < 8) {
//                byteStr += "0";
//            }
//
//            byteArray[i / 8] = (byte) Integer.parseInt(byteStr, 2);
//        }
//
//        return byteArray;
//    }
//
//    // Sauvegarder les données compressées dans un fichier
//    public static void saveCompressedData(byte[] compressedData, String filename) {
//        try (FileOutputStream fos = new FileOutputStream(filename)) {
//            fos.write(compressedData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Charger un fichier compressé et convertir en binaire
//    public static byte[] loadCompressedData(String filename) {
//        try (FileInputStream fis = new FileInputStream(filename)) {
//            return fis.readAllBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new byte[0];
//    }
//
//    // Convertir un tableau de bytes en chaîne binaire
//    public static String convertBytesToBinary(byte[] byteArray) {
//        StringBuilder binaryString = new StringBuilder();
//        for (byte b : byteArray) {
//            binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
//        }
//        return binaryString.toString();
//    }
//
//    public static void main(String[] args) {
//        String text = "abc";
//
//        // Étape 1: Compter les fréquences des caractères
//        Map<Character, Integer> freqMap = new HashMap<>();
//        for (char c : text.toCharArray()) {
//            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
//        }
//        System.out.println(freqMap);
//
//        // Étape 2: Construire l'arbre de Huffman
//        HuffmanNode root = buildHuffmanTree(freqMap);
//
//        // Étape 3: Générer les codes
//        Map<Character, String> huffmanCodes = new HashMap<>();
//        generateCodes(root, "", huffmanCodes);
//
//        // Étape 4: Encoder le texte
//        StringBuilder encodedText = new StringBuilder();
//        for (char c : text.toCharArray()) {
//            encodedText.append(huffmanCodes.get(c));
//            System.out.println(c+":"+huffmanCodes.get(c));
//        }
//
//        System.out.println("Texte compressé (binaire) : " + encodedText);
//
//        // Étape 5: Convertir en bytes et enregistrer
//        byte[] compressedBytes = convertToBytes(encodedText.toString());
//        saveCompressedData(compressedBytes, "compressed.bin");
//
//        System.out.println("Fichier compressé 'compressed.bin' généré avec succès!");
//
//        // Étape 6: Charger et reconstruire les bits
//        byte[] loadedBytes = loadCompressedData("compressed.bin");
//        String binaryString = convertBytesToBinary(loadedBytes);
//
//        System.out.println("Bits récupérés : " + binaryString);
//    }
//}
