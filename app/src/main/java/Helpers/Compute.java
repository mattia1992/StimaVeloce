package Helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Compute {
    private Context application_context;
    private ArrayList<float[]> abete_tariffe = new ArrayList<>();
    private float[] abete_cubature = new float[]{0.000177367991170896f, 1.56425370572013f, 1.05156473615877f, 3.69465f};
    private ArrayList<float[]> larice_tariffe = new ArrayList<>();
    private float[] larice_cubature = new float[]{0.000107820129127088f, 1.40775581651764f, 1.34137722851875f, 3.69465f};
    private ArrayList<float[]> faggio_tariffe = new ArrayList<>();
    private float[] faggio_cubature = new float[]{0.0000552712344957134f, 1.94208862027426f, 1.00642023166998f, 4.0091f};
    private ArrayList<float[]> abeteb_tariffe = new ArrayList<>();
    private float[] abeteb_cubature = new float[]{0.000162898357199892f, 1.70656012564636f, 0.94190457472194f, 3.69465f};
    private ArrayList<float[]> pinos_tariffe = new ArrayList<>();
    private float[] pinos_cubature = new float[]{0.000101825735269425f, 1.91818421016015f, 0.830164143958094f, 3.69465f};
    private ArrayList<float[]> pinoc_tariffe = new ArrayList<>();
    private float[] pinoc_cubature = new float[]{0.000188167619876239f, 1.61371288034635f, 0.985265642143746f, 3.69465f};
    private float[] pinon_cubature = new float[]{0.000128924310780902f, 1.76308589457555f, 0.938444909041497f, 3.69465f};

    public enum specie {Larice, Abeter, Abeteb, Faggio, Pinos, Pinoc, Pinon}
    public Compute (Context context){
        application_context=context;
        abete_tariffe.add(new float[]{1.322019115354600f, 0.000193168589137997f, -0.000561338718330220f,
                0.00000848633014924548f, -0.0000000385414285314989f});
        abete_tariffe.add(new float[]{1.253310110922040f, -0.00292803946147456f, -0.000394818245994073f,
                0.00000597851328281470f, -0.0000000262939152563139f});
        abete_tariffe.add(new float[]{ 1.158747653357300f, -0.00254770232488503f, -0.000359810517955313f,
                0.00000538069375586631f, -0.0000000234121022021542f});
        abete_tariffe.add(new float[]{ 1.006250931121290f,  0.00371564927208322f, -0.000534673717423504f,
                0.00000767184701944932f, -0.0000000341372675023622f});
        abete_tariffe.add(new float[]{  0.891588780473929f,  0.00395174405336071f, -0.000490030434657358f,
                0.00000700514808560604f, -0.0000000313010125027530f});
        abete_tariffe.add(new float[]{  0.735212679007051f,  0.00703101464440913f, -0.000520550769841175f,
                0.00000714533676766523f, -0.0000000314963735713988f});
        abete_tariffe.add(new float[]{  0.602485636412812f,  0.00938982360030814f, -0.000586119461476346f,
                0.00000827700782041379f, -0.0000000378814404686078f});
        abete_tariffe.add(new float[]{   0.508762602308169f,   0.00735072463485610f, -0.000457659356475586f,
                0.00000637306880966114f, -0.0000000288493693629086f});
        abete_tariffe.add(new float[]{    0.408553854843333f,   0.00729926112445350f, -0.000426086684775741f,
                0.00000596624159181636f, -0.0000000272269588507870f});

        larice_tariffe.add(new float[]{1.31732068351736f, -0.0277429626582718f, 0.000466727240901007f,
                -0.00000476803918354418f, 0.0000000195253819975482f});
        larice_tariffe.add(new float[]{1.24616774659729f, -0.0257236863905608f, 0.000427379042756368f,
                -0.00000432653178233126f, 0.0000000175138180086095f});
        larice_tariffe.add(new float[]{1.15079184034974f, -0.0207258080035654f,  0.000283077996610427f,
                -0.0000025025544394757f,  0.00000000935819868546977f});
        larice_tariffe.add(new float[]{ 1.069999152971960f, -0.0187995634686115f,   0.000268643992511396f,
                -0.00000262616077426383f,   0.0000000107979581472733f});
        larice_tariffe.add(new float[]{ 0.976735427916441f, -0.014480793177091f,   0.00014291693665576f,
                -0.00000102112778710952f,   0.00000000354881207317334f});
        larice_tariffe.add(new float[]{ 0.933261562624184f, -0.0156938674163838f,   0.00021293764647882f,
                -0.0000021296270864916f,   0.00000000922174826688834f});
        larice_tariffe.add(new float[]{ 0.849422649931721f, -0.0130410020944966f,   0.000122435643236745f,
                -0.000000761505075680516f,   0.00000000230218230137239f});
        larice_tariffe.add(new float[]{ 0.738283460551023f, -0.0108796891424147f,    0.000083240918621902f,
                -0.000000299802690866207f,    0.000000000135678516677951f});
        larice_tariffe.add(new float[]{  0.676337892187883f, -0.0126549090266761f,     0.000119649868495366f,
                -0.00000036664879032602f,    -0.000000000965180759280312f});

        faggio_tariffe.add(new float[]{  1.307427143026860f, -0.02008796622659800f,     0.0001231987888215130f,
                -0.0000002218030271553380f});
        faggio_tariffe.add(new float[]{  1.186166963418970f, -0.01812297002266890f,     0.0001086705248160950f,
                -0.0000001783682242198100f});
        faggio_tariffe.add(new float[]{  1.0508802804399f, -0.0154858209812336f,     0.0000840657884517577f,
                -8.76132252504116E-08f});
        faggio_tariffe.add(new float[]{  0.930159335177276f, -0.01367571655218380f,     0.0000749485940452063f,
                -0.0000000832118197133290f});
        faggio_tariffe.add(new float[]{  0.797821516961136f, -0.01122963978835770f,     0.0000538754923183045f,
                -0.0000000134670676871118f});
        faggio_tariffe.add(new float[]{  0.670139179729665f, -0.00906239403887230f,     0.0000382267817236813f,
                0.0000000266516089593243f});
        faggio_tariffe.add(new float[]{  0.540349082636768f, -0.00653498029652474f,     0.0000126048858631319f,
                0.0000001346765041950140f});

        abeteb_tariffe.add(new float[]{1.121204292323840f, -0.00112812463013990f, -0.000342004845011366f,
                0.00000469260927226540f, -0.0000000191908708200858f});
        abeteb_tariffe.add(new float[]{0.981300021165136f, 0.00451935738618873f, -0.000454727406669216f,
                0.00000570049980076179f, -0.000000022496327495929f});
        abeteb_tariffe.add(new float[]{0.871061791722291f, 0.00846118590064735f,  -0.000556964293515881f,
                0.00000700141469256472f,  -0.0000000284650571734363f});
        abeteb_tariffe.add(new float[]{ 0.751423757251665f, 0.01230282841864070f,   -0.000636114695962130f,
                0.00000785967171329327f,   -0.0000000320543199266247f});
        abeteb_tariffe.add(new float[]{ 0.687308000626729f, 0.01002934062259540f,   -0.000532271098658902f,
                0.00000656147315201537f,   -0.0000000267551320004059f});
        abeteb_tariffe.add(new float[]{ 0.563146653905253f, 0.01221909813940410f,   -0.000539260760864182f,
                0.00000633244923684870f,   -0.0000000249428033579244f});
        abeteb_tariffe.add(new float[]{ 0.400188244562452f, 0.01860265734781900f,   -0.000698650705387670f,
                0.00000824096488969802f,   -0.0000000332979660009276f});
        abeteb_tariffe.add(new float[]{ 0.315400408932237f, 0.01681503115693590f,    -0.000628079946390089f,
                0.00000758664283123937f,    -0.0000000315648221042745f});
        abeteb_tariffe.add(new float[]{  0.305075495263081f, 0.00901549463481577f,     -0.000402391633797053f,
                0.00000520510006112426f,    -0.0000000228005067418939f});

        pinos_tariffe.add(new float[]{1.39485528157834f, -0.0173132562493727f, -0.000111224369160071f,
                0.00000432818944905412f, -0.0000000270804212705393f});
        pinos_tariffe.add(new float[]{1.11730423517553f, 0.00462820217612667f, -0.000876038876059999f,
                0.00001599268186786f, -0.0000000914662986407017f});
        pinos_tariffe.add(new float[]{0.950386398135421f, 0.0140104801683463f,  -0.00114651910786276f,
                0.0000196103997233256f,  -0.000000109691886719991f});
        pinos_tariffe.add(new float[]{ 0.755575894751024f, 0.0225225580166138f,   -0.00131140501238242f,
                0.0000208892488636416f,   -0.000000112182434878885f});
        pinos_tariffe.add(new float[]{ 0.646262947472806f, 0.0251106417221477f,   -0.0013829182761359f,
                0.000022344663089834f,   -0.000000122803922062264f});
        pinos_tariffe.add(new float[]{ 0.533239392042914f, 0.0268774627317926f,   -0.00137293395701313f,
                0.0000216635612592482f,   -0.000000117057138373594f});
        pinos_tariffe.add(new float[]{ 0.545339466215345f, 0.01270700510322f,   -0.000784722373720131f,
                0.0000126932785455457f,   -0.0000000696350073951958f});
        pinos_tariffe.add(new float[]{ 0.367950960101659f, 0.0213786404638334f,    -0.00105218673858519f,
                0.000016443270222391f,    -0.0000000883505738858313f});
        pinos_tariffe.add(new float[]{  0.254005822037151f, 0.0189524654664182f,     -0.000870121043127567f,
                0.0000131424507015272f,    -0.000000068665465235719f});

        pinoc_tariffe.add(new float[]{ 0.840578777757921f, -0.000545001627817387f,   -0.000300117275676114f,
                0.00000427628443199169f,   -0.0000000181986290980746f});
        pinoc_tariffe.add(new float[]{ 0.726353407597132f, 0.000849222514314748f,   -0.000302425407205645f,
                0.00000420000427917910f,   -0.0000000177375409582746f});
        pinoc_tariffe.add(new float[]{ 0.586556932913235f,  0.003680288982446010f,   -0.000329612991095656f,
                0.00000430207216493596f,   -0.0000000177639095453902f});
        pinoc_tariffe.add(new float[]{ 0.482968911797867f, 0.003844331035558460f,    -0.000292026195332979f,
                0.00000373065484981362f,    -0.0000000152029938142345f});
        pinoc_tariffe.add(new float[]{  0.324375113876090f, 0.009520040520312760f,     -0.000426656252914761f,
                0.00000533053370403397f,    -0.0000000222542831807451f});


    }

    public int compute_tariffa(Boolean larice, JSONArray diametri, JSONArray altezze) throws JSONException {
     int tariffa = 0;
     if (larice){
         float min_sum = 100000000000f;
         for (float [] params: larice_tariffe
         ) {
             float somma = 0;
             for(int i=0; i<diametri.length(); i++){
                 int diametro = diametri.getInt(i);
                 int altezza = altezze.getInt(i);
                 Log.i("compute_tariffa_abete_diametro", String.valueOf(diametro));
                 if (altezza>0){
                     float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                             +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                     somma += Math.abs(altezza_stimata-(float)altezza);
                 }
             }
             Log.i("compute_tariffa_abete_somma", String.valueOf(somma));
             if ((somma < min_sum)&(somma!=0)){
                 tariffa+=1;
                 min_sum = somma;
             }
         }
     }else{
         float min_sum = 100000000000f;
         for (float [] params: abete_tariffe
              ) {
             float somma = 0;
             for(int i=0; i<diametri.length(); i++){
                 int diametro = diametri.getInt(i);
                 int altezza = altezze.getInt(i);
                 Log.i("compute_tariffa_abete_diametro", String.valueOf(diametro));
                 if (altezza>0){
                     float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                             +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                     somma += Math.abs(altezza_stimata-(float)altezza);
                 }
             }
             Log.i("compute_tariffa_abete_somma", String.valueOf(somma));
             if ((somma < min_sum)&(somma!=0)){
                 tariffa+=1;
                 min_sum = somma;
             }
         }
     }
     return tariffa;
    }

    public float [] compute_mc_n(specie s, JSONArray diametri, int tariffa) throws JSONException {
        float mc = 0f;
        float n = 0f;
        if (s==specie.Larice){
            float[] params = larice_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                mc += (float) (larice_cubature[0]*Math.pow(((float)diametro-larice_cubature[3]),larice_cubature[1])*Math.pow(altezza_stimata, larice_cubature[2]));
                n += 1;
            }
        }else if (s==specie.Abeter){
            float[] params = abete_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                mc += (float) (abete_cubature[0]*Math.pow(((float)diametro-abete_cubature[3]),abete_cubature[1])*Math.pow(altezza_stimata, abete_cubature[2]));
                n += 1;
            }
        }else if(s==specie.Faggio){
            float[] params = faggio_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4);
                mc += (float) (faggio_cubature[0]*Math.pow(((float)diametro-faggio_cubature[3]),faggio_cubature[1])*Math.pow(altezza_stimata, faggio_cubature[2]));
                n += 1;

            }
        }else if (s==specie.Abeteb) {
            float[] params = abeteb_tariffe.get(tariffa - 1);
            for (float param:params
                 ) {
                Log.i("abete_bianco_parametri", String.valueOf(param));
            }

            for (int i = 0; i < diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                Log.i("abete_bianco_altezza", String.valueOf(altezza_stimata));
                Log.i("abete_bianco_diametro", String.valueOf(diametro));
                mc += (float) (abeteb_cubature[0] * Math.pow(((float) diametro - abeteb_cubature[3]), abeteb_cubature[1]) * Math.pow(altezza_stimata, abeteb_cubature[2]));
                n += 1;
                Log.i("abete_bianco_mc", String.valueOf(mc));
            }
        }else if (s==specie.Pinos) {
            float[] params = pinos_tariffe.get(tariffa - 1);
            for (float param:params
            ) {
                Log.i("pino_silvestre_parametri", String.valueOf(param));
            }

            for (int i = 0; i < diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f + params[0] * (float) diametro + params[1] * (float) Math.pow((float) diametro, 2) + params[2] * (float) Math.pow((float) diametro, 3)
                        + params[3] * (float) Math.pow((float) diametro, 4) + params[4] * (float) Math.pow((float) diametro, 5);
                Log.i("pino_silvestre_altezza", String.valueOf(altezza_stimata));
                Log.i("pino_silvestre_diametro", String.valueOf(diametro));
                mc += (float) (pinos_cubature[0] * Math.pow(((float) diametro - pinos_cubature[3]), pinos_cubature[1]) * Math.pow(altezza_stimata, pinos_cubature[2]));
                n += 1;
                Log.i("pino_silvestre_mc", String.valueOf(mc));
            }

        }else if (s==specie.Pinoc) {
            float[] params = pinoc_tariffe.get(tariffa - 1);
            for (int i = 0; i < diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f + params[0] * (float) diametro + params[1] * (float) Math.pow((float) diametro, 2) + params[2] * (float) Math.pow((float) diametro, 3)
                        + params[3] * (float) Math.pow((float) diametro, 4)+ params[4] * (float) Math.pow((float) diametro, 5);
                mc += (float) (pinoc_cubature[0] * Math.pow(((float) diametro - pinoc_cubature[3]), pinoc_cubature[1]) * Math.pow(altezza_stimata, pinoc_cubature[2]));
                n += 1;
            }
        }else if (s==specie.Pinon) {
            /**Per il pino nero a pag. 26 del I NUOVI MODELLI DENDROMETRICI PER
             LA STIMA DELLE MASSE ASSESTAMENTALI
             IN PROVINCIA DI TRENTO (Gianfranco SCRINZI, David GALVAGNI, Laura MARZULLO) dicono: "	sono state allestite nuove tariffe anche per il Pino nero, pur con le approssimazioni relative alle
             curve ipsometriche di base desunte da quelle preesistenti del Pino silvestre;" quinid per il calcolo dell'altezza si usano
             quelle del pino silvestre**/
            Log.i("Pinonmc", "Pinon");
            float[] params = pinos_tariffe.get(tariffa - 1);
            for (int i = 0; i < diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                Log.i("Pinonmc", String.valueOf(diametro));
                float altezza_stimata = 1.3f + params[0] * (float) diametro + params[1] * (float) Math.pow((float) diametro, 2) + params[2] * (float) Math.pow((float) diametro, 3)
                        + params[3] * (float) Math.pow((float) diametro, 4) + params[4] * (float) Math.pow((float) diametro, 5);
                Log.i("Pinonmc", String.valueOf(altezza_stimata));
                mc += (float) (pinon_cubature[0] * Math.pow(((float) diametro - pinon_cubature[3]), pinon_cubature[1]) * Math.pow(altezza_stimata, pinon_cubature[2]));
                n += 1;
            }
        }
        return new float[]{mc, n};
    }

    public float [][] compute_mc_single_plant(specie s, JSONArray diametri, int tariffa) throws JSONException {
        float [] mc = new float[diametri.length()];
        float [] d =  new float[diametri.length()];
        if (s==specie.Larice){
            float[] params = larice_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                mc[i] = (float) (larice_cubature[0]*Math.pow(((float)diametro-larice_cubature[3]),larice_cubature[1])*Math.pow(altezza_stimata, larice_cubature[2]));
                d[i] = diametro;
            }
        }else if (s==specie.Abeter){
            float[] params = abete_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                mc[i] = (float) (abete_cubature[0]*Math.pow(((float)diametro-abete_cubature[3]),abete_cubature[1])*Math.pow(altezza_stimata, abete_cubature[2]));
                d[i] = diametro;
            }
        }else if (s==specie.Faggio){
            float[] params = faggio_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4);
                mc[i] = (float) (faggio_cubature[0]*Math.pow(((float)diametro-faggio_cubature[3]),faggio_cubature[1])*Math.pow(altezza_stimata, faggio_cubature[2]));
                d[i] = diametro;

            }
        }else if (s==specie.Abeteb){
            float[] params = abeteb_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+params[4]*(float)Math.pow((float)diametro, 5);
                mc[i] = (float) (abeteb_cubature[0]*Math.pow(((float)diametro-abeteb_cubature[3]),abeteb_cubature[1])*Math.pow(altezza_stimata, abeteb_cubature[2]));
                d[i] = diametro;

            }
        }else if (s==specie.Pinos){
            float[] params = pinos_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f + params[0] * (float) diametro + params[1] * (float) Math.pow((float) diametro, 2) + params[2] * (float) Math.pow((float) diametro, 3)
                        + params[3] * (float) Math.pow((float) diametro, 4) + params[4] * (float) Math.pow((float) diametro, 5);
                Log.i("pino_silvestre_altezza", String.valueOf(altezza_stimata));
                mc[i] = (float) (pinos_cubature[0] * Math.pow(((float) diametro - pinos_cubature[3]), pinos_cubature[1]) * Math.pow(altezza_stimata, pinos_cubature[2]));
                d[i] = diametro;
            }

        }else if (s==specie.Pinoc){
            float[] params = pinoc_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4)+ params[4] * (float) Math.pow((float) diametro, 5);
                mc[i] = (float) (pinoc_cubature[0]*Math.pow(((float)diametro-pinoc_cubature[3]),pinoc_cubature[1])*Math.pow(altezza_stimata, pinoc_cubature[2]));
                d[i] = diametro;

            }
        }else if (s==specie.Pinon){
            /**Per il pino nero a pag. 26 del I NUOVI MODELLI DENDROMETRICI PER
             LA STIMA DELLE MASSE ASSESTAMENTALI
             IN PROVINCIA DI TRENTO (Gianfranco SCRINZI, David GALVAGNI, Laura MARZULLO) dicono: "	sono state allestite nuove tariffe anche per il Pino nero, pur con le approssimazioni relative alle
             curve ipsometriche di base desunte da quelle preesistenti del Pino silvestre;" quinid per il calcolo dell'altezza si usano
             quelle del pino silvestre**/
            float[] params = pinos_tariffe.get(tariffa-1);
            for(int i=0; i<diametri.length(); i++) {
                int diametro = diametri.getInt(i);
                float altezza_stimata = 1.3f+params[0]*(float)diametro+params[1]*(float)Math.pow((float)diametro, 2)+params[2]*(float)Math.pow((float)diametro, 3)
                        +params[3]*(float)Math.pow((float)diametro, 4) + params[4] * (float) Math.pow((float) diametro, 5);
                mc[i] = (float) (pinon_cubature[0]*Math.pow(((float)diametro-pinon_cubature[3]),pinon_cubature[1])*Math.pow(altezza_stimata, pinon_cubature[2]));
                d[i] = diametro;

            }
        }
        return new float[][]{mc, d};
    }
    public float [][] crea_classi_diametriche (int diametro_minimo, int intervallo, float[] mc, float[] diametri){
        float max = 0f;
        for (float d: diametri
             ) {
            if (d-diametro_minimo>max){
                max = d - diametro_minimo;
            }
        }
        int n_classi = Math.round(max / (float) intervallo) + 1;
        float[] classi_diametriche = new float[n_classi];
        float[] n_piante = new float[n_classi];
        float[] v_medio = new float[n_classi];
        float[] v_tot = new float[n_classi];
        for( int i=0; i<n_classi; i++){
            classi_diametriche[i] = (float) diametro_minimo + (i * intervallo);
        }
        for (int i = 0; i<diametri.length; i++){
            float d = diametri[i];
            float v = mc[i];
            int indice = Math.round((d - diametro_minimo) / (float) intervallo);
            // Sicurezze per evitare OutOfBounds
            indice = Math.min(indice, n_classi - 1);
            indice = Math.max(0, indice);
            n_piante[indice] += 1.0f;
            v_tot[indice] += v;
            v_medio[indice] = v_tot[indice]/n_piante[indice];
        }
        return new float [][]{classi_diametriche, n_piante, v_medio, v_tot};
    }
}
