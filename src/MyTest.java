import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.System;

public class MyTest {

    public static void main(String[] args) {
/*
        int[][] m = convertArray((getMatrixFromFile("ex5.txt")));
        for(int i = 0; i<m.length; i++){
            System.out.println();
            for(int j = 0; j<m.length; j++){
                System.out.print(m[i][j]+", ");
            }
        }*/
        Algorithm al = new Algorithm("ex5.txt", 8);
        al.run();
        //al.algorithm(5, "ex5.txt");
    }
}