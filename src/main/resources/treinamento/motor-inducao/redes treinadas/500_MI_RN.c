/**
     Arquivo gerado automaticamente pelo software de treinamento.
     Info : ricosv
     Data: Fri Aug 18 12:02:24 BRT 2017

	    Topologia: [I@77397d60
	    Funcao de ativacao nas camadas escondidas:*outVal = tanh(x) + 0.001* x; 
	    Funcao de ativacao nas saidas: Linear
	    Eta: 0.1
 	Momentum:0.2
	    Epoca maxima de treinamento: 3000
	    Erro alvo durante treinamento: 1.0E-5


     O nome das variaveis possui informacoes a respeito de sua localizacao na ANN. ex:
     w010 -> o primeiro 0 indica a camada que o neuronio possuidor desse peso esta,
     o segundo numero,'1', indica o número do neurônio da camada (o ultimo neuronio e sempre um bias)
     o ultimo numero ,'0', indica qual a posicao do neuronio alvo localizado na camada seguinte. 
     As variáveis de entrada estão na mesma ordem em que são encontradas no arquivo de treinamento.

   Colunas de ENTRADA referente aos dados de treino :
   1   2   3   4
   Colunas de SAIDA referente aos dados de treino :
   5
*/


#include <math.h>
// -------- Layer 0 --------
// Neuron 0
double w0_0_0=-0.5812490087486022,w0_0_1=-0.9700306456963259,w0_0_2=0.3246608730484187,w0_0_3=1.7083344633271709,w0_0_4=-0.9399559233647513,w0_0_5=0.18630502031919818,w0_0_6=0.6234194972405196,w0_0_7=-1.5301922374784447,w0_0_8=-1.1761366232757389,w0_0_9=0.5492945547928028,w0_0_10=-2.273440211528908,w0_0_11=-1.2965478193334838,w0_0_12=1.290025573156506,w0_0_13=1.9921848861681077,w0_0_14=0.6989462413608062;
// Neuron 1
double w0_1_0=-1.0299314622080258,w0_1_1=-1.8086895210183713,w0_1_2=-2.525336532745251,w0_1_3=0.5902673710609481,w0_1_4=-1.4230434908370295,w0_1_5=-1.2137175118690473,w0_1_6=0.06532032899071237,w0_1_7=-0.7960420218910595,w0_1_8=1.062855356975343,w0_1_9=-0.5449476824046524,w0_1_10=-1.745163968744608,w0_1_11=-2.527440923984856,w0_1_12=-1.4922104118444734,w0_1_13=0.37344293954217733,w0_1_14=0.710000613265169;
// Neuron 2
double w0_2_0=2.7929847404959247,w0_2_1=-0.9805626961130293,w0_2_2=0.14304570514149575,w0_2_3=1.794624477103051,w0_2_4=-2.4416016425068414,w0_2_5=-1.812404161774357,w0_2_6=1.127007452899549,w0_2_7=2.013973359678756,w0_2_8=0.32398985127594704,w0_2_9=-1.017293639015923,w0_2_10=0.9817732220922578,w0_2_11=-0.6384880760620861,w0_2_12=2.6176980447808664,w0_2_13=0.7509858959416686,w0_2_14=-0.4847818981714968;
// Neuron 3
double w0_3_0=1.6373418317010113,w0_3_1=-1.0500248791772646,w0_3_2=0.7123791627041184,w0_3_3=0.7444433154381473,w0_3_4=2.0150297012739133,w0_3_5=2.1320467120908644,w0_3_6=-1.3117386941429774,w0_3_7=-0.44764521918457917,w0_3_8=-1.0709061205234633,w0_3_9=0.006005010446439378,w0_3_10=1.66891380897109,w0_3_11=2.468315391409831,w0_3_12=0.07265422834777874,w0_3_13=-0.174395422592226,w0_3_14=-0.39470562087188193;
// Bias
double w0_4_0=0.5706134026840608,w0_4_1=-0.28497405110615887,w0_4_2=1.499019006149885,w0_4_3=1.8549517809069467,w0_4_4=-1.0204440033065594,w0_4_5=0.01641598808342205,w0_4_6=-0.9905883988047403,w0_4_7=1.2220586783667735,w0_4_8=0.003251980395868583,w0_4_9=0.9307026190025081,w0_4_10=-0.1327645018866243,w0_4_11=-0.7774234878705768,w0_4_12=0.09699397754313367,w0_4_13=-0.1688224368935447,w0_4_14=-0.424839624563799;
// -------- Layer 1 --------
// Neuron 0
double w1_0_0=-1.1335776712398105,w1_0_1=-1.8029225111771496,w1_0_2=-0.09789593992376493,w1_0_3=-0.21790866525940383,w1_0_4=-1.244264357905889,w1_0_5=1.1224606584527832,w1_0_6=-0.057847001626272376,w1_0_7=0.5259875773651802,w1_0_8=-0.90862030062573,w1_0_9=-0.3236312900451307;
// Neuron 1
double w1_1_0=1.5589650553216992,w1_1_1=-0.5678417749946172,w1_1_2=-0.5567417393241295,w1_1_3=0.5896961719537653,w1_1_4=-0.26319764206068574,w1_1_5=0.2377650689044021,w1_1_6=-0.6968899320805201,w1_1_7=-1.12633484392593,w1_1_8=0.16027547897157915,w1_1_9=-0.8012791006248806;
// Neuron 2
double w1_2_0=0.5177069678720353,w1_2_1=0.273798906287749,w1_2_2=0.07865128721683058,w1_2_3=-0.6371196633621005,w1_2_4=0.4013872968370847,w1_2_5=1.2220113207219707,w1_2_6=-1.1396245998212835,w1_2_7=-1.4151413757784752,w1_2_8=0.36735679161062823,w1_2_9=-0.2815888633745369;
// Neuron 3
double w1_3_0=-0.7961952368316556,w1_3_1=-0.4840081619352391,w1_3_2=-0.09005941487194155,w1_3_3=-2.0691871578527103,w1_3_4=-0.6727220431566779,w1_3_5=-0.4411389110251012,w1_3_6=0.4380264692334074,w1_3_7=-0.4849252592979907,w1_3_8=0.20577396846142573,w1_3_9=-1.0658922746900246;
// Neuron 4
double w1_4_0=-1.613859984083995,w1_4_1=-0.19255759288186275,w1_4_2=-0.2814919241857713,w1_4_3=-0.5401368379740922,w1_4_4=0.8356839793706496,w1_4_5=0.8673116713341559,w1_4_6=0.6122626146736868,w1_4_7=2.4095994950007023,w1_4_8=0.7641585411890088,w1_4_9=0.5558050175763333;
// Neuron 5
double w1_5_0=0.09289379605953259,w1_5_1=-0.17526999645395627,w1_5_2=0.7351268153884483,w1_5_3=-0.953658174642311,w1_5_4=1.7350685730749291,w1_5_5=-0.1621464615907943,w1_5_6=0.36759947011520067,w1_5_7=0.509436766763912,w1_5_8=1.527358961740725,w1_5_9=-0.357018002513049;
// Neuron 6
double w1_6_0=-0.28103222942984196,w1_6_1=-1.5835967619594888,w1_6_2=-1.4806293341074228,w1_6_3=-0.01084568782912007,w1_6_4=0.2645910440292825,w1_6_5=0.26156291491997696,w1_6_6=-0.21901954373510313,w1_6_7=0.44742131833391585,w1_6_8=-0.03253099556899339,w1_6_9=0.4867044695840643;
// Neuron 7
double w1_7_0=-0.3854814547951032,w1_7_1=-0.49596017903917483,w1_7_2=0.05633010893350281,w1_7_3=0.7743690970773541,w1_7_4=-1.5637441305578852,w1_7_5=-0.20847116570051963,w1_7_6=-0.6240218946248496,w1_7_7=-1.0888168587949005,w1_7_8=-0.5912126571404903,w1_7_9=-1.033660434723824;
// Neuron 8
double w1_8_0=-0.2694934339709974,w1_8_1=0.7909289212046452,w1_8_2=-0.82037529363671,w1_8_3=1.7629230733658112,w1_8_4=-0.07528094855809116,w1_8_5=-0.9125505990730955,w1_8_6=0.2518842972192358,w1_8_7=0.27477685698585036,w1_8_8=-0.1762432322352792,w1_8_9=-0.6920597515284995;
// Neuron 9
double w1_9_0=0.9918640139065483,w1_9_1=0.07116181441610557,w1_9_2=-0.66737328267283,w1_9_3=-0.004177800140322151,w1_9_4=-0.16345958899712917,w1_9_5=1.883934860051914,w1_9_6=1.1897830590736906,w1_9_7=0.31875494728499304,w1_9_8=0.959190247125779,w1_9_9=-0.9183314715490413;
// Neuron 10
double w1_10_0=-0.1347098668636009,w1_10_1=-1.8418834981448142,w1_10_2=0.5164092351220673,w1_10_3=-0.331321534595397,w1_10_4=-0.8206320564963479,w1_10_5=0.9038034682251682,w1_10_6=-1.1157468098073313,w1_10_7=-0.8609965604095282,w1_10_8=-0.12537831373943062,w1_10_9=1.3044172416882411;
// Neuron 11
double w1_11_0=-0.15033733008782818,w1_11_1=-0.864857350061481,w1_11_2=0.44544558384202676,w1_11_3=-0.2845219189251887,w1_11_4=1.2000987527048466,w1_11_5=2.656551404621355,w1_11_6=0.1967670295321897,w1_11_7=0.3649675921954945,w1_11_8=0.5924851016571085,w1_11_9=1.5285687329130961;
// Neuron 12
double w1_12_0=-0.673984595170227,w1_12_1=-1.26203090542056,w1_12_2=0.7430193430176026,w1_12_3=0.06814316182534728,w1_12_4=0.23046809086136075,w1_12_5=0.2316891688729626,w1_12_6=2.458344042674671,w1_12_7=1.8693045212034374,w1_12_8=-0.9034871988548435,w1_12_9=0.2517861436933744;
// Neuron 13
double w1_13_0=0.6899828147926679,w1_13_1=0.2735899798057796,w1_13_2=0.422124180293931,w1_13_3=-0.1192820767878593,w1_13_4=-1.6102975307624465,w1_13_5=-0.7146954233022752,w1_13_6=1.4595527127469365,w1_13_7=0.5808527698615578,w1_13_8=-0.5579002309605956,w1_13_9=-0.5001314152885555;
// Neuron 14
double w1_14_0=-0.2326209467572459,w1_14_1=0.4530332544693106,w1_14_2=-0.2568677463027525,w1_14_3=0.699755591604315,w1_14_4=1.2168908963217018,w1_14_5=-0.28657388537468137,w1_14_6=0.6288709748160292,w1_14_7=0.5015155627737607,w1_14_8=0.4904963086920403,w1_14_9=-0.34242919003174455;
// Bias
double w1_15_0=0.4691970698795386,w1_15_1=0.6136679877916111,w1_15_2=1.1224372754676166,w1_15_3=-0.6305397081335019,w1_15_4=0.162260794943833,w1_15_5=0.24600994207303942,w1_15_6=-0.22602708868162674,w1_15_7=-1.0732386519680166,w1_15_8=0.9294337090284326,w1_15_9=-1.177130797915306;
// -------- Layer 2 --------
// Neuron 0
double w2_0_0=0.16149294039170542;
// Neuron 1
double w2_1_0=0.0342292572727523;
// Neuron 2
double w2_2_0=0.41448446450869886;
// Neuron 3
double w2_3_0=0.27215853071906404;
// Neuron 4
double w2_4_0=0.2343780162637976;
// Neuron 5
double w2_5_0=0.03838181650061462;
// Neuron 6
double w2_6_0=-0.1469037253660122;
// Neuron 7
double w2_7_0=0.3032863948531237;
// Neuron 8
double w2_8_0=0.16881420105775355;
// Neuron 9
double w2_9_0=0.1897598729988713;
// Bias
double w2_10_0=0.2907556575220956;

     double outMin = -1; 
     double  outMax = 1;
 
