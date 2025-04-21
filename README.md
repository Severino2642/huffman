# 🧩 Compression and Steganography Application: Huffman and WAV/PNG Files

This application allows users to **generate a compression code** using the **Huffman algorithm** and then **apply steganography** to hide a file (text, image, or other file) inside **WAV audio files** and **PNG image files**.

## 🔑 Key Features

- **Data Compression with the Huffman Algorithm**  
  The application generates an optimal compression code for a text file by applying the Huffman algorithm. This code reduces the file size by using shorter symbols for more frequent data.

- **WAV File Steganography**  
  Insert text into a **WAV audio file** without perceptibly altering its quality. The algorithm hides information within the least significant bits of the audio file.

- **PNG File Steganography**  
  Hide information inside a **PNG image** by manipulating pixels, embedding data without visually affecting the image.

- **Extraction of Hidden Files**  
  The application also supports extracting hidden information from PNG images and WAV audio files. The reverse steganography process allows users to retrieve the hidden data from modified files.

## 🛠️ Technologies Used

- **Compression**: Huffman Algorithm (Java)  
- **Steganography**: Pixel manipulation (PNG), Audio bit modification (WAV)  
- **Backend**: Java

## 🎯 Objective

Provide a powerful **compression and steganography tool** that reduces data size using the Huffman algorithm and protects this data by hiding it in multimedia files (audio and images). Ideal for confidentiality applications, data protection, and discreet transmission.