package com.goodworkalan.addendum.api;

import java.util.Date;

import com.goodworkalan.addendum.Addendum;
import com.goodworkalan.addendum.Definition;
import com.goodworkalan.addendum.GeneratorType;

public class BlogDefinition implements Definition {
    public void define(Addendum addendum) {
        addendum
            .table("Post")
                .add("id", Long.class).generator(GeneratorType.AUTO).end()
                .add("created_at", Date.class).notNull().end()
                .add("body", String.class).end()
                .end()
            .table("Comment")
                .add("id", Long.class).generator(GeneratorType.AUTO).end()
                .add("post_id", Long.class).notNull().end()
                .add("created_at", Date.class).notNull().end()
                .add("body", String.class).end()
                .end();
    }
}
