package com.goodworkalan.addendum.jpa;

import com.goodworkalan.cafe.ProjectModule;
import com.goodworkalan.cafe.builder.Builder;
import com.goodworkalan.cafe.outline.JavaProject;

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
                    .production("org.hibernate/hibernate-core/3.5.2-Final")
                    .production("org.hibernate/hibernate-annotations/3.5.2-Final")
                    .production("org.hibernate/hibernate-entitymanager/3.5.2-Final")
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