double inMax[] = { 8.9760492356205, 0.0, -1.7126264925442E-8, 63.046899090084};
double inMin[] = { -0.061017785998642, -17.498921913957, -376.44556378968, 0.0};
double outMaxD[] = { 193.14794426635};
double outMinD[] = { -2.1714083281165};
// --------------------------------------------------------------------------------------
void normaliza(double *in, int i, double *outN) //(double min, double max, double x)
{
	double a = (2.0) / (inMax[i] - inMin[i]);
	double b = ((-(1.0)) * inMin[i] + (-1) * inMax[i]) / (inMax[i] - inMin[i]);
	*outN = (a * *in + b);
}
void desnormaliza(double x, int i, double *resNorm)
{
	double a = (1 - (-1)) / (outMaxD[i] - outMinD[i]);
	double b = (-(1) * outMinD[i] + (-1) * outMaxD[i]) / (outMaxD[i] - outMinD[i]);
	*resNorm = (x - b) / a;
}
void actvFunc(double x, double *outVal){
*outVal = tanh(x) + 0.001* x;
}
void reden_(double *in0,double *in1,double *in2,double *in3,double *out0){
      double   in1_0 = 0,  in1_1 = 0,  in1_2 = 0,  in1_3 = 0,  in1_4 = 0,  in1_5 = 0,  in1_6 = 0,  in1_7 = 0,  in1_8 = 0,  in1_9 = 0,  in1_10 = 0,  in1_11 = 0,  in1_12 = 0,  in1_13 = 0,  in1_14 = 0;
      double   in2_0 = 0,  in2_1 = 0,  in2_2 = 0,  in2_3 = 0,  in2_4 = 0,  in2_5 = 0,  in2_6 = 0,  in2_7 = 0,  in2_8 = 0,  in2_9 = 0;
double  out0D = 0;
double  in0N = 0, in1N = 0, in2N = 0, in3N = 0;
normaliza(in0,0,&in0N);
normaliza(in1,1,&in1N);
normaliza(in2,2,&in2N);
normaliza(in3,3,&in3N);
actvFunc(( in0N * w0_0_0 +  in1N * w0_1_0 +  in2N * w0_2_0 +  in3N * w0_3_0 + -1.0*w0_4_0), &in1_0);
actvFunc(( in0N * w0_0_1 +  in1N * w0_1_1 +  in2N * w0_2_1 +  in3N * w0_3_1 + -1.0*w0_4_1), &in1_1);
actvFunc(( in0N * w0_0_2 +  in1N * w0_1_2 +  in2N * w0_2_2 +  in3N * w0_3_2 + -1.0*w0_4_2), &in1_2);
actvFunc(( in0N * w0_0_3 +  in1N * w0_1_3 +  in2N * w0_2_3 +  in3N * w0_3_3 + -1.0*w0_4_3), &in1_3);
actvFunc(( in0N * w0_0_4 +  in1N * w0_1_4 +  in2N * w0_2_4 +  in3N * w0_3_4 + -1.0*w0_4_4), &in1_4);
actvFunc(( in0N * w0_0_5 +  in1N * w0_1_5 +  in2N * w0_2_5 +  in3N * w0_3_5 + -1.0*w0_4_5), &in1_5);
actvFunc(( in0N * w0_0_6 +  in1N * w0_1_6 +  in2N * w0_2_6 +  in3N * w0_3_6 + -1.0*w0_4_6), &in1_6);
actvFunc(( in0N * w0_0_7 +  in1N * w0_1_7 +  in2N * w0_2_7 +  in3N * w0_3_7 + -1.0*w0_4_7), &in1_7);
actvFunc(( in0N * w0_0_8 +  in1N * w0_1_8 +  in2N * w0_2_8 +  in3N * w0_3_8 + -1.0*w0_4_8), &in1_8);
actvFunc(( in0N * w0_0_9 +  in1N * w0_1_9 +  in2N * w0_2_9 +  in3N * w0_3_9 + -1.0*w0_4_9), &in1_9);
actvFunc(( in0N * w0_0_10 +  in1N * w0_1_10 +  in2N * w0_2_10 +  in3N * w0_3_10 + -1.0*w0_4_10), &in1_10);
actvFunc(( in0N * w0_0_11 +  in1N * w0_1_11 +  in2N * w0_2_11 +  in3N * w0_3_11 + -1.0*w0_4_11), &in1_11);
actvFunc(( in0N * w0_0_12 +  in1N * w0_1_12 +  in2N * w0_2_12 +  in3N * w0_3_12 + -1.0*w0_4_12), &in1_12);
actvFunc(( in0N * w0_0_13 +  in1N * w0_1_13 +  in2N * w0_2_13 +  in3N * w0_3_13 + -1.0*w0_4_13), &in1_13);
actvFunc(( in0N * w0_0_14 +  in1N * w0_1_14 +  in2N * w0_2_14 +  in3N * w0_3_14 + -1.0*w0_4_14), &in1_14);
actvFunc(( in1_0 * w1_0_0 +  in1_1 * w1_1_0 +  in1_2 * w1_2_0 +  in1_3 * w1_3_0 +  in1_4 * w1_4_0 +  in1_5 * w1_5_0 +  in1_6 * w1_6_0 +  in1_7 * w1_7_0 +  in1_8 * w1_8_0 +  in1_9 * w1_9_0 +  in1_10 * w1_10_0 +  in1_11 * w1_11_0 +  in1_12 * w1_12_0 +  in1_13 * w1_13_0 +  in1_14 * w1_14_0 + -1.0*w1_15_0), &in2_0);
actvFunc(( in1_0 * w1_0_1 +  in1_1 * w1_1_1 +  in1_2 * w1_2_1 +  in1_3 * w1_3_1 +  in1_4 * w1_4_1 +  in1_5 * w1_5_1 +  in1_6 * w1_6_1 +  in1_7 * w1_7_1 +  in1_8 * w1_8_1 +  in1_9 * w1_9_1 +  in1_10 * w1_10_1 +  in1_11 * w1_11_1 +  in1_12 * w1_12_1 +  in1_13 * w1_13_1 +  in1_14 * w1_14_1 + -1.0*w1_15_1), &in2_1);
actvFunc(( in1_0 * w1_0_2 +  in1_1 * w1_1_2 +  in1_2 * w1_2_2 +  in1_3 * w1_3_2 +  in1_4 * w1_4_2 +  in1_5 * w1_5_2 +  in1_6 * w1_6_2 +  in1_7 * w1_7_2 +  in1_8 * w1_8_2 +  in1_9 * w1_9_2 +  in1_10 * w1_10_2 +  in1_11 * w1_11_2 +  in1_12 * w1_12_2 +  in1_13 * w1_13_2 +  in1_14 * w1_14_2 + -1.0*w1_15_2), &in2_2);
actvFunc(( in1_0 * w1_0_3 +  in1_1 * w1_1_3 +  in1_2 * w1_2_3 +  in1_3 * w1_3_3 +  in1_4 * w1_4_3 +  in1_5 * w1_5_3 +  in1_6 * w1_6_3 +  in1_7 * w1_7_3 +  in1_8 * w1_8_3 +  in1_9 * w1_9_3 +  in1_10 * w1_10_3 +  in1_11 * w1_11_3 +  in1_12 * w1_12_3 +  in1_13 * w1_13_3 +  in1_14 * w1_14_3 + -1.0*w1_15_3), &in2_3);
actvFunc(( in1_0 * w1_0_4 +  in1_1 * w1_1_4 +  in1_2 * w1_2_4 +  in1_3 * w1_3_4 +  in1_4 * w1_4_4 +  in1_5 * w1_5_4 +  in1_6 * w1_6_4 +  in1_7 * w1_7_4 +  in1_8 * w1_8_4 +  in1_9 * w1_9_4 +  in1_10 * w1_10_4 +  in1_11 * w1_11_4 +  in1_12 * w1_12_4 +  in1_13 * w1_13_4 +  in1_14 * w1_14_4 + -1.0*w1_15_4), &in2_4);
actvFunc(( in1_0 * w1_0_5 +  in1_1 * w1_1_5 +  in1_2 * w1_2_5 +  in1_3 * w1_3_5 +  in1_4 * w1_4_5 +  in1_5 * w1_5_5 +  in1_6 * w1_6_5 +  in1_7 * w1_7_5 +  in1_8 * w1_8_5 +  in1_9 * w1_9_5 +  in1_10 * w1_10_5 +  in1_11 * w1_11_5 +  in1_12 * w1_12_5 +  in1_13 * w1_13_5 +  in1_14 * w1_14_5 + -1.0*w1_15_5), &in2_5);
actvFunc(( in1_0 * w1_0_6 +  in1_1 * w1_1_6 +  in1_2 * w1_2_6 +  in1_3 * w1_3_6 +  in1_4 * w1_4_6 +  in1_5 * w1_5_6 +  in1_6 * w1_6_6 +  in1_7 * w1_7_6 +  in1_8 * w1_8_6 +  in1_9 * w1_9_6 +  in1_10 * w1_10_6 +  in1_11 * w1_11_6 +  in1_12 * w1_12_6 +  in1_13 * w1_13_6 +  in1_14 * w1_14_6 + -1.0*w1_15_6), &in2_6);
actvFunc(( in1_0 * w1_0_7 +  in1_1 * w1_1_7 +  in1_2 * w1_2_7 +  in1_3 * w1_3_7 +  in1_4 * w1_4_7 +  in1_5 * w1_5_7 +  in1_6 * w1_6_7 +  in1_7 * w1_7_7 +  in1_8 * w1_8_7 +  in1_9 * w1_9_7 +  in1_10 * w1_10_7 +  in1_11 * w1_11_7 +  in1_12 * w1_12_7 +  in1_13 * w1_13_7 +  in1_14 * w1_14_7 + -1.0*w1_15_7), &in2_7);
actvFunc(( in1_0 * w1_0_8 +  in1_1 * w1_1_8 +  in1_2 * w1_2_8 +  in1_3 * w1_3_8 +  in1_4 * w1_4_8 +  in1_5 * w1_5_8 +  in1_6 * w1_6_8 +  in1_7 * w1_7_8 +  in1_8 * w1_8_8 +  in1_9 * w1_9_8 +  in1_10 * w1_10_8 +  in1_11 * w1_11_8 +  in1_12 * w1_12_8 +  in1_13 * w1_13_8 +  in1_14 * w1_14_8 + -1.0*w1_15_8), &in2_8);
actvFunc(( in1_0 * w1_0_9 +  in1_1 * w1_1_9 +  in1_2 * w1_2_9 +  in1_3 * w1_3_9 +  in1_4 * w1_4_9 +  in1_5 * w1_5_9 +  in1_6 * w1_6_9 +  in1_7 * w1_7_9 +  in1_8 * w1_8_9 +  in1_9 * w1_9_9 +  in1_10 * w1_10_9 +  in1_11 * w1_11_9 +  in1_12 * w1_12_9 +  in1_13 * w1_13_9 +  in1_14 * w1_14_9 + -1.0*w1_15_9), &in2_9);
desnormaliza( ( in2_0 * w2_0_0 +  in2_1 * w2_1_0 +  in2_2 * w2_2_0 +  in2_3 * w2_3_0 +  in2_4 * w2_4_0 +  in2_5 * w2_5_0 +  in2_6 * w2_6_0 +  in2_7 * w2_7_0 +  in2_8 * w2_8_0 +  in2_9 * w2_9_0 + -1.0*w2_10_0),0, &out0D);
 *out0 =  out0D;
}