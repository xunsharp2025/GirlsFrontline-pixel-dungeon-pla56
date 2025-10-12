package com.shatteredpixel.shatteredpixeldungeon.utils.diglog;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.SparseArray;

public class Script {
	public enum Character{
	   NONE,UMP45,HK416,G11,UMP9,STAR_45,EXCU_TIONER,M16A1,
	   UROBOROS,PPSH_47,P7,DESTROYER,DREAMER,GAGER,ELPHELT,
	   NOEL
	}

	public static Image AvatarUMP45(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,0,24,24,24);
			case 1: return new Image(Assets.EMOTION,24,24,24,24);
			case 2: return new Image(Assets.EMOTION,48,24,24,24);
		}
		return null;
	}

	public static Image AvatarG11(int index) {
		switch (index){
			case 0:return new Image(Assets.EMOTION,0,0,24,24);
			case 1:return new Image(Assets.EMOTION,24,0,24,24);
			case 2:return new Image(Assets.EMOTION,48,0,24,24);
		}
		return null;
	}

	public static Image AvatarUMP9(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,0,48,24,24);
			case 1: return new Image(Assets.EMOTION,24,48,24,24);
			case 2: return new Image(Assets.EMOTION,48,48,24,24);
		}
		return null;
	}

	public static Image AvatarHK416(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,0,72,24,24);
			case 1: return new Image(Assets.EMOTION,24,72,24,24);
			case 2: return new Image(Assets.EMOTION,48,72,24,24);
		}
		return null;
	}

	public static Image AvatarSTAR45(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,72,0,24,24);
			case 1: return new Image(Assets.EMOTION,96,0,24,24);
			case 2: return new Image(Assets.EMOTION,120,0,24,24);
		}
		return null;
	}

	public static Image NpcM16A1(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,72,24,24,24);
			case 1: return new Image(Assets.EMOTION,96,24,24,24);
			case 2: return new Image(Assets.EMOTION,120,24,24,24);
		}
		return null;
	}

	public static Image NpcPpsh47(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,72,48,24,24);
			case 1: return new Image(Assets.EMOTION,96,48,24,24);
			case 2: return new Image(Assets.EMOTION,120,48,24,24);
		}
		return null;
	}

	public static Image NpcP7(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,72,72,24,24);
			case 1: return new Image(Assets.EMOTION,96,72,24,24);
			case 2: return new Image(Assets.EMOTION,120,72,24,24);
		}
		return null;
	}

	public static Image NpcNoel(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,144,48,24,24);
			case 1: return new Image(Assets.EMOTION,168,48,24,24);
			case 2: return new Image(Assets.EMOTION,192,48,24,24);
		}
		return null;
	}

	public static Image BossExcutioner() {
		return new Image(Assets.EMOTION,216,0,24,24);
	}

	public static Image BossUroboros() {
		return new Image(Assets.EMOTION,216,24,24,24);
	}

	public static Image BossGager() {
		return new Image(Assets.EMOTION,216,48,24,24);
	}

	public static Image BossDestroyer(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,216,72,24,24);
			case 1: return new Image(Assets.EMOTION,240,72,24,24);
			case 2: return new Image(Assets.EMOTION,264,72,24,24);
		}
		return null;
	}

	public static Image BossDreamer(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,216,96,24,24);
			case 1: return new Image(Assets.EMOTION,240,96,24,24);
			case 2: return new Image(Assets.EMOTION,264,96,24,24);
		}
		return null;
	}

	public static Image BossElphelt(int index) {
		switch (index){
			case 0: return new Image(Assets.EMOTION,144,0,24,24);
			case 1: return new Image(Assets.EMOTION,168,0,24,24);
			case 2: return new Image(Assets.EMOTION,192,0,24,24);
		}
		return null;
	}

	public static String Name(Character character){
		String str=null;
		switch (character) {
			case UMP45:      str = Messages.get(Script.class,"name_ump45");break;
			case G11:        str = Messages.get(Script.class,"name_g11");break;
			case HK416:      str = Messages.get(Script.class,"name_hk416");break;
			case UMP9:       str = Messages.get(Script.class,"name_ump9");break;
			case STAR_45:    str = Messages.get(Script.class,"name_star15");break;
			case EXCU_TIONER:str = Messages.get(Script.class,"name_excutioner");break;
			case M16A1:      str = Messages.get(Script.class,"name_m16a1");break;
			case UROBOROS:   str = Messages.get(Script.class,"name_uroboros");break;
			case PPSH_47:    str = Messages.get(Script.class,"name_ppsh47");break;
			case P7:         str = Messages.get(Script.class,"name_p7");break;
			case DESTROYER:  str = Messages.get(Script.class,"name_destroyer");break;
			case DREAMER:    str = Messages.get(Script.class,"name_dreamer");break;
			case GAGER:      str = Messages.get(Script.class,"name_gager");break;
			case ELPHELT:    str = Messages.get(Script.class,"name_elphelt");break;
			case NOEL:       str = Messages.get(Script.class,"name_noel");break;
		}
		return str;
	}

	public static final int ID_SEWERS  = 0;
	public static final int ID_PRISON  = 1;
	public static final int ID_CAVES   = 2;
	public static final int ID_CITY    = 3;
	public static final int ID_RECAVES = 4;
	public static final int ID_HALLS   = 5;

	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();

	static {
		CHAPTERS.put( ID_SEWERS, "sewers" );
		CHAPTERS.put( ID_PRISON, "prison" );
		CHAPTERS.put( ID_CAVES, "caves" );
		CHAPTERS.put( ID_CITY, "city" );
		CHAPTERS.put( ID_RECAVES, "recaves");
		CHAPTERS.put( ID_HALLS, "halls" );
	};

	public static boolean checkChapter(int chapter) {
		if (Dungeon.chapters.contains(chapter)) {
			return false;
		} else {
			Dungeon.chapters.add(chapter);
			return true;
		}
	}
}
