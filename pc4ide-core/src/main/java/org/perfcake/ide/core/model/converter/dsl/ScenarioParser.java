/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.core.model.converter.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.perfcake.ide.core.exception.ModelConversionException;
import org.perfcake.model.HeaderType;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario;

/**
 * Parses string with dsl scenario definition into PerfCake XML scenario model.
 *
 * @author Stanislav Kaleta, Jakub Knetl
 */
public class ScenarioParser {

    ScenarioParser() {
    }

    /**
     * Parses perfcake dsl scenario definition from string.
     *
     * @param scenario scenario dsl defintion
     * @return PerfCake xml scenario model
     * @throws ModelConversionException when model cannot be converted
     */
    Scenario parseScenario(String scenario) throws ModelConversionException {
        Scenario model = new Scenario();

        String[] lines = scenario.split("\n");

        if (!lines[0].contains("scenario")) {
            throw new IllegalArgumentException("First line does not contain \"scenario\" string!");
        }

        if (!lines[lines.length - 1].contains("end")) {
            throw new IllegalArgumentException("Last line does not contain \"end\" string!");
        }

        String propertiesLine = null;
        String runLine = null;
        String generatorLine = null;
        String senderLine = null;
        String receiverLine = null;
        List<String> sequncesLines = new ArrayList<>();
        int receiverLineIndex = -1;
        String correlatorLine = null;
        Map<String, List<String>> reporterLines = new LinkedHashMap<>();
        String validationLine = null;
        List<String> validatorLines = new ArrayList<>();
        List<String> messageLines = new ArrayList<>();

        // TODO: is run required to be on the second line  (in other words is order important?)
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(" ");
            if (parts.length == 0 || isComment(lines[i])) {
                // skip empty line or line which starts with comment
                continue;
            }
            if (i == 1 && !lines[i].contains("run ")) {
                propertiesLine = lines[i];
            }
            if (lines[i].contains("run ") && (i == 1 || i == 2)) {
                runLine = lines[i];
            }
            if (lines[i].contains("generator ") && (i == 2 || i == 3)) {
                generatorLine = lines[i];
            }
            if (lines[i].contains("sender ") && (i == 3 || i == 4)) {
                senderLine = lines[i];
            }
            if (lines[i].contains("receiver ")) {
                receiverLine = lines[i];
                receiverLineIndex = i;
            }
            if (lines[i].contains(("correlator")) && (receiverLineIndex + 1) == i) {
                correlatorLine = lines[i];
            }
            if (lines[i].contains("sequence") && !lines[i].contains("sequences")) {
                sequncesLines.add(lines[i]);
            }

            if (lines[i].contains("reporter ")) {
                String reporterLine = lines[i];
                List<String> destinationLines = new ArrayList<>();
                for (int d = i + 1; d < lines.length; d++) {
                    if (lines[d].contains("destination ")) {
                        destinationLines.add(lines[d]);
                    } else {
                        d = lines.length;
                    }
                }
                reporterLines.put(reporterLine, destinationLines);
            }
            if (lines[i].contains("validation ")) {
                validationLine = lines[i];
                for (int v = i + 1; v < lines.length; v++) {
                    if (lines[v].contains("validator ")) {
                        validatorLines.add(lines[v]);
                    } else {
                        v = lines.length;
                    }
                }
            }
            if (lines[i].contains("message")) {
                messageLines.add(lines[i]);
            }

        }

        Scenario.Generator generator = parseGenerator(generatorLine);
        model.setGenerator(generator);
        model.setRun(parseRun(runLine, generator));
        model.setSender(parseSender(senderLine));
        model.setSequences(parseSequences(sequncesLines));
        Scenario.Receiver.Correlator correlator = parserCorrelator(correlatorLine);
        model.setReceiver(parseReceiver(receiverLine, correlator));
        model.setReporting(parseReporting(reporterLines));
        model.setValidation(parseValidation(validationLine, validatorLines));
        model.setMessages(parseMessages(messageLines));
        model.setProperties(parseProperties(propertiesLine));


