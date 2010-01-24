package com.goodworkalan.addendum.mysql.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class AddendumMysqlProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces(new Artifact("com.goodworkalan/addendum-mysql/0.1"))
                .main()
                    .depends()
                        .artifact(new Artifact("org.slf4j/slf4j-api/1.4.2"))
                        .artifact(new Artifact("com.goodworkalan/addendum/0.7"))
                        .end()
                    .end()
                .test()
                    .depends()
                        .artifact(new Artifact("org.slf4j/slf4j-log4j12/1.4.2"))
                        .artifact(new Artifact("log4j/log4j/1.2.14"))
                        .artifact(new Artifact("org.testng/testng/5.10/jdk15"))
                        .artifact(new Artifact("org.mockito/mockito-core/1.6"))
                        .end()
                    .end()
                .end()
            .end();
    }
}