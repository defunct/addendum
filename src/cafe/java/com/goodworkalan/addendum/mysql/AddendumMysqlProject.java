package com.goodworkalan.addendum.mysql;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Build definition for Addendum MySQL.
 *
 * @author Alan Gutierrez
 */
public class AddendumMysqlProject implements ProjectModule {
    /**
     * Build the project definition for Addendum MySQL.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum-mysql/0.1.0.1")
                .depends()
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .production("com.github.bigeasy.addendum/addendum/0.+7")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .end()
                .end()
            .end();
    }
}
