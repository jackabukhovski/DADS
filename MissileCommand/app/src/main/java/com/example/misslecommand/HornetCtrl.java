package com.example.misslecommand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.util.*;

public class HornetCtrl {
    public int count =0;
    public List<Hornets> hornets;
    Random random;
    Context contxt;
    public boolean removeHornet = false;
    public int hornetsToSpawn;
    public int spawnTimer = 0;

    public HornetCtrl(Context context, int ammo) {
        contxt = context;
        hornetsToSpawn = ammo;
        hornets = new ArrayList<>();
        random = new Random();
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < hornets.size(); i++) {
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawLine(hornets.get(i).initX,
                    hornets.get(i).initY,
                    hornets.get(i).xPosition,
                    hornets.get(i).yPosition,
                    paint);

            canvas.save();
            canvas.rotate( (float)hornets.get(i).rotateDeg, hornets.get(i).xPosition, hornets.get(i).yPosition);
            //canvas.drawRect(hornets.get(i).mRect, paint);
            if (count > 100){
                count = 0;
            }
            if (count < 50){
                canvas.drawBitmap(hornets.get(i).getBitmap2(), hornets.get(i).getRect().left, hornets.get(i).getRect().top, paint);
            }
            else {
                canvas.drawBitmap(hornets.get(i).getBitmap(), hornets.get(i).getRect().left, hornets.get(i).getRect().top, paint);
            }
            count++;
            canvas.restore();

        }
    }

    public void spawnHornets(int level, CowsCtrl cowsCtrl, int screenX, Context context) {
        int randCow = random.nextInt(cowsCtrl.cowNum);
        int didFire = random.nextInt(100);
        if (didFire <= level && hornetsToSpawn > 0) {
            Cows target = cowsCtrl.cows[randCow];
            hornets.add(new Hornets(random.nextInt(screenX), 0, target, level, context));
            hornetsToSpawn--;
            spawnTimer = 0;
        }
    }

    public void spawnSingle(int level, CowsCtrl cowsCtrl, int screenX, Context context) {
        int randCow = random.nextInt(cowsCtrl.cowNum);
        Cows target = cowsCtrl.cows[randCow];
        hornets.add(new Hornets(random.nextInt(screenX), 0, target, level, context));
    }


    public void update(long fps, int level, CowsCtrl cowsCtrl, int screenX) {
        for (int i = 0; i < hornets.size(); i++) {
            hornets.get(i).update(fps);
            hornets.get(i).kill();
            if (hornets.get(i).status == false) {
                hornets.remove(i);
                removeHornet = true;
            }
        }
        spawnTimer++;
        if(spawnTimer >= 33) {  // Have a chance to spawn a hornet every 33 frames
            spawnHornets(level, cowsCtrl, screenX, contxt);
        }
    }

}
