package io.github.arrayv.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.github.arrayv.panes.JErrorPane;

/**
 *
 * Writes the array to a file
 */
public final class ArrayFileWriter {
    private ArrayFileWriter() {
    }

    /**
     *
     * Writes the main array to the file path.
     *
     * @param fileName the path or name
     * @param array    the array
     * @param length   how much of the array from the start
     * @return whether it worked or not
     */
    public static boolean writeArray(String fileName, int[] array, int length) {
        try {
            FileWriter writer = new FileWriter(fileName);
            write(writer, array, length);
        } catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        return true;
    }

    /**
     *
     * Writes the main array to the file object.
     *
     * @param file   the file
     * @param array  the array
     * @param length how much of the array from the start
     * @return whether it worked or not
     */
    public static boolean writeArray(File file, int[] array, int length) {
        try {
            FileWriter writer = new FileWriter(file);
            write(writer, array, length);
        } catch (IOException e) {
            JErrorPane.invokeErrorMessage(e);
            return false;
        }
        return true;
    }

    private static void write(FileWriter writer, int[] array, int length) throws IOException {
        for (int i = 0; i < length - 1; i++) {
            writer.write(array[i] + " ");
        }
        writer.write("" + array[length - 1]);
        writer.close();
    }
}
