package mg.util;

import java.util.List;
import java.util.Random;

public class Generateur {
    public boolean isInForTab2D (int [][] tab,int a,int b){
        for (int [] row : tab) {
            if (row[0] == a && row[1] == b) {
                return true;
            }
        }
        return false;
    }

    public boolean isInForTab1D (int [] tab,int a){
        for (int row : tab) {
            if (row == a) {
                return true;
            }
        }
        return false;
    }

    public int [][] generateRandomPosition(int width, int height, int length) {
        int [][] result = new int[length][2];
        Random random = new Random();
        int i = 0;
        while (length>0){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (!isInForTab2D(result,x,y)){
                result[i][0] = x;
                result[i][1] = y;
                length--;
                i++;
            }
        }
        return result;
    }

    public int [] generateRandomPositionForWav(int length,int messageLength) {
        int [] result = new int[messageLength];
        Random random = new Random();
        int i = 0;
        while (messageLength>0){
            int x = random.nextInt(length);
            if (x>44){
                if (!isInForTab1D(result,x)){
                    result[i] = x;
                    messageLength--;
                    i++;
                }
            }
        }
        triage(result);
        return result;
    }

    public void triage(int[] array) {
        if (array.length < 2) {
            return;
        }

        int mid = array.length / 2;
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];

        // Diviser le tableau en deux moitiés
        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);

        // Tri récursif des deux moitiés
        triage(left);
        triage(right);
        // Fusion des deux moitiés triées
        merge(array, left, right);
    }

    // Fonction de fusion des deux sous-tableaux triés
    private void merge(int [] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Fusionner les deux sous-tableaux
        while (i < left.length && j < right.length) {
            if (left[i]<=right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }

        // Copier les éléments restants du tableau gauche (le cas échéant)
        while (i < left.length) {
            array[k++] = left[i++];
        }

        // Copier les éléments restants du tableau droit (le cas échéant)
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

}
