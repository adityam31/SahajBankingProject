package com.bankingapp.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestUtility {
    public static List<String> getFileAsList(File file) throws IOException {
        List<String> stringList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
            stringList.add(inputLine);
        }
        return stringList;
    }
}
