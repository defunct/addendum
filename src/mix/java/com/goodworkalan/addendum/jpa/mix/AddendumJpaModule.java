package com.goodworkalan.addendum.jpa.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class AddendumJpaModule extends BasicJavaModule {
    public AddendumJpaModule() {
        super(new Artifact("com.goodworkalan", "addendum-jpa", "0.1"));
        addDependency(new Artifact("org.slf4j", "slf4j-api", "1.4.2"));
        addDependency(new Artifact("com.goodworkalan", "addendum", "0.7"));
        addDependency(new Artifact("com.goodworkalan", "prattle", "0.1"));
        addDependency(new Artifact("org.hibernate", "hibernate-core", "3.3.1.GA"));
        addDependency(new Artifact("org.hibernate", "hibernate-annotations", "3.4.0.GA"));
        addTestDependency(new Artifact("org.slf4j", "slf4j-log4j12", "1.4.2"));
        addTestDependency(new Artifact("log4j", "log4j", "1.2.14"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addTestDependency(new Artifact("org.mockito", "mockito-core", "1.6"));
        addTestDependency(new Artifact("com.goodworkalan", "addendum-h2", "0.1"));
        addTestDependency(new Artifact("com.h2database", "h2", "1.1.115"));
    }
}
