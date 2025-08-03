package com.shatteredpixel.shatteredpixeldungeon.custom.utils.cell;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.input.PointerEvent;
import com.watabou.input.ScrollEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ScrollArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

import java.util.LinkedList;

public class CellSelectorPro extends ScrollArea {

    public boolean enabled;

    private float dragThreshold;

    private int selections = 1;
    private boolean needConfirm = false;
    private int maxTargets = 1;

    protected LinkedList<Integer> selectedCells = new LinkedList<>();
    public ListenerPro pro = null;
    private SelectorProControlPanel controlPanel;
    private CommonSelector commonSelector;

    public CellSelectorPro( DungeonTilemap map ) {
        super( map );
        camera = map.camera();

        dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2f;

        mouseZoom = camera.zoom;
    }

    private float mouseZoom;

    public void activate(){
        active = true;
    }

    public void deactivate(){
        active = false;
    }

    public void setPro(ListenerPro p){
        pro = p;
        initSelection();
        activate();
    }

    public void initSelection(){
        if(pro != null){
            selections = pro.selections();
            needConfirm = pro.needConfirm();
            maxTargets = pro.maxTargets();

            if(needConfirm) {
                controlPanel = new SelectorProControlPanel();
                GameScene.scene.add(controlPanel);
                controlPanel.camera = GameScene.uiCamera;
                controlPanel.setPos((GameScene.uiCamera.width - controlPanel.width()) / 2f, GameScene.uiCamera.height - 50f);
            }else{
                commonSelector = new CommonSelector(pro.prompt());
                GameScene.scene.add(commonSelector);
                commonSelector.camera = GameScene.uiCamera;
                commonSelector.setPos((GameScene.uiCamera.width - commonSelector.width()) / 2f, GameScene.uiCamera.height - 50f);
            }
        }
    }

    @Override
    protected void onScroll( ScrollEvent event ) {
        float diff = event.amount/10f;

        //scale zoom difference so zooming is consistent
        diff /= ((camera.zoom+1)/camera.zoom)-1;
        diff = Math.min(1, diff);
        mouseZoom = GameMath.gate( PixelScene.minZoom, mouseZoom - diff, PixelScene.maxZoom );

        zoom( Math.round(mouseZoom) );
    }

    @Override
    protected void onClick( PointerEvent event ) {
        if(active) {
            if (dragging) {

                dragging = false;

            } else {
                if (selections > 0) {
                    if (!needConfirm) {
                        handleSelect(event, true);
                        --selections;
                    } else {
                        handleSoftSelect(event);
                    }
                }

            }
        }

    }

    private void handleMultiConfirm(){}

    private void handleSoftSelect(PointerEvent p){

    }

    private void renderCurSelect(){

    }

    private void softSelectSell(int cell, boolean add){
        if(add) {
            selectedCells.add(cell);
        }else{
            selectedCells.removeLastOccurrence(cell);
        }
        renderCurSelect();
    }

    private void handleSelect(PointerEvent event, boolean trigger){
            //Prioritizes a sprite if it and a tile overlap, so long as that sprite isn't more than 4 pixels into another tile.
            //The extra check prevents large sprites from blocking the player from clicking adjacent tiles
            PointF p = Camera.main.screenToCamera((int) event.current.x, (int) event.current.y);
            //hero first
            if (Dungeon.hero.sprite != null && Dungeon.hero.sprite.overlapsPoint( p.x, p.y )){
                PointF c = DungeonTilemap.tileCenterToWorld(Dungeon.hero.pos);
                if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
                    if(trigger) {
                        select(Dungeon.hero.pos, event.button);
                    }
                    return;
                }
            }

