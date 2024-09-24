# Flow Log Parser

## Overview
This program parses flow log data and maps each row to a tag based on a lookup table provided in a CSV file. The program generates two output files:
1. **tagCountsOutput.csv**: Contains counts of tags applied to the flow log data.
2. **protocolCountsOutput.csv**: Contains counts of port/protocol combinations.

## Prerequisites
- Java Development Kit (JDK 8 or higher)
- IDE (e.g., IntelliJ IDEA)

## How to Run
1. Clone the repository and open it in IntelliJ.
2.  Run the `Main.java` class. The output files will be generated in the same directory.
3.  The output files:
    - **tagCountsOutput.csv**
    - **protocolCountsOutput.csv**

## Tests
- Tested with sample flow log data and lookup table.
