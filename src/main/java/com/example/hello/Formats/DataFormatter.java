package com.example.hello.Formats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public final class DataFormatter {

    public static List<Full> fromJsonString (String json) throws IOException {
        return new ObjectMapper().readValue(json, new TypeReference<List<Full>>(){});
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Full {

        @JsonProperty("terms")
        private List<Integer> terms;

        @JsonProperty("termNames")
        private List<String> termName;

        @JsonProperty("periods")
        private List<Periods> periods;

        public List<Integer> getTerms() {
            return terms;
        }

        public void setTerms(List<Integer> terms) {
            this.terms = terms;
        }

        public List<String> getTermName() {
            return termName;
        }

        public void setTermName(List<String> termName) {
            this.termName = termName;
        }

        public List<Periods> getPeriods() {
            return periods;
        }

        public void setPeriods(List<Periods> periods) {
            this.periods = periods;
        }

    }

    public static class Periods {
        @JsonProperty("name")
        private String name;
        @JsonProperty("date")
        private String date;
        @JsonProperty("value")
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

