package com.goodworkalan.addendum.h2.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class AddendumH2Project extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.goodworkalan/addendum-h2/0.1")
                .main()
                    .depends()
                        .include("org.slf4j/slf4j-api/1.4.2")
                        .include("com.goodworkalan/addendum/0.7")
                        .end()
                    .end()
                .test()
                    .depends()
                        .include("org.slf4j/slf4j-log4j12/1.4.2")
                        .include("log4j/log4j/1.2.14")
                        .include("org.testng/testng-jdk15/5.10")
                        .include("org.mockito/mockito-core/1.6")
                        .include("com.h2database/h2/1.1.115")
                        .end()
                    .end()
                .end()
            .end();
    }
}
