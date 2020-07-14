package com.edurt.sli.guice.sample;

import com.google.inject.Guice;
import com.google.inject.Inject;
import lombok.Data;

@Data
public class ApplicationMultiple {

    private Service service;
    private PrintService printService;

    @Inject
    public ApplicationMultiple(Service service, PrintService printService) {
        this.service = service;
        this.printService = printService;
    }

    public static void main(String[] args) {
        ApplicationMultiple multiple = Guice.createInjector().getInstance(ApplicationMultiple.class);
        multiple.getPrintService().print();
        multiple.getService().print("Multiple");
    }

}
