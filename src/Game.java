import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.StyledEditorKit.BoldAction;

class Game {

	final static int numberofGenes = 291;
	final static int populationSize = 100;
	static double mutationRate = 0.2;
	static double crossoverRate = 15;
	static double winnerSurvivalRate = 70;
	static int numOfWins = 0;
	// static ArrayList<Integer> list = new ArrayList<Integer>();

	// This returns the first chromosome(row) from a
	// population with values/weights which will be use to play the game
	static double[] evolveWeights() throws Exception {
		// Create a random initial population
		Random r = new Random();
		// Matrix is a two dimensional array, populationSize are rows, Genes are columns
		Matrix population = new Matrix(populationSize, numberofGenes);
		for (int i = 0; i < 100; i++) {
			// returns elements(genes) of every row
			// Think of every row as a seperate chromosome of parent
			double[] chromosome = population.row(i);
			// create every gene for each chrosome in the population
			for (int j = 0; j < chromosome.length; j++) {
				double gene = 0.03 * r.nextGaussian();
				chromosome[j] = gene;
			}
		}

		int generationNum = 0;
		// do battle with chromosomes until winning condition is found
		// Controller.doBattleNoGui(new ReflexAgent(), new
		// NeuralAgent(population.row(0)))
		while (Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.row(0))) != -1) {

			System.out.println("Generation " + (generationNum));

			int mightLive = r.nextInt(100);

			if (generationNum == 10)
				mutationRate -= 0.01;
			if (generationNum == 20)
				mutationRate -= 0.02;
			if (generationNum == 30)
				mutationRate -= 0.02;

			// Mutate the genes of the current population
			for (int y = 0; y < populationSize; y++) {
				for (int x = 0; x < numberofGenes; x++) {
					if (Math.random() <= mutationRate) {
						population.changeGene(x, y, r.nextGaussian());
					}
				}
			}

			// Make random number of battles
			for (int i = 0; i < 40; i++) {
				// Create two teams with two random chromosomes from the population
				int soldier_a = r.nextInt(population.rows());
				int soldier_b = r.nextInt(population.rows());

				// Ensure that both teams don't have the same chromosome
				if (soldier_a == soldier_b)
					soldier_b = r.nextInt(population.rows());

				// Do Battle between teams and select winner
				if (Controller.doBattleNoGui(new NeuralAgent(population.row(soldier_a)),
						new NeuralAgent(population.row(soldier_b))) == 1) {
					// Chooses if the winner survives
					if (mightLive < winnerSurvivalRate)
						population.removeRow(soldier_b);
					else
						population.removeRow(soldier_a);
				}

				else if (Controller.doBattleNoGui(new NeuralAgent(population.row(soldier_a)),
						new NeuralAgent(population.row(soldier_b))) == -1) {
					// Chooses if the winner survives
					if (mightLive < winnerSurvivalRate)
						population.removeRow(soldier_a);
					else
						population.removeRow(soldier_b);
				}
			}

			// Reproduce for the population (This is where the magic happens)
			// int currentPopulation = population.rows();
			while (population.rows() < 100) {
				// Retrieve random parent
				int parentID = r.nextInt(population.rows());
				double[] parent = population.row(parentID);
				int[] potentialPartners = new int[10];
				int abs_min_value = Integer.MIN_VALUE;
				Boolean foundpartner = false;
				int partner = 0;

				for (int i = 0; i < potentialPartners.length; i++) {
					int potentialPartnerID = r.nextInt(population.rows());
					if (parentID == potentialPartnerID)
						potentialPartnerID = r.nextInt(population.rows());
					potentialPartners[i] = potentialPartnerID;
				}

				for (int i = 0; i < potentialPartners.length; i++) // Finding most compatiable parent #2.
				{
					int compatiablity = similarities(parent, population.row(potentialPartners[i]));
					if (compatiablity > abs_min_value) {
						partner = potentialPartners[i];
						foundpartner = true;
					}
				}
				if (foundpartner == false)
					partner = r.nextInt(population.rows());

				double[] secondParent = population.row(partner);
				double[] newChild = population.newRow();
				int splitpoint = r.nextInt((int) numberofGenes / 4);

				for (int i = 0; i < splitpoint; i++) // logic to a for loop and two if statements
					newChild[i] = parent[i];
				for (int i = splitpoint; i < numberofGenes; i++)
					newChild[i] = secondParent[i];

				// for (int i = 0; i < population.rows(); i++) {
				// if (Controller.doBattleNoGui(new ReflexAgent(), new
				// NeuralAgent(population.row(i))) == -1) {
				// population.row(i) = population.newRow();
				// numOfWins++;
				// } else { }

				// Test for the best in the given population
				// win_Collection.add(numOfWins);
				// printWeights(population.row(0));

			}
			generationNum++;
			for (int i = 0; i < population.rows(); i++) {
				if (Controller.doBattleNoGui(new ReflexAgent(), new NeuralAgent(population.row(i))) == -1) {
					numOfWins++;
					// population.row(i) = population.newRow();
				} else {
				}
			}
			System.out.println("Number of Winners: " + numOfWins);
			numOfWins = 0;

		}
		printWeights(population.row(0));
		return population.row(0);
	}

	public static int similarities(double[] parent, double[] anotherParent) {
		int similarities = 0;
		for (int i = 0; i < parent.length && i < anotherParent.length; i++) {
			// Compare the gene of each parent
			if (parent[i] == anotherParent[i])
				similarities++;
		}
		return similarities;
	}

	public static void printWeights(double[] weights) {
		for (int i = 0; i < weights.length; i++) {
			System.out.print(weights[i] + ", ");
		}
		System.out.println();
	}

	// Finds the parents that have the simlarities to produce the best children

	public static void main(String[] args) throws Exception {
		// double[] w = evolveWeights();
		// printWeights(w);
		double[] myweights = { -2.21466955153828, -0.07474857363573262, -1.1749783341411326, 0.16397829193498464,
				0.07131629649717008, 1.61328716208057, -2.641459977820003, -2.189110351161901, -0.20970779418672014,
				1.4439418468883647, -0.2184717560493591, -1.5078991641372075, -0.4975766167697391, -0.02410194533351991,
				-1.528121086508629, 0.4893670827264642, 0.24474596975099, -0.33381774735153213, -2.0197364115373686,
				-2.285127694998558, -1.6102939758132773, -1.5170275119464784, 2.839569745070095, -2.2196308724273552,
				-0.0212824892080149, 0.014455462339448166, -0.8294873147131405, -4.008294779402316, -1.6636632454217084,
				-1.9129203960517205, 0.25183900782052027, -3.9328447788262255, 1.7493959642027366, 0.4956568965253274,
				-0.07060668503545968, -1.0647141555175716, -2.5621340861527404, -0.5646708616200512,
				-3.3199764295441483, -0.7423376302286884, -2.0759415442240625, 1.8468059487043376, 1.8301868662020766,
				0.3265136721962445, 0.2963634372088102, -1.570986722570204, 0.030502603573360328, 1.4024108876313992,
				-0.14772072123156077, 1.4790013912446605, -0.19399829641453603, 0.025796871304820657,
				-1.7353744443081178, -0.7774882870945194, -0.8864079092655823, 0.01860475661039064,
				-0.38816929845666215, 5.433590999656766, -0.7039983033363961, 1.1052189890385327, -0.6337678641299205,
				2.577846157879395, -1.6954645339963452, -3.3151515029911014, -0.5905535947937579, -3.208862197909249,
				0.3533810265008289, -0.014479467387126133, -0.4997747691476975, 0.17895696721499255, 0.7889684353058366,
				0.6640692831578959, -1.7362432877961986, 2.4182626823702855, 1.2843792391756987, 1.9815093968825601,
				-0.30220651427285306, 0.7436105113169372, 1.0680163086014036, 1.7517072771957438, 1.2282614956016236,
				-4.1695808227962035, 0.5978930687432595, -1.5020444810097677, 1.2212439971243139, -3.2648411343496164,
				0.6909710728147561, -1.3070485527971965, -2.028589199427193, -1.2316367055137034, -1.1776549704837918,
				3.0932207527724955, 0.09163042597406289, 0.14951497594494684, 3.4362942167755, -0.5107751983055535,
				1.4777997649386783, -1.6160308476702507, -0.7331259806111848, 2.6279925296341906, -1.7722891199616801,
				-2.632229462809496, 0.44428581550083274, 1.6278865636527509, -0.0329347232073198, -2.918867540199039,
				0.9628195606239641, -2.156481296002708, -1.056790342242603, -2.2556629595171356, -0.9553204054731589,
				2.413880737785754, -0.9992245987211152, 0.9108199628723169, -1.134380039817171, -1.0116006672645148,
				-0.5854694644938652, -3.8446665938925046, 3.232988970599319, -0.05839122943338931, -0.12200406901238987,
				-1.1970864247655226, -1.7736651076674108, 3.2042060881784344, -0.06681900340608203, 0.6095617884784129,
				2.6621135698954426, -1.9808653269739094, 2.4179303951852247, -0.1020298670663256, 0.8455422105561868,
				-0.6172016676389036, 1.5106799954170962, 0.22045980127688758, -0.32315411146978057, 0.3521551186665719,
				-1.376041285561516, -0.5007424056636434, 1.361180226793381, 0.5485767449283305, 0.4822090152603501,
				0.0035720899920257954, 0.20198792489462591, 2.9637620623310696, 0.746515676821096, 1.33314607227669,
				1.7880569950236676, 0.24310824088346455, -0.7720019287108711, 0.2770938718675544, -0.002821079170251539,
				0.6432076089386127, -2.080883646553964, -2.2316867814270336, -0.8152503328954235, 0.07666991930368347,
				0.7994585307407924, 4.040588645491936, 3.293290177485252, -0.6587327188020271, -0.2608808875988929,
				0.9747677475984142, 2.398983410951306, -0.6437438305433649, 0.02124428359002813, 1.5585328640214078,
				-0.301679640295372, -1.6053388324773985, 0.24993532845039462, 0.24408970961912746, 2.425260721978244,
				-0.8068206244484942, 0.05105476442114232, 0.6059510612060248, -3.2193288600922187, -3.631848243294158,
				1.4240768232681529, -1.7707952765701727, 1.2344743497386608, 2.4338900468729023, 0.43014199440688067,
				0.20816791679939328, 1.4989715392344287, 0.13455171261820276, 1.0621048796860084, 0.9728863685939746,
				-3.9871305903296723, 0.6897219833391851, -1.098819235411824, -0.5984492289686463, -2.303924190561265,
				1.1193184751481047, -1.0328499504466768, -0.8910211970908617, 0.9946509861991504, -0.6898892448411882,
				-0.43356416305497014, 0.054783696872076626, 0.233434394673659, 3.225842335938765, 3.297560597443148,
				-0.9815414837749732, -1.5241431138614758, 0.2003787279485732, -0.6881136445150136, 3.4238717049813934,
				0.003453173792474847, -0.8061218778816472, -0.4402066310426072, -1.636723071030285, 3.163218840903199,
				0.5100537731772297, -1.103269998723354, -0.7879470972745664, -2.236992460857296, -2.1393375184180212,
				-1.1548394915305815, -2.280645418548106, 0.6909647906413796, -0.8314321184642959, -3.2122142779955563,
				0.37452253354430365, -0.7849739993770579, 0.3504279749557794, -1.0540725377016087, 0.041950910604904586,
				0.8784481358277388, -0.14974538330350365, 1.559727397979539, -0.6605219061003249, 3.622853835751025,
				1.7401420193245865, 1.220309981802207, -0.22257446946377682, -0.7746021595685124, -1.117421721755124,
				-0.9138705765688842, 0.14610649511784396, -0.7526008992175952, 0.10653763536360544,
				-0.06514332909119891, -0.2097381331142011, -1.0529132828316619, 2.1798628015621757, -0.3091083139673463,
				0.8577372690910885, 1.6985927086198869, 0.4634835727447051, 0.03566063568597455, -0.13895025188806187,
				0.8498827214996195, -0.9091546683669671, 1.3826935600107073, -0.8152255589205574, 1.7274291860876136,
				0.786497013333733, 1.6227793753957989, 0.7876476663994035, 2.157300684833067, -0.09447665526454312,
				-2.11514518468542, -1.089786880457046, -0.1570647222609598, 0.743643370603463, -0.4237412616449528,
				2.38254290787229, 5.305775057877128, 1.8737860414844345, -1.8325132176577972, 2.5427149426139777,
				0.6923303547353667, -1.1867898040888405, 2.584322135207721, 1.9779450607594122, -0.8178488064297246,
				1.9581575076866673, -0.2412475645390626, 1.2870350743909154, 0.2570224449276464, -4.092909749196866,
				3.898874605498058, -0.16220823886347796, 0.2634423746472414, -2.5921511819647276, -0.29329004951913773,
				-3.485083266813694, 3.5670785842138093, 0.7244265147251102, -0.0029356664095335516, 1.4901040035846655,
				2.8999162174147775 };

		Controller.doBattle(new ReflexAgent(), new NeuralAgent(myweights));
		// System.out.print(win_Collection[]);
	}

}
