package org.example;

import org.example.service.FlowLogParser;

import java.io.IOException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        String lookUpFile = "lookup.csv";
        String logFile = "sample.log";
        FlowLogParser parser =new FlowLogParser(logFile, lookUpFile);
        parser.parseFlowLogsAndGenerateOutputs();
    }
}