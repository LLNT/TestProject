import java.util.ArrayList;
import java.util.List;

public class ItemBank {
	protected int max_wear;
	protected int value;
	protected WeaponType type;
	protected int power;
	protected int weight;
	protected int hit;
	protected int critical;
	protected int max_range;
	protected int min_range;
	protected int[] ability_bonus=new int[Global.CURRENT_ABILITY_VECTOR_LENGTH];
	protected List<Integer> special_effect;
	protected int rank;
	protected List<Integer> skills;
	protected String info;
	protected int use() {
		return 0;
	}
	protected int equip(){
		return 0;
	}
	private ItemBank[] itembank=new ItemBank[Global.ITEM_BANK_SIZE];
	ItemBank(){}
	ItemBank(int i){
		itembank[ItemOrd.DUMMY_ITEM.ordinal()]=new Dummy_Item();
		itembank[ItemOrd.IRON_SWORD.ordinal()]=new Iron_Sword();
		itembank[ItemOrd.STEEL_SWORD.ordinal()]=new Steel_Sword();
		itembank[ItemOrd.IRON_LANCE.ordinal()]=new Iron_Lance();
	}
	public ItemBank get_item(ItemOrd itemord){
		return itembank[itemord.ordinal()];
	}
}
class Dummy_Item extends ItemBank{
	Dummy_Item(){
		info="Dummy Item";
		type=WeaponType.DUMMY_WEAPON;
		weight=0;
	}
}
class Iron_Sword extends ItemBank{
	Iron_Sword(){
		info="Iron Sword";
		max_wear=46;
		value=460;
		type=WeaponType.SWORD;
		power=5;
		weight=5;
		hit=85;
		critical=0;
		max_range=1;
		min_range=1;
		for (int i=0;i<Global.CURRENT_ABILITY_VECTOR_LENGTH;i++){
			ability_bonus[i]=0;
		}
		special_effect=new ArrayList<Integer>();
		rank=1;
		skills=new ArrayList<Integer> ();
	}
	protected final int use(){
		return -1;
	}
	protected final int equip(){
		return ItemOrd.IRON_SWORD.ordinal();
	}
	
}
class Iron_Lance extends ItemBank{
	Iron_Lance(){
		info="Iron Lance";
		max_wear=45;
		value=300;
		type=WeaponType.LANCE;
		power=7;
		weight=8;
		hit=70;
		critical=0;
		max_range=1;
		min_range=1;
		for (int i=0;i<Global.CURRENT_ABILITY_VECTOR_LENGTH;i++){
			ability_bonus[i]=0;
		}
		special_effect=new ArrayList<Integer>();
		rank=1;
		skills=new ArrayList<Integer> ();
	}
	protected final int use(){
		return -1;
	}
	protected final int equip(){
		return ItemOrd.IRON_LANCE.ordinal();
	}	
}
class Steel_Sword extends ItemBank{
	Steel_Sword(){
		info="Steel Sword";
		max_wear=30;
		value=600;
		type=WeaponType.SWORD;
		power=8;
		weight=10;
		hit=70;
		critical=0;
		max_range=1;
		min_range=1;
		for (int i=0;i<Global.CURRENT_ABILITY_VECTOR_LENGTH;i++){
			ability_bonus[i]=0;
		}
		rank=30;
		special_effect=new ArrayList<Integer>();
		skills=new ArrayList<Integer> ();
	}
	protected final int use(){
		return -1;
	}
	protected final int equip(){
		return ItemOrd.STEEL_SWORD.ordinal();
	}
	
}
