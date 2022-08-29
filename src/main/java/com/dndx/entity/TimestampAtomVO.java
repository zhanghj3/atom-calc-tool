package com.dndx.entity;

import java.util.List;

public class TimestampAtomVO {
    private int count;
    private int timestamp;
    private List<Atom> atoms;

    public TimestampAtomVO() {
    }

    public TimestampAtomVO(int count, int timestamp, List<Atom> atoms) {
        this.count = count;
        this.timestamp = timestamp;
        this.atoms = atoms;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<Atom> getAtoms() {
        return atoms;
    }

    public void setAtoms(List<Atom> atoms) {
        this.atoms = atoms;
    }
}
