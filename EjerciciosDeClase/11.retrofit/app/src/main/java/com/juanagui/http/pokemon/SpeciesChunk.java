package com.juanagui.http.pokemon;

import java.net.URI;
import java.util.List;

public class SpeciesChunk {
    public int count;
    public URI next;
    public URI previous;
    public List<Species> results;
}