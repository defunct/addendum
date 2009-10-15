package com.goodworkalan.addendum.mix;

import com.goodworkalan.go.go.Artifact;
import com.goodworkalan.mix.BasicJavaModule;

public class AddendumModule extends BasicJavaModule {
    public AddendumModule() {
        super(new Artifact("com.goodworkalan", "addendum", "0.7"));
        addDependency(new Artifact("org.slf4j", "slf4j-api", "1.4.2"));
        addDependency(new Artifact("com.goodworkalan", "furnish", "0.1"));
        addDependency(new Artifact("com.goodworkalan", "prattle", "0.1"));
        addTestDependency(new Artifact("org.slf4j", "slf4j-log4j12", "1.4.2"));
        addTestDependency(new Artifact("log4j", "log4j", "1.2.14"));
        addTestDependency(new Artifact("org.testng", "testng", "5.10"));
        addTestDependency(new Artifact("org.mockito", "mockito-core", "1.6"));
    }
}
