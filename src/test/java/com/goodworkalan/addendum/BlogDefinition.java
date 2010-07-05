package com.goodworkalan.addendum;

import java.util.Date;

import com.goodworkalan.addendum.Addendum;
import com.goodworkalan.addendum.Definition;
import com.goodworkalan.addendum.GeneratorType;

/**
 * An example of an Addendum definition.
 *
 * @author Alan Gutierrez
 */
public class BlogDefinition implements Definition {
    /**
     * Create a definition of blog.
     * 
     * @param addendum
     *            The addendum.
     */
    public void define(Addendum addendum) {
        addendum
            .define("Post")
                .add("id", Long.class).generator(GeneratorType.AUTO).end()
                .add("created_at", Date.class).notNull().end()
                .add("body", String.class).end()
                .end()
            .define("Comment")
                .add("id", Long.class).generator(GeneratorType.AUTO).end()
                .add("post_id", Long.class).notNull().end()
                .add("created_at", Date.class).notNull().end()
                .add("body", String.class).end()
                .end();
    }
}
