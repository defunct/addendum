package com.goodworkalan.addendum.jpa.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.cookbook.JavaProject;

/**
 * Build definition for Addendum JPA.
 *
 * @author Alan Gutierrez
 */
public class AddendumJpaProject implements ProjectModule {
    /**
     * Build the project definition for Addendum JPA.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.addendum/addendum-jpa/0.1")
                .depends()
                    .production("org.slf4j/slf4j-api/1.4.2")
                    .production("com.github.bigeasy.addendum/addendum/0.+7")
                    .production("org.hibernate/hibernate-core/3.3.1.GA")
                    .production("org.hibernate/hibernate-annotations/3.4.0.GA")
                    .development("org.slf4j/slf4j-log4j12/1.4.2")
                    .development("log4j/log4j/1.2.14")
                    .development("org.testng/testng-jdk15/5.10")
                    .development("org.mockito/mockito-core/1.6")
                    .development("com.h2database/h2/1.1.115")
                    .development("com.github.bigeasy.addendum/addendum-h2/0.+1")
                    .end()
                .end()
            .end();
    }
}
