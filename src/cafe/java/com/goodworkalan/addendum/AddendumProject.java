package com.goodworkalan.addendum;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Builds the project definition for Addendum.
 *
 * @author Alan Gutierrez
 */
public class AddendumProject implements ProjectModule {
    /**
     * Build the project definition for Addendum.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum/0.7.0.6")
                .depends()
                    .production("com.github.bigeasy.danger/danger/0.+1")
                    .production("com.github.bigeasy.furnish/furnish/0.+1")
                    .production("com.github.bigeasy.class/class-boxer/0.+1")
                    .production("com.github.bigeasy.notice/notice/0.+1")
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .development("com.github.bigeasy.danger/danger-test/0.+1")
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
