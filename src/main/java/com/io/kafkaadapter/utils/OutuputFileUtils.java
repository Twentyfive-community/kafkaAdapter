package com.io.kafkaadapter.utils;

import com.io.kafkaadapter.messages.FileNamingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class OutuputFileUtils {

    public String outputFileName(String serviceName){
        return FileNamingUtils.STANDARD_PREFIX+ serviceName+ FileNamingUtils.STANDARD_OUTPUT_SUFFIX+ FileNamingUtils.STANDARD_EXTENSION;
    }
    public String errorFileName(String serviceName){
        return FileNamingUtils.STANDARD_PREFIX+ serviceName+ FileNamingUtils.STANDARD_ERROR_SUFFIX+ FileNamingUtils.STANDARD_EXTENSION;
    }
    /*private PrintStream getFileStream(String filePathString) throws FileNotFoundException {
        System.out.println("ciao");
        File f = new File(filePathString);
        //if(!(f.exists() && !f.isDirectory()))
        //    createFile(filePathString);
        return new PrintStream(filePathString);
    }*/

    /*public PrintStream getStdOutputFileStream(String serviceName) throws FileNotFoundException {
        String filePathString = outputFileName(serviceName);
        return getFileStream(filePathString);
    }*/

    public File getStdOutputFile(String serviceName)  {
        String filepath = outputFileName(serviceName);
        File f = new File(outputFileName(serviceName));
        if(!(f.exists() && !f.isDirectory()))
            createFile(outputFileName(serviceName));
        return new File(outputFileName(serviceName));
    }

    public File getStdErrorFile(String serviceName)  {
        File f = new File(errorFileName(serviceName));
        if(!(f.exists() && !f.isDirectory()))
            createFile(errorFileName(serviceName));
        return new File(errorFileName(serviceName));
    }

    /*public PrintStream getStdErrorFileStream(String serviceName) throws FileNotFoundException {
        String filePathString = errorFileName(serviceName);
        return getFileStream(filePathString);
    }*/

    public void createFile(String filename) {
        try {
            File newFile = new File(filename);
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
