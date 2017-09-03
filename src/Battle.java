import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class Battle {
	class Attack{
		int AorD;//0=A,1=D
		int continued_attack;
		int wea_dup_attack;
		int shootingstar_attack;
		int sky_ecl_attack;
		int moonlight_attack;
		Attack(int a,int cn, int wd, int sh, int se, int ml){
			AorD=a;
			continued_attack=cn;
			wea_dup_attack=wd;
			shootingstar_attack=sh;
			sky_ecl_attack=se;
			moonlight_attack=ml;
		}
	}
	private Deque<Attack> queue;
	private int dist;
	private int wear_buf_a;
	private int wear_buf_d;
	private int exp_buf_a;
	private int exp_buf_d;
	private int charged_combat;
	private int att_sun;
	private int att_moon;
	private int att_wrath;
	private int att_sup_eff;
	private int att_promised;
	private int att_crt;
	private int att_shield;
	private int bspd_a;
	private int bspd_d;
	private int wp_buf_a;
	private int wp_buf_d;
	private Item weapon_a;
	private Item weapon_d;
	private int weapon_rank_buf_a;
	private int weapon_rank_buf_d;
	private List<Integer> skills_a;
	private List<Integer> skills_d;
	private int battleround;//0=both attack, 1=no counter
	int[] su_d=new int[Global.BATTLE_ATTR_VECTOR_LENGTH];
	int[] su_a=new int[Global.BATTLE_ATTR_VECTOR_LENGTH];
	Person a;
	Person d;
	Battle(Person _a, Person _d, int[] _sua, int[] _sud, int _dist){
		dist=_dist;
		a=_a;
		d=_d;
		for (int i=0;i<_sua.length;i++){
			su_a[i]=_sua[i];
		}
		for (int i=0;i<_sud.length;i++){
			su_d[i]=_sud[i];
		}
		exp_buf_a=a.exp;
		exp_buf_d=d.exp;
		wp_buf_a=0;
		wp_buf_d=0;
		battleround=0;
		att_sun=0;
		att_moon=0;
		att_wrath=0;
		att_sup_eff=0;
		att_promised=0;
		att_crt=0;
		att_shield=0;
		charged_combat=0;
		weapon_a=a.get_equip();
		weapon_d=d.get_equip();
		if (weapon_d.itemtype.type==WeaponType.DUMMY_WEAPON){
			battleround=1;
		}
		else{
			if (weapon_d.itemtype.max_range<dist){
				battleround=1;
			}
			if (weapon_d.itemtype.min_range>dist){
				battleround=1;
			}
		}
		wp_buf_a=wp_buf_count();
		wp_buf_d=-wp_buf_a;
		weapon_rank_buf_a=a.weapon_rank[weapon_a.itemtype.type.ordinal()];
		weapon_rank_buf_d=0;
		if (battleround==0){
			weapon_rank_buf_d=d.weapon_rank[weapon_d.itemtype.type.ordinal()];
		}
		wear_buf_a=weapon_a.wear;
		wear_buf_d=0;
		if (battleround==0){
			wear_buf_d=weapon_d.wear;
		}
		skills_a=new ArrayList<Integer>();
		skills_d=new ArrayList<Integer>();
		for (int skl:weapon_a.itemtype.skills){
			skills_a.add(skl);
		}
		for (int skl:a.skills){
			skills_a.add(skl);
		}
		for (int skl:a.occupation.occupation.skills){
			skills_a.add(skl);
		}
		for (int skl:weapon_d.itemtype.skills){
			skills_d.add(skl);
		}
		for (int skl:d.skills){
			skills_d.add(skl);
		}
		for (int skl:d.occupation.occupation.skills){
			skills_d.add(skl);
		}
		int aw_a=0;
		int aw_d=0;
		if (skills_a.contains(SkillOrd.AWARENESS.ordinal())){
			aw_a=1;
		}
		if (skills_d.contains(SkillOrd.AWARENESS.ordinal())){
			aw_d=1;
		}
		if (aw_a==1){
			skills_d.clear();
		}
		if (aw_d==1){
			skills_a.clear();
		}
		bspd_a=a.current_ability[Ability.SPD.ordinal()];
		bspd_d=d.current_ability[Ability.SPD.ordinal()];
		if (weapon_a.itemtype.weight>a.current_ability[Ability.BLD.ordinal()]){
			bspd_a+=(a.current_ability[Ability.BLD.ordinal()]-weapon_a.itemtype.weight);
		}
		if (weapon_d.itemtype.weight>d.current_ability[Ability.BLD.ordinal()]){
			bspd_d+=(d.current_ability[Ability.BLD.ordinal()]-weapon_d.itemtype.weight);
		}
		queue=new ArrayDeque<Attack>();
	}
	int wp_buf_count(){
		if (weapon_a.itemtype.type==WeaponType.SWORD){
			if (weapon_d.itemtype.type==WeaponType.AXE){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.LANCE){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.LANCE){
			if (weapon_d.itemtype.type==WeaponType.SWORD){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.AXE){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.AXE){
			if (weapon_d.itemtype.type==WeaponType.LANCE){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.SWORD){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.FIRE){
			if (weapon_d.itemtype.type==WeaponType.LIGHT){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.WIND){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.DARK){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.THUNDER){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.THUNDER){
			if (weapon_d.itemtype.type==WeaponType.LIGHT){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.FIRE){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.DARK){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.WIND){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.WIND){
			if (weapon_d.itemtype.type==WeaponType.LIGHT){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.THUNDER){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.DARK){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.FIRE){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.LIGHT){
			if (weapon_d.itemtype.type==WeaponType.DARK){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.FIRE){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.THUNDER){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.WIND){
				return -1;
			}
			return 0;
		}
		if (weapon_a.itemtype.type==WeaponType.DARK){
			if (weapon_d.itemtype.type==WeaponType.LIGHT){
				return -1;
			}
			if (weapon_d.itemtype.type==WeaponType.FIRE){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.THUNDER){
				return 1;
			}
			if (weapon_d.itemtype.type==WeaponType.WIND){
				return 1;
			}
			return 0;
		}
		return 0;
	}
	int ambush_a(){
		if (!skills_a.contains(SkillOrd.AMBUSH.ordinal())){
			return 0;
		}
		if (a.current_ability[Ability.HP.ordinal()]>a.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		if (bspd_a*1.5<bspd_d){
			return 0;
		}
		return 1;
	}
	int ambush_d(){
		if (!skills_d.contains(SkillOrd.AMBUSH.ordinal())){
			return 0;
		}
		if (d.current_ability[Ability.HP.ordinal()]>d.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		if (bspd_d*1.5<bspd_a){
			return 0;
		}
		return 1;
	}
	int charge_a(){
		if (charged_combat==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.CHARGE.ordinal())){
			return 0;
		}
		if (a.current_ability[Ability.HP.ordinal()]<a.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		if (bspd_a<=bspd_d){
			return 0;
		}
		return 1;
	}
	int charge_d(){
		if (charged_combat==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.CHARGE.ordinal())){
			return 0;
		}
		if (d.current_ability[Ability.HP.ordinal()]<d.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		if (bspd_d<=bspd_a){
			return 0;
		}
		return 1;
	}
	int continue_a(int ca){
		if (ca==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.CONTINUE.ordinal())){
			return 0;
		}
		int p=a.current_ability[Ability.SPD.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int continue_d(int ca){
		if (ca==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.CONTINUE.ordinal())){
			return 0;
		}
		int p=d.current_ability[Ability.SPD.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int weapon_duplicate_a(int wd){
		if (wd==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.WEAPONDUPLICATE.ordinal())){
			return 0;
		}
		return 1;
	}
	int weapon_duplicate_d(int wd){
		if (wd==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.WEAPONDUPLICATE.ordinal())){
			return 0;
		}
		return 1;
	}
	int shootingstar_a(int sh){
		if (sh==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.SHOOTINGSTAR.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(a.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int shootingstar_d(int sh){
		if (sh==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.SHOOTINGSTAR.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(d.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int skylight_a(int se){
		if (se==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.SKYLIGHT.ordinal())){
			return 0;
		}
		int p=a.current_ability[Ability.SKL.ordinal()];
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int skylight_d(int se){
		if (se==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.SKYLIGHT.ordinal())){
			return 0;
		}
		int p=d.current_ability[Ability.SKL.ordinal()];
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int eclipse_a(int se){
		if (se==1){
			return 0;
		}
		if (!skills_a.contains(SkillOrd.ECLIPSE.ordinal())){
			return 0;
		}
		int p=a.current_ability[Ability.SKL.ordinal()];
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int eclipse_d(int se){
		if (se==1){
			return 0;
		}
		if (!skills_d.contains(SkillOrd.ECLIPSE.ordinal())){
			return 0;
		}
		int p=d.current_ability[Ability.SKL.ordinal()];
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int moonlight_a(){
		if (!skills_a.contains(SkillOrd.MOONLIGHT.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(a.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int moonlight_d(){
		if (!skills_d.contains(SkillOrd.MOONLIGHT.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(d.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int wrath_a(){
		if (!skills_a.contains(SkillOrd.WRATH.ordinal())){
			return 0;
		}
		if (a.current_ability[Ability.HP.ordinal()]>a.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		return 1;
	}
	int wrath_d(){
		if (!skills_d.contains(SkillOrd.WRATH.ordinal())){
			return 0;
		}
		if (d.current_ability[Ability.HP.ordinal()]>d.current_ability[Ability.MHP.ordinal()]/2){
			return 0;
		}
		return 1;
	}
	int promised_a(){
		if (!skills_a.contains(SkillOrd.PROMISED.ordinal())){
			return 0;
		}
		return 1;
	}
	int promised_d(){
		if (!skills_d.contains(SkillOrd.PROMISED.ordinal())){
			return 0;
		}
		return 1;
	}
	int sunlight_a(){
		if (!skills_a.contains(SkillOrd.SUNLIGHT.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(a.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int sunlight_d(){
		if (!skills_d.contains(SkillOrd.SUNLIGHT.ordinal())){
			return 0;
		}
		int p=(int) Math.ceil(d.current_ability[Ability.SKL.ordinal()]*1.5);
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int shield_a(){
		if (!skills_a.contains(SkillOrd.SHIELD.ordinal())){
			return 0;
		}
		int p=a.current_ability[Ability.LV.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int shield_d(){
		if (!skills_d.contains(SkillOrd.SHIELD.ordinal())){
			return 0;
		}
		int p=d.current_ability[Ability.LV.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int prayer_a(){
		if (!skills_a.contains(SkillOrd.PRAYER.ordinal())){
			return 0;
		}
		int p=a.current_ability[Ability.LUK.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int prayer_d(){
		if (!skills_d.contains(SkillOrd.PRAYER.ordinal())){
			return 0;
		}
		int p=d.current_ability[Ability.LUK.ordinal()]*2;
		Random random=new Random();
		int q=random.nextInt(99);
		if (q>=p){
			return 0;
		}
		return 1;
	}
	int battle(){
		System.out.println("Battle began with "+a.info+" against "+d.info+"!");
		System.out.println(a.info+" has "+String.valueOf(a.current_ability[Ability.HP.ordinal()])+" hp!");
		System.out.println(d.info+" has "+String.valueOf(d.current_ability[Ability.HP.ordinal()])+" hp!");
		
		if (battleround==0){
			return battlea();
		}
		else{
			return battleb();
		}
	}
	int battlea(){
		String tempa="";
		String tempd="";
		if (wp_buf_a>0){
			tempa="Superiority!!  ";
			tempd="Inferiority!!  ";
		}
		if (wp_buf_a<0){
			tempa="Inferiority!!  ";
			tempd="Superiority!!  ";
		}
		int hita=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]*2
				+weapon_a.itemtype.hit
				+a.current_ability[Ability.LUK.ordinal()])
				*(100+su_a[BattleAttr.HIT.ordinal()])/100);
		int avod=(int) Math.ceil((d.current_ability[Ability.SPD.ordinal()]*2
				+d.current_ability[Ability.LUK.ordinal()])
				*(100+su_d[BattleAttr.AVO.ordinal()])/100);
		int hit_ba=hita-avod;
		hit_ba+=(wp_buf_a*10);
		if (hit_ba<0){
			hit_ba=0;
		}
		if (hit_ba>100){
			hit_ba=100;
		}
		int atka=0;
		int defd=0;
		if ((weapon_a.itemtype.type==WeaponType.SWORD)||(weapon_a.itemtype.type==WeaponType.LANCE)
				||(weapon_a.itemtype.type==WeaponType.AXE)||(weapon_a.itemtype.type==WeaponType.BOW)){
			atka=a.current_ability[Ability.STR.ordinal()];
			defd=d.current_ability[Ability.DEF.ordinal()];
		}
		if ((weapon_a.itemtype.type==WeaponType.FIRE)||(weapon_a.itemtype.type==WeaponType.THUNDER)
				||(weapon_a.itemtype.type==WeaponType.WIND)||(weapon_a.itemtype.type==WeaponType.LIGHT)
				||(weapon_a.itemtype.type==WeaponType.DARK)){
			atka=a.current_ability[Ability.MGC.ordinal()];
			defd=d.current_ability[Ability.RES.ordinal()];
		}
		atka+=weapon_a.itemtype.power;
		atka+=su_a[BattleAttr.ATK.ordinal()];
		defd+=su_d[BattleAttr.DEF.ordinal()];
		int dmg_ba=atka-defd;
		dmg_ba+=wp_buf_a;
		if (dmg_ba<=0){
			dmg_ba=0;
		}
		int crta=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]/2
				+weapon_a.itemtype.critical+
				a.current_ability[Ability.LUK.ordinal()]/4)
				*(100+su_a[BattleAttr.CRT.ordinal()])/100);
		int crad=(int) Math.ceil((d.current_ability[Ability.LUK.ordinal()]/2
				+d.current_ability[Ability.SKL.ordinal()]/4
				+d.current_ability[Ability.SPD.ordinal()]/4)
				*(100+su_d[BattleAttr.CRA.ordinal()])/100);
		int crt_ba=crta-crad;
		if (crt_ba<0){
			crt_ba=0;
		}
		if (crt_ba>100){
			crt_ba=100;
		}
		System.out.println(tempa+a.info+" HIT="+String.valueOf(hit_ba)+" DMG="+String.valueOf(dmg_ba)+" CRT="+String.valueOf(crt_ba));
		int hitd=(int) Math.ceil((d.current_ability[Ability.SKL.ordinal()]*2
				+weapon_d.itemtype.hit
				+d.current_ability[Ability.LUK.ordinal()])
				*(100+su_d[BattleAttr.HIT.ordinal()])/100);
		int avoa=(int) Math.ceil((a.current_ability[Ability.SPD.ordinal()]*2
				+a.current_ability[Ability.LUK.ordinal()])
				*(100+su_a[BattleAttr.AVO.ordinal()])/100);
		int hit_bd=hitd-avoa;
		hit_bd+=wp_buf_d*10;
		if (hit_bd<0){
			hit_bd=0;
		}
		if (hit_bd>100){
			hit_bd=100;
		}
		int atkd=0;
		int defa=0;
		if ((weapon_d.itemtype.type==WeaponType.SWORD)||(weapon_d.itemtype.type==WeaponType.LANCE)
				||(weapon_d.itemtype.type==WeaponType.AXE)||(weapon_d.itemtype.type==WeaponType.BOW)){
			atkd=d.current_ability[Ability.STR.ordinal()];
			defa=a.current_ability[Ability.DEF.ordinal()];
		}
		if ((weapon_d.itemtype.type==WeaponType.FIRE)||(weapon_d.itemtype.type==WeaponType.THUNDER)
				||(weapon_d.itemtype.type==WeaponType.WIND)||(weapon_d.itemtype.type==WeaponType.LIGHT)
				||(weapon_d.itemtype.type==WeaponType.DARK)){
			atkd=d.current_ability[Ability.MGC.ordinal()];
			defa=a.current_ability[Ability.RES.ordinal()];
		}
		atkd+=weapon_d.itemtype.power;
		atkd+=su_d[BattleAttr.ATK.ordinal()];
		defa+=su_a[BattleAttr.DEF.ordinal()];
		int dmg_bd=atkd-defa;
		dmg_bd+=wp_buf_d;
		if (dmg_bd<=0){
			dmg_bd=0;
		}
		int crtd=(int) Math.ceil((d.current_ability[Ability.SKL.ordinal()]/2
				+weapon_d.itemtype.critical+
				d.current_ability[Ability.LUK.ordinal()]/4)
				*(100+su_d[BattleAttr.CRT.ordinal()])/100);
		int craa=(int) Math.ceil((a.current_ability[Ability.LUK.ordinal()]/2
				+a.current_ability[Ability.SKL.ordinal()]/4
				+a.current_ability[Ability.SPD.ordinal()]/4)
				*(100+su_a[BattleAttr.CRA.ordinal()])/100);
		int crt_bd=crtd-craa;
		if (crt_bd<0){
			crt_bd=0;
		}
		if (crt_bd>100){
			crt_bd=100;
		}
		System.out.println(tempd+d.info+" HIT="+String.valueOf(hit_bd)+" DMG="+String.valueOf(dmg_bd)+" CRT="+String.valueOf(crt_bd));
		
		queue.push(new Attack(0,0,0,0,0,0));
		queue.push(new Attack(1,0,0,0,0,0));
		if (ambush_d()==1){
			queue.pop();queue.pop();
			queue.push(new Attack(1,0,0,0,0,0));
			queue.push(new Attack(0,0,0,0,0,0));
			System.out.println(d.info+" plays Ambush!");
		}
		if (ambush_a()==1){
			queue.pop();queue.pop();
			queue.push(new Attack(0,0,0,0,0,0));
			queue.push(new Attack(1,0,0,0,0,0));
			System.out.println(a.info+" plays Ambush!");
		}
		if (bspd_a>=bspd_d+4){
			queue.push(new Attack(0,0,0,0,0,0));
		}
		if (bspd_d>=bspd_a+4){
			queue.push(new Attack(1,0,0,0,0,0));
		}
		while (true){
			if (queue.isEmpty()){
				if ((charge_d()==1)||(charge_a()==1)){
					System.out.println("CHARGE!");
					queue.push(new Attack(0,0,0,0,0,0));
					queue.push(new Attack(1,0,0,0,0,0));
					if (bspd_a>=bspd_d+4){
						queue.push(new Attack(0,0,0,0,0,0));
					}
					if (bspd_d>=bspd_a+4){
						queue.push(new Attack(1,0,0,0,0,0));
					}
				}
				else{
					break;
				}
			}
			Attack att=queue.getLast();
			queue.removeLast();
			int r=execute(att);
			
			System.out.println(a.info+" has "+String.valueOf(a.current_ability[Ability.HP.ordinal()])+" hp!");
			System.out.println(d.info+" has "+String.valueOf(d.current_ability[Ability.HP.ordinal()])+" hp!");
			if (r==0){
				if (att.AorD==0){
					System.out.println("Notice! "+a.info+" is run out of weapon!");
				}
				else{
					System.out.println("Notice! "+d.info+" is run out of weapon!");
				}
			}
			if (r==2){
				if (att.AorD==0){
					System.out.println("Battle Finished with "+d.info+" dies");
					return 1;//D dies
				}
				else{
					System.out.println("Battle Finished with "+a.info+" dies");
					return 2;//A dies
				}
			}
		}
		System.out.println("Battle Finished ");
		return 0;
	}
	int battleb(){
		int hita=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]*2
				+weapon_a.itemtype.hit
				+a.current_ability[Ability.LUK.ordinal()])
				*(100+su_a[BattleAttr.HIT.ordinal()])/100);
		int avod=(int) Math.ceil((d.current_ability[Ability.SPD.ordinal()]*2
				+d.current_ability[Ability.LUK.ordinal()])
				*(100+su_d[BattleAttr.AVO.ordinal()])/100);
		int hit_ba=hita-avod;
		hit_ba+=wp_buf_a*10;
		if (hit_ba<0){
			hit_ba=0;
		}
		if (hit_ba>100){
			hit_ba=100;
		}
		int atka=0;
		int defd=0;
		if ((weapon_a.itemtype.type==WeaponType.SWORD)||(weapon_a.itemtype.type==WeaponType.LANCE)
				||(weapon_a.itemtype.type==WeaponType.AXE)||(weapon_a.itemtype.type==WeaponType.BOW)){
			atka=a.current_ability[Ability.STR.ordinal()];
			defd=d.current_ability[Ability.DEF.ordinal()];
		}
		if ((weapon_a.itemtype.type==WeaponType.FIRE)||(weapon_a.itemtype.type==WeaponType.THUNDER)
				||(weapon_a.itemtype.type==WeaponType.WIND)||(weapon_a.itemtype.type==WeaponType.LIGHT)
				||(weapon_a.itemtype.type==WeaponType.DARK)){
			atka=a.current_ability[Ability.MGC.ordinal()];
			defd=d.current_ability[Ability.RES.ordinal()];
		}
		atka+=weapon_a.itemtype.power;
		atka+=su_a[BattleAttr.ATK.ordinal()];
		defd+=su_d[BattleAttr.DEF.ordinal()];
		int dmg_ba=atka-defd;
		dmg_ba+=wp_buf_a;
		if (dmg_ba<=0){
			dmg_ba=0;
		}
		int crta=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]/2
				+weapon_a.itemtype.critical+
				a.current_ability[Ability.LUK.ordinal()]/4)
				*(100+su_a[BattleAttr.CRT.ordinal()])/100);
		int crad=(int) Math.ceil((d.current_ability[Ability.LUK.ordinal()]/2
				+d.current_ability[Ability.SKL.ordinal()]/4
				+d.current_ability[Ability.SPD.ordinal()]/4)
				*(100+su_d[BattleAttr.CRA.ordinal()])/100);
		int crt_ba=crta-crad;
		if (crt_ba<0){
			crt_ba=0;
		}
		if (crt_ba>100){
			crt_ba=100;
		}
		String tempa="";
		String tempd="";
		if (wp_buf_a>0){
			tempa="Superiority!!  ";
			tempd="Inferiority!!  ";
		}
		if (wp_buf_a<0){
			tempa="Inferiority!!  ";
			tempd="Superiority!!  ";
		}
		System.out.println(tempa+a.info+" HIT="+String.valueOf(hit_ba)+" DMG="+String.valueOf(dmg_ba)+" CRT="+String.valueOf(crt_ba));
		System.out.println(tempd+d.info+" HIT=/ DMG=/ CRT=/");
		queue.push(new Attack(0,0,0,0,0,0));
		if (bspd_a>=bspd_d+4){
			queue.push(new Attack(0,0,0,0,0,0));
		}
		while (true){
			if (queue.isEmpty()){
				if ((charge_d()==1)||(charge_a()==1)){
					System.out.println("CHARGE!");
					queue.push(new Attack(0,0,0,0,0,0));
					if (bspd_a>=bspd_d+4){
						queue.push(new Attack(0,0,0,0,0,0));
					}
				}
				else{
					break;
				}
			}
			Attack att=queue.getLast();
			queue.removeLast();
			int r=execute(att);
			System.out.println(a.info+" has "+String.valueOf(a.current_ability[Ability.HP.ordinal()])+" hp!");
			System.out.println(d.info+" has "+String.valueOf(d.current_ability[Ability.HP.ordinal()])+" hp!");
			if (r==0){
				if (att.AorD==0){
					System.out.println("Notice! "+a.info+" is run out of weapon!");
				}
				else{
					System.out.println("Notice! "+d.info+" is run out of weapon!");
				}
			}
			if (r==2){
				if (att.AorD==0){
					System.out.println("Battle Finished with "+d.info+" dies");
					return 1;//D dies
				}
				else{
					System.out.println("Battle Finished with "+a.info+" dies");
					return 2;//A dies
				}
			}
		}
		System.out.println("Battle Finished ");
		return 0;
	}
	int execute(Attack att){
		if (att.AorD==0){
			return executea(att);
		}
		else{
			return executed(att);
		}
	}
	int executea(Attack att){
		if (wear_buf_a==0){
			return 0; //no weapon
		}
		System.out.println(a.info+" Attacks!");
		if (continue_a(att.continued_attack)==1){
			queue.addLast(new Attack(0,1,0,0,0,0));
			System.out.println("Continue!");
		}
		if (weapon_duplicate_a(att.wea_dup_attack)==1){
			queue.addLast(new Attack(0,att.continued_attack,1,0,0,0));
		}
		if (shootingstar_a(att.shootingstar_attack)==1){
			queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,0,0));
			System.out.println("Shootingstar!");
		}
		if (skylight_a(att.sky_ecl_attack)==1){
			att_sun=1;
			queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,1));
			System.out.println("Skylight!");
		}
		else{
			if (eclipse_a(att.sky_ecl_attack)==1){
				att_sun=1;
				queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(0,att.continued_attack,att.wea_dup_attack,1,1,0));
				System.out.println("Eclipse!");
			}
		}
		if (att.moonlight_attack==1){
			att_moon=1;
		}
		if (moonlight_a()==1){
			att_moon=1;
			System.out.println("Moonlight!");
		}
		if (wrath_a()==1){
			att_wrath=1;
			System.out.println("Wrath!");
		}
		for (int seg:d.occupation.occupation.special_effect_group){
			if (weapon_a.itemtype.special_effect.contains(seg)){
				att_sup_eff=1;
			}
		}
		if (promised_d()==1){
			att_promised=1;
			System.out.println(d.info+" plays Promised!");
		}
		int crt=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]/2
				+weapon_a.itemtype.critical+
				a.current_ability[Ability.LUK.ordinal()]/4)
				*(100+su_a[BattleAttr.CRT.ordinal()])/100*(1+0.5*att_wrath));
		int cra=(int) Math.ceil((d.current_ability[Ability.LUK.ordinal()]/2
				+d.current_ability[Ability.SKL.ordinal()]/4
				+d.current_ability[Ability.SPD.ordinal()]/4)
				*(100+su_d[BattleAttr.CRA.ordinal()])/100
				+1023*att_promised);
		int crt_b=crt-cra;
		if (crt_b<0){
			crt_b=0;
		}
		if (crt_b>100){
			crt_b=100;
		}
		Random random=new Random();
		int q=random.nextInt(99);
		if (q<crt_b){
			att_crt=1;
		}
		if(sunlight_a()==1){
			att_sun=1;
		}
		int hit=(int) Math.ceil((a.current_ability[Ability.SKL.ordinal()]*2
				+weapon_a.itemtype.hit
				+a.current_ability[Ability.LUK.ordinal()])
				*(100+su_a[BattleAttr.HIT.ordinal()])/100);
		int avo=(int) Math.ceil((d.current_ability[Ability.SPD.ordinal()]*2
				+d.current_ability[Ability.LUK.ordinal()])
				*(100+su_d[BattleAttr.AVO.ordinal()])/100);
		int hit_b=hit-avo+(wp_buf_a*10);
		if (hit_b<0){
			hit_b=0;
		}
		if (hit_b>100){
			hit_b=100;
		}
		int atk=0;
		int def=0;
		if ((weapon_a.itemtype.type==WeaponType.SWORD)||(weapon_a.itemtype.type==WeaponType.LANCE)
				||(weapon_a.itemtype.type==WeaponType.AXE)||(weapon_a.itemtype.type==WeaponType.BOW)){
			atk=a.current_ability[Ability.STR.ordinal()];
			def=d.current_ability[Ability.DEF.ordinal()];
		}
		if ((weapon_a.itemtype.type==WeaponType.FIRE)||(weapon_a.itemtype.type==WeaponType.THUNDER)
				||(weapon_a.itemtype.type==WeaponType.WIND)||(weapon_a.itemtype.type==WeaponType.LIGHT)
				||(weapon_a.itemtype.type==WeaponType.DARK)){
			atk=a.current_ability[Ability.MGC.ordinal()];
			def=d.current_ability[Ability.RES.ordinal()];
		}
		atk+=weapon_a.itemtype.power;
		atk+=su_a[BattleAttr.ATK.ordinal()];
		atk*=(1+att_sup_eff);
		def+=su_d[BattleAttr.DEF.ordinal()];
		def*=(1-att_moon);
		if (shield_d()==1){
			att_shield=1;
			System.out.println(d.info+" plays Shield!");
		}
		int dmg=(atk-def+wp_buf_a)*(1-att_shield)*(1+2*att_crt);
		if (dmg<0){
			dmg=0;
		}
		if (dmg>d.current_ability[Ability.HP.ordinal()]){
			dmg=d.current_ability[Ability.HP.ordinal()];
		}
		if (dmg==d.current_ability[Ability.HP.ordinal()]){
			if(prayer_d()==1){
				hit_b=0;
				System.out.println(d.info+" plays Prayer!");
			}
		}
		Random rand1=new Random();
		int q1=rand1.nextInt(99);
		if (q1<hit_b){
			if (att_crt==1){
				System.out.println("CRITICAL!!!");
			}
			if (att_sun==1){
				int hpadd=dmg;
				if (hpadd>(a.current_ability[Ability.MHP.ordinal()]-a.current_ability[Ability.HP.ordinal()])){
					hpadd=a.current_ability[Ability.MHP.ordinal()]-a.current_ability[Ability.HP.ordinal()];
				}
				a.current_ability[Ability.HP.ordinal()]+=hpadd;
				if (hpadd>0){
				System.out.println(String.valueOf(hpadd)+" hp is added by "+a.info+"!");
				}
			}
			weapon_rank_buf_a+=1;
			exp_buf_a+=1;
			exp_buf_d+=1;
			att_sun=0;
			att_moon=0;
			att_wrath=0;
			att_sup_eff=0;
			att_promised=0;
			att_crt=0;
			att_shield=0;
			if (dmg==0){
				System.out.println("NO DAMAGE!!!");
			}
			else{
				System.out.println(String.valueOf(dmg)+" damage is given to "+d.info+"!");
			}
			d.current_ability[Ability.HP.ordinal()]-=dmg;
			if (d.current_ability[Ability.HP.ordinal()]<=0){
				System.out.println("OOPS! "+d.info+" is beaten up!");
				return 2;//d is beaten
			}
			else{
				return 1;//still alive
			}
		}
		else{
			weapon_rank_buf_a+=1;
			exp_buf_a+=1;
			exp_buf_d+=1;
			att_sun=0;
			att_moon=0;
			att_wrath=0;
			att_sup_eff=0;
			att_promised=0;
			att_crt=0;
			att_shield=0;
			System.out.println("MISS!!!");
			return 3;//miss
		}
	}
	int executed(Attack att){
		if (wear_buf_d==0){
			return 0; //no weapon
		}
		System.out.println(d.info+" Attacks!");
		if (continue_d(att.continued_attack)==1){
			queue.addLast(new Attack(1,1,0,0,0,0));
			System.out.println("Continue!");
		}
		if (weapon_duplicate_d(att.wea_dup_attack)==1){
			queue.addLast(new Attack(1,att.continued_attack,1,0,0,0));
		}
		if (shootingstar_d(att.shootingstar_attack)==1){
			queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,0,0));
			queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,0,0));
			System.out.println("Shootingstar!");
		}
		if (skylight_d(att.sky_ecl_attack)==1){
			att_sun=1;
			queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,1));
			System.out.println("Skylight!");
		}
		else{
			if (eclipse_d(att.sky_ecl_attack)==1){
				att_sun=1;
				queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,0));
				queue.addLast(new Attack(1,att.continued_attack,att.wea_dup_attack,1,1,0));
				System.out.println("Eclipse!");
			}
		}
		if (att.moonlight_attack==1){
			att_moon=1;
		}
		if (moonlight_d()==1){
			att_moon=1;
			System.out.println("Moonlight!");
		}
		if (wrath_d()==1){
			att_wrath=1;
			System.out.println("Wrath!");
		}
		for (int seg:a.occupation.occupation.special_effect_group){
			if (weapon_d.itemtype.special_effect.contains(seg)){
				att_sup_eff=1;
			}
		}
		if (promised_a()==1){
			att_promised=1;
			System.out.println(a.info+" plays Promised!");
		}
		int crt=(int) Math.ceil((d.current_ability[Ability.SKL.ordinal()]/2
				+weapon_d.itemtype.critical+
				d.current_ability[Ability.LUK.ordinal()]/4)
				*(100+su_d[BattleAttr.CRT.ordinal()])/100*(1+0.5*att_wrath));
		int cra=(int) Math.ceil((a.current_ability[Ability.LUK.ordinal()]/2
				+a.current_ability[Ability.SKL.ordinal()]/4
				+a.current_ability[Ability.SPD.ordinal()]/4)
				*(100+su_a[BattleAttr.CRA.ordinal()])/100
				+1023*att_promised);
		int crt_b=crt-cra;
		if (crt_b<0){
			crt_b=0;
		}
		if (crt_b>100){
			crt_b=100;
		}
		Random random=new Random();
		int q=random.nextInt(99);
		if (q<crt_b){
			att_crt=1;
		}
		if(sunlight_d()==1){
			att_sun=1;
		}
		int hit=(int) Math.ceil((d.current_ability[Ability.SKL.ordinal()]*2
				+weapon_d.itemtype.hit
				+d.current_ability[Ability.LUK.ordinal()])
				*(100+su_d[BattleAttr.HIT.ordinal()])/100);
		int avo=(int) Math.ceil((a.current_ability[Ability.SPD.ordinal()]*2
				+a.current_ability[Ability.LUK.ordinal()])
				*(100+su_a[BattleAttr.AVO.ordinal()])/100);
		int hit_b=hit-avo+(wp_buf_d*10);
		if (hit_b<0){
			hit_b=0;
		}
		if (hit_b>100){
			hit_b=100;
		}
		int atk=0;
		int def=0;
		if ((weapon_d.itemtype.type==WeaponType.SWORD)||(weapon_d.itemtype.type==WeaponType.LANCE)
				||(weapon_d.itemtype.type==WeaponType.AXE)||(weapon_d.itemtype.type==WeaponType.BOW)){
			atk=d.current_ability[Ability.STR.ordinal()];
			def=a.current_ability[Ability.DEF.ordinal()];
		}
		if ((weapon_d.itemtype.type==WeaponType.FIRE)||(weapon_d.itemtype.type==WeaponType.THUNDER)
				||(weapon_d.itemtype.type==WeaponType.WIND)||(weapon_d.itemtype.type==WeaponType.LIGHT)
				||(weapon_d.itemtype.type==WeaponType.DARK)){
			atk=d.current_ability[Ability.MGC.ordinal()];
			def=a.current_ability[Ability.RES.ordinal()];
		}
		atk+=weapon_d.itemtype.power;
		atk+=su_d[BattleAttr.ATK.ordinal()];
		atk*=(1+att_sup_eff);
		def+=su_a[BattleAttr.DEF.ordinal()];
		def*=(1-att_moon);
		if (shield_a()==1){
			att_shield=1;
			System.out.println(a.info+" plays Shield!");
		}
		int dmg=(atk-def+wp_buf_d)*(1-att_shield)*(1+2*att_crt);
		if (dmg<0){
			dmg=0;
		}
		if (dmg>a.current_ability[Ability.HP.ordinal()]){
			dmg=a.current_ability[Ability.HP.ordinal()];
		}
		if (dmg==a.current_ability[Ability.HP.ordinal()]){
			if(prayer_a()==1){
				hit_b=0;
				System.out.println(a.info+" plays Prayer!");
			}
		}
		Random rand1=new Random();
		int q1=rand1.nextInt(99);
		if (q1<hit_b){
			if (att_crt==1){
				System.out.println("CRITICAL!!!");
			}
			if (att_sun==1){
				int hpadd=dmg;
				if (hpadd>(d.current_ability[Ability.MHP.ordinal()]-d.current_ability[Ability.HP.ordinal()])){
					hpadd=d.current_ability[Ability.MHP.ordinal()]-d.current_ability[Ability.HP.ordinal()];
				}
				d.current_ability[Ability.HP.ordinal()]+=hpadd;
				if (hpadd>0){
				System.out.println(String.valueOf(hpadd)+" hp is added by "+d.info+"!");
				}
			}
			weapon_rank_buf_d+=1;
			exp_buf_a+=1;
			exp_buf_d+=1;
			att_sun=0;
			att_moon=0;
			att_wrath=0;
			att_sup_eff=0;
			att_promised=0;
			att_crt=0;
			att_shield=0;
			if (dmg==0){
				System.out.println("NO DAMAGE!!!");
			}
			else{
				System.out.println(String.valueOf(dmg)+" damage is given to "+a.info+"!");
			}
			a.current_ability[Ability.HP.ordinal()]-=dmg;
			if (a.current_ability[Ability.HP.ordinal()]<=0){
				System.out.println("OOPS! "+a.info+" is beaten up!");
				return 2;//d is beaten
			}
			else{
				return 1;//still alive
			}
		}
		else{
			weapon_rank_buf_d+=1;
			exp_buf_a+=1;
			exp_buf_d+=1;
			att_sun=0;
			att_moon=0;
			att_wrath=0;
			att_sup_eff=0;
			att_promised=0;
			att_crt=0;
			att_shield=0;
			System.out.println("MISS!!!");
			return 3;//miss
		}
	}
}
