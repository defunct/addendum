package com.goodworkalan.go.go.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class AddendumH2Module extends BasicJavaModule {
    public AddendumH2Module() {
        super(new Artifact("com.goodworkalan", "addendum-h2", "0.1"));
        addDependency(new Artifact("org.slf4j", "slf4j-api", "1.4.2"));
        addDependency(new Artifact("com.goodworkalan", "addendum", "0.7"));
        addDependency(new Artifact("com.goodworkalan", "prattle", "0.1"));
        addTestDependency(new Artifact("org.slf4j", "slf4j-log4j12", "1.4.2"));
        addTestDependency(new Artifact("log4j", "log4j", "1.2.14"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addTestDependency(new Artifact("org.mockito", "mockito-core", "1.6"));
        addTestDependency(new Artifact("com.h2database", "h2", "1.1.115"));
    }
}