            //then mobs
            for (Char mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob.sprite != null && mob.sprite.overlapsPoint( p.x, p.y )){
                    PointF c = DungeonTilemap.tileCenterToWorld(mob.pos);
                    if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
                        select(mob.pos, event.button);
                        return;
                    }
                }
            }

            //then heaps
            for (Heap heap : Dungeon.level.heaps.valueList()){
                if (heap.sprite != null && heap.sprite.overlapsPoint( p.x, p.y)){
                    PointF c = DungeonTilemap.tileCenterToWorld(heap.pos);
                    if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
                        select(heap.pos, event.button);
                        return;
                    }
                }
            }

            select( ((DungeonTilemap)target).screenToTile(
                (int) event.current.x,
                (int) event.current.y,
                true ), event.button );
    }

    private float zoom( float value ) {

        value = GameMath.gate( PixelScene.minZoom, value, PixelScene.maxZoom );
        SPDSettings.zoom((int) (value - PixelScene.defaultZoom));
        camera.zoom( value );

        //Resets char and item sprite positions with the new camera zoom
        //This is important as sprites are centered on a 16x16 tile, but may have any sprite size
        //This can lead to none-whole coordinate, which need to be aligned with the zoom
        for (Char c : Actor.chars()){
            if (c.sprite != null && !c.sprite.isMoving){
                c.sprite.point(c.sprite.worldToCamera(c.pos));
            }
        }
        for (Heap heap : Dungeon.level.heaps.valueList()){
            if (heap.sprite != null){
                heap.sprite.point(heap.sprite.worldToCamera(heap.pos));
            }
        }

        return value;
    }

    public void select( int cell, int button ) {
        if (enabled && Dungeon.hero.ready && !GameScene.interfaceBlockingHero()
            /*&& listener != null*/ && cell != -1) {

            switch (button){
                default:
                    //listener.onSelect( cell );
                    break;
                case PointerEvent.RIGHT:
                    //listener.onRightClick( cell );
                    break;
            }
            GameScene.ready();

        } else {

            GameScene.cancel();

        }
    }

    private boolean pinching = false;
    private PointerEvent another;
    private float startZoom;
    private float startSpan;

    @Override
    protected void onPointerDown( PointerEvent event ) {

        if (event != curEvent && another == null) {

            if (curEvent.type == PointerEvent.Type.UP) {
                curEvent = event;
                onPointerDown( event );
                return;
            }
            pinching = true;

            another = event;
            startSpan = PointF.distance( curEvent.current, another.current );
            startZoom = camera.zoom;

            dragging = false;
        } else if (event != curEvent) {
            reset();
        }
    }

    @Override
    protected void onPointerUp( PointerEvent event ) {
        if (pinching && (event == curEvent || event == another)) {

            pinching = false;

            zoom(Math.round( camera.zoom ));

            dragging = true;
            if (event == curEvent) {
                curEvent = another;
            }
            another = null;
            lastPos.set( curEvent.current );
        }
    }

    private boolean dragging = false;
    private PointF lastPos = new PointF();

    @Override
    protected void onDrag( PointerEvent event ) {

        if (pinching) {

            float curSpan = PointF.distance(curEvent.current, another.current);
            float zoom = (startZoom * curSpan / startSpan);
            camera.zoom(GameMath.gate(
                PixelScene.minZoom,
                zoom - (zoom % 0.1f),
                PixelScene.maxZoom));

        } else {

            if (!dragging && PointF.distance(event.current, event.start) > dragThreshold) {

                dragging = true;
                lastPos.set(event.current);

            } else if (dragging) {
                camera.shift(PointF.diff(lastPos, event.current).invScale(camera.zoom));
                lastPos.set(event.current);
            }
        }

    }

    public void cancel() {
        deactivate();
        if(pro != null){
            pro = null;
        }
        controlPanel.killAndErase();
        controlPanel = null;
        /*
        if (listener != null) {
            listener.onSelect( null );
        }
         */

        GameScene.ready();
    }

    @Override
    public void reset() {
        super.reset();
        another = null;
        if (pinching){
            pinching = false;

            zoom( Math.round( camera.zoom ) );
        }
    }

    public void enable(boolean value){
        if (enabled != value){
            enabled = value;
        }
    }

    public class CommonSelector extends Component{
        protected NinePatch bg;
        protected IconButton close;
        protected RenderedTextBlock text;
        public int leftSelections;

        public CommonSelector(String txt){
            super();

            leftSelections = selections;

            text = PixelScene.renderTextBlock(txt + ": " + leftSelections, 8);

            width = Math.min(text.width() + 16, 110);
            height = 16;

        }

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = Chrome.get(Chrome.Type.TOAST_TR);
            add(bg);

            close = new IconButton( Icons.get( Icons.CLOSE ) ) {
                protected void onClick() {
                    onClose();
                }
            };
            close.setSize(close.icon().width(), close.icon().height());
            add( close );
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            close.setPos(bg.x + bg.marginHor() / 2f + 2, (height - close.height()) / 2f);
            PixelScene.align(close);

            text.maxWidth((int) (bg.width + bg.x - close.right() - 4));
            text.setPos(bg.width + bg.x - close.right() + 4, (height - text.height())/2f);
            PixelScene.align(text);
        }

        protected void onClose() {
            CellSelectorPro.this.deactivate();
        }

        public void onSelect(){
            --leftSelections;
            text.text(text.text().replace(String.valueOf(leftSelections + 1), String.valueOf(leftSelections)));
        }
    }

    public class SelectorProControlPanel extends Component {
        protected NinePatch bg;
        protected IconButton close;
        protected IconButton confirm;
        protected IconButton switcher;
        protected RenderedTextBlock text;
        public boolean add;
        public int left;

        public SelectorProControlPanel(){
            super();

            add = true;
            text = PixelScene.renderTextBlock(
                (maxTargets > 99 ? "99+" : String.valueOf(maxTargets)) + " " + (add ? "++" : "--"),
                8);
            text.hardlight(0x00FF00);
            left = maxTargets;

            width = 90;
            height = 16;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = Chrome.get(Chrome.Type.TOAST_TR);
            add(bg);

            close = new IconButton( Icons.get( Icons.CLOSE ) ) {
                protected void onClick() {
                    onClose();
                }
            };
            close.setSize(close.icon().width(), close.icon().height());
            add( close );

            confirm = new IconButton( Icons.get(Icons.ARROW)){
                @Override
                protected void onClick() {
                    onConfirm();
                }
            };
            confirm.setSize(confirm.icon().width, confirm.icon().height);
            confirm.icon().hardlight(0xFFFF44);
            add(confirm);

            switcher = new IconButton(Icons.get(Icons.CHANGES)){
                @Override
                protected void onClick() {
                    super.onClick();
                    onSwitch();
                }
            };
            switcher.setSize(switcher.icon().width, switcher.icon().height);
            add(switcher);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            close.setPos(bg.x + bg.marginHor() / 2f + 2, (height - close.height()) / 2f);
            PixelScene.align(close);
            confirm.setPos( bg.x + bg.width() - bg.marginHor() / 2f - 2 - confirm.width(), (height - confirm.height())/2f);
            PixelScene.align(confirm);

            switcher.setPos((confirm.left() - close.right())/2f - switcher.width() - 1f, (height - switcher.height())/2f);
            PixelScene.align(switcher);

            text.maxWidth((int) (confirm.left() - switcher.right() - 4));
            text.setPos(switcher.right() + 2, (height - text.height())/2f);
            PixelScene.align(text);
        }

        protected void onConfirm(){
            CellSelectorPro.this.handleMultiConfirm();
        }

        protected void onClose() {
            CellSelectorPro.this.deactivate();
        }

        public void onSwitch(){
            add = !add;
            text.text(text.text().replace(add?"--":"++", add?"++":"--"));
            text.hardlight(add ? 0x00FF00 : 0xFF0000);
        }

        public boolean isQualifiedForNextSelect(){
            return add && left > 0 || !add && left < maxTargets;
        }

        public void onSelect(){
            if(add && left > 0){
                left--;
            }else if(!add && left < maxTargets){
                left++;
            }
            text.text((left > 99 ? "99+" : String.valueOf(left)) + " " + (add ? "++" : "--"));
        }
    }


    public abstract static class ListenerPro{
        public abstract int selections();
        public abstract boolean needConfirm();
        public abstract int maxTargets();
        public abstract boolean onSelect(Integer cell);
        public abstract boolean onFinish();
        public abstract String prompt();

    }

}
