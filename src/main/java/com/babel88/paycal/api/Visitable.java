package com.babel88.paycal.api;

import com.babel88.paycal.api.view.Visitor;

public interface Visitable {

    void accept(Visitor visitor);
}
