package com.android.flash.util;

/**
 * User: johnwright
 * Date: 4/15/12
 * Time: 1:30 AM
 *
 * Interface for different kinds of datasources in our sql lite db
 */
public interface DataSource {

    public void open();

    public void close();

    public <T> T create(T item);

    public void delete(int id);

    public <T> T get(int id);

    public <T> T getAll();
}
