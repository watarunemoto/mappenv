package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by amemiyaY on 2016/10/25.
 */
public class Draw_line {

    GoogleMap map;

    public Draw_line(GoogleMap map) {
        this.map = map;
    }

    public void execute(LatLng latlng) {
        DecimalFormat df = new DecimalFormat("0.00");

        Double botlat = latlng.latitude - 0.25;
        Double botlng = latlng.longitude - 0.25;
        Double toplat = latlng.latitude + 0.25;
        Double toplng = latlng.longitude + 0.25;

        BigDecimal bdbtlat = new BigDecimal(botlat);
        BigDecimal bdbtlng = new BigDecimal(botlng);

        BigDecimal bdtplat = new BigDecimal(toplat);
        BigDecimal bdtplng = new BigDecimal(toplng);

        bdbtlat = bdbtlat.setScale(2, BigDecimal.ROUND_DOWN);
        bdbtlng = bdbtlng.setScale(2, BigDecimal.ROUND_DOWN);

        bdtplat = bdtplat.setScale(2, BigDecimal.ROUND_DOWN);
        bdtplng = bdtplng.setScale(2, BigDecimal.ROUND_DOWN);

        botlat = Double.valueOf(df.format(bdbtlat.doubleValue()));
        botlng = Double.valueOf(df.format(bdbtlng.doubleValue()));

        toplat = Double.valueOf(df.format(bdtplat.doubleValue()));
        toplng = Double.valueOf(df.format(bdtplng.doubleValue()));



        for (Double i = botlat; i < toplat + 0.05; i += 0.05) {
            PolylineOptions opt = new PolylineOptions();
            opt.width((float) 0.5);
            opt.add(new LatLng(i, botlng));
            opt.add(new LatLng(i, toplng));

            map.addPolyline(opt);
            Log.v("afa",String.valueOf(i) + "," +botlng + "," +toplng);
        }

        for (Double i = botlng; i < toplng+ 0.05; i += 0.05) {
            PolylineOptions opt = new PolylineOptions();
            opt.width((float) 0.5);

            opt.add(new LatLng(botlat,i));
            opt.add(new LatLng(toplat,i));
            map.addPolyline(opt);

            Log.v("afb",String.valueOf(i) + "," +botlat+ "," +toplat);
        }

    }
}
