package com.gevernova.onlinecourses.model;
public class Module {
    private final String moduleId;
    private final String name;
    private final int weight; // percentage weight of module

    public Module(String moduleId, String name, int weight) {
        this.moduleId = moduleId;
        this.name = name;
        this.weight = weight;
    }

    public String getModuleId() { return moduleId; }
    public String getName() { return name; }
    public int getWeight() { return weight; }
}

