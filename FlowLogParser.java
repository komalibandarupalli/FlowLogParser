package org.example.service;

import org.example.dto.Lookup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class FlowLogParser {
    private final String logFile;
    private final String lookUpFile;
    public FlowLogParser(String logFile, String lookUpFile) {
        this.logFile = logFile;
        this.lookUpFile = lookUpFile;
    }
    // Load the lookup table from the CSV file
    private Map<Integer, Lookup> loadLookUpTable() throws IOException {
        System.out.println("Loading lookup table");
        Map<Integer, Lookup> lookupTable = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(lookUpFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("dstport")) {
                    String[] parts = line.split(",");
                    int dstPort = Integer.parseInt(parts[0].trim());
                    String protocol = parts[1].trim().toLowerCase(Locale.ROOT);
                    String tag = parts[2].trim();
                    lookupTable.put(dstPort, new Lookup(dstPort, protocol, tag));
                }
            }
        }
        System.out.println("Lookup table loaded");
        return lookupTable;
    }
    public void parseFlowLogsAndGenerateOutputs() throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();
        Map<Integer, Lookup> lookupTable = loadLookUpTable();
        System.out.println("Parsing flow logs started");
        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int dstPort = Integer.parseInt(parts[5].trim());
                Lookup lookup = lookupTable.getOrDefault(dstPort, new Lookup(-1, "-1", "untagged"));
                // Add tag count - check the tag from the lookup and aggregate the count
                tagCounts.put(lookup.tag(), tagCounts.getOrDefault(lookup.tag(), 0) + 1);
                // Only consider TCP protocols
                if (lookup.protocol().equalsIgnoreCase("tcp")) {
                    String protocolKey = dstPort + "," + lookup.protocol().toLowerCase(Locale.ROOT);
                    portProtocolCounts.put(protocolKey, portProtocolCounts.getOrDefault(protocolKey, 0) + 1);
                }
            }
        }
        System.out.println("Parsing flow logs Ended");
        // Write the output files
        writeOutputs(List.of("Tag,Count"), tagCounts, "tagCountsOutput.csv");
        writeOutputs(List.of("Port,Protocol,Count"), portProtocolCounts, "protocolCountsOutput.csv");
    }
    // Helper method to write the outputs to CSV files
    private void writeOutputs(List<String> headers, Map<String, Integer> map, String fileName) throws IOException {
        System.out.println("Generating " + fileName + " output started");
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName))) {
            bw.write(String.join(",", headers) + "\n");
            map.forEach((k, v) -> {
                try {
                    bw.write(k + "," + v + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        System.out.println("Generating " + fileName + " output completed" + "\n");
    }
}
