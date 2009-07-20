package com.goodworkalan.addendum;

public interface Extend extends Execute
{
    public <T extends ExtensionElement> T run(T extension);
}
