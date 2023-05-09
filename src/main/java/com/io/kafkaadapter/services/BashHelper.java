package com.io.kafkaadapter.services;

import com.io.kafkaadapter.messages.MessageType;
import com.io.kafkaadapter.utils.OutuputFileUtils;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public interface BashHelper {

    OutuputFileUtils outuputFileUtils = new OutuputFileUtils();

    default public File createTempScript(List<String> commands) throws IOException {
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);
        printWriter.println("#!/bin/bash");
        commands.forEach((c) -> printWriter.println(c));
        printWriter.close();

        return tempScript;
    }

    public default void executeCommands(List<String> commands, String serviceName, Long timeout, ProcessHelper processHelper) throws IOException, InterruptedException {
        File tempScript = createTempScript(commands);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            pb.redirectOutput(outuputFileUtils.getStdOutputFile(serviceName));
            pb.redirectError(outuputFileUtils.getStdErrorFile(serviceName));
            //inheritIO(process.getInputStream(), outuputFileUtils.getStdOutputFileStream(serviceName));
            //inheritIO(process.getErrorStream(), outuputFileUtils.getStdErrorFileStream(serviceName));
            if (processHelper != null) processHelper.processes.put(serviceName, process);
            if (timeout != 0) process.waitFor(timeout, TimeUnit.SECONDS);
            else process.waitFor();
        }
        finally {
            tempScript.delete();
        }
    }

    private void inheritIO(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                dest.println("buonasera ragazzi, come va la vita?");
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
                dest.flush();
            }
        }).start();
    }

    public default void executeCommands(List<String> commands, String serviceName, Integer timeoutInSeconds) throws IOException, InterruptedException, Exception {
        File tempScript = createTempScript(commands);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            pb.redirectOutput(outuputFileUtils.getStdOutputFile(serviceName));
            pb.redirectError(outuputFileUtils.getStdErrorFile(serviceName));
            //inheritIO(process.getInputStream(), outuputFileUtils.getStdOutputFileStream(serviceName));
            //inheritIO(process.getErrorStream(), outuputFileUtils.getStdErrorFileStream(serviceName));
            if (process.waitFor(timeoutInSeconds, TimeUnit.SECONDS))
                throw new Exception(MessageType.STARTING_SERVICE_ERROR);
        }
        finally {
            tempScript.delete();
        }
    }

}
