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

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.perfcake.model.HeaderType;
import org.perfcake.model.PropertyType;
import org.perfcake.model.Scenario;

/**
 * Builds dsl scenario from PerfCake xml model.
 *
 * @author Stanislav Kaleta, Jakub Knetl
 */
public class ScenarioBuilder {

    ScenarioBuilder() {
    }

    String buildScenario(Scenario model, String name) {
        StringBuilder sb = new StringBuilder();

        String scenarioDefinition = "scenario \"" + name + "\"\n";
        sb.append(scenarioDefinition);


        if (model.getProperties() != null) {
            sb.append(buildProperties(model.getProperties()));
        }

        sb.append(buildRunAndGenerator(model));

        if (model.getSequences() != null && !model.getSequences().getSequence().isEmpty()) {
            sb.append(buildSequences(model.getSequences()));
        }
        sb.append(buildSender(model.getSender()));

        if (model.getReceiver() != null) {
            sb.append(buildReceiver(model.getReceiver()));
        }

        if (model.getReporting() != null) {
            sb.append(buildReporting(model.getReporting()));
        }

        if (model.getMessages() != null) {
            sb.append(buildMessages(model.getMessages()));
        }

        if (model.getValidation() != null) {
            sb.append(buildValidation(model.getValidation()));
        }

        sb.append("end");

        return sb.toString();
    }

    private String buildSequences(Scenario.Sequences sequences) {
        StringBuilder builder = new StringBuilder()
                .append("  sequences");

        for (Scenario.Sequences.Sequence sequence : sequences.getSequence()) {
            builder.append("\n");
            builder.append(String.format("    sequences \"%s\"", sequence.getClazz()));
            if (StringUtils.isNotBlank(sequence.getId())) {
                builder.append(String.format(" id \"%s\"", sequence.getId()));
            }
        }

        return builder.toString();
    }

    private String buildReceiver(Scenario.Receiver receiver) {
        StringBuilder receiverBuilder = new StringBuilder()
                .append("  receiver")
                .append(String.format(" \"%s\"", receiver.getClazz()));

        if (StringUtils.isNotBlank(receiver.getThreads())) {
            receiverBuilder.append(String.format(" with %s.threads", receiver.getThreads()));
        }
        if (StringUtils.isNotBlank(receiver.getSource())) {
            receiverBuilder.append(String.format(" source \"%s\"", receiver.getSource()));
        }

        receiverBuilder.append(buildProperties(receiver.getProperty()));

        receiverBuilder
                .append("\n")
                .append(String.format("    correlator \"%s\"", receiver.getCorrelator().getClazz()))
                .append(buildProperties(receiver.getCorrelator().getProperty()));

        return receiverBuilder.toString();
    }

    private String buildRunAndGenerator(Scenario model) {
        String run = "  run " + buildPeriod(model.getRun().getType(), model.getRun().getValue())
                + " with " + model.getGenerator().getThreads() + ".threads\n";

        String generator = "  generator \"" + model.getGenerator().getClazz() + "\"" + buildProperties(model.getGenerator().getProperty());

        return run + generator + "\n";
    }

    private String buildSender(Scenario.Sender model) {
        String sender = "  sender \"" + model.getClazz() + "\"" + buildProperties(model.getProperty());

        return sender + "\n";
    }

    private String buildReporting(Scenario.Reporting model) {
        String reporting = "";

        for (Scenario.Reporting.Reporter reporterModel : model.getReporter()) {
            String reporter = "  reporter \"" + reporterModel.getClazz() + "\""
                    + buildProperties(reporterModel.getProperty()) + buildEnabled(reporterModel.isEnabled()) + "\n";

            for (Scenario.Reporting.Reporter.Destination destinationModel : reporterModel.getDestination()) {
                String destination = "    destination \"" + destinationModel.getClazz() + "\"";

                for (Scenario.Reporting.Reporter.Destination.Period periodModel : destinationModel.getPeriod()) {
                    String period = " every " + buildPeriod(periodModel.getType(), periodModel.getValue());
                    destination = destination + period;
                }

                destination = destination + buildProperties(destinationModel.getProperty()) + buildEnabled(destinationModel.isEnabled());
                reporter = reporter + destination + "\n";
            }

            reporting = reporting + reporter;
        }
        /*TODO reporting properties? + how ""/"enabled"/"disabled" */
        return reporting;
    }

    private String buildMessages(Scenario.Messages model) {
        String messages = "";
        /* TODO: content:"c" / file:"f" / "file" / "text" + ako naraz + send/"" + headers name:"value",name:"value" + validate
           "v" validate "v"*/
        for (Scenario.Messages.Message messageModel : model.getMessage()) {
            String uri = (messageModel.getUri() != null) ? "file:\"" + messageModel.getUri() + "\" " : "";
            String content = (messageModel.getContent() != null)
                    ? "content:\"" + messageModel.getContent() + "\" " : "";
            if (uri.equals("") && content.equals("")) {
                content = "content:\"\" ";
            }
            String message = "  message " + content + uri + "send " + messageModel.getMultiplicity() + ".times"
                    + buildProperties(messageModel.getProperty());

            for (int i = 0; i < messageModel.getHeader().size(); i++) {
                if (i == 0) {
                    message = message + " headers ";
                }

                HeaderType header = messageModel.getHeader().get(i);
                message = message + header.getName() + ":\"" + header.getValue() + "\"";

                if (i < messageModel.getHeader().size() - 1) {
                    message = message + ",";
                }
            }

            for (Scenario.Messages.Message.ValidatorRef ref : messageModel.getValidatorRef()) {
                message = message + " validate \"" + ref.getId() + "\"";
            }
            messages = messages + message + "\n";
        }

        return messages;
    }

    private String buildValidation(Scenario.Validation model) {
        String validation = "  validation"
                + ((model.isFastForward()) ? " fast" : "") + buildEnabled(model.isEnabled()) + "\n";

        for (Scenario.Validation.Validator validatorModel : model.getValidator()) {
            String validator = "    validator \"" + validatorModel.getClazz() + "\" id \""
                    + validatorModel.getId() + "\"" + buildProperties(validatorModel.getProperty()) + "\n";
            validation = validation + validator;
        }

        return validation;
    }

    private String buildPeriod(String type, String value) {
        switch (type) {
            case "time": /*TODO ms,s,m,h,d / .it... or " it..."?*/
                return value + ".ms";
            case "iteration":
                return value + ".iterations";
            case "percentage":
                return value + ".percent";
            default:
                throw new IllegalArgumentException("Invalid period type!");
        }
    }

    private String buildProperties(Scenario.Properties model) {
        return " " + buildProperties(model.getProperty()) + "\n";
    }

    private String buildProperties(List<PropertyType> propertyList) {
        String properties = "";
        for (PropertyType property : propertyList) {
            properties = properties + " " + property.getName() + " \"" + property.getValue() + "\"";
        }
        return properties;
    }

    private String buildEnabled(boolean isEnabled) {
        return (isEnabled) ? " enabled" : " disabled";
    }
}
