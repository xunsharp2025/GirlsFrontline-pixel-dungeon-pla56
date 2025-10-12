package com.shatteredpixel.shatteredpixeldungeon.ui.dialog.boss;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Plot;
import com.shatteredpixel.shatteredpixeldungeon.utils.diglog.Script;
import com.watabou.noosa.Image;

public class Elphelt_Plot extends Plot {
	{
		processCount = 1;
	}

	@Override
	public Image getImage(int process){
		switch(process){
			case 0:return Script.NpcNoel(1);
		}
		return null;
	}

	@Override
	public String getName(int process){
		switch(process){
			case 0:return Script.Name(Script.Character.NOEL);
		}
		return null;
	}

	@Override
	public String getDialog(int process){
		switch(process){
			case 0:return Messages.get(this,"dialog");
		}
		return null;
	}

	public static class End extends Elphelt_Plot{
		{
			processCount = 6;
		}

		@Override
		public Image getImage(int process){
			switch(process){
				case 0:return Script.BossElphelt(2);
				case 1:return Script.AvatarHK416(2);
				case 2:return Script.NpcNoel    (0);
				case 3:return Script.BossElphelt(0);
				case 4:return Script.BossElphelt(1);
				case 5:return Script.NpcNoel    (2);
			}
			return null;
		}

		@Override
		public String getName(int process){
			switch(process){
				case 0:return Script.Name(Script.Character.ELPHELT);
				case 1:return Script.Name(Script.Character.HK416  );
				case 2:return Script.Name(Script.Character.NOEL   );
				case 3:return Script.Name(Script.Character.ELPHELT);
				case 4:return Script.Name(Script.Character.ELPHELT);
				case 5:return Script.Name(Script.Character.NOEL   );
			}
			return null;
		}

		@Override
		public String getDialog(int process){
			switch(process){
				case 0:return Messages.get(this,"dialog0");
				case 1:return Messages.get(this,"dialog1");
				case 2:return Messages.get(this,"dialog2");
				case 3:return Messages.get(this,"dialog3");
				case 4:return Messages.get(this,"dialog4");
				case 5:return Messages.get(this,"dialog5");
			}
			return null;
		}
	}
}