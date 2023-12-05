package org.dgfoundation.amp.nireports.testcases.tanzania.columns;

import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ModeOfPaymentCells extends HardcodedCells<TextCell>{

    public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, NiDimension dim, String key) {
        super(activityNames, entityNames, degenerate(dim, key));
    }
    public ModeOfPaymentCells(Map<String, Long> activityNames, Map<String, Long> entityNames, LevelColumn lc) {
        super(activityNames, entityNames, lc);
    }

    @Override
    protected List<TextCell> populateCells() {
        return  Arrays.asList(
        cell("01322528", "Tabora MC", 370),
        cell("1381868f", "Urban West- Unguja", 385),
        cell("1cc98366", "Morogoro DC", 326),
        cell("2375f4d1", "Songea MC", 353),
        cell("23b656f5", "Ngara DC", 286),
        cell("28cac123", "Dar es Salaam CC", 259),
        cell("3888c6c8", "Dar es Salaam CC", 259),
        cell("3e96151e", "Kibaha DC", 253),
        cell("42a2c699", "Dar es Salaam CC", 259),
        cell("4d3653be", "Geita DC", 336),
        cell("4d6f2de9", "Ininga MC", 271),
        cell("516b6cb4", "Dar es Salaam CC", 259),
        cell("59571deb", "Dar es Salaam CC", 259),
        cell("5c759ac0", "Same DC", 296),
        cell("6c11ef27", "Kilwa DC", 298),
        cell("6eed25d1", "Dar es Salaam CC", 259),
        cell("6ffc9e43", "Bagamoyo DC", 252),
        cell("767b496a", "Babati DC", 304),
        cell("8e97b09e", "Dar es Salaam CC", 259),
        cell("92738a17", "Dar es Salaam CC", 259),
        cell("92c54c87", "Dar es Salaam CC", 259),
        cell("95576da6", "Shinyanga MC", 362),
        cell("98fd3250", "Dar es Salaam CC", 259),
        cell("a0854c58", "Sumbawanga DC", 347),
        cell("a6376c49", "Dar es Salaam CC", 259),
        cell("a753b4f5", "Dar es Salaam CC", 259),
        cell("b37e471b", "Kilombero DC", 324),
        cell("b3c33d53", "Iringa DC", 270),
        cell("b3c33d53", "Iringa RAS", 272),
        cell("b3c33d53", "Makete DC", 275),
        cell("b3c33d53", "Mbarali DC", 319),
        cell("b3c33d53", "Mbeya DC", 321),
        cell("b3c33d53", "Mufindi DC", 276),
        cell("b3c33d53", "Njombe DC", 277),
        cell("b3c33d53", "Njombe RAS", 343),
        cell("b3c33d53", "Temeke MC", 262),
        cell("b59bae58", "Dar es Salaam CC", 259),
        cell("bb4dce50", "Bagamoyo DC", 252),
        cell("bb4dce50", "Hai DC", 291),
        cell("bb4dce50", "Iringa DC", 270),
        cell("bb4dce50", "Kasulu DC", 287),
        cell("bb4dce50", "Magu DC", 338),
        cell("bb4dce50", "Mbarali DC", 319),
        cell("bb4dce50", "Mbeya DC", 321),
        cell("bb4dce50", "Mtwara DC", 331),
        cell("bb4dce50", "Mufindi DC", 276),
        cell("bb4dce50", "Njombe DC", 277),
        cell("bb4dce50", "Siha DC", 297),
        cell("bce9728e", "Babati DC", 304),
        cell("bce9728e", "Chamwino DC", 264),
        cell("bce9728e", "Iramba DC", 363),
        cell("bce9728e", "Karatu DC", 247),
        cell("bce9728e", "Kishapu DC", 358),
        cell("bce9728e", "Kitelo DC", 307),
        cell("bce9728e", "Monduli DC", 250),
        cell("bce9728e", "Ngorongoro DC", 251),
        cell("bce9728e", "Simanjiro DC", 309),
        cell("bce9728e", "Singida DC", 365),
        cell("c61de978", "Dodoma MC", 266),
        cell("c721e0a2", "Dar es Salaam CC", 259),
        cell("c97979d7", "Direct Payment", 140),
        cell("cc93f9be", "Kasulu DC", 287),
        cell("db53358d", "Dar es Salaam CC", 259),
        cell("ddef5bca", "North Pemba", 381),
        cell("eacb826a", "Bagamoyo DC", 252),
        cell("eb4ca178", "Dar es Salaam CC", 259),
        cell("ec228d38", "Singida MC", 366),
        cell("efc2711e", "Dar es Salaam CC", 259));
    }

}
