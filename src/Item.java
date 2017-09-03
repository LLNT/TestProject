
public class Item {
	public ItemBank itemtype;
	public int wear;
	Item(ItemOrd itemord){
		itemtype=Global.itembank.get_item(itemord);
		wear=itemtype.max_wear;
	}
}