        /*TODO check if model is valid*/
        return model;
    }

    private Scenario.Sequences parseSequences(List<String> sequncesLines) throws ModelConversionException {
        if (sequncesLines.isEmpty()) {
            return null;
        }

        Scenario.Sequences sequences = new Scenario.Sequences();
        for (String line : sequncesLines) {
            Scenario.Sequences.Sequence sequence = new Scenario.Sequences.Sequence();

            String[] parts = line.split(" ");
            if (!"sequence".equals(parts[0])) {
                throw new ModelConversionException("Sequence line not starting with \"sequence\"");
            }
            sequence.setClazz(parts[1].substring(1, parts[1].length() - 1));

            sequence.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));

            Pattern idPattern = Pattern.compile("id \".*?\"");
            Matcher idMatcher = idPattern.matcher(line);
            if (idMatcher.matches()) {
                String id = idMatcher.group().split("\"")[1];
                sequence.setId(id);
            }
            sequences.getSequence().add(sequence);
        }

        return sequences;
    }

    private Scenario.Receiver.Correlator parserCorrelator(String correlatorLine) throws ModelConversionException {
        if (correlatorLine == null) {
            return null;
        }

        Scenario.Receiver.Correlator correlator = new Scenario.Receiver.Correlator();
        String[] parts = correlatorLine.split(" ");

        if (!parts[0].equals("correlator")) {
            throw new ModelConversionException("correlator line not starting with \"correlator\"");
        }
        correlator.setClazz(parts[1].substring(1, parts[1].length() - 1));

        correlator.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
        return correlator;
    }

    private Scenario.Receiver parseReceiver(String receiverLine, Scenario.Receiver.Correlator correlator) throws ModelConversionException {
        if (receiverLine == null) {
            return null;
        }

        Scenario.Receiver receiver = new Scenario.Receiver();

        receiverLine = removeFirstSpaces(receiverLine);
        String[] parts = receiverLine.split(" ");

        if (!parts[0].equals("receiver")) {
            throw new ModelConversionException("receiver line not starting with \"genrator\"");
        }

        receiver.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (correlator == null) {
            throw new ModelConversionException("Receiver does not contain correlator");
        }
        receiver.setCorrelator(correlator);

        Pattern threadsPattern = Pattern.compile("\\d\\.threads");
        Matcher threadsMatcher = threadsPattern.matcher(receiverLine);
        if (threadsMatcher.find()) {
            String threads = threadsMatcher.group().split("\\.")[0];
            receiver.setThreads(threads);
        }

        Pattern sourcePatter = Pattern.compile("source \".*?\"");
        Matcher sourceMatcher = sourcePatter.matcher(receiverLine);
        if (sourceMatcher.matches()) {
            String source = sourceMatcher.group().split("\"")[1];
            receiver.setSource(source);
        }

        receiver.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));

        return receiver;
    }

    /* message file:"a.txt" send 1.times validate "b"
     * message file:"b.txt" send 1.times headers h:"a",hh:"b" */
    private Scenario.Generator parseGenerator(String generatorLine) throws ModelConversionException {
        if (generatorLine == null) {
            throw new ModelConversionException("generator line not found!");
        }

        Scenario.Generator model = new Scenario.Generator();

        generatorLine = removeFirstSpaces(generatorLine);
        String[] parts = generatorLine.split(" ");
        if (!parts[0].equals("generator")) {
            throw new ModelConversionException("generator line not starting with \"generator\"!");
        }

        model.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (parts.length > 2) {
            model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
        }

        return model;
    }

    private Scenario.Sender parseSender(String senderLine) throws ModelConversionException {
        if (senderLine == null) {
            throw new ModelConversionException("sender line not found!");
        }

        senderLine = removeFirstSpaces(senderLine);
        String[] parts = senderLine.split(" ");
        if (!parts[0].equals("sender")) {
            throw new ModelConversionException("sender line not starting with \"sender\"!");
        }

        Scenario.Sender model = new Scenario.Sender();
        model.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (parts.length > 2) {
            model.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 2, parts.length)));
        }

        return model;
    }

    private Scenario.Reporting parseReporting(Map<String, List<String>> reporterLines) throws ModelConversionException {
        if (reporterLines.keySet().size() == 0) {
            return null;
        }
        Scenario.Reporting reportingModel = new Scenario.Reporting();

        for (String reporterLine : reporterLines.keySet()) {
            List<String> destinationLines = reporterLines.get(reporterLine);
            reporterLine = removeFirstSpaces(reporterLine);
            String[] parts = reporterLine.split(" ");
            if (!parts[0].equals("reporter")) {
                throw new ModelConversionException("reporter line not starting with \"reporter\"!");
            }
            Scenario.Reporting.Reporter reporterModel = new Scenario.Reporting.Reporter();
            reporterModel.setClazz(parts[1].substring(1, parts[1].length() - 1));

            if (reporterLine.contains("disabled")) {
                reporterModel.setEnabled(false);
            } else {
                reporterModel.setEnabled(true);
            }
            if (parts.length - 2 > 1) {
                String[] properties = removeTokens(2, parts, "enabled", "disabled");
                reporterModel.getProperty().addAll(parseProperties(properties));
            }
            for (String destinationLine : destinationLines) {
                reporterModel.getDestination().add(parseDestination(destinationLine));
            }
            reportingModel.getReporter().add(reporterModel);
        }
        return reportingModel;
    }

    private Scenario.Validation parseValidation(String validationLine, List<String> validatorLines) throws ModelConversionException {
        if (validationLine == null) {
            return null;
        }
        validationLine = removeFirstSpaces(validationLine);
        String[] parts = validationLine.split(" ");
        if (!parts[0].equals("validation")) {
            throw new ModelConversionException("validation line not starting with \"validation\"!");
        }
        Scenario.Validation validationModel = new Scenario.Validation();
        if (parts.length > 2) {
            if (parts[1].equals("fast")) {
                validationModel.setFastForward(true);
            } else {
                throw new ModelConversionException("string " + parts[1] + " where \"fast\" is expected!");
            }
            validationModel.setEnabled(parseEnabled(parts[2]));
        } else {
            validationModel.setEnabled(parseEnabled(parts[1]));
        }
        for (String validatorLine : validatorLines) {
            validationModel.getValidator().add(parseValidator(validatorLine));
        }
        return validationModel;
    }

    private Scenario.Messages parseMessages(List<String> messageLines) throws ModelConversionException {
        if (messageLines.size() == 0) {
            return null;
        }
        Scenario.Messages messagesModel = new Scenario.Messages();
        for (String messageLine : messageLines) {
            messagesModel.getMessage().add(parseMessage(messageLine));
        }
        return messagesModel;
    }

    private Scenario.Properties parseProperties(String propertiesLine) throws ModelConversionException {
        if (propertiesLine == null) {
            return null;
        }
        propertiesLine = removeFirstSpaces(propertiesLine);
        String[] parts = propertiesLine.split(" ");
        Scenario.Properties propertiesModel = new Scenario.Properties();
        propertiesModel.getProperty().addAll(parseProperties(parts));

        return propertiesModel;
    }

    private List<PropertyType> parseProperties(String[] propertyArray) throws ModelConversionException {
        if (propertyArray == null || (propertyArray.length % 2) != 0) {
            throw new ModelConversionException("unable to parse properties!");
        }

        List<PropertyType> properties = new ArrayList<>();

        for (int i = 0; i < propertyArray.length; i = i + 2) {
            PropertyType property = new PropertyType();
            property.setName(propertyArray[i]);
            property.setValue(propertyArray[i + 1].substring(1, propertyArray[i + 1].length() - 1));
            properties.add(property);
        }

        return properties;
    }

    private Scenario.Run parseRun(String runLine, Scenario.Generator generator) throws ModelConversionException {
        if (runLine == null) {
            throw new ModelConversionException("run line not found!");
        }

        runLine = removeFirstSpaces(runLine);
        String[] parts = runLine.split(" ");
        if (!parts[0].equals("run")) {
            throw new ModelConversionException("run line not starting with \"run\"!");
        }

        Scenario.Run run = new Scenario.Run();
        Period period = parsePeriod(parts[1]);
        run.setType(period.getType());
        run.setValue(period.getValue());

        if (!parts[2].equals("with")) {
            throw new ModelConversionException("run line not containing \"with\"!");
        }
        if (!parts[3].contains("threads")) {
            throw new ModelConversionException("run line not containing \"threads\"!");
        }
        generator.setThreads(parts[3].substring(0, parts[3].length() - 8));

        return run;
    }

    private Scenario.Reporting.Reporter.Destination parseDestination(String destinationLine) throws ModelConversionException {
        destinationLine = removeFirstSpaces(destinationLine);
        String[] parts = destinationLine.split(" ");
        if (!parts[0].equals("destination")) {
            throw new ModelConversionException("destination line not starting with \"destination\"!");
        }
        Scenario.Reporting.Reporter.Destination destinationModel = new Scenario.Reporting.Reporter.Destination();
        destinationModel.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (!parts[2].equals("every")) {
            throw new ModelConversionException("destination line not containing \"every\"!");
        } else {
            Period p = parsePeriod(parts[3]);
            Scenario.Reporting.Reporter.Destination.Period period = new Scenario.Reporting.Reporter.Destination.Period();
            period.setType(p.getType());
            period.setValue(p.getValue());
            destinationModel.getPeriod().add(period);
        }
        if (destinationLine.contains("disabled")) {
            destinationModel.setEnabled(false);
        } else {
            destinationModel.setEnabled(true);
        }
        if (parts.length - 4 > 1) {
            String[] properties = removeTokens(4, parts, "enabled", "disabled");
            destinationModel.getProperty().addAll(parseProperties(properties));
        }
        return destinationModel;
    }

    private Scenario.Validation.Validator parseValidator(String validatorLine) throws ModelConversionException {
        validatorLine = removeFirstSpaces(validatorLine);
        String[] parts = validatorLine.split(" ");
        parts = mergeTokens(parts); //merge tokens which belong together because of quotation marks
        if (!parts[0].equals("validator")) {
            throw new ModelConversionException("validator line not starting with \"validator\"!");
        }
        Scenario.Validation.Validator validatorModel = new Scenario.Validation.Validator();
        validatorModel.setClazz(parts[1].substring(1, parts[1].length() - 1));
        if (!parts[2].equals("id")) {
            throw new ModelConversionException("validator line not containing \"id\"!");
        } else {
            validatorModel.setId(parts[3].substring(1, parts[3].length() - 1));
        }
        if (parts.length > 4) {
            validatorModel.getProperty().addAll(parseProperties(Arrays.copyOfRange(parts, 4, parts.length)));
        }
        return validatorModel;
    }

    private Scenario.Messages.Message parseMessage(String messageLine) throws ModelConversionException {
        messageLine = removeFirstSpaces(messageLine);
        String[] parts = messageLine.split(" ");
        if (!parts[0].equals("message")) {
            throw new ModelConversionException("message line not starting with \"message\"!");
        }
        Scenario.Messages.Message messageModel = new Scenario.Messages.Message();
        if (parts[1].contains("content")) {
            messageModel.setContent(parts[1].substring(9, parts[1].length() - 1));
        } else {
            if (parts[2].contains("content")) {
                messageModel.setContent(parts[2].substring(9, parts[2].length() - 1));
            }
        }
        if (parts[1].contains("file")) {
            messageModel.setUri(parts[1].substring(6, parts[1].length() - 1));
        } else {
            if (parts[2].contains("file")) {
                messageModel.setUri(parts[2].substring(6, parts[2].length() - 1));
            }
        }
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("send")) {
                String multiplicity = parts[i + 1];
                messageModel.setMultiplicity(multiplicity.substring(0, multiplicity.length() - 6));
            }
        }
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("headers")) {
                String[] headers = parts[i + 1].split(",");
                if ((headers.length % 2) != 0) {
                    throw new ModelConversionException("unable to parse headers");
                }
                for (String headerAsString : headers) {
                    String[] headerParts = headerAsString.split(":");
                    if (headerParts.length != 2) {
                        throw new ModelConversionException("unable to parse header");
                    }
                    HeaderType header = new HeaderType();
                    header.setName(headerParts[0]);
                    header.setValue(headerParts[1].substring(1, headerParts[1].length() - 1));
                    messageModel.getHeader().add(header);
                }
            }
        }
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("validate")) {
                Scenario.Messages.Message.ValidatorRef ref = new Scenario.Messages.Message.ValidatorRef();
                ref.setId(parts[i + 1].substring(1, parts[i + 1].length() - 1));
                messageModel.getValidatorRef().add(ref);
            }
        }
        return messageModel;
    }

    private boolean parseEnabled(String enabled) throws ModelConversionException {
        if (enabled.equals("enabled")) {
            return true;
        }
        if (enabled.equals("disabled")) {
            return false;
        }
        throw new ModelConversionException("unable to parse enabled/disabled");
    }

    private Period parsePeriod(String periodAsString) throws ModelConversionException {
        if (periodAsString == null) {
            throw new ModelConversionException("unable to parse period!");
        }
        if (periodAsString.contains(".percent") || periodAsString.contains("%")) {
            Period period = new Period();
            period.setType("percentage");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 8));
            return period;
        }
        if (periodAsString.contains(".iterations")) {
            Period period = new Period();
            period.setType("iteration");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 11));
            return period;
        }
        if (periodAsString.contains(".ms")) {
            Period period = new Period();
            period.setType("time");
            period.setValue(periodAsString.substring(0, periodAsString.length() - 3));
            return period;
        }
        if (periodAsString.contains(".s")) {
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 1000L));
            return period;
        }
        if (periodAsString.contains(".m")) {
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 60000L));
            return period;
        }
        if (periodAsString.contains(".h")) {
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 3600000L));
            return period;
        }
        if (periodAsString.contains(".d")) {
            Period period = new Period();
            period.setType("time");
            period.setValue(countMilliseconds(periodAsString.substring(0, periodAsString.length() - 2), 86400000L));
            return period;
        }
        throw new ModelConversionException("unable to parse period - invalid period type: " + periodAsString);
    }

    /**
     * Copies array from the begin index and filters out all tokens which are equal to any of removeTokens.
     *
     * @param beginIndex   begin index of the array from
     * @param tokens       tokens
     * @param removeTokens tokens which will be removed from toknes array
     * @return Array of filtered tokens
     */
    private String[] removeTokens(int beginIndex, String[] tokens, String... removeTokens) {
        if (removeTokens == null || removeTokens.length == 0) {
            return tokens;
        }

        if (beginIndex >= tokens.length) {
            throw new IllegalArgumentException("beginIndex out of range");
        }

        Set<String> removeList = new HashSet(Arrays.asList(removeTokens));
        List<String> result = new ArrayList<>();
        for (String token : tokens) {
            if (!removeList.contains(token)) {
                result.add(token);
            }
        }

        return result.toArray(new String[] {});

    }

    /**
     * Determines whether a line is a comment.
     *
     * @param line line to be checked
     * @return true if this lines is comment only
     */
    private boolean isComment(String line) {
        String trimmed = line.trim();
        return trimmed.startsWith("//");
    }

    /**
     * Merges tokens which belongs together because of quotation marks.
     *
     * @param tokens array of tokens
     * @return tokens
     */
    private String[] mergeTokens(String[] tokens) throws ModelConversionException {
        List<String> merged = new ArrayList<>();

        boolean mergeInProgress = false;
        StringBuilder mergedToken = null;
        for (String token : tokens) {
            // start merging if token starts with qutation mark, but does not end with quoatation mark
            if (token.startsWith("\"") && !token.endsWith("\"")) {
                mergeInProgress = true;
                mergedToken = new StringBuilder();
            }

            if (mergeInProgress) {
                mergedToken.append(token);
                if (token.endsWith("\"")) {
                    mergeInProgress = false;
                    merged.add(mergedToken.toString());
                }
            } else {
                merged.add(token);
            }
        }

        if (mergeInProgress) {
            throw new ModelConversionException("missing ending quotation mark (\") in tokens: " + String.join(" ", tokens));
        }

        return merged.toArray(new String[] {});
    }

    private String countMilliseconds(String value, Long multiplier) {
        try {
            Long number = Long.parseLong(value);
            number = number * multiplier;
            return String.valueOf(number);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private String removeFirstSpaces(String stringToTransform) {
        while (stringToTransform.startsWith(" ")) {
            stringToTransform = stringToTransform.substring(1);
        }
        return stringToTransform;
    }

    private class Period {
        private String type;
        private String value;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
