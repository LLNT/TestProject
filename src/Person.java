import java.util.ArrayList;
import java.util.List;

public class Person {
	
	private int[] ability =new int[Global.ABILITY_VECTOR_LENGTH];
	public int[] current_ability=new int[Global.CURRENT_ABILITY_VECTOR_LENGTH];
	public Item[] items=new Item[Global.MAX_ITEMS];
	public Occupation occupation;
	public int exp;
	public String info;
	public int[] weapon_rank=new int[Global.WEAPON_RANK_VECTOR_LENGTH];
	public List<Integer> skills;
	public int item_count=0;
	Person(int[] abl, String inf, Item[] its, Occupation occ, int[] weap, SkillOrd[] sks){
		info=inf;
		occupation=occ;
		for (int i=0;i<Global.ABILITY_VECTOR_LENGTH;i++){
			ability[i]=abl[i];
		}
		for (int i=0;i<its.length;i++){
			items[i]=its[i];
			item_count+=1;
		}
		ability[Ability.MOV.ordinal()]+=occupation.occupation.basic_move;
		for (int i=0;i<Global.ABILITY_VECTOR_LENGTH;i++){
			current_ability[i]=ability[i];
			for (int j=0;j<item_count;j++){
				current_ability[i]+=items[j].itemtype.ability_bonus[i];
			}
		}
		current_ability[Ability.HP.ordinal()]=current_ability[Ability.MHP.ordinal()];
		current_ability[Ability.CRY.ordinal()]=current_ability[Ability.BLD.ordinal()]-1;
		exp=0;
		for (int i=0;i<Global.WEAPON_RANK_VECTOR_LENGTH;i++){
			weapon_rank[i]=weap[i]+occupation.occupation.basic_weapon_rank[i];
		}
		skills=new ArrayList<Integer>();
		for (int i=0;i<sks.length;i++){
			skills.add(sks[i].ordinal());
		}
	}
	public int equip_wp(int pos){
		if (pos>=item_count){
			return -1;//pos overflow
		}
		if ((items[pos].itemtype.type==WeaponType.DUMMY_WEAPON)||
				(items[pos].itemtype.type==WeaponType.CARRIABLE)||
				(items[pos].itemtype.type==WeaponType.USABLE)||
				(items[pos].itemtype.type==WeaponType.WAND)){
			return -2;//type error
		}
		if (weapon_rank[items[pos].itemtype.type.ordinal()]<items[pos].itemtype.rank){
			return -3;//rank not enough
		}
		Item temp=items[0];
		items[0]=items[pos];
		items[pos]=temp;
		return 0;
	}
	public Item get_equip(){
		for (Item item:items){
			if ((item.itemtype.type!=WeaponType.CARRIABLE)&&(item.itemtype.type!=WeaponType.USABLE)&&(item.itemtype.type!=WeaponType.WAND)){
				if (item.itemtype.rank<=weapon_rank[item.itemtype.type.ordinal()]){
					return item;
				}
			}
		}
		return new Item(ItemOrd.DUMMY_ITEM);
	}

}
