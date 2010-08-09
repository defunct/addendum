package com.goodworkalan.addendum.jdbc;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

/**
 * Build definition for Addendum JDBC.
 *
 * @author Alan Gutierrez
 */
public class AddendumJDBCProject implements ProjectModule {
    /**
     * Build the project definition for Addendum JDBC.
     *
     * @param builder The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum-jdbc/0.1")
                .depends()
                    .production("com.github.bigeasy.addendum/addendum/0.+7")
                    .development("mysql/mysql-connector-java/5.1.10")
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
