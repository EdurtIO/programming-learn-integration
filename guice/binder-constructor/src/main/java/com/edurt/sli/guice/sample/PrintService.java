package com.edurt.sli.guice.sample;

import com.google.inject.ImplementedBy;

@ImplementedBy(PrintServiceImpl.class)
public interface PrintService {

    void print();

}
