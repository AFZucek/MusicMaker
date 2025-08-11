import acm.program.*;
import acm.graphics.*;

public class MusicMaker extends GraphicsProgram {

    public static int height = 170;
    public static int width = 100;
    public static int xstart = 50;
    public static int row = 0;

    public static int chordStar = 0;

    public static void main(String[] args) {
        new MusicMaker().start(args);
    }

    public void run() {

        for (int i = 0; i < 8; i++) {
            oneMeasureGrid(i);
        }

        GLabel title = new GLabel("Addison's Magical Music Maker");
        add(title, 550, 150);

        int randomMeasureStart = (int)(Math.random() * 2);

        for (int i = 0; i < 8; i++){

            Measure m = new Measure(getScale(getChordName(i, randomMeasureStart)));
            System.out.println(getChordName(i, randomMeasureStart));
            for (int j = 0; j < 4; j++){
                System.out.println(((Note)m.getMelody()[j]).getName());

                if (m.getLowMelody()[j] != null) System.out.println(((Note)m.getLowMelody()[j]).getName() + " is bottom");


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                double ran = Math.random();
                boolean moveBaseDown = false;
                if (m.getMoveDown()) moveBaseDown = true;
                addNote((Note)m.getBase()[j], 105 + (width * (i%4)) + j*20, i/4, false, true, moveBaseDown);
                moveBaseDown = false;

                if (Math.random() < .25){
                    addNote(((Note)m.getMelody()[j]).makeRest(), 105 + (width * (i%4)) + j*20, i/4, true, true, moveBaseDown);
                    ((Note)m.getMelody()[j]).makeNotRest();
                }
                else {
                    if (((getChordName(i, randomMeasureStart).equals("e") || (getChordName(i, randomMeasureStart).equals("a")))
                            && ((Note)m.getMelody()[j]).getName() == 9 && ((Note)m.getMelody()[j]).getName() == 6)){
                        addNote((Note)m.getMelody()[j], 105 + (width * (i%4)) + j*20, i/4, true, true, moveBaseDown);
                    }
                    else {
                        addNote((Note)m.getMelody()[j], 105 + (width * (i%4)) + j*20, i/4, true, true, moveBaseDown);
                        if (ran > .5){
                            addNote((Note)m.getMidMelody()[j], 105+ (width * (i%4)) + j*20, i/4, true, false, moveBaseDown);
                        }
                    }

                }
                //addNote((Note)m.getLowMelody()[j], 92 + (width * (i/4)) + j*20, i/4, true, false);
            }
        }
    }

    public void oneMeasureGrid(int numMeasures) {
        int width = 100;
        if (numMeasures % 4 == 0) width += xstart;

        int yconstant = numMeasures / 4 * height;
        int xconstant = (numMeasures % 4) * width;
        int lowestBar = 200;

        if (numMeasures % 4 == 0) {
            GImage treble = new GImage("trebleimage.png", 35, yconstant + 50);
            treble.setSize(60, 80);
            add(treble);

            GImage bass = new GImage("bassimage.png",
                    35, yconstant + 140);
            bass.setSize(60, 80);
            add(bass);

            GImage timeSig = new GImage("timesigimage.png",
                    80, yconstant + 70);
            timeSig.setSize(20, 40);
            add(timeSig);

            GImage timeSig2 = new GImage("timesigimage.png",
                    80, yconstant + 160);
            timeSig2.setSize(20, 40);
            add(timeSig2);
        }

        for (int i = 0; i < 10; i++) {
            int spaceBetweenBars = 10;
            GLine line = new GLine(xstart + xconstant, lowestBar - (i * spaceBetweenBars) + yconstant,
                    xstart + width + xconstant, lowestBar - (i * spaceBetweenBars) + yconstant);
            add(line);
            if (i == 4) lowestBar -= 40;
        }

        GLine line2 = new GLine(xstart + xconstant, lowestBar + 40 + yconstant,
                xstart + xconstant, lowestBar - 9 * 10 + yconstant);
        add(line2);

        GLine line3 = new GLine(xstart + width + xconstant, lowestBar + 40 + yconstant,
                xstart + width + xconstant, lowestBar - 9 * 10 + yconstant);
        add(line3);

        if (numMeasures % 4 == 0) xstart += 50;
        if (numMeasures % 4 == 3) {
            row++;
            xstart -= 50;
        }
    }


