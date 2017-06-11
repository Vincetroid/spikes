package com.novoda.pianohero;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Sequence {

    private final Notes notes;
    private final int position;
    @Nullable
    private final Note latestError;

    private Sequence(Notes notes, int position, @Nullable Note latestError) {
        this.notes = notes;
        this.position = position;
        this.latestError = latestError;
    }

    public Note get(int position) {
        return notes.get(position);
    }

    public int position() {
        return position;
    }

    public int length() {
        return notes.length();
    }

    public boolean hasError() {
        return latestError != null && betweenC4AndB5Inclusive(latestError);
    }

    private boolean betweenC4AndB5Inclusive(Note note) {
        return note.midi() >= 60 && note.midi() <= 83;
    }

    @Nullable
    public Note latestError() {
        return latestError;
    }

    public Note getCurrentNote() {
        return notes.get(position);
    }

    public Note getNextNote() {
        int nextPosition = this.position + 1;
        if (notes.hasNoteAt(nextPosition)) {
            return notes.get(nextPosition);
        } else {
            return Note.NONE;
        }
    }

    public boolean currentNoteIs(Note note) {
        return notes.get(position).equals(note);
    }

    public Sequence nextPosition() {
        return new Sequence.Builder(this)
                .atPosition(position + 1)
                .withLatestError(null)
                .build();
    }

    public boolean isFinal(Note note) {
        return position == notes.length() - 1
                && note.equals(notes.get(position));
    }

    public Sequence error() {
        return new Sequence.Builder(this)
                .withLatestError(notes.get(position))
                .build();
    }

    public List<Note> subList(int start, int end) {
        return notes.asList().subList(start, end);
    }

    public static class Builder {

        private final List<Note> notes = new ArrayList<>();

        @Nullable
        private Note latestError;

        private int position = 0;

        Builder(Sequence sequence) {
            this(sequence.notes.asList());
            this.latestError = sequence.latestError();
            this.position = sequence.position;
        }

        private Builder(List<Note> notes) {
            this.notes.addAll(notes);
        }

        Builder() {
            this(new ArrayList<Note>());
        }

        Builder add(Note note) {
            notes.add(note);
            return this;
        }

        Builder withLatestError(@Nullable Note latestError) {
            this.latestError = latestError;
            return this;
        }

        Builder atPosition(int position) {
            this.position = position;
            return this;
        }

        Sequence build() {
            return new Sequence(new Notes(notes), position, latestError);
        }
    }
}