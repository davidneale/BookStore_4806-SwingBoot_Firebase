package com.app.models;

import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Model {
    @Exclude
    public String id;

    public <T extends Model> T withId(@NonNull final String id) {
        this.id = id;
        return (T) this;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }
}