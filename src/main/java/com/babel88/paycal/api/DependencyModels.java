package com.babel88.paycal.api;

/**
 * High level representation for dependency models
 */
public interface DependencyModels<T> {

    T getDependency(String dependencyName);
}
