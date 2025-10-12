package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.quest;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class Noel_Plot_L1 extends Plot {
	{
		processCount = 17;
	}

	@Override
	public Image getImage(int process){
		switch(process){
			case 0 :return Script.AvatarUMP9 (1);
			case 1 :return Script.AvatarUMP45(2);
			case 2 :return Script.NpcNoel    (1);
			case 3 :return Script.AvatarHK416(2);
			case 4 :return Script.AvatarUMP45(0);
			case 5 :return Script.NpcNoel    (0);
			case 6 :return Script.AvatarUMP9 (2);
			case 7 :return Script.AvatarG11  (1);
			case 8 :return Script.AvatarUMP45(0);
			case 9 :return Script.NpcNoel    (2);
			case 10:return Script.NpcNoel    (0);
			case 11:return Script.AvatarUMP45(2);
			case 12:return Script.NpcNoel    (0);
			case 13:return Script.NpcNoel    (0);
			case 14:return Script.NpcNoel    (2);
			case 15:return Script.NpcNoel    (2);
			case 16:return Script.AvatarUMP45(0);
		}
		return null;
	}

	@Override
	public String getName(int process){
		switch(process){
			case 0 :return Script.Name(Script.Character.UMP9 );
			case 1 :return Script.Name(Script.Character.UMP45);
			case 2 :return Script.Name(Script.Character.NOEL );
			case 3 :return Script.Name(Script.Character.HK416);
			case 4 :return Script.Name(Script.Character.UMP45);
			case 5 :return Script.Name(Script.Character.NOEL );
			case 6 :return Script.Name(Script.Character.UMP9 );
			case 7 :return Script.Name(Script.Character.G11  );
			case 8 :return Script.Name(Script.Character.UMP45);
			case 9 :return Script.Name(Script.Character.NOEL );
			case 10:return Script.Name(Script.Character.NOEL );
			case 11:return Script.Name(Script.Character.UMP45);
			case 12:return Script.Name(Script.Character.NOEL );
			case 13:return Script.Name(Script.Character.NOEL );
			case 14:return Script.Name(Script.Character.NOEL );
			case 15:return Script.Name(Script.Character.NOEL );
			case 16:return Script.Name(Script.Character.UMP45);
		}
		return null;
	}

	@Override
	public String getDialog(int process){
		switch(process){
			case  0:return Messages.get(this,"dialog00");
			case  1:return Messages.get(this,"dialog01");
			case  2:return Messages.get(this,"dialog02");
			case  3:return Messages.get(this,"dialog03");
			case  4:return Messages.get(this,"dialog04");
			case  5:return Messages.get(this,"dialog05");
			case  6:return Messages.get(this,"dialog06");
			case  7:return Messages.get(this,"dialog07");
			case  8:return Messages.get(this,"dialog08");
			case  9:return Messages.get(this,"dialog09");
			case 10:return Messages.get(this,"dialog10");
			case 11:return Messages.get(this,"dialog11");
			case 12:return Messages.get(this,"dialog12");
			case 13:return Messages.get(this,"dialog13");
			case 14:return Messages.get(this,"dialog14");
			case 15:return Messages.get(this,"dialog15");
			case 16:return Messages.get(this,"dialog16");
		}
		return null;
	}
}



