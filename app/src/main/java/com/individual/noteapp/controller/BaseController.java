package com.individual.noteapp.controller;

import com.orm.SugarRecord;

/**
 * Created by Blackout on 1/28/2017.
 */

public class BaseController<T extends SugarRecord> {

    public Long save(T t) {
        return T.save(t);
    }

    public Long update(T t) {
        return T.save(t);
    }

    public boolean delete(T t) {
        return T.delete(t);
    }

}
