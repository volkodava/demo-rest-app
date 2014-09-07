package com.demo.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.demo.web.JsonViews;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonView;

@Entity
@Table(name = "NEWS")
public class NewsEntry extends BusinessEntity {

    @Column(name = "news_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column
    private String content;

    public NewsEntry() {

        this.date = new Date();
    }

    @JsonView(JsonViews.Admin.class)
    @Override
    public Long getId() {
        return super.getId();
    }

    @JsonView(JsonViews.User.class)
    public Date getDate() {

        return this.date;
    }

    public void setDate(Date date) {

        this.date = date;
    }

    @JsonView(JsonViews.User.class)
    public String getContent() {

        return this.content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsEntry{" + "date=" + date + ", content=" + content + '}';
    }
}
