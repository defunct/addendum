package com.goodworkalan.addendum.h2.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Build definition for Addendum H2.
 *
 * @author Alan Gutierrez
 */
public class AddendumH2Project implements ProjectModule {
    /**
     * Build the project definition for Addendum H2.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum-h2/0.1.0.1")
                .depends()
                    .production("com.github.bigeasy.addendum/addendum/0.+7.0.1")
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .development("com.h2database/h2/1.1.115")
                    .end()
                .end()
            .end();
    }
}
