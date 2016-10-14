package com.morphiles.game;

import com.morphiles.export.ExcelExporter;
import junit.framework.TestCase;
import org.junit.Ignore;

//TODO tests not currently implemented here.
@Ignore
public class ExcelExporterTest extends TestCase{

    String[] hands = {
            "HIGH CARD",
            "gutshot",
            "straightDraw",
            "flushDraw",
            "flushDraw +gutshot",
            "flushDraw +straightDraw",
            "PAIR",
            "TWO PAIR",
            "SET",
            "STRAIGHT",
            "FLUSH",
            "FULL HOUSE",
            "QUADS",
            "STRAIGHT FLUSH",
            "ROYAL FLUSH"
    };

    int i = 0;
    int j = 0;

    ExcelExporter exporter;

//    @Before
//    public void setUp(){
//        exporter = new ExcelExporter();
//    }

}
