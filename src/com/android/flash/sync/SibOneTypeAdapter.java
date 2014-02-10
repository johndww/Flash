package com.android.flash.sync;

import java.io.IOException;

import com.android.flash.SibOne;
import com.android.flash.SibTwo;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * @author john.wright
 * @since 21
 */
public class SibOneTypeAdapter
        extends TypeAdapter<SibOne> {
    @Override
    public void write(final JsonWriter jsonWriter, final SibOne sibOne)
            throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("sibone");
        jsonWriter.value(sibOne.getName());
        jsonWriter.name("sibtwo");
        jsonWriter.value(sibOne.getPair().getName());
        jsonWriter.endObject();
    }

    @Override
    public SibOne read(final JsonReader jsonReader)
            throws IOException {
        return new SibOne(jsonReader.nextString(), new SibTwo(jsonReader.nextString()), 0, 0);
    }
}