package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FundingStatusCells extends HardcodedCells<TextCell>{

    public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public FundingStatusCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
        cell("01322528", "Tabora", 241),
        cell("1381868f", "zanzibar", 243),
        cell("1cc98366", "Morogoro", 232),
        cell("2375f4d1", "Ruvama", 238),
        cell("23b656f5", "Kagera", 225),
        cell("27780646", "Dar-es-Salaam", 222),
        cell("279d5404", "Lindi", 228),
        cell("28cac123", "Dar-es-Salaam", 222),
        cell("3421adeb", "MWANZA", 234),
        cell("3888c6c8", "Dar-es-Salaam", 222),
        cell("39cc0b73", "Arusha", 220),
        cell("3e96151e", "Coast", 221),
        cell("3f4ed460", "Dodoma", 223),
        cell("425edc13", "zanzibar", 243),
        cell("42a2c699", "Dar-es-Salaam", 222),
        cell("44e14911", "Tabora", 241),
        cell("48c5cbbf", "Dar-es-Salaam", 222),
        cell("4d3653be", "MWANZA", 234),
        cell("4d6f2de9", "Iringa", 224),
        cell("516b6cb4", "Dar-es-Salaam", 222),
        cell("53241daa", "zanzibar", 243),
        cell("59571deb", "Dar-es-Salaam", 222),
        cell("5ad0205a", "zanzibar", 243),
        cell("5c759ac0", "Kilimanjaro", 227),
        cell("5ff06c43", "zanzibar", 243),
        cell("616cb31b", "zanzibar", 243),
        cell("67ce4429", "zanzibar", 243),
        cell("6bdf8bf7", "zanzibar", 243),
        cell("6c11ef27", "Lindi", 228),
        cell("6c1b25c9", "Tabora", 241),
        cell("6d5cdd19", "zanzibar", 243),
        cell("6eed25d1", "Dar-es-Salaam", 222),
        cell("6ffc9e43", "Pwani", 236),
        cell("73ceb46b", "Mbeya", 231),
        cell("74864ab4", "zanzibar", 243),
        cell("767b496a", "Manyara", 229),
        cell("7a96827b", "Shinyanga", 239),
        cell("7ab01e58", "Dar-es-Salaam", 222),
        cell("7ed552ba", "Morogoro", 232),
        cell("83a8a57d", "Shinyanga", 239),
        cell("8b8f3147", "zanzibar", 243),
        cell("8e97b09e", "Dar-es-Salaam", 222),
        cell("919a293c", "zanzibar", 243),
        cell("92738a17", "Dar-es-Salaam", 222),
        cell("92c54c87", "Dar-es-Salaam", 222),
        cell("9323d7cf", "zanzibar", 243),
        cell("95576da6", "Shinyanga", 239),
        cell("98fd3250", "Dar-es-Salaam", 222),
        cell("9a3d6d7c", "Shinyanga", 239),
        cell("a0854c58", "Rukwa", 237),
        cell("a49b6be7", "zanzibar", 243),
        cell("a6376c49", "Dar-es-Salaam", 222),
        cell("a753b4f5", "Dar-es-Salaam", 222),
        cell("b37e471b", "Morogoro", 232),
        cell("b3c33d53", "Dar-es-Salaam", 222),
        cell("b3c33d53", "Iringa", 224),
        cell("b3c33d53", "Mbeya", 231),
        cell("b3c33d53", "Njombe Region", 235),
        cell("b59bae58", "Dar-es-Salaam", 222),
        cell("bb4dce50", "Coast", 221),
        cell("bb4dce50", "Iringa", 224),
        cell("bb4dce50", "Kigoma", 226),
        cell("bb4dce50", "Kilimanjaro", 227),
        cell("bb4dce50", "Mbeya", 231),
        cell("bb4dce50", "mtwara", 233),
        cell("bb4dce50", "MWANZA", 234),
        cell("bb4dce50", "Njombe Region", 235),
        cell("bce9728e", "Arusha", 220),
        cell("bce9728e", "Dodoma", 223),
        cell("bce9728e", "Manyara", 229),
        cell("bce9728e", "Shinyanga", 239),
        cell("bce9728e", "Singida", 240),
        cell("c070eb70", "zanzibar", 243),
        cell("c61de978", "Dodoma", 223),
        cell("c721e0a2", "Dar-es-Salaam", 222),
        cell("cc93f9be", "Kigoma", 226),
        cell("ce5e1126", "zanzibar", 243),
        cell("db355dd1", "zanzibar", 243),
        cell("db53358d", "Dar-es-Salaam", 222),
        cell("dd297bd6", "zanzibar", 243),
        cell("ddef5bca", "zanzibar", 243),
        cell("e97367c9", "zanzibar", 243),
        cell("eacb826a", "Coast", 221),
        cell("eb3e294d", "zanzibar", 243),
        cell("eb4ca178", "Dar-es-Salaam", 222),
        cell("ec228d38", "Singida", 240),
        cell("efc2711e", "Dar-es-Salaam", 222),
        cell("f11e7d3e", "zanzibar", 243),
        cell("fca98d7a", "zanzibar", 243),
        cell("fdbb15ea", "zanzibar", 243));
    }

}
