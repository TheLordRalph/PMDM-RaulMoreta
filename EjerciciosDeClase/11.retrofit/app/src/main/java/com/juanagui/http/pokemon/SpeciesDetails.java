package com.juanagui.http.pokemon;

import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.util.List;

public class SpeciesDetails {

    public class FlavorTextEntry {

        public class Language {
            public String name;
            public URI url;
        }

        public class Version {
            public String name;
            public URI url;
        }

        //@SerializedName("flavor_text") commented coz we're setting the proper FieldNamingPolicy in the builder
        public String flavorText;
        public Language language;
        public Version version;
    }

    public String name;
    //@SerializedName("flavor_text_entries") commented coz we're setting the proper FieldNamingPolicy in the builder
    public List<FlavorTextEntry> flavorTextEntries;
}