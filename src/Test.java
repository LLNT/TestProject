
public class Test {
	public static void main(String[] args){
		for (OccupationOrd ord:OccupationOrd.values()){
			System.out.println(ord.toString());
		}
		Item sword1=new Item(ItemOrd.IRON_SWORD);
		Item sword2=new Item(ItemOrd.STEEL_SWORD);
		Item sword3=new Item(ItemOrd.STEEL_SWORD);
		Item lance1=new Item(ItemOrd.IRON_LANCE);
		int[] abl1=new int[]{0,1,30,5,5,5,5,3,3,5,5,0};
		int[] abl2=new int[]{0,2,50,7,5,2,2,4,4,0,6,0};
		String inf1="Lyn";
		String inf2="Kent";
		Item its1[]=new Item[]{sword3,sword1};
		Item its2[]=new Item[]{lance1,sword2};
		Occupation occ1=new Occupation(OccupationOrd.LORD);
		Occupation occ2=new Occupation(OccupationOrd.CAVALIER);
		int weap1[]=new int[]{0,10,0,0,0,0,0,0,0,0,0};
		int weap2[]=new int[]{0,100,100,0,0,0,0,0,0,0,0};
		SkillOrd[] sks1=new SkillOrd[]{SkillOrd.SHOOTINGSTAR,SkillOrd.CONTINUE,SkillOrd.MOONLIGHT};
		SkillOrd[] sks2=new SkillOrd[]{SkillOrd.SHIELD};
		Person lyn=new Person(abl1,inf1,its1,occ1,weap1,sks1);
		Person kent=new Person(abl2,inf2,its2,occ2,weap2,sks2);
		int dist=1;
		int su_lyn[]=new int[]{0,0,0,0,0,0,0};
		int su_kent[]=new int[]{0,0,0,0,0,0,0};
		kent.equip_wp(0);
		Battle testbattle=new Battle(lyn,kent,su_lyn,su_kent,dist);
		testbattle.battle();
		System.out.println(kent.current_ability[Ability.HP.ordinal()]);
	}
}
