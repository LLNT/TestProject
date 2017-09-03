
public class Occupation {
	protected OccupationBank occupation;
	Occupation(OccupationOrd occord){
		occupation=Global.occupationbank.get_occupation(occord);
	}
}
