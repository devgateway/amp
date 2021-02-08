package org.digijava.kernel.xmlpatches;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloseDrcActivities extends AbstractAmpActivityCloserPatch {

    static final Map<Integer, List<String>> AMP_IDS_TO_CLOSE = new HashMap<>();
    private static final Integer ZERO_INDEX = 0;
    private static final Integer ONE_INDEX = 1;
    private static final Integer TWO_INDEX = 2;
    private static final Integer THREE_INDEX = 3;
    private static final Integer FOUR_INDEX = 4;
    private static final Integer FIVE_INDEX = 5;
    private static final Integer SIX_INDEX = 6;
    private static final Integer SEVEN_INDEX = 7;
    private static final Integer EIGHT_INDEX = 8;
    private static final Integer NINE_INDEX = 9;
    private static final Integer TEN_INDEX = 10;
    private static final Integer ELEVEN_INDEX = 11;
    private static final Integer TWELVE_INDEX = 12;


    public static void run(Integer index) {
        new CloseDrcActivities().closeActivities(index);
    }

    static {
        AMP_IDS_TO_CLOSE.put(ZERO_INDEX, Arrays.asList("112009877600", "11200955218009", "11200955218016",
                "11200955217990",
                "11200955218069", "11200955218066", "11200955218076", "11200971152415", "11200955229819",
                "112009896129", "11200955229804", "11200955218077", "11200971152367", "1120098719847", "112009103822",
                "11200955217982", "11200955218080", "11200971152401", "1120098711819", "1120091254435",
                "11200955218078", "1120091095075", "1120091253788", "1120091254420", "11200971199196", "1120098735402",
                "11200955213835", "11200955213829", "11200912513991", "1120098735429", "11200955213903",
                "112009894966", "1120098711433", "11200988727", "1120098711446", "11200955213901", "11200955213895",
                "11200955213896", "11200955213899", "11200912513988", "11200955213806", "1120098710877",
                "11200955213857", "1120098711509", "1120098711426", "1120098711508", "1120098744355", "11200955213900",
                "1120098711440", "112009879203", "11200955213856", "11200912513918", "11200912513921",
                "1120098711499", "112009873454", "112009711104195", "1120098749337", "112009879204", "1120098710745",
                "1120098796363", "11200987106459", "1120098710838", "1120098990152", "1120098745224", "1120098990155",
                "1120098751956", "1120098711681", "1120098711706", "1120098711678", "1120098711476", "1120093033758",
                "1120093033751", "1120098711704", "1120098711629", "1120098711556", "1120098711699", "1120098711670",
                "1120098711701", "1120098711702", "1120098711703", "1120098712351", "112009891514", "1120098710376",
                "1120098749104", "1120098711972", "112009891511", "1120098711617", "1120098711573", "1120098711667",
                "1120098711696", "1120098711668", "1120098711625", "1120098711757", "11200976969", "11200988290",
                "112009879224", "1120098710374", "112009878594", "1120098754857", "112009873346"));
        AMP_IDS_TO_CLOSE.put(ONE_INDEX, Arrays.asList("1120098747262", "1120098711766", "1120098711897",
                "11200987102974",
                "1120098711705", "1120098711669", "1120098711698", "1120093033759", "1120098711726", "11200987102977",
                "1120098710425", "1120098713630", "1120098710424", "112009879623", "112009899414", "1120098710843",
                "112009899419", "1120098710407", "112009898520", "11200912513567", "11200955237019", "11200955234995",
                "11200912545175", "11200912545184", "11200912545186", "1120098730791", "1120098710387",
                "11200912513559", "1120098710379", "1120098967513", "1120098967517", "11200955267515",
                "11200958567542", "11200958567544", "11200958567545", "11200955267522", "11200955267529",
                "11200955267534", "1120098767538", "11200955267512", "11200955267520", "11200955267523",
                "11200955267526", "11200955236966", "11200955236968", "11200955234989", "11200976995", "1120098967514",
                "1120098710378", "1120094057932", "112009899405", "112009875583", "1120098710416", "1120098710417",
                "11200955235031", "11200912513555", "1120094057874", "11200912545182", "11200955236967",
                "11200958567527", "11200976969", "11200912513554", "112009899412", "1120098711911", "11200912513565",
                "11200912513561", "112009899424", "1120098710413", "1120098711917", "11200958567531", "11200912545173",
                "11200912513560", "1120098710433", "1120098710420", "11200912513563", "1120098730793",
                "1120098711922", "11200912513557", "11200912513550", "11200912513568", "11200955237021",
                "11200955235027", "11200955235029", "11200955237017", "11200955237016", "1120098745152",
                "11200912545172", "11200912545171", "11200912545185", "11200912545177", "11200912513556",
                "11200912545180", "11200912513553", "112009899433", "1120098710388", "11200912545170",
                "11200955235030", "11200955234986", "11200912513558", "11200955236969"));
        AMP_IDS_TO_CLOSE.put(TWO_INDEX, Arrays.asList("1120098710818", "11200955235025", "11200955235028",
                "11200955234969",
                "11200955234996", "11200955237015", "11200955234976", "1120094057893", "1120094057854",
                "11200955237022", "1120098967508", "1120098710423", "11200912513566", "11200912513551",
                "1120098710389", "1120098710819", "112009897924", "112009761300", "1120098710384", "1120098710394",
                "1120098767547", "11200958567507", "11200955267543", "11200955267557", "11200958567537",
                "112009894755", "112009878716", "112009iati44901", "1120098710386", "11200955267556", "1120098745157",
                "1120098711819", "1120091254398", "1120098710395", "1120098710391", "112009899409", "1120098710397",
                "1120098710403", "1120098710412", "1120098710415", "1120098710398", "1120098710409",
                "11200912512261", "1120098710380", "112009899425", "1120098742768", "11200955234940", "1120098715082",
                "1120098715085", "1120098714968", "11200955234935", "1120098715099", "11200955234934",
                "1120098742745", "1120098742750", "1120098715180", "1120098742753", "1120098742746", "1120098715102",
                "1120098715186", "11200955234941", "1120098715174", "1120098715193", "1120098715199", "1120098742764",
                "1120098715172", "1120098752899", "1120098752918", "1120098742736", "1120098742760", "1120098742757",
                "1120098742748", "1120098742729", "1120098742733", "1120098715081", "11200958552346",
                "1120091055474", "11200955234945", "1120098750678", "11200955252274", "1120098742734", "1120098715089",
                "11200958552340", "11200955252329", "1120098714926", "1120098714951", "11200955234923",
                "1120098715237", "11200955234943", "1120091055498", "11200958552373", "11200958552348",
                "11200955252333", "11200955252341", "11200958552379", "11200989754", "11200958567328",
                "11200958567317", "11200958567322", "11200958567326"));
        AMP_IDS_TO_CLOSE.put(THREE_INDEX, Arrays.asList("1120091055472", "11200958552331", "112009899213",
                "11200958552325",
                "1120091055322", "11200955252311", "11200955252297", "1120098715288", "1120098715084",
                "11200958552351", "1120098715088", "1120091251666", "11200955230784", "1120098715101", "1120098742765",
                "1120098715122", "1120098715116", "1120098715083", "1120098715135", "1120098715147", "1120091055275",
                "11200955230820", "1120098715286", "1120098715410", "1120098715217", "1120098750544",
                "1120098715221", "1120098715103", "11200958552318", "1120098715261", "1120098715094", "11200958567320",
                "11200958567318", "11200955252304", "11200955252337", "11200955252294", "1120098750741",
                "1120098715110", "11200955230788", "1120098715100", "11200958567324", "11200955234918",
                "11200958552224", "11200958552264", "11200958552253", "1120098711812", "11200958567325",
                "1120098711980", "1120098715509", "11200958552353", "11200958552369", "11200958552384",
                "1120091253753", "1120098735951", "112009873050", "112009761874", "1120098735952", "1120091052347",
                "112009iati24163", "112009iati24171", "112009iati24162", "112009iati24166", "112009iati24164",
                "112009iati24152", "112009iati24161", "112009iati24165", "112009iati24168", "112009iati24159",
                "1120098776235", "1120098776229", "1120098776237", "112009iati23969", "112009iati23981",
                "112009iati23998", "112009iati23975", "112009iati23982", "112009iati23978", "112009895566",
                "112009877802", "112009877815", "11200912519049", "1120098733420", "11200912519051", "11200912519040",
                "11200912519038", "11200912519027", "11200912519019", "11200912519046", "11200955246253",
                "1120098718781", "112009877801", "112009877800", "11200912518929", "1120098711894", "11200912519052",
                "11200912518939", "11200912519042", "11200912519034", "11200912519039", "112009877798"));
        AMP_IDS_TO_CLOSE.put(FOUR_INDEX, Arrays.asList("11200912519002", "112009873800", "11200912519043",
                "1120092788952",
                "11200976969", "11200987491", "11200987490", "11200987622", "112009761237", "11200987484",
                "112009761238", "11200987586", "1120091252334", "11200987569", "11200987598", "1120091252335",
                "11200987588", "1120091252342", "11200987624", "1120098711387", "11200987488", "1120098752636",
                "1120098711819", "1120091254322", "1120091051680", "1120098746238", "1120091252333", "1120091254326",
                "11200912512238", "1120091254330", "1120091252344", "11200987551", "11200987680", "11200987522",
                "11200955246261", "1120098767220", "112009761240", "1120091253736", "112009783104919",
                "112009783104920", "112009783104922", "1120098711514", "1120091252341", "1120091252339",
                "1120091252332", "1120091252337", "1120094057413", "1120091998587", "1120098711942", "112009103558",
                "112009896186", "1120098711916", "112009897482", "112009896211", "112009871351", "1120091253732",
                "112009895297", "112009895312", "112009895313", "112009896237", "112009896245", "112009896296",
                "112009896304", "112009896308", "112009896321", "112009896889", "112009896323", "112009896377",
                "112009896320", "112009896951", "112009897021", "112009896987", "112009897034", "112009896901",
                "112009897127", "112009897106", "112009897139", "112009897515", "1120098711960", "1120098711915",
                "112009897117", "112009896984", "112009896972", "112009896208", "112009896202", "112009896203",
                "112009896176", "112009896210", "112009896031", "112009896200", "112009896161", "112009871349",
                "112009897219", "112009896188", "112009897115", "112009896219", "112009896967", "112009897017",
                "112009897504", "112009896899"));
        AMP_IDS_TO_CLOSE.put(FIVE_INDEX, Arrays.asList("112009896163", "112009897033", "112009896033", "112009895539",
                "11200976980", "1120091253740", "112009895253", "112009897134", "112009897193", "112009896178",
                "11200958542544", "1120098744668", "1120098744871", "112009896295", "1120091253777", "112009895900",
                "112009896182", "1120098711346", "112009896990", "11200989754", "1120091055472", "11200987507",
                "112009896912", "11200976995", "11200958542546", "112009897054", "112009895212", "112009895223",
                "112009895221", "112009895316", "112009895431", "112009895440", "112009895465", "112009895512",
                "1120091253776", "112009896155", "112009896159", "112009896216", "112009896272", "112009896334",
                "112009896914", "112009897116", "112009897129", "112009897131", "1120098710881", "1120098711236",
                "112009897103", "112009897118", "1120091253741", "11200958542537", "112009895289", "112009896267",
                "112009896223", "112009896336", "112009897111", "112009896212", "112009896338", "112009896895",
                "112009897083", "112009897005", "112009897026", "112009896236", "112009897130", "112009879909",
                "112009896342", "112009896235", "112009896234", "112009896326", "112009896315", "112009896232",
                "112009896330", "112009897037", "112009897045", "112009895325", "112009896333", "112009896332",
                "1120098711950", "11200958542535", "1120098710376", "112009895294", "1120098710063", "112009896038",
                "112009895871", "1120091253775", "11200976984", "112009895144", "1120091042026", "112009895293",
                "112009895970", "112009895859", "112009896192", "1120098744861", "112009895854", "112009895940",
                "112009895231", "112009895260", "112009895332", "112009895336", "112009879914", "112009896325"));
        AMP_IDS_TO_CLOSE.put(SIX_INDEX, Arrays.asList("112009895254", "112009103857", "112009895217", "112009895251",
                "112009895256", "112009895265", "112009895309", "112009896275", "112009897121", "1120098711526",
                "112009897125", "1120098710850", "112009895209", "112009895222", "112009895235", "112009895239",
                "112009895246", "112009895247", "112009895295", "112009896172", "112009897112", "112009897521",
                "112009879915", "112009892429", "112009103922", "112009895178", "112009895346", "112009897221",
                "112009103860", "112009873335", "112009895207", "112009895240", "112009895282", "112009895350",
                "112009896256", "112009895241", "112009895326", "112009895342", "112009895317", "112009895347",
                "112009896231", "112009895285", "112009873336", "112009895264", "112009897018", "112009897007",
                "112009896184", "112009896194", "112009896204", "112009897510", "1120098752645", "112009896993",
                "112009895214", "112009896042", "112009896254", "112009896037", "112009896036", "112009895969",
                "112009895898", "112009895852", "112009896310", "112009895249", "112009897508", "112009103851",
                "112009897498", "112009897502", "112009897140", "112009895306", "1120098736845", "112009895934",
                "112009895965", "1120098752596", "112009895967", "112009895933", "112009896224", "112009896276",
                "112009895365", "112009895362", "1120098710849", "112009895868", "112009895887", "112009895931",
                "112009895943", "112009895954", "112009895962", "112009896029", "112009895508", "112009896010",
                "112009895910", "112009896028", "112009105950", "112009895244", "112009895257", "112009895284",
                "112009895339", "112009896026", "112009897030", "112009895286", "11200912576171", "112009896242"));
        AMP_IDS_TO_CLOSE.put(SEVEN_INDEX, Arrays.asList("112009103537", "112009895902", "112009895983", "112009897512",
                "112009896243", "112009895250", "112009895945", "112009895955", "112009896314", "112009895299",
                "11200989758", "11200987966", "112009897525", "112009761026", "112009895416", "1120091042801",
                "112009879564", "112009879191", "112009896337", "112009896331", "112009896190", "112009897143",
                "112009897119", "112009895460", "112009895442", "112009895315", "112009873789", "1120091253728",
                "112009895262", "112009896220", "112009896952", "112009895213", "112009895345", "112009895495",
                "112009895470", "112009895479", "112009895494", "112009895499", "112009895557", "112009895524",
                "112009895528", "112009895531", "112009895911", "112009896154", "112009896156", "112009896238",
                "1120098711953", "112009897057", "112009896303", "1120091557128", "112009896215", "112009896322",
                "112009896892", "112009896910", "112009896911", "112009897122", "112009897133", "112009882586",
                "11200987501", "1120098711152", "112009872773", "1120098711250", "11200987511", "11200987504",
                "11200989763", "11200987538", "112009872760", "112009882585", "112009871809", "1120098710584",
                "112009891787", "112009103863", "112009882588", "112009103859", "112009879930", "112009103854",
                "1120091291526", "112009761300", "11200987470", "1120091051682", "11200958542570", "112009893426",
                "112009882576", "112009882595", "1120098711819", "112009895219", "112009895234", "112009896239",
                "112009896244", "112009896273", "112009896300", "112009897040", "112009897110", "112009897136",
                "1120094057364", "112009897511", "112009897514", "112009897517", "112009897520", "1120098711381"));
        AMP_IDS_TO_CLOSE.put(EIGHT_INDEX, Arrays.asList("112009103531", "112009103544", "11200987572", "112009895228",
                "112009896257", "112009896274", "112009896341", "112009897049", "112009897142", "112009897493",
                "112009897036", "112009897015", "112009897013", "112009896992", "112009896989", "112009896207",
                "112009896233", "112009897104", "112009896307", "112009896318", "112009896327", "112009897023",
                "11200987413", "11200958542644", "112009879933", "11200958542665", "1120091051806", "11200958542668",
                "112009879910", "1120091051807", "1120098711936", "11200958542669", "11200958542587", "11200958542650",
                "11200958542608", "11200958542651", "11200958542642", "1120091051679", "11200958542643",
                "11200958542654", "1120091051793", "1120091051796", "11200958542620", "1120091051790",
                "11200958542641", "1120098743376", "1120091252395", "1120098711264", "11200958542598",
                "11200958542637", "11200958542607", "1120098743366", "1120098747176", "11200989760", "11200958542658",
                "11200958542670", "1120091252401", "1120091997824", "1120091051768", "11200958542640",
                "11200958542616", "11200958542636", "11200958542573", "112009879900", "11200958542632", "112009879929",
                "112009879895", "112009879932", "11200958542550", "112009872747", "11200958542639", "11200958542633",
                "112009879896", "11200958542653", "1120098743350", "112009879934", "112009896180", "11200958542630",
                "11200958542631", "11200958542575", "112009896964", "112009761149", "112009897052", "112009895892",
                "1120091253736", "112009896020", "112009895292", "112009896222", "112009897028", "112009895862",
                "112009895866", "112009895964", "112009895959", "112009896973", "112009897027", "112009895894",
                "112009896001", "112009896005", "112009895861", "112009895853"));
        AMP_IDS_TO_CLOSE.put(NINE_INDEX, Arrays.asList("112009895255", "112009895937", "112009895990", "112009895944",
                "112009895989", "112009896003", "112009891811", "112009873328", "1120098743367", "112009895540",
                "112009895545", "112009895496", "112009896971", "112009896974", "112009897016", "112009897029",
                "112009896961", "112009895529", "112009895889", "112009895466", "1120098710847", "112009896240",
                "11200958542635", "112009896277", "112009103847", "11200989764", "1120091052347", "112009895224",
                "112009895229", "112009895174", "112009895218", "112009895266", "112009895226", "112009895261",
                "112009895215", "112009895258", "112009895283", "112009895216", "112009895510", "112009895310",
                "112009896241", "112009896301", "112009896897", "112009897105", "112009897526", "112009896963",
                "112009896206", "112009896205", "112009896982", "112009896032", "112009897478", "11200958542543",
                "112009895530", "112009895433", "112009896226", "112009895435", "112009897107", "112009897039",
                "112009896030", "112009895333", "112009879499", "112009896221", "11200989769", "112009897024",
                "112009897010", "112009896956", "112009895883", "112009895950", "112009896000", "112009895513",
                "112009896006", "112009896016", "112009895908", "112009896014", "112009896018", "112009896021",
                "112009896027", "112009896983", "112009896976", "112009896985", "112009103535", "112009895429",
                "112009895477", "112009895481", "112009895504", "112009896209", "112009895468", "112009895553",
                "112009897137", "112009895511", "11200987617", "112009897102", "112009103526", "112009896913",
                "112009897109", "112009897020", "112009897006", "112009896975", "112009896969", "112009896959"));
        AMP_IDS_TO_CLOSE.put(TEN_INDEX, Arrays.asList("112009897141", "112009896970", "112009871354", "1120098743338",
                "11200958542623", "11200958542655", "112009879543", "112009896986", "112009896012", "112009895935",
                "112009895939", "112009895941", "112009895942", "112009896977", "112009895526", "112009895351",
                "1120098711252", "11200958542584", "112009896319", "11200958542541", "11200958542538",
                "11200958542671", "11200958542580", "11200958542576", "1120098710745", "1120098711531",
                "1120098717469", "1120098743383", "1120098743505", "1120098743513", "1120098743514", "1120098743515",
                "1120098743516", "1120098743518", "1120098743519", "112009893724", "1120098745946", "11200912517458",
                "11200912517444", "11200912517439", "11200912517448", "1120098743520", "11200912517446",
                "1120098743384", "112009893718", "1120091256166", "112009891684", "112009893737", "1120098711819",
                "1120091253788", "11200912517437", "11200912517451", "112009iati23309", "112009iati23330",
                "112009iati23398", "1120091253788", "112009895690", "1120098738532", "11200955235619",
                "112009741106397", "112009741106393", "112009741106392", "112009741106391", "112009741106396",
                "112009741106394", "112009741106387", "112009741106395", "1120098781015", "1120098951975",
                "1120098951971", "1120091052515", "1120098951972", "1120098951976", "1120098951970", "1120098711819",
                "112009897052", "112009873744", "1120091253788", "11200987104627", "11200987104632", "11200987104642",
                "11200987104666", "11200987104654", "11200987104656", "11200987104655", "11200987104657",
                "11200987105260", "1120098951974", "1120091252856", "1120098711378", "11200987104659",
                "11200987104658", "1120091052389", "11200987104599", "11200987104612", "11200987104593",
                "11200987104614", "11200987104652", "1120094038980", "1120098710815"));
        AMP_IDS_TO_CLOSE.put(ELEVEN_INDEX, Arrays.asList("1120094038734", "1120098711496", "11200955247748",
                "11200955247746",
                "1120098734043", "11200989701", "1120098747659", "1120098744599", "11200987339", "1120098710709",
                "112009878829", "112009878822", "112009878826", "112009878799", "112009878832", "112009878828",
                "112009878819", "112009878797", "1120098711657", "112009761085", "112009881095", "112009878827",
                "112009883313", "112009883320", "112009878825", "112009878834", "112009878802", "1120098711236",
                "1120098710703", "1120091051115", "1120091051109", "1120091051206", "1120091051133", "1120091051198",
                "1120091051209", "112009881116", "112009881128", "112009881114", "1120091051125", "1120091051243",
                "1120091051202", "1120091051244", "1120091051208", "1120091051207", "1120091051205", "1120098711233",
                "1120098711944", "1120091051179", "112009761090", "112009881170", "112009881094", "1120091051196",
                "112009881110", "112009881108", "112009881166", "112009881131", "112009881174", "112009881171",
                "112009881178", "112009761093", "112009881132", "11200955233794", "11200955233526", "11200955233680",
                "11200955233662", "11200955233533", "1120091051130", "1120091051204", "112009873808", "1120091093821",
                "112009878791", "112009878833", "1120098711987", "112009761280", "1120091093826", "1120091252366",
                "112009881144", "112009873807", "112009881152", "112009873814", "112009881186", "112009878882",
                "112009881173", "1120091051118", "112009761149", "112009881188", "1120091051164", "1120091051182",
                "112009881180", "112009881183", "1120091051154", "112009881187", "112009881185", "112009881150",
                "112009881184", "112009873809", "112009873811", "1120091051192", "11200973497251", "112009883309"));
        AMP_IDS_TO_CLOSE.put(TWELVE_INDEX, Arrays.asList("112009873306", "112009873816", "1120098739040",
                "11200955233522",
                "11200955233535", "11200955233765", "11200955233541", "1120098782384", "11200987103155",
                "112009878804", "11200987103270", "11200987105026", "11200987103153", "11200987103154",
                "1120098794880", "1120098782394", "11200987103156", "1120098710747", "112009883311", "112009883308",
                "11200912533606", "112009881177", "112009873304", "1120098751008", "1120091051197", "1120091254535",
                "112009894968", "1120091998600", "11200971184018", "1120098745800", "112009103583", "112009103819",
                "11200955217986", "1120098798976", "112009873356", "11200989160", "112009878554", "1120098711843",
                "112009895027", "11200973496706", "112009873408"));
    }

    @Override
    public List<String> getAmpIds(Integer index) {
        return AMP_IDS_TO_CLOSE.get(index);
    }
}