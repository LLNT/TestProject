import java.util.ArrayList;
import java.util.List;

public class OccupationBank {
	protected List<Integer> special_effect_group;
	protected int basic_move;
	protected int[] basic_weapon_rank=new int[Global.WEAPON_RANK_VECTOR_LENGTH];
	protected int ride;//boolean
	protected int[] move_cost=new int[Global.MOVE_COST_VECTOR_LENGTH];
	protected List<Integer> skills;
	private OccupationBank[] occupationbank=new OccupationBank[Global.OCCUPATION_BANK_SIZE];
	OccupationBank(){}
	OccupationBank(int i){
		occupationbank[OccupationOrd.DUMMY_OCCUPATION.ordinal()]=new Lord();
		occupationbank[OccupationOrd.LORD.ordinal()]=new Lord();
		occupationbank[OccupationOrd.CAVALIER.ordinal()]=new Cavalier();
	}
	public OccupationBank get_occupation(OccupationOrd occord){
		return occupationbank[occord.ordinal()];
	}
}

class Lord extends OccupationBank{
	Lord(){
		special_effect_group=new ArrayList<Integer>();
		special_effect_group.add(SpecialEffectGroup.HUMAN.ordinal());
		special_effect_group.add(SpecialEffectGroup.INFANTRY.ordinal());
		basic_move=4;
		basic_weapon_rank=new int[] {-65536,1,-65536,-65536,-65536,-65536,-65536,-65536,-65536,-65536,-65536};
		ride=0;//not ride
		skills=new ArrayList<Integer>();
	}
}

class Cavalier extends OccupationBank{
	Cavalier(){
		special_effect_group=new ArrayList<Integer>();
		special_effect_group.add(SpecialEffectGroup.HUMAN.ordinal());
		special_effect_group.add(SpecialEffectGroup.HORSE.ordinal());
		basic_move=6;
		basic_weapon_rank=new int[] {-65536,1,1,-65536,-65536,-65536,-65536,-65536,-65536,-65536,-65536};
		ride=1;//ride
		skills=new ArrayList<Integer>();
	}
}