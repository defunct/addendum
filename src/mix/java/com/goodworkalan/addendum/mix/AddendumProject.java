package com.goodworkalan.addendum.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Build definition for Addendum.
 *
 * @author Alan Gutierrez
 */
public class AddendumProject extends ProjectModule {
    /**
     * Build the project definition for Addendum.
     *
     * @param builder
     *          The project builder.
     */
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum/0.7.0.1")
                .depends()
                    .production("com.github.bigeasy.danger/danger/0.+1")
                    .production("com.github.bigeasy.furnish/furnish/0.+1")
                    .production("com.github.bigeasy.class-boxer/class-boxer/0.+1")
                    .production("com.github.bigeasy.notice/notice/0.+1")
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .development("com.h2database/h2/1.1.115")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
