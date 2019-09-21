package com.inspur.plugins.kaoshi.model;

import java.util.List;

public class Topic {
    String topic_id;
    String topic_name;
    String topic_score;
    String topic_answer;
    String topic_type;
    List<String> topic_options;

    public Topic() {
    }

    public Topic(String topic_id, String topic_name, String topic_score, String topic_type) {
        this.topic_id = topic_id;
        this.topic_name = topic_name;
        this.topic_score = topic_score;
        this.topic_type = topic_type;
    }

    public Topic(String topic_id, String topic_name, String topic_score, String topic_answer, String topic_type, List<String> topic_options) {
        this.topic_id = topic_id;
        this.topic_name = topic_name;
        this.topic_score = topic_score;
        this.topic_answer = topic_answer;
        this.topic_type = topic_type;
        this.topic_options = topic_options;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getTopic_score() {
        return topic_score;
    }

    public void setTopic_score(String topic_score) {
        this.topic_score = topic_score;
    }

    public String getTopic_answer() {
        return topic_answer;
    }

    public void setTopic_answer(String topic_answer) {
        this.topic_answer = topic_answer;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public List<String> getTopic_options() {
        return topic_options;
    }

    public void setTopic_options(List<String> topic_options) {
        this.topic_options = topic_options;
    }

    @Override
    public String toString() {
        return "{" +
                "topic_id='" + topic_id + '\'' +
                ", topic_name='" + topic_name + '\'' +
                ", topic_score='" + topic_score + '\'' +
                ", topic_answer='" + topic_answer + '\'' +
                ", topic_type='" + topic_type + '\'' +
                ", topic_options=" + topic_options +
                '}';
    }
}
