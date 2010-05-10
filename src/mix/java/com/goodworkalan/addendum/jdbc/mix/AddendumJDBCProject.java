package com.goodworkalan.addendum.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Build definition for Addendum JDBC.
 *
 * @author Alan Gutierrez
 */
public class AddendumJDBCProject extends ProjectModule {
    /**
     * Build the project definition for Addendum JDBC.
     *
     * @param builder. The project builder.
     */
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum-jdbc/0.1")
                .main()
                    .depends()
                        .include("com.github.bigeasy.addendum/addendum/0.+7")
                        .end()
                    .end()
                .test()
                    .depends()
                        .include("com.h2database/h2/1.1.115")
                        .include("org.slf4j/slf4j-log4j12/1.4.2")
                        .include("log4j/log4j/1.2.14")
                        .include("org.testng/testng-jdk15/5.10")
                        .include("org.mockito/mockito-core/1.6")
                        .end()
                    .end()
                .end()
            .end();
    }
}
