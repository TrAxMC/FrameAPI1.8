package de.framedev.frameapi.queries;


public interface Callback<V extends Object, T extends Throwable> {

    public void call(V result, T thrown);

}
