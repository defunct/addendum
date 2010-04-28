package com.goodworkalan.addendum.api;

import java.util.Date;

import com.goodworkalan.addendum.Addendum;
import com.goodworkalan.addendum.Definition;
import com.goodworkalan.addendum.GeneratorType;

public class BlogDefinition implements Definition {
    public void define(Addendum addendum) {
        addendum
            .table("Post")
                .column("id", Long.class).generator(GeneratorType.AUTO).end()
                .column("created_at", Date.class).notNull().end()
                .column("body", String.class).end()
                .end()
            .table("Comment")
                .column("id", Long.class).generator(GeneratorType.AUTO).end()
                .column("post_id", Long.class).notNull().end()
                .column("created_at", Date.class).notNull().end()
                .column("body", String.class).end()
                .end();
    }
}
