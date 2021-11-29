package com.juanagui.http.pokemon;

import java.net.URI;

public class Species {
    public String name;
    public URI url;

    @Override
    public String toString() {
        return "Species{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }
}