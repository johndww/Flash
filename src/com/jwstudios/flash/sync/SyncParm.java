package com.jwstudios.flash.sync;

import android.content.Context;
import android.widget.Button;
import com.jwstudios.flash.SibOne;

import java.util.Set;

/**
 * User: johnwright
 * Date: 3/29/14
 * Time: 12:16 PM
 */
public class SyncParm {
    final Set<SibOne> words;
    final Context context;
    final Button button;

    private SyncParm(final Set<SibOne> words, final Button button, final Context context) {
        this.words = words;
        this.button = button;
        this.context = context;
    }

    public Set<SibOne> getWords() {
        return this.words;
    }

    public Context getContext() {
        return this.context;
    }

    public Button getButton() {
        return button;
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Set<SibOne> words;
        private Context context;
        private Button button;

        private Builder() {
            //should be called from static
        }

        public SyncParm build() {
            return new SyncParm(this.words, this.button, this.context);
        }

        public Builder setWords(final Set<SibOne> words) {
            this.words = words;
            return this;
        }

        public Builder setButton(final Button button) {
            this.button = button;
            return this;
        }

        public Builder setContext(final Context context) {
            this.context = context;
            return this;
        }
    }
}