    public void addNote(Note note, int xPos, int rowInput, boolean treble, boolean addStem, boolean moveBaseDown) {
        int ovalWidth = 12;
        int ovalHeight = 8;
        int yPos = 0;

        if (note.isRest()){
            GImage sharp = new GImage("quarternoteimage.png", xPos, (height * rowInput) + 80);
            sharp.setSize(25, 25);
            add(sharp);
        }

        else {
            if (treble) yPos = -note.getName() * 5 + (height * rowInput) + 116;
            if (!treble) yPos = -note.getName() * 5 + (height * rowInput) + 182;

            if (moveBaseDown) yPos += (35);

            if (note.getAccidental().equals("sharp")) {
                GImage sharp = new GImage("sharpimage.png", xPos - ovalWidth - 6, yPos - ovalHeight);
                sharp.setSize(25, 25);
                add(sharp);
            }

            if (note.getAccidental().equals("flat")) {
                GImage sharp = new GImage("flatimage.png", xPos - ovalWidth - 6, yPos - ovalHeight - 5);
                sharp.setSize(25, 25);
                add(sharp);
            }

            if (note.getRhythm() == .25) {
                GOval oval = new GOval(xPos, yPos, ovalWidth, ovalHeight);
                oval.setFilled(true);
                add(oval);

                if (addStem){
                    if (note.getName() < 7) {
                        GLine stem = new GLine(xPos + ovalWidth, yPos + (ovalHeight / 2), xPos + ovalWidth, yPos - 38);
                        add(stem);
                    } else {
                        GLine stem = new GLine(xPos, yPos + (ovalHeight / 2), xPos, yPos + 38);
                        add(stem);
                    }
                }
            }

            // makes bars under low notes in treble, dont have bars for anything else
            if (treble) {
                if (note.getName() < 1) {
                    for (int i = 1; i > note.getName(); i--) {
                        if (130 - i * 10 < yPos + 8) {
                            GLine bar = new GLine(xPos - 5, 130 - i * 10 + (rowInput * 170), xPos + 15, 130 - i * 10 + (rowInput * 170));
                            add(bar);
                        }
                    }
                }
            }


            if (!moveBaseDown && !treble && yPos < 149 + (rowInput * 170)){ //adds line for base above staff

                if (xPos < 200){
                    GLine bar = new GLine(140, 150 + (rowInput * 170), 160, 150 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 300){
                    GLine bar = new GLine(240, 150 + (rowInput * 170), 260, 150 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 400){
                    GLine bar = new GLine(340, 150 + (rowInput * 170), 360, 150 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 500){
                    GLine bar = new GLine(440, 150 + (rowInput * 170), 460, 150 + (rowInput * 170));
                    add(bar);
                }
            }


            if (yPos > 200 + (rowInput * 170)){

                if (xPos < 200){
                    GLine bar = new GLine(100, 212 + (rowInput * 170), 120, 212 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 300){
                    GLine bar = new GLine(200, 212 + (rowInput * 170), 220, 212 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 400){
                    GLine bar = new GLine(300, 212 + (rowInput * 170), 320, 212 + (rowInput * 170));
                    add(bar);
                }
                else if (xPos < 500){
                    GLine bar = new GLine(400, 212 + (rowInput * 170), 420, 212 + (rowInput * 170));
                    add(bar);
                }
            }
        }
    }

    public Note[] getScale(String chordName) {
        Note[] inKeyNotes = new Note[14];
        chordStar = 0;

        for (int i = 0; i < 14; i++) {
            inKeyNotes[i] = new Note(i, .25, "na");
        }

        if (chordName.equals("g") || chordName.equals("d") || chordName.equals("a") || chordName.equals("e")){
            inKeyNotes[3] = new Note(3, .25, "sharp");
            inKeyNotes[10] = new Note(10, .25, "sharp");
            chordStar = 4;

            if (chordName.equals("d") || chordName.equals("a") || chordName.equals("e")){
                inKeyNotes[0] = new Note(0, .25, "sharp");
                inKeyNotes[7] = new Note(0, .25, "sharp");
                chordStar = 1;

                if (chordName.equals("a") || chordName.equals("e")){
                    inKeyNotes[4] = new Note(4, .25, "sharp");
                    inKeyNotes[11] = new Note(4, .25, "sharp");
                    chordStar = 5;

                    if (chordName.equals("e")){
                        inKeyNotes[1] = new Note(1, .25, "sharp");
                        inKeyNotes[8] = new Note(8, .25, "sharp");
                        chordStar = 2;
                    }
                }
            }
        }

        if (chordName.equals("f") || chordName.equals("bflat") || chordName.equals("eflat")){
            inKeyNotes[6] = new Note(6, .25, "flat");
            inKeyNotes[13] = new Note(6, .25, "flat");
            chordStar = 3;

            if (chordName.equals("bflat") || chordName.equals("eflat")){
                inKeyNotes[2] = new Note(2, .25, "flat");
                inKeyNotes[9] = new Note(2, .25, "flat");
                chordStar = 6;

                if (chordName.equals("eflat")){
                    inKeyNotes[5] = new Note(5, .25, "flat");
                    inKeyNotes[12] = new Note(5, .25, "flat");
                    chordStar = 2;
                }
            }
        }
        return inKeyNotes;
    }

    public String getChordName(int measure, int ran){
        System.out.println("Starting chord is " + ran);
        if (ran == 0){ // c
            if (measure % 8 == 7 || measure % 4 == 0){
                return "c";
            }
            if (measure % 8 == 2){
                return "d";
            }
            if (measure % 8 == 3){
                return "g";
            }
            else {
                if (Math.random() > .5) return "e";
                else return "a";
            }
        }

        else if (ran == 1){ // f
            System.out.println("working");
            if (measure % 8 == 7 || measure % 4 == 0){
                return "f";
            }
            if (measure % 8 == 2){
                return "g";
            }
            if (measure % 8 == 3){
                return "c";
            }
            else {
                if (Math.random() > .5) return "a";
                else return "d";
            }
        }
        else if (ran == 0){ // g
            if (measure % 4 == 1 || measure % 8 == 0){
                return "g";
            }
        }
        else{ // d
            if (measure % 4 == 1 || measure % 8 == 0){
                return "d";
            }
        }
        return null;
    }
    public static int getChordStart(){return chordStar;}
}


class Beat{
    private double rhythm;
    public Beat(double rhythm){
        this.rhythm = rhythm;
    }
    public double getRhythm(){
        return rhythm;
    }
}

class Note extends Beat{
    private int name;// c is 0, b is 11
    private GImage image;
    private boolean isRest = false;
    private String accidental;
    public Note(int name, double rhythm, String accidental){
        super(rhythm);
        this.name = name;
        this.accidental = accidental;
    }

    public Note makeRest(){
        isRest = true;
        return this;
    }

    public Note makeNotRest(){
        isRest = false;
        return this;
    }

    public boolean isRest(){
        return isRest;
    }

    public int getName() {
        return name;
    }

    public String getAccidental(){
        return accidental;
    }


}

class Rest extends Beat{
    public Rest(double rhythm){
        super(rhythm);
    }
}

class Measure{

    boolean moveDown = false;

    public boolean getMoveDown(){
        return moveDown;
    }

    private Beat[] melody = new Beat[4];
    private Beat[] belowmelody = new Beat[4];;
    private Beat[] lowestmelody = new Beat[4];;
    private Beat[] base = new Beat[4];;


    private int chord; // c is 0, b is 11

    public Measure(Note[] scale){
        if (MusicMaker.getChordStart() > 0) moveDown = true;
        for (int i = 0; i < 4; i++){
            if (i % 4 == 0) base[i] = scale[MusicMaker.getChordStart()];
            else if ( i % 2 == 0){
                base[i] = scale[MusicMaker.getChordStart() + 7];
            }
            else base[i] = scale[MusicMaker.getChordStart() + 4];
            int newNote = (int)(Math.random() * 4);
            if (newNote == 3) newNote = 4;
            if (newNote + MusicMaker.getChordStart() < 5){
                melody[i] = scale[newNote + MusicMaker.getChordStart() + 7];
                if (newNote == 4){
                    belowmelody[i] = scale[newNote - 2 + MusicMaker.getChordStart() + 7];
                    lowestmelody[i] = scale[newNote - 3 + MusicMaker.getChordStart() + 7];
                }
                else if (newNote == 2){
                    belowmelody[i] = scale[newNote - 2 + MusicMaker.getChordStart() + 7];
                }
                else {
                    belowmelody[i] = scale[newNote - 5 + MusicMaker.getChordStart() + 7];
                }
            }

            else {
                melody[i] = scale[newNote + MusicMaker.getChordStart()];
                if (newNote == 4){
                    belowmelody[i] = scale[newNote - 2 + MusicMaker.getChordStart()];
                    lowestmelody[i] = scale[newNote - 3 + MusicMaker.getChordStart()];
                }
                else if (newNote == 2){
                    belowmelody[i] = scale[newNote - 2 + MusicMaker.getChordStart()];
                }
                else{
                    belowmelody[i] = scale[newNote + 2 + MusicMaker.getChordStart()];
                }
            }
        }
    }

    public Beat[] getMelody(){
        return melody;
    }
    public Beat[] getMidMelody(){
        return belowmelody;
    }

    public Beat[] getLowMelody(){
        return lowestmelody;
    }

    public Beat[] getBase(){
        return base;
    }

}

class verse{

}

class chorus{

}

class bridge{

}


