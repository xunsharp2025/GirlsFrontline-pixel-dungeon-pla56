package com.shatteredpixel.shatteredpixeldungeon.utils.diglog;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.SparseArray;

public class Script {
    //Well if we wants to make plot related(mostly dialogue)clear and able to store enough date(if we still have an chronicle-like campaign?)
    //Whatever,this at least help save some data needed
    //More information can be found at WndDialog.java
    //By Teller 2021/8/20

    //GirlPD is Implement
    //By JDSALing 2025/7/28

    public enum Character{
       NONE,UMP45,HK416,G11,UMP9
    }

    public static Image AvatarG11(int index) {
        Image i;
        switch (index){
            default:
            case 0:
                i = new Image(Assets.EMOTION,0,0,24,24);
                break;
            case 1:
                i = new Image(Assets.EMOTION,24,0,24,24);
                break;
            case 2:
                i = new Image(Assets.EMOTION,48,0,24,24);
                break;
        }
        return i;
    }

    public static Image AvatarUMP45(int index) {
        Image i;
        switch (index){
            default:
            case 0:
                i = new Image(Assets.EMOTION,0,24,24,24);
                break;
            case 1:
                i = new Image(Assets.EMOTION,24,24,24,24);
                break;
            case 2:
                i = new Image(Assets.EMOTION,48,24,24,24);
                break;
        }
        return i;
    }

    public static Image AvatarUMP9(int index) {
        Image i;
        switch (index){
            default:
            case 0:
                i = new Image(Assets.EMOTION,0,48,24,24);
                break;
            case 1:
                i = new Image(Assets.EMOTION,24,48,24,24);
                break;
            case 2:
                i = new Image(Assets.EMOTION,48,48,24,24);
                break;
        }
        return i;
    }

    public static Image AvatarHK416(int index) {
        Image i;
        switch (index){
            default:
            case 0:
                i = new Image(Assets.EMOTION,0,72,24,24);
                break;
            case 1:
                i = new Image(Assets.EMOTION,24,72,24,24);
                break;
            case 2:
                i = new Image(Assets.EMOTION,48,72,24,24);
                break;
        }
        return i;
    }



    public enum FormalPlot
    {
        NOBODY,SEWER,PRISON,CAVES,CITY,HALLS
    }


    public static final int ID_SEWERS		= 0;
    public static final int ID_PRISON		= 1;
    public static final int ID_CAVES		= 2;
    public static final int ID_CITY     	= 3;
    public static final int ID_HALLS		= 4;

    public static final int ID_FROSTNOVA_INTERACT = 5;

    private static final SparseArray<String> CHAPTERS = new SparseArray<String>();

    static {
        CHAPTERS.put( ID_SEWERS, "sewers" );
        CHAPTERS.put( ID_PRISON, "prison" );
        CHAPTERS.put( ID_CAVES, "caves" );
        CHAPTERS.put( ID_CITY, "city" );
        CHAPTERS.put( ID_HALLS, "halls" );
    };

    public static boolean checkChapter(int chapter) {
        if(Dungeon.chapters.contains(chapter)) {
            return false;
        }
        else {
            Dungeon.chapters.add(chapter);
            return true;
        }
    }

    public static String Name(Character character)
    {
        String str;
        switch (character) {
            default:
            case UMP45:
                str = Messages.get(Script.class,"name_ump45");
                break;
            case G11:
                str = Messages.get(Script.class,"name_g11");
                break;
            case HK416:
                str = Messages.get(Script.class,"name_hk416");
                break;
            case UMP9:
                str = Messages.get(Script.class,"name_ump9");
                break;
        }
        //GLog.i(str);
        return str;
    }

    public static Image Portrait(Character character)
    {
        int row = -1;
        switch (character) {
            default:
            case UMP45:
                row = 0;
                break;
            case G11:
                row = 1;
                break;
            case HK416:
                row = 2;
                break;
            case UMP9:
                row = 3;
                break;
        }
        Image image = new Image(Assets.Sprites.AVATARS, 0, 48* row,48, 48);

        switch (character)
        {
            case NONE:
                image = new Image(Assets.EMOTION,0,24,24,24);

                AlphaTweener invisible = new AlphaTweener( image, 0.4f, 0.4f );
                if (image.parent != null){
                    image.parent.add(invisible);
                } else
                    image.alpha( 0.4f );

                image.alpha(0.4f);
                break;
            default:break;
        }

        return image;
    }

    public static Plot ReturnPlot(FormalPlot plot)
    {
        switch (plot)
        {
            default:
            case SEWER:
            case PRISON:
            case CAVES:
            case CITY:
            case HALLS: return null;
        }
    }
}
